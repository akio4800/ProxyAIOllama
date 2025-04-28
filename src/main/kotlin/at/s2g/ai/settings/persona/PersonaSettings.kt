package at.s2g.ai.settings.persona

import at.s2g.ai.settings.prompts.PersonasState
import com.intellij.openapi.components.*

@Deprecated("Use PromptsSettings instead")
@Service
@State(
    name = "CodeGPT_PersonaSettings",
    storages = [Storage("CodeGPT_PersonaSettings.xml")]
)
class PersonaSettings :
    SimplePersistentStateComponent<PersonaSettingsState>(PersonaSettingsState())

class PersonaSettingsState : BaseState() {
    var userCreatedPersonas by list<PersonaDetailsState>()
}

class PersonaDetailsState : BaseState() {
    var id by property(1L)
    var name by string("CodeGPT Default")
    var instructions by string(PersonasState.DEFAULT_PERSONA_PROMPT)
}
