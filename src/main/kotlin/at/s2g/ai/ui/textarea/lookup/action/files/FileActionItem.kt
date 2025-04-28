package at.s2g.ai.ui.textarea.lookup.action.files

import at.s2g.ai.ui.textarea.UserInputPanel
import at.s2g.ai.ui.textarea.header.tag.EditorTagDetails
import at.s2g.ai.ui.textarea.lookup.action.AbstractLookupActionItem
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

class FileActionItem(private val project: Project, val file: VirtualFile) :
    AbstractLookupActionItem() {

    override val displayName = file.name
    override val icon = file.fileType.icon ?: AllIcons.FileTypes.Any_type

    override fun setPresentation(element: LookupElement, presentation: LookupElementPresentation) {
        super.setPresentation(element, presentation)

        val projectDir = project.guessProjectDir()
        presentation.typeText = if (projectDir != null) {
            VfsUtil.getRelativePath(file, projectDir) ?: file.path
        } else {
            file.path
        }
        presentation.isTypeGrayed = true
    }

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        userInputPanel.addTag(EditorTagDetails(file))
    }
}