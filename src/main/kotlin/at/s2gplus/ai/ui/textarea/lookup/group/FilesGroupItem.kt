package at.s2gplus.ai.ui.textarea.lookup.group

import at.s2gplus.ai.CodeGPTBundle
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.icons.AllIcons
import com.intellij.openapi.application.readAction
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import at.s2gplus.ai.ui.textarea.header.tag.FileTagDetails
import at.s2gplus.ai.ui.textarea.header.tag.TagManager
import at.s2gplus.ai.ui.textarea.header.tag.TagUtil
import at.s2gplus.ai.ui.textarea.lookup.DynamicLookupGroupItem
import at.s2gplus.ai.ui.textarea.lookup.LookupActionItem
import at.s2gplus.ai.ui.textarea.lookup.LookupUtil
import at.s2gplus.ai.ui.textarea.lookup.action.files.FileActionItem
import at.s2gplus.ai.ui.textarea.lookup.action.files.IncludeOpenFilesActionItem

class FilesGroupItem(
    private val project: Project,
    private val tagManager: TagManager
) : AbstractLookupGroupItem(), DynamicLookupGroupItem {

    override val displayName: String = CodeGPTBundle.get("suggestionGroupItem.files.displayName")
    override val icon = AllIcons.FileTypes.Any_type

    override suspend fun updateLookupList(lookup: LookupImpl, searchText: String) {
        project.service<ProjectFileIndex>().iterateContent {
            if (!it.isDirectory && !containsTag(it)) {
                runInEdt {
                    LookupUtil.addLookupItem(lookup, FileActionItem(project, it))
                }
            }
            true
        }
    }

    override suspend fun getLookupItems(searchText: String): List<LookupActionItem> {
        return readAction {
            val projectFileIndex = project.service<ProjectFileIndex>()
            project.service<FileEditorManager>().openFiles
                .filter { projectFileIndex.isInContent(it) && !containsTag(it) }
                .toFileSuggestions()
        }
    }

    private fun containsTag(file: VirtualFile): Boolean {
        return tagManager.containsTag(file)
    }

    private fun Iterable<VirtualFile>.toFileSuggestions(): List<LookupActionItem> {
        val selectedFileTags = TagUtil.getExistingTags(project, FileTagDetails::class.java)
        return filter { file -> selectedFileTags.none { it.virtualFile == file } }
            .take(10)
            .map { FileActionItem(project, it) } + listOf(IncludeOpenFilesActionItem())
    }
}