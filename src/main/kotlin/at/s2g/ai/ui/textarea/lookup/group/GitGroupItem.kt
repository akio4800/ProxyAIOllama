package at.s2g.ai.ui.textarea.lookup.group

import at.s2g.ai.CodeGPTBundle
import at.s2g.ai.Icons
import at.s2g.ai.ui.textarea.lookup.DynamicLookupGroupItem
import at.s2g.ai.ui.textarea.lookup.LookupActionItem
import at.s2g.ai.ui.textarea.lookup.LookupUtil
import at.s2g.ai.ui.textarea.lookup.action.git.GitCommitActionItem
import at.s2g.ai.ui.textarea.lookup.action.git.IncludeCurrentChangesActionItem
import at.s2g.ai.util.GitUtil
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.swing.Icon

class GitGroupItem(private val project: Project) : AbstractLookupGroupItem(), DynamicLookupGroupItem {

    override val displayName: String = CodeGPTBundle.get("suggestionGroupItem.git.displayName")
    override val icon: Icon = Icons.VCS

    override suspend fun updateLookupList(lookup: LookupImpl, searchText: String) {
        withContext(Dispatchers.Default) {
            GitUtil.getProjectRepository(project)?.let {
                GitUtil.visitRepositoryCommits(project, it) { commit ->
                    if (commit.id.asString().contains(searchText, true)
                        || commit.fullMessage.contains(searchText, true)
                    ) {
                        runInEdt {
                            LookupUtil.addLookupItem(lookup, GitCommitActionItem(commit))
                        }
                    }
                }
            }
        }
    }

    override suspend fun getLookupItems(searchText: String): List<LookupActionItem> {
        return withContext(Dispatchers.Default) {
            GitUtil.getProjectRepository(project)?.let {
                val recentCommits = GitUtil.getAllRecentCommits(project, it, searchText)
                    .take(10)
                    .map { commit -> GitCommitActionItem(commit) }
                listOf(IncludeCurrentChangesActionItem()) + recentCommits
            } ?: emptyList()
        }
    }
}