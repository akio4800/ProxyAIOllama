package at.s2g.ai.codecompletions

import at.s2g.ai.Icons
import com.intellij.codeInsight.inline.completion.InlineCompletionProviderPresentation
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.components.service
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLabel
import javax.swing.JComponent
import javax.swing.SwingConstants

class CodeCompletionProviderPresentation : InlineCompletionProviderPresentation {

    override fun getTooltip(project: Project?): JComponent {
        val selectedModelCode =
            project?.service<CodeCompletionService>()?.getSelectedModelCode() ?: ""
        val text = if (selectedModelCode.isNotEmpty()) {
            buildString {
                append("<html>Model: (<strong>$selectedModelCode</strong>) | ")
                append("Accept Word: (<strong>${getShortcutText(AcceptNextWordInlayAction.ID)}</strong>) | ")
                append("Accept Line: (<strong>${getShortcutText(AcceptNextLineInlayAction.ID)}</strong>)</html>")
            }
        } else {
            "ProxyAI"
        }

        return JBLabel(text, Icons.DefaultSmall, SwingConstants.LEADING)
    }

    private fun getShortcutText(actionId: String): String {
        return KeymapUtil.getFirstKeyboardShortcutText(
            ActionManager.getInstance().getAction(actionId)
        )
    }
}