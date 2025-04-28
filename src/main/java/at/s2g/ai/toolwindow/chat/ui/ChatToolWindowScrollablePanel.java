package at.s2g.ai.toolwindow.chat.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.*;

import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel;
import com.intellij.openapi.roots.ui.componentsList.layout.VerticalStackLayout;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import at.s2g.ai.settings.GeneralSettings;
import at.s2g.ai.settings.service.ServiceType;
import at.s2g.ai.settings.service.ollama.OllamaSettingsConfigurable;
import at.s2g.ai.toolwindow.ui.ResponseMessagePanel;
import at.s2g.ai.ui.UIUtil;
import at.s2g.ai.util.ApplicationUtil;
import at.s2gplus.ai.credentials.CredentialsStore;
import at.s2gplus.ai.credentials.CredentialsStore.CredentialKey;

import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;

public class ChatToolWindowScrollablePanel extends ScrollablePanel {

  private final Map<UUID, JPanel> visibleMessagePanels = new HashMap<>();

  public ChatToolWindowScrollablePanel() {
    super(new VerticalStackLayout());
  }

  public void displayLandingView(JComponent landingView) {
    clearAll();
    add(landingView);
    if (GeneralSettings.isSelected(ServiceType.CODEGPT)
        && !CredentialsStore.INSTANCE.isCredentialSet(CredentialKey.CodeGptApiKey.INSTANCE)) {

      var panel = new ResponseMessagePanel();
      panel.addContent(UIUtil.createTextPane("""
              <html>
              <p style="margin-top: 4px; margin-bottom: 4px;">
                Pls configure Ollama settings:. Visit <a href="#OPEN_SETTINGS">Ollama Settings</a> to do so.
              </p>
              </html>""",
          false,
          event -> {
            if (ACTIVATED.equals(event.getEventType())
                && "#OPEN_SETTINGS".equals(event.getDescription())) {
              ShowSettingsUtil.getInstance().showSettingsDialog(
                  ApplicationUtil.findCurrentProject(),
                  OllamaSettingsConfigurable.class);
            } else {
              UIUtil.handleHyperlinkClicked(event);
            }
          }));
      panel.setBorder(JBUI.Borders.customLine(JBColor.border(), 1, 0, 0, 0));
      add(panel);
    }
  }

  public ResponseMessagePanel getResponseMessagePanel(UUID messageId) {
    return (ResponseMessagePanel) Arrays.stream(visibleMessagePanels.get(messageId).getComponents())
        .filter(ResponseMessagePanel.class::isInstance)
        .findFirst().orElseThrow();
  }

  public JPanel addMessage(UUID messageId) {
    var messageWrapper = new JPanel();
    messageWrapper.setLayout(new BoxLayout(messageWrapper, BoxLayout.PAGE_AXIS));
    add(messageWrapper);
    visibleMessagePanels.put(messageId, messageWrapper);
    return messageWrapper;
  }

  public void removeMessage(UUID messageId) {
    remove(visibleMessagePanels.get(messageId));
    update();
    visibleMessagePanels.remove(messageId);
  }

  public void clearAll() {
    visibleMessagePanels.clear();
    removeAll();
    update();
  }

  public void update() {
    repaint();
    revalidate();
  }
}
