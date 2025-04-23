package at.s2gplus.ai.ui.textarea.lookup.action.personas

import at.s2gplus.ai.settings.prompts.PersonaDetails
import at.s2gplus.ai.ui.textarea.UserInputPanel
import at.s2gplus.ai.ui.textarea.header.tag.PersonaTagDetails
import at.s2gplus.ai.ui.textarea.lookup.action.AbstractLookupActionItem
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project

class PersonaActionItem(
    private val personaDetails: PersonaDetails
) : AbstractLookupActionItem() {

    override val displayName = personaDetails.name
    override val icon = AllIcons.General.User

    override fun execute(project: Project, userInputPanel: UserInputPanel) {
        userInputPanel.addTag(PersonaTagDetails(personaDetails))
    }
}
