package at.s2g.ai.settings.service.openai;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

@State(name = "CodeGPT_OpenAISettings_210", storages = @Storage("CodeGPT_OpenAISettings_210.xml"))
public class OpenAISettings implements PersistentStateComponent<OpenAISettingsState> {

  private OpenAISettingsState state = new OpenAISettingsState();

  @Override
  @NotNull
  public OpenAISettingsState getState() {
    return state;
  }

  @Override
  public void loadState(@NotNull OpenAISettingsState state) {
    this.state = state;
  }

  public static OpenAISettingsState getCurrentState() {
    return getInstance().getState();
  }

  public static OpenAISettings getInstance() {
    return ApplicationManager.getApplication().getService(OpenAISettings.class);
  }
}
