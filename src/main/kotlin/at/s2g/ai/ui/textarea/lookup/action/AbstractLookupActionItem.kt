package at.s2g.ai.ui.textarea.lookup.action

import at.s2g.ai.ui.textarea.lookup.AbstractLookupItem
import at.s2g.ai.ui.textarea.lookup.LookupActionItem
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementPresentation

abstract class AbstractLookupActionItem : AbstractLookupItem(), LookupActionItem {

    override fun setPresentation(element: LookupElement, presentation: LookupElementPresentation) {
        presentation.icon = icon
        presentation.itemText = displayName
        presentation.isItemTextBold = false
    }

    override fun getLookupString(): String {
        return "action_${displayName.replace(" ", "_").lowercase()}"
    }
}