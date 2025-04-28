package at.s2g.ai.toolwindow.chat.ui.textarea;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.ui.popup.ListPopup;

import at.s2g.ai.Icons;
import at.s2g.ai.settings.GeneralSettings;
import at.s2g.ai.settings.service.ProviderChangeNotifier;
import at.s2g.ai.settings.service.ServiceType;
import at.s2g.ai.settings.service.ollama.OllamaSettings;
import at.s2g.ai.toolwindow.ui.ModelListPopup;

import static at.s2g.ai.settings.service.ServiceType.OLLAMA;

public class ModelComboBoxAction extends ComboBoxAction {

  private static final Logger LOG = Logger.getInstance(ModelComboBoxAction.class);

  private final Consumer<ServiceType> onModelChange;
  private final Project project;
  private final List<ServiceType> availableProviders;

  public ModelComboBoxAction(
      Project project,
      Consumer<ServiceType> onModelChange,
      ServiceType selectedService) {
    this(project, onModelChange, selectedService, List.of(OLLAMA));
  }

  public ModelComboBoxAction(
      Project project,
      Consumer<ServiceType> onModelChange,
      ServiceType selectedProvider,
      List<ServiceType> availableProviders) {
    this.project = project;
    this.onModelChange = onModelChange;
    this.availableProviders = availableProviders;
    setSmallVariant(true);
    updateTemplatePresentation(selectedProvider);
    ApplicationManager.getApplication().getMessageBus()
        .connect()
        .subscribe(
            ProviderChangeNotifier.getTOPIC(),
            (ProviderChangeNotifier) this::updateTemplatePresentation);
  }

  public JComponent createCustomComponent(@NotNull String place) {
    return createCustomComponent(getTemplatePresentation(), place);
  }

  @NotNull
  @Override
  public JComponent createCustomComponent(
      @NotNull Presentation presentation,
      @NotNull String place) {
    ComboBoxButton button = createComboBoxButton(presentation);
    button.setForeground(
        EditorColorsManager.getInstance().getGlobalScheme().getDefaultForeground());
    button.setBorder(null);
    button.putClientProperty("JButton.backgroundColor", new Color(0, 0, 0, 0));
    return button;
  }

  @Override
  protected JBPopup createActionPopup(DefaultActionGroup group, @NotNull DataContext context,
      @Nullable Runnable disposeCallback) {
    ListPopup popup = new ModelListPopup(group, context);
    if (disposeCallback != null) {
      popup.addListener(new JBPopupListener() {
        @Override
        public void onClosed(@NotNull LightweightWindowEvent event) {
          disposeCallback.run();
        }
      });
    }
    popup.setShowSubmenuOnHover(true);
    return popup;
  }

  @Override
  protected @NotNull DefaultActionGroup createPopupActionGroup(JComponent button) {
    var presentation = ((ComboBoxButton) button).getPresentation();
    var actionGroup = new DefaultActionGroup();
    if (availableProviders.contains(OLLAMA)) {
      var ollamaGroup = DefaultActionGroup.createPopupGroup(() -> "Ollama");
      ollamaGroup.getTemplatePresentation().setIcon(Icons.Ollama);
      ApplicationManager.getApplication()
          .getService(OllamaSettings.class)
          .getState()
          .getAvailableModels()
          .forEach(model ->
              ollamaGroup.add(createOllamaModelAction(model, presentation)));
      actionGroup.add(ollamaGroup);
    }

    return actionGroup;
  }

  @Override
  protected boolean shouldShowDisabledActions() {
    return true;
  }

  private void updateTemplatePresentation(ServiceType selectedService) {
    var application = ApplicationManager.getApplication();
    var templatePresentation = getTemplatePresentation();
    switch (selectedService) {
        case OLLAMA:
        templatePresentation.setIcon(Icons.Ollama);
        templatePresentation.setText(application.getService(OllamaSettings.class)
            .getState()
            .getModel());
        break;
      default:
        break;
    }
  }


  private AnAction createModelAction(
      ServiceType serviceType,
      String label,
      Icon icon,
      Presentation comboBoxPresentation) {
    return createModelAction(serviceType, label, icon, comboBoxPresentation, null);
  }

  private AnAction createModelAction(
      ServiceType serviceType,
      String label,
      Icon icon,
      Presentation comboBoxPresentation,
      Runnable onModelChanged) {
    return new DumbAwareAction(label, "", icon) {
      @Override
      public void update(@NotNull AnActionEvent event) {
        var presentation = event.getPresentation();
        presentation.setEnabled(!presentation.getText().equals(comboBoxPresentation.getText()));
      }

      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        if (onModelChanged != null) {
          onModelChanged.run();
        }
        handleModelChange(serviceType);
      }

      @Override
      public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
      }
    };
  }

  private void handleModelChange(
      ServiceType serviceType) {
    GeneralSettings.getCurrentState().setSelectedService(serviceType);
    updateTemplatePresentation(serviceType);
    onModelChange.accept(serviceType);
  }

  private AnAction createOllamaModelAction(String model, Presentation comboBoxPresentation) {
    return createModelAction(OLLAMA, model, Icons.Ollama, comboBoxPresentation,
        () -> ApplicationManager.getApplication()
            .getService(OllamaSettings.class)
            .getState()
            .setModel(model));
  }

}
