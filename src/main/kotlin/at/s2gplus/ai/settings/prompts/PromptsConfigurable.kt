package at.s2gplus.ai.settings.prompts

import at.s2gplus.ai.actions.editor.EditorActionsUtil
import at.s2gplus.ai.settings.prompts.form.PromptsForm
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class PromptsConfigurable : Configurable {

    private lateinit var component: PromptsForm

    override fun getDisplayName(): String {
        return "ProxyAI: Prompts"
    }

    override fun createComponent(): JComponent {
        component = PromptsForm()
        return component.createPanel()
    }

    override fun isModified(): Boolean = component.isModified()

    override fun apply() {
        component.applyChanges()
        EditorActionsUtil.refreshActions()
    }

    override fun reset() {
        component.resetChanges()
    }
}