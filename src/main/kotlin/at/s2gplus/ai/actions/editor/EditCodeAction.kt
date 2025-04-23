package at.s2gplus.ai.actions.editor

import at.s2gplus.ai.Icons
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import at.s2gplus.ai.ui.EditCodePopover
import javax.swing.Icon

open class EditCodeAction(icon: Icon) : BaseEditorAction(icon) {
    override fun actionPerformed(project: Project, editor: Editor, selectedText: String) {
        runInEdt {
            EditCodePopover(editor).show()
        }
    }
}

class EditCodeFloatingMenuAction : EditCodeAction(Icons.DefaultSmall)

class EditCodeContextMenuAction : EditCodeAction(Icons.Sparkle)
