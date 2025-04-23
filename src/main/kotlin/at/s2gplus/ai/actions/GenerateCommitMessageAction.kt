package at.s2gplus.ai.actions

import at.s2gplus.ai.codecompletions.CompletionProgressNotifier
import at.s2gplus.ai.completions.CommitMessageCompletionParameters
import at.s2gplus.ai.completions.CompletionRequestService
import at.s2gplus.ai.settings.prompts.CommitMessageTemplate
import com.intellij.openapi.project.Project
import com.intellij.vcs.commit.CommitWorkflowUi

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