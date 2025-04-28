package at.s2g.ai.actions

import at.s2g.ai.settings.GeneralSettings
import at.s2g.ai.settings.service.ServiceType.OLLAMA
import at.s2g.ai.settings.service.ollama.OllamaSettings
import at.s2g.ai.codecompletions.CodeCompletionService
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction


abstract class CodeCompletionFeatureToggleActions(
    private val enableFeatureAction: Boolean
) : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) = when (GeneralSettings.getSelectedService()) {
        OLLAMA -> service<OllamaSettings>().state::codeCompletionsEnabled::set
        else -> { _: Boolean -> Unit } // no-op for these services
    }(enableFeatureAction)

    override fun update(e: AnActionEvent) {
        val selectedService = GeneralSettings.getSelectedService()
        val codeCompletionEnabled =
            e.project?.service<CodeCompletionService>()?.isCodeCompletionsEnabled(selectedService)
                ?: false
        e.presentation.isVisible = codeCompletionEnabled != enableFeatureAction
        e.presentation.isEnabled = when (selectedService) {
            OLLAMA -> true
            else -> false
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}

class EnableCompletionsAction : CodeCompletionFeatureToggleActions(true)

class DisableCompletionsAction : CodeCompletionFeatureToggleActions(false)