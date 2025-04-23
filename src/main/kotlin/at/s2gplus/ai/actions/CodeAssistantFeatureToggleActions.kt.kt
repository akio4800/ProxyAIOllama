package at.s2gplus.ai.actions

import at.s2gplus.ai.settings.GeneralSettings
import at.s2gplus.ai.settings.service.ServiceType.CODEGPT
import at.s2gplus.ai.settings.service.codegpt.CodeGPTServiceSettings
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction

abstract class CodeAssistantFeatureToggleAction(
    private val enableFeatureAction: Boolean
) : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val settings = service<CodeGPTServiceSettings>().state
        settings.nextEditsEnabled = enableFeatureAction
    }

    override fun update(e: AnActionEvent) {
        val codeAssistantEnabled = service<CodeGPTServiceSettings>().state.nextEditsEnabled

        e.presentation.isVisible = GeneralSettings.getSelectedService() == CODEGPT
                    && codeAssistantEnabled != enableFeatureAction
        e.presentation.isEnabled = GeneralSettings.getSelectedService() == CODEGPT
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}

class EnableNextEditsAction : CodeAssistantFeatureToggleAction(true)

class DisableNextEditsAction : CodeAssistantFeatureToggleAction(false)