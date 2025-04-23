package at.s2gplus.ai.completions;

import at.s2gplus.ai.conversations.Conversation;
import at.s2gplus.ai.conversations.message.Message;
import at.s2gplus.ai.events.CodeGPTEvent;
import ee.carlrobert.llm.client.openai.completion.ErrorDetails;

public interface CompletionResponseEventListener {

  default void handleMessage(String message) {
  }

  default void handleError(ErrorDetails error, Throwable ex) {
  }

  default void handleTokensExceeded(Conversation conversation, Message message) {
  }

  default void handleCompleted(String fullMessage) {
  }

  default void handleCompleted(String fullMessage, ChatCompletionParameters callParameters) {
  }

  default void handleCodeGPTEvent(CodeGPTEvent event) {
  }

  default void handleRequestOpen() {
  }
}
