package at.s2gplus.ai.actions.editor

import at.s2gplus.ai.toolwindow.chat.ChatToolWindowContentManager
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project

class AddSelectionToContextAction : BaseEditorAction(AllIcons.General.Add) {

    override fun actionPerformed(project: Project, editor: Editor, selectedText: String) {
        val chatToolWindowContentManager = project.service<ChatToolWindowContentManager>()
        val chatTabPanel = chatToolWindowContentManager
            .tryFindActiveChatTabPanel()
            .orElseThrow()

        val toolwindow = chatToolWindowContentManager.toolWindow
        if (!toolwindow.isActive) {
            toolwindow.show()
        }

        chatTabPanel.addSelection(editor.virtualFile, editor.selectionModel)
    }
}
