package at.s2gplus.ai.toolwindow.ui

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory
import com.intellij.ui.SimpleColoredComponent
import com.intellij.ui.popup.PopupFactoryImpl
import com.intellij.ui.popup.list.PopupListElementRenderer
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.*

class ModelListPopup(
    actionGroup: ActionGroup,
    context: DataContext
) : PopupFactoryImpl.ActionGroupPopup(
    null,
    actionGroup,
    context,
    false,
    false,
    true,
    false,
    null,
    -1,
    null,
    null,
    MenuItemPresentationFactory(),
    false
) {

    override fun getListElementRenderer(): ListCellRenderer<*> {
        return object : PopupListElementRenderer<Any>(this) {
            private lateinit var secondaryLabel: SimpleColoredComponent

            override fun createLabel() {
                super.createLabel()
                secondaryLabel = SimpleColoredComponent()
            }

            override fun createItemComponent(): JComponent? {
                createLabel()
                val panel = JPanel(BorderLayout()).apply {
                    add(myTextLabel, BorderLayout.WEST)
                    add(secondaryLabel, BorderLayout.EAST)
                }
                myIconBar = createIconBar()
                return layoutComponent(panel)
            }

            override fun createIconBar(): JComponent? {
                return Box.createHorizontalBox().apply {
                    border = JBUI.Borders.emptyRight(JBUI.CurrentTheme.ActionsList.elementIconGap())
                    add(myIconLabel)
                }
            }

            override fun customizeComponent(
                list: JList<out Any>?,
                value: Any?,
                isSelected: Boolean
            ) {
                super.customizeComponent(list, value, isSelected)
                setupSecondaryLabel()
            }

            private fun setupSecondaryLabel() {
                secondaryLabel.apply {
                    font = JBUI.Fonts.toolbarSmallComboBoxFont()
                    border = JBUI.Borders.emptyLeft(8)
                    clear()
                }
            }
        }
    }
}
