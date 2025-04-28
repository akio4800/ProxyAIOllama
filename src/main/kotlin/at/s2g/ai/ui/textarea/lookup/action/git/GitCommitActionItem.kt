package at.s2g.ai.ui.textarea.lookup.action.git

import at.s2g.ai.ui.textarea.UserInputPanel
import at.s2g.ai.ui.textarea.header.tag.GitCommitTagDetails
import at.s2g.ai.ui.textarea.lookup.action.AbstractLookupActionItem
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import git4idea.GitCommit

class GitCommitActionItem(
    private val gitCommit: GitCommit,
) : AbstractLookupActionItem() {

    val description: String = gitCommit.id.asString().take(6)

    override val displayName: String = gitCommit.subject
    override val icon = AllIcons.Vcs.CommitNode

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        userInputPanel.addTag(GitCommitTagDetails(gitCommit))
    }
}