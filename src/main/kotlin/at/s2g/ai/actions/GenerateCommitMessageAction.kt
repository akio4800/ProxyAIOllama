package at.s2g.ai.actions

import at.s2g.ai.completions.CommitMessageCompletionParameters
import at.s2g.ai.completions.CompletionRequestService
import at.s2g.ai.settings.prompts.CommitMessageTemplate
import at.s2g.ai.codecompletions.CompletionProgressNotifier
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