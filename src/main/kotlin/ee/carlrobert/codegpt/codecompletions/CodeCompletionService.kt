package ee.carlrobert.codegpt.codecompletions

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import ee.carlrobert.codegpt.codecompletions.CodeCompletionRequestFactory.buildOllamaRequest
import ee.carlrobert.codegpt.completions.CompletionClientProvider
import ee.carlrobert.codegpt.settings.GeneralSettings
import ee.carlrobert.codegpt.settings.service.ServiceType
import ee.carlrobert.codegpt.settings.service.ServiceType.OLLAMA
import ee.carlrobert.codegpt.settings.service.ollama.OllamaSettings
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
                .getCompletionAsync(buildOllamaRequest(infillRequest), eventListener)
            else -> throw IllegalArgumentException("Code completion not supported for ${selectedService.name}")
        }
}
