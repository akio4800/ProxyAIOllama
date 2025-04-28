package at.s2g.ai.ui.textarea.lookup.group

import at.s2g.ai.ui.textarea.lookup.AbstractLookupItem
import at.s2g.ai.ui.textarea.lookup.LookupGroupItem
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.icons.AllIcons

abstract class AbstractLookupGroupItem : AbstractLookupItem(), LookupGroupItem {

    override fun setPresentation(element: LookupElement, presentation: LookupElementPresentation) {
        presentation.itemText = displayName
        presentation.icon = icon
        presentation.setTypeText("", AllIcons.Icons.Ide.NextStep)
        presentation.isTypeIconRightAligned = true
        presentation.isItemTextBold = false
    }

    override fun getLookupString(): String {
        return "group_${displayName.replace(" ", "_").lowercase()}"
    }
}