package at.s2gplus.ai.actions.editor;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;

import at.s2gplus.ai.Icons;
import at.s2gplus.ai.conversations.message.Message;
import at.s2gplus.ai.toolwindow.chat.ChatToolWindowContentManager;
import at.s2gplus.ai.ui.UIUtil;
import at.s2gplus.ai.util.file.FileUtil;

import static java.lang.String.format;

public class AskQuestionAction extends BaseEditorAction {

  private static String previousUserPrompt = "";

  AskQuestionAction() {
    super(Icons.QuestionMark);
  }

  @Override
  protected void actionPerformed(Project project, Editor editor, String selectedText) {
    if (selectedText != null && !selectedText.isEmpty()) {
      var fileExtension = FileUtil.getFileExtension(editor.getVirtualFile().getName());
      var dialog = new CustomPromptDialog(previousUserPrompt);
      if (dialog.showAndGet()) {
        previousUserPrompt = dialog.getUserPrompt();
        var message = new Message(
            format("%s%n```%s%n%s%n```", previousUserPrompt, fileExtension, selectedText));
        SwingUtilities.invokeLater(() ->
            project.getService(ChatToolWindowContentManager.class).sendMessage(message));
      }
    }
  }

  public static class CustomPromptDialog extends DialogWrapper {

    private final JTextArea userPromptTextArea;

    public CustomPromptDialog(String previousUserPrompt) {
      super(true);
      this.userPromptTextArea = new JTextArea(previousUserPrompt);
      this.userPromptTextArea.setCaretPosition(previousUserPrompt.length());
      setTitle("Custom Prompt");
      setSize(400, getRootPane().getPreferredSize().height);
      init();
    }

    @Nullable
    public JComponent getPreferredFocusedComponent() {
      return userPromptTextArea;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
      userPromptTextArea.setLineWrap(true);
      userPromptTextArea.setWrapStyleWord(true);
      userPromptTextArea.setMargin(JBUI.insets(5));
      UIUtil.addShiftEnterInputMap(userPromptTextArea, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          clickDefaultButton();
        }
      });

      return FormBuilder.createFormBuilder()
          .addComponent(UI.PanelFactory.panel(userPromptTextArea)
              .withLabel("Prefix:")
              .moveLabelOnTop()
              .withComment("Example: Find bugs in the following code")
              .createPanel())
          .getPanel();
    }

    public String getUserPrompt() {
      return userPromptTextArea.getText();
    }
  }
}
