package at.s2gplus.ai.codecompletions

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import at.s2gplus.ai.completions.CompletionClientProvider
import at.s2gplus.ai.settings.GeneralSettings
import at.s2gplus.ai.settings.service.ServiceType
import at.s2gplus.ai.settings.service.ServiceType.OLLAMA
import at.s2gplus.ai.settings.service.ollama.OllamaSettings
import ee.carlrobert.llm.completion.CompletionEventListener
import okhttp3.sse.EventSource

@Service(Service.Level.PROJECT)
class CodeCompletionService {

    // TODO: Consolidate logic in ModelComboBoxAction
    fun getSelectedModelCode(): String? {
        return when (service<GeneralSettings>().state.selectedService) {
            OLLAMA -> service<OllamaSettings>().state.model
            else -> null
        }
    }

    fun isCodeCompletionsEnabled(selectedService: ServiceType): Boolean =
        when (selectedService) {
            OLLAMA -> service<OllamaSettings>().state.codeCompletionsEnabled
            else -> false
        }

    fun getCodeCompletionAsync(
        infillRequest: InfillRequest,
        eventListener: CompletionEventListener<String>
    ): EventSource =
        when (val selectedService = GeneralSettings.getSelectedService()) {
            OLLAMA -> CompletionClientProvider.getOllamaClient()
                .getCompletionAsync(CodeCompletionRequestFactory.buildOllamaRequest(infillRequest), eventListener)
            else -> throw IllegalArgumentException("Code completion not supported for ${selectedService.name}")
        }
}
