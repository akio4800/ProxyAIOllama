package at.s2g.ai.ui.textarea.lookup.action.docs

import at.s2g.ai.CodeGPTBundle
import at.s2g.ai.settings.GeneralSettings
import at.s2g.ai.settings.documentation.DocumentationsConfigurable
import at.s2g.ai.settings.service.ServiceType
import at.s2g.ai.ui.textarea.UserInputPanel
import at.s2g.ai.ui.textarea.lookup.action.AbstractLookupActionItem
import com.intellij.openapi.components.service
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project

class ViewAllDocsActionItem : AbstractLookupActionItem() {

    override val displayName: String =
        "${CodeGPTBundle.get("suggestionActionItem.viewDocumentations.displayName")} →"
    override val icon = null
    override val enabled = GeneralSettings.getSelectedService() == ServiceType.CODEGPT

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        service<ShowSettingsUtil>().showSettingsDialog(
            project,
            DocumentationsConfigurable::class.java
        )
    }
}