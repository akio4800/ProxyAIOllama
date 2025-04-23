package at.s2gplus.ai.actions

import at.s2gplus.ai.completions.ConversationType
import at.s2gplus.ai.conversations.message.Message
import at.s2gplus.ai.toolwindow.chat.ChatToolWindowContentManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.vcs.commit.CommitWorkflowUi

class ReviewChangesAction : BaseCommitWorkflowAction() {

    override fun getTitle(commitWorkflowUi: CommitWorkflowUi): String {
        return if (commitWorkflowUi.getIncludedChanges().size > 1) "Review Changes"
        else "Review Change"
    }

    override fun performAction(
        project: Project,
        commitWorkflowUi: CommitWorkflowUi,
        gitDiff: String
    ) {
        project.service<ChatToolWindowContentManager>().sendMessageInNewTab(
            Message(
                buildString {
                    appendLine("Review the following changes:")
                    appendLine("```diff")
                    appendLine(gitDiff)
                    appendLine("```")
                }
            ).apply {
                referencedFilePaths = commitWorkflowUi.getIncludedChanges()
                    .mapNotNull { it.virtualFile?.path }
            }, ConversationType.REVIEW_CHANGES
        )
    }
}