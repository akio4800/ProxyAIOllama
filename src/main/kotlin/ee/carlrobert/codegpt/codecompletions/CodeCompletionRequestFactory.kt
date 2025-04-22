package ee.carlrobert.codegpt.codecompletions

import com.intellij.openapi.components.service
import ee.carlrobert.codegpt.settings.Placeholder.*
import ee.carlrobert.codegpt.settings.service.ollama.OllamaSettings
import ee.carlrobert.llm.client.ollama.completion.request.OllamaCompletionRequest
import ee.carlrobert.llm.client.ollama.completion.request.OllamaParameters

object CodeCompletionRequestFactory {

    private const val MAX_TOKENS = 128

    fun buildOllamaRequest(details: InfillRequest): OllamaCompletionRequest {
        val settings = service<OllamaSettings>().state
        val stopTokens = buildList {
            if (details.stopTokens.isNotEmpty()) addAll(details.stopTokens)
        }.toMutableList()
        val prompt = if (settings.fimOverride) {
            settings.fimTemplate.stopTokens?.let { stopTokens.addAll(it) }
            settings.fimTemplate.buildPrompt(details)
        } else {
            details.prefix
        }

        return OllamaCompletionRequest.Builder(
            settings.model,
            prompt
        )
            .setSuffix(if (settings.fimOverride) null else details.suffix)
            .setStream(true)
            .setOptions(
                OllamaParameters.Builder()
                    .stop(stopTokens.ifEmpty { null })
                    .numPredict(MAX_TOKENS)
                    .temperature(0.4)
                    .build()
            )
            .setRaw(true)
            .build()
    }

    private fun transformValue(
        value: Any,
        template: InfillPromptTemplate,
        details: InfillRequest
    ): Any {
        if (value !is String) return value

        return when (value) {
            FIM_PROMPT.code -> template.buildPrompt(details)
            PREFIX.code -> details.prefix
            SUFFIX.code -> details.suffix
            else -> {
                return value.takeIf { it.contains(PREFIX.code) || it.contains(SUFFIX.code) }
                    ?.replace(PREFIX.code, details.prefix)
                    ?.replace(SUFFIX.code, details.suffix) ?: value
            }
        }
    }
}
