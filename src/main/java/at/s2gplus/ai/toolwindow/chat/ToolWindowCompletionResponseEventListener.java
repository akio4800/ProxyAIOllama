package at.s2gplus.ai.toolwindow.chat;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.*;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import at.s2gplus.ai.EncodingManager;
import at.s2gplus.ai.codecompletions.CompletionProgressNotifier;
import at.s2gplus.ai.completions.ChatCompletionParameters;
import at.s2gplus.ai.completions.CompletionResponseEventListener;
import at.s2gplus.ai.conversations.Conversation;
import at.s2gplus.ai.conversations.ConversationService;
import at.s2gplus.ai.conversations.message.Message;
import at.s2gplus.ai.events.CodeGPTEvent;
import at.s2gplus.ai.toolwindow.chat.ui.ChatMessageResponseBody;
import at.s2gplus.ai.toolwindow.chat.ui.textarea.TotalTokensPanel;
import at.s2gplus.ai.toolwindow.ui.ResponseMessagePanel;
import at.s2gplus.ai.toolwindow.ui.UserMessagePanel;
import at.s2gplus.ai.ui.OverlayUtil;
import at.s2gplus.ai.ui.textarea.UserInputPanel;
import ee.carlrobert.llm.client.openai.completion.ErrorDetails;

import static com.intellij.openapi.ui.Messages.OK;

abstract class ToolWindowCompletionResponseEventListener implements
    CompletionResponseEventListener {

  private static final Logger LOG = Logger.getInstance(
      ToolWindowCompletionResponseEventListener.class);
  private static final int UPDATE_INTERVAL_MS = 8;

  private final Project project;
  private final StringBuilder messageBuilder = new StringBuilder();
  private final EncodingManager encodingManager;
  private final ResponseMessagePanel responsePanel;
  private final UserMessagePanel userMessagePanel;
  private final ChatMessageResponseBody responseContainer;
  private final TotalTokensPanel totalTokensPanel;
  private final UserInputPanel textArea;

  private final Timer updateTimer = new Timer(UPDATE_INTERVAL_MS, e -> processBufferedMessages());
  private final ConcurrentLinkedQueue<String> messageBuffer = new ConcurrentLinkedQueue<>();
  private boolean stopped = false;
  private boolean streamResponseReceived = false;

  public ToolWindowCompletionResponseEventListener(
      Project project,
      UserMessagePanel userMessagePanel,
      ResponseMessagePanel responsePanel,
      TotalTokensPanel totalTokensPanel,
      UserInputPanel textArea) {
    this.encodingManager = EncodingManager.getInstance();
    this.project = project;
    this.userMessagePanel = userMessagePanel;
    this.responsePanel = responsePanel;
    this.responseContainer = (ChatMessageResponseBody) responsePanel.getContent();
    this.totalTokensPanel = totalTokensPanel;
    this.textArea = textArea;
  }

  public abstract void handleTokensExceededPolicyAccepted();

  @Override
  public void handleRequestOpen() {
    updateTimer.start();
  }

  @Override
  public void handleMessage(String partialMessage) {
    streamResponseReceived = true;

    try {
      messageBuilder.append(partialMessage);
      var ongoingTokens = encodingManager.countTokens(messageBuilder.toString());
      messageBuffer.offer(partialMessage);
      ApplicationManager.getApplication().invokeLater(() ->
          totalTokensPanel.update(totalTokensPanel.getTokenDetails().getTotal() + ongoingTokens)
      );
    } catch (Exception e) {
      responseContainer.displayError("Something went wrong.");
      throw new RuntimeException("Error while updating the content", e);
    }
  }

  @Override
  public void handleError(ErrorDetails error, Throwable ex) {
    ApplicationManager.getApplication().invokeLater(() -> {
      try {
        if ("insufficient_quota".equals(error.getCode())) {
          responseContainer.displayQuotaExceeded();
        } else {
          responseContainer.displayError(error.getMessage());
        }
      } finally {
        stopStreaming(responseContainer);
      }
    });
  }

  @Override
  public void handleTokensExceeded(Conversation conversation, Message message) {
    ApplicationManager.getApplication().invokeLater(() -> {
      var answer = OverlayUtil.showTokenLimitExceededDialog();
      if (answer == OK) {
        ConversationService.getInstance().discardTokenLimits(conversation);
        handleTokensExceededPolicyAccepted();
      } else {
        stopStreaming(responseContainer);
      }
    });
  }

  @Override
  public void handleCompleted(String fullMessage, ChatCompletionParameters callParameters) {
    ConversationService.getInstance().saveMessage(fullMessage, callParameters);

    ApplicationManager.getApplication().invokeLater(() -> {
      try {
        responsePanel.enableAllActions(true);
        if (!streamResponseReceived && !fullMessage.isEmpty()) {
          responseContainer.withResponse(fullMessage);
        }
        totalTokensPanel.updateUserPromptTokens(textArea.getText());
        totalTokensPanel.updateConversationTokens(callParameters.getConversation());
      } finally {
        stopStreaming(responseContainer);
      }
    });
  }

  @Override
  public void handleCodeGPTEvent(CodeGPTEvent event) {
    responseContainer.handleCodeGPTEvent(event);
  }

  private void processBufferedMessages() {
    if (messageBuffer.isEmpty()) {
      if (stopped) {
        updateTimer.stop();
      }
      return;
    }

    StringBuilder accumulatedMessage = new StringBuilder();
    String message;
    while ((message = messageBuffer.poll()) != null) {
      accumulatedMessage.append(message);
    }

    responseContainer.updateMessage(accumulatedMessage.toString());
  }

  private void stopStreaming(ChatMessageResponseBody responseContainer) {
    stopped = true;
    textArea.setSubmitEnabled(true);
    userMessagePanel.enableAllActions(true);
    responsePanel.enableAllActions(true);
    responseContainer.hideCaret();
    CompletionProgressNotifier.update(project, false);
  }
}
