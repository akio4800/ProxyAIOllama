package at.s2g.ai.toolwindow.ui

import at.s2g.ai.CodeGPTBundle
import at.s2g.ai.Icons
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBFont
import javax.swing.SwingConstants

open class ResponseMessagePanel : BaseMessagePanel() {

    override fun createDisplayNameLabel(): JBLabel {
        return JBLabel(
            CodeGPTBundle.get("project.label"),
            Icons.Default,
            SwingConstants.LEADING
        )
            .setAllowAutoWrapping(true)
            .withFont(JBFont.label().asBold())
            .apply {
                iconTextGap = 6
            }
    }
}