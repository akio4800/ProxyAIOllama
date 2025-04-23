package at.s2gplus.ai.settings.service

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import at.s2gplus.ai.conversations.ConversationsState
import at.s2gplus.ai.settings.GeneralSettings
import at.s2gplus.ai.toolwindow.chat.ChatToolWindowContentManager
import at.s2gplus.ai.util.ApplicationUtil.findCurrentProject
import javax.swing.JComponent

class ServiceConfigurable : Configurable {

    private lateinit var component: ServiceConfigurableComponent

    override fun getDisplayName(): String {
        return "ProxyAI: Services"
    }

    override fun createComponent(): JComponent {
        component = ServiceConfigurableComponent()
        return component.getPanel()
    }

    override fun isModified(): Boolean {
        return component.getSelectedService() != service<GeneralSettings>().state.selectedService
    }

    override fun apply() {
        val state = service<GeneralSettings>().state
        state.selectedService = component.getSelectedService()

        val serviceChanged = component.getSelectedService() != state.selectedService
        if (serviceChanged) {
            resetActiveTab()
        }
    }

    private fun resetActiveTab() {
        service<ConversationsState>().currentConversation = null
        val project = findCurrentProject()
            ?: throw RuntimeException("Could not find current project.")
        project.getService(ChatToolWindowContentManager::class.java).resetAll()
    }
}