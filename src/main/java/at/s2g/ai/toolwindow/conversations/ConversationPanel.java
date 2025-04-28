package at.s2g.ai.toolwindow.conversations;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.JBUI;

import at.s2g.ai.actions.toolwindow.DeleteConversationAction;
import at.s2g.ai.conversations.Conversation;
import at.s2g.ai.conversations.ConversationsState;
import at.s2g.ai.settings.GeneralSettings;
import at.s2g.ai.toolwindow.chat.ChatToolWindowContentManager;
import at.s2g.ai.ui.IconActionButton;
import at.s2g.ai.ui.ModelIconLabel;

class ConversationPanel extends JPanel {

  ConversationPanel(
      @NotNull Project project,
      @NotNull Conversation conversation,
      @NotNull Runnable onDelete) {
    super(new BorderLayout());
    var toolWindowContentManager = project.getService(ChatToolWindowContentManager.class);
    init(toolWindowContentManager, conversation, onDelete);
  }

  private void init(
      ChatToolWindowContentManager toolWindowContentManager,
      Conversation conversation,
      Runnable onDelete) {
    setBackground(JBColor.background());
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        GeneralSettings.getInstance().sync(conversation);
        toolWindowContentManager.displayConversation(conversation);
      }
    });
    addStyles(isSelected(conversation));
    addTextPanel(conversation, onDelete);
    setCursor(new Cursor(Cursor.HAND_CURSOR));
  }

  private boolean isSelected(Conversation conversation) {
    var currentConversation = ConversationsState.getCurrentConversation();
    return currentConversation != null && currentConversation.getId().equals(conversation.getId());
  }

  private void addStyles(boolean isSelected) {
    var border = isSelected
        ? JBUI.Borders.customLine(JBUI.CurrentTheme.ActionButton.focusedBorder(), 2, 2, 2, 2)
        : JBUI.Borders.customLine(JBColor.border(), 1, 0, 1, 0);
    setBorder(JBUI.Borders.compound(border, JBUI.Borders.empty(8)));
    setLayout(new GridBagLayout());
    setCursor(new Cursor(Cursor.HAND_CURSOR));
  }

  private void addTextPanel(Conversation conversation, Runnable onDelete) {
    var constraints = new GridBagConstraints();
    constraints.gridx = 1;
    constraints.weightx = 1.0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    add(createTextPanel(conversation, onDelete), constraints);
  }

  private JPanel createTextPanel(Conversation conversation, Runnable onDelete) {
    var headerPanel = new JPanel(new GridBagLayout());
    headerPanel.setBorder(JBUI.Borders.emptyBottom(12));

    var gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.gridx = 0;

    headerPanel.add(new JBLabel(getFirstPrompt(conversation))
        .withFont(JBFont.label().asBold()), gbc);

    gbc.gridx = 1;
    gbc.weightx = 0;
    headerPanel.add(new IconActionButton(new DeleteConversationAction(onDelete), "DELETE"), gbc);

    var bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(new JLabel(conversation.getUpdatedOn()
        .format(DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a"))), BorderLayout.WEST);
    if (conversation.getModel() != null) {
      bottomPanel.add(
          new ModelIconLabel(conversation.getClientCode(), conversation.getModel()),
          BorderLayout.EAST);
    }

    var textPanel = new JPanel(new BorderLayout());
    textPanel.add(headerPanel, BorderLayout.NORTH);
    textPanel.add(bottomPanel, BorderLayout.SOUTH);
    return textPanel;
  }

  private String getFirstPrompt(Conversation conversation) {
    var messages = conversation.getMessages();
    if (messages.isEmpty()) {
      return "";
    }
    return messages.get(0).getPrompt();
  }
}
