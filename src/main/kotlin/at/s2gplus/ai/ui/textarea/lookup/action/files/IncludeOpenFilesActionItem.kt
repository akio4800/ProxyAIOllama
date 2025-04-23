package at.s2gplus.ai.ui.textarea.lookup.action.files

import at.s2gplus.ai.CodeGPTBundle
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import at.s2gplus.ai.Icons
import at.s2gplus.ai.ui.textarea.UserInputPanel
import at.s2gplus.ai.ui.textarea.header.tag.FileTagDetails
import at.s2gplus.ai.ui.textarea.lookup.action.AbstractLookupActionItem
import javax.swing.Icon

class IncludeOpenFilesActionItem : AbstractLookupActionItem() {
    override val displayName: String =
        CodeGPTBundle.get("suggestionActionItem.includeOpenFiles.displayName")
    override val icon: Icon = Icons.ListFiles

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        val fileTags = userInputPanel.getSelectedTags().filterIsInstance<FileTagDetails>()
        project.service<FileEditorManager>().openFiles
            .filter { openFile ->
                fileTags.none { it.virtualFile == openFile }
            }
            .forEach {
                userInputPanel.addTag(FileTagDetails(it))
            }
    }
}