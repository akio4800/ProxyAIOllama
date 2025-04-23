package at.s2gplus.ai.ui.textarea.lookup.action

import at.s2gplus.ai.CodeGPTBundle
import at.s2gplus.ai.settings.GeneralSettings
import at.s2gplus.ai.settings.service.ServiceType
import at.s2gplus.ai.ui.textarea.UserInputPanel
import at.s2gplus.ai.ui.textarea.header.tag.TagManager
import at.s2gplus.ai.ui.textarea.header.tag.WebTagDetails
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project

class WebActionItem(private val tagManager: TagManager) : AbstractLookupActionItem() {

    override val displayName: String =
        CodeGPTBundle.get("suggestionActionItem.webSearch.displayName")
    override val icon = AllIcons.General.Web
    override val enabled: Boolean
        get() = enabled()

    fun enabled(): Boolean {
        if (GeneralSettings.getSelectedService() != ServiceType.CODEGPT) {
            return false
        }
        return tagManager.getTags().none { it is WebTagDetails }
    }

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        userInputPanel.addTag(WebTagDetails())
    }
}