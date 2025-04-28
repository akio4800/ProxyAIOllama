package at.s2g.ai.actions;

import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons.General;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;

import at.s2g.ai.CodeGPTBundle;
import at.s2g.ai.settings.service.ServiceConfigurable;

public class OpenSettingsAction extends AnAction {

  public OpenSettingsAction() {
    super(CodeGPTBundle.get("action.openSettings.title"),
        CodeGPTBundle.get("action.openSettings.description"),
        General.Settings);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), ServiceConfigurable.class);
  }
}
