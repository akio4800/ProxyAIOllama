package at.s2gplus.ai.ui.textarea.lookup.group

import at.s2gplus.ai.CodeGPTBundle
import at.s2gplus.ai.settings.GeneralSettings
import at.s2gplus.ai.settings.documentation.DocumentationSettings
import at.s2gplus.ai.settings.service.ServiceType
import at.s2gplus.ai.ui.DocumentationDetails
import at.s2gplus.ai.ui.textarea.header.tag.DocumentationTagDetails
import at.s2gplus.ai.ui.textarea.header.tag.TagManager
import at.s2gplus.ai.ui.textarea.lookup.LookupActionItem
import at.s2gplus.ai.ui.textarea.lookup.action.docs.AddDocActionItem
import at.s2gplus.ai.ui.textarea.lookup.action.docs.DocActionItem
import at.s2gplus.ai.ui.textarea.lookup.action.docs.ViewAllDocsActionItem
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import java.time.Instant
import java.time.format.DateTimeParseException

class DocsGroupItem(
    private val tagManager: TagManager
) : AbstractLookupGroupItem() {

    override val displayName: String = CodeGPTBundle.get("suggestionGroupItem.docs.displayName")
    override val icon = AllIcons.Toolwindows.Documentation
    override val enabled: Boolean
        get() = enabled()

    fun enabled(): Boolean {
        if (GeneralSettings.getSelectedService() != ServiceType.CODEGPT) {
            return false
        }

        return tagManager.getTags().none { it is DocumentationTagDetails }
    }

    override suspend fun getLookupItems(searchText: String): List<LookupActionItem> =
        listOf(AddDocActionItem(), ViewAllDocsActionItem()) +
                service<DocumentationSettings>().state.documentations
                    .sortedByDescending { parseDateTime(it.lastUsedDateTime) }
                    .filter {
                        searchText.isEmpty() || (it.name?.contains(searchText, true) ?: false)
                    }
                    .take(10)
                    .map {
                        DocActionItem(DocumentationDetails(it.name ?: "", it.url ?: ""))
                    }

    private fun parseDateTime(dateTimeString: String?): Instant {
        return dateTimeString?.let {
            try {
                Instant.parse(it)
            } catch (e: DateTimeParseException) {
                Instant.EPOCH
            }
        } ?: Instant.EPOCH
    }
}