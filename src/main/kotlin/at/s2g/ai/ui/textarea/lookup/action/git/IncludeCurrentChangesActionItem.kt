package at.s2g.ai.ui.textarea.lookup.action.git

import at.s2g.ai.CodeGPTBundle
import at.s2g.ai.ui.textarea.UserInputPanel
import at.s2g.ai.ui.textarea.header.tag.CurrentGitChangesTagDetails
import at.s2g.ai.ui.textarea.lookup.action.AbstractLookupActionItem
import com.intellij.openapi.project.Project
import javax.swing.Icon

class IncludeCurrentChangesActionItem : AbstractLookupActionItem() {

    override val displayName: String =
        CodeGPTBundle.get("suggestionActionItem.includeCurrentChanges.displayName")
    override val icon: Icon? = null

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        userInputPanel.addTag(CurrentGitChangesTagDetails())
    }
}