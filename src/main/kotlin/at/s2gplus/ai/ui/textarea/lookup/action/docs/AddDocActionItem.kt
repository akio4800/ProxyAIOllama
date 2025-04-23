package at.s2gplus.ai.ui.textarea.lookup.action.docs

import at.s2gplus.ai.CodeGPTBundle
import at.s2gplus.ai.settings.GeneralSettings
import at.s2gplus.ai.settings.documentation.DocumentationSettings
import at.s2gplus.ai.settings.service.ServiceType
import at.s2gplus.ai.ui.AddDocumentationDialog
import at.s2gplus.ai.ui.textarea.UserInputPanel
import at.s2gplus.ai.ui.textarea.header.tag.DocumentationTagDetails
import at.s2gplus.ai.ui.textarea.lookup.action.AbstractLookupActionItem
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

class AddDocActionItem : AbstractLookupActionItem() {

    override val displayName: String =
        CodeGPTBundle.get("suggestionActionItem.createDocumentation.displayName")
    override val icon = AllIcons.General.Add
    override val enabled = GeneralSettings.getSelectedService() == ServiceType.CODEGPT

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        val addDocumentationDialog = AddDocumentationDialog(project)
        if (addDocumentationDialog.showAndGet()) {
            service<DocumentationSettings>()
                .updateLastUsedDateTime(addDocumentationDialog.documentationDetails.url)
            userInputPanel.addTag(DocumentationTagDetails(addDocumentationDialog.documentationDetails))
        }
    }
}