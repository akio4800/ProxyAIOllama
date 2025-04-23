package at.s2gplus.ai.ui.textarea.lookup.group

import at.s2gplus.ai.CodeGPTBundle
import at.s2gplus.ai.settings.prompts.PersonaDetails
import at.s2gplus.ai.settings.prompts.PromptsSettings
import at.s2gplus.ai.ui.textarea.header.tag.PersonaTagDetails
import at.s2gplus.ai.ui.textarea.header.tag.TagManager
import at.s2gplus.ai.ui.textarea.lookup.LookupActionItem
import at.s2gplus.ai.ui.textarea.lookup.action.personas.AddPersonaActionItem
import at.s2gplus.ai.ui.textarea.lookup.action.personas.PersonaActionItem
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service

class PersonasGroupItem(private val tagManager: TagManager) :
    AbstractLookupGroupItem() {

    override val displayName: String = CodeGPTBundle.get("suggestionGroupItem.personas.displayName")
    override val icon = AllIcons.General.User
    override val enabled: Boolean
        get() = tagManager.getTags().none { it is PersonaTagDetails }

    override suspend fun getLookupItems(searchText: String): List<LookupActionItem> {
        return listOf(AddPersonaActionItem()) + service<PromptsSettings>().state.personas.prompts
            .map {
                PersonaDetails(it.id, it.name ?: "Unknown", it.instructions ?: "Unknown")
            }
            .filter {
                searchText.isEmpty() || it.name.contains(searchText, true)
            }
            .map { PersonaActionItem(it) }
            .take(10)
    }
}