package at.s2g.ai.settings;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

import at.s2g.ai.conversations.Conversation;
import at.s2g.ai.settings.service.ProviderChangeNotifier;
import at.s2g.ai.settings.service.ServiceType;
import at.s2g.ai.settings.service.ollama.OllamaSettings;
import at.s2g.ai.util.ApplicationUtil;

@State(name = "CodeGPT_GeneralSettings_270", storages = @Storage("CodeGPT_GeneralSettings_270.xml"))
public class GeneralSettings implements PersistentStateComponent<GeneralSettingsState> {

  private GeneralSettingsState state = new GeneralSettingsState();

  @Override
  @NotNull
  public GeneralSettingsState getState() {
    return state;
  }

  @Override
  public void loadState(@NotNull GeneralSettingsState state) {
    this.state = state;
  }

  public static GeneralSettingsState getCurrentState() {
    return getInstance().getState();
  }

  public static GeneralSettings getInstance() {
    return ApplicationManager.getApplication().getService(GeneralSettings.class);
  }

  public static ServiceType getSelectedService() {
    return getCurrentState().getSelectedService();
  }

  public static boolean isSelected(ServiceType serviceType) {
    return getSelectedService() == serviceType;
  }

  public void sync(Conversation conversation) {
    var project = ApplicationUtil.findCurrentProject();
    var provider = ServiceType.fromClientCode(conversation.getClientCode());
    switch (provider) {
      case OLLAMA:
        ApplicationManager.getApplication().getService(OllamaSettings.class).getState()
            .setModel(conversation.getModel());
        break;
      default:
        break;
    }
    state.setSelectedService(provider);
    if (project != null) {
      project.getMessageBus()
          .syncPublisher(ProviderChangeNotifier.getTOPIC())
          .providerChanged(provider);
    }
  }

  public String getModel() {
    switch (state.getSelectedService()) {
      case OLLAMA:
        return ApplicationManager.getApplication()
            .getService(OllamaSettings.class)
            .getState()
            .getModel();
      default:
        return "Unknown";
    }
  }
}
