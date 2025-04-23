package at.s2gplus.ai.ui.textarea.lookup.action.docs

import at.s2gplus.ai.settings.GeneralSettings
import at.s2gplus.ai.settings.documentation.DocumentationSettings
import at.s2gplus.ai.settings.service.ServiceType
import at.s2gplus.ai.ui.DocumentationDetails
import at.s2gplus.ai.ui.textarea.UserInputPanel
import at.s2gplus.ai.ui.textarea.header.tag.DocumentationTagDetails
import at.s2gplus.ai.ui.textarea.lookup.action.AbstractLookupActionItem
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

class DocActionItem(
    private val documentationDetails: DocumentationDetails
) : AbstractLookupActionItem() {

    override val displayName = documentationDetails.name
    override val icon = AllIcons.Toolwindows.Documentation
    override val enabled = GeneralSettings.getSelectedService() == ServiceType.CODEGPT

    override fun setPresentation(element: LookupElement, presentation: LookupElementPresentation) {
        super.setPresentation(element, presentation)

        presentation.typeText = documentationDetails.url
        presentation.isTypeGrayed = true
    }

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        service<DocumentationSettings>().updateLastUsedDateTime(documentationDetails.url)
        userInputPanel.addTag(DocumentationTagDetails(documentationDetails))
    }
}