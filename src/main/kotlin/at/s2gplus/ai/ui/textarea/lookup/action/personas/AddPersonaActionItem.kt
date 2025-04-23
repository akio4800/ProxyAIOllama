package at.s2gplus.ai.ui.textarea.lookup.action.personas

import at.s2gplus.ai.CodeGPTBundle
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import at.s2gplus.ai.settings.prompts.PromptsConfigurable
import at.s2gplus.ai.ui.textarea.UserInputPanel
import at.s2gplus.ai.ui.textarea.lookup.action.AbstractLookupActionItem

class AddPersonaActionItem : AbstractLookupActionItem() {

    override val displayName: String =
        CodeGPTBundle.get("suggestionActionItem.createPersona.displayName")
    override val icon = AllIcons.General.Add

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        service<ShowSettingsUtil>().showSettingsDialog(
            project,
            PromptsConfigurable::class.java
        )
    }
}