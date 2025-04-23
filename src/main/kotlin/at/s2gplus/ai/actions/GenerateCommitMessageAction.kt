package at.s2gplus.ai.actions

import com.intellij.openapi.project.Project
import com.intellij.vcs.commit.CommitWorkflowUi
import at.s2gplus.ai.codecompletions.CompletionProgressNotifier
import at.s2gplus.ai.completions.CommitMessageCompletionParameters
import at.s2gplus.ai.completions.CompletionRequestService
import at.s2gplus.ai.settings.prompts.CommitMessageTemplate

class GenerateCommitMessageAction : BaseCommitWorkflowAction() {

    override fun getTitle(commitWorkflowUi: CommitWorkflowUi): String {
        return "Generate Message"
    }

    override fun performAction(
        project: Project,
        commitWorkflowUi: CommitWorkflowUi,
        gitDiff: String
    ) {
        CompletionProgressNotifier.update(project, true)
        CompletionRequestService.getInstance().getCommitMessageAsync(
            CommitMessageCompletionParameters(
                gitDiff,
                project.getService(CommitMessageTemplate::class.java).getSystemPrompt()
            ),
            CommitMessageEventListener(project, commitWorkflowUi)
        )
    }
}