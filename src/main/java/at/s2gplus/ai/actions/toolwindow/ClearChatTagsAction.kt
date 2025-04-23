package at.s2gplus.ai.actions.toolwindow

import at.s2gplus.ai.toolwindow.chat.ChatToolWindowContentManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ClearChatTagsAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project: Project = event.project ?: return
        project.getService(ChatToolWindowContentManager::class.java).clearAllTags()
    }
}