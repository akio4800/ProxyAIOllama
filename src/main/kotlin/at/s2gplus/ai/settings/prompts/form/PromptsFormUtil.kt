package at.s2gplus.ai.settings.prompts.form

import at.s2gplus.ai.settings.prompts.ChatActionPromptDetailsState
import at.s2gplus.ai.settings.prompts.CodeAssistantPromptDetailsState
import at.s2gplus.ai.settings.prompts.CoreActionPromptDetailsState
import at.s2gplus.ai.settings.prompts.PersonaPromptDetailsState
import at.s2gplus.ai.settings.prompts.form.details.*
import javax.swing.tree.DefaultMutableTreeNode

object PromptsFormUtil {

    fun CodeAssistantPromptDetails.toState(): CodeAssistantPromptDetailsState {
        val state = CodeAssistantPromptDetailsState()
        state.code = this.code
        state.name = this.name
        state.instructions = this.instructions
        return state
    }

    fun CoreActionPromptDetails.toState(): CoreActionPromptDetailsState {
        val state = CoreActionPromptDetailsState()
        state.code = this.code
        state.name = this.name
        state.instructions = this.instructions
        return state
    }

    fun ChatActionPromptDetails.toState(): ChatActionPromptDetailsState {
        val state = ChatActionPromptDetailsState()
        state.id = this.id
        state.code = this.code
        state.name = this.name
        state.instructions = this.instructions
        return state
    }

    fun PersonaPromptDetails.toState(): PersonaPromptDetailsState {
        val state = PersonaPromptDetailsState()
        state.id = this.id
        state.name = this.name
        state.instructions = this.instructions
        state.disabled = this.disabled
        return state
    }

    inline fun <reified T : FormPromptDetails> getFormState(
        formNode: DefaultMutableTreeNode,
    ): List<T> {
        return formNode.children().toList()
            .filterIsInstance<PromptDetailsTreeNode>()
            .map { it.details }
            .filterIsInstance<T>()
    }
}