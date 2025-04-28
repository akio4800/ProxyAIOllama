package at.s2g.ai.settings.service.ollama


import at.s2g.ai.settings.GeneralSettings
import at.s2g.ai.settings.service.ServiceType
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class OllamaSettingsConfigurable : Configurable {

    private lateinit var component: OllamaSettingsForm

    override fun getDisplayName(): String {
        return "ProxyAI: Ollama Service"
    }

    override fun createComponent(): JComponent {
        component = OllamaSettingsForm()
        return component.getForm()
    }

    override fun isModified(): Boolean {
        return component.isModified()
    }

    override fun apply() {
        component.applyChanges()
        service<GeneralSettings>().state.selectedService = ServiceType.OLLAMA
    }

    override fun reset() {
        component.resetForm()
    }
}