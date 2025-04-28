package at.s2g.ai.completions.factory

import at.s2g.ai.completions.BaseRequestFactory
import at.s2g.ai.completions.ChatCompletionParameters
import at.s2g.ai.completions.factory.OpenAIRequestFactory.Companion.buildOpenAIMessages
import at.s2g.ai.settings.configuration.ConfigurationSettings
import at.s2g.ai.settings.service.ollama.OllamaSettings
import com.intellij.openapi.components.service
import ee.carlrobert.llm.client.openai.completion.request.OpenAIChatCompletionRequest
import ee.carlrobert.llm.completion.CompletionRequest

class OllamaRequestFactory : BaseRequestFactory() {

    override fun createChatRequest(params: ChatCompletionParameters): OpenAIChatCompletionRequest {
        val model = service<OllamaSettings>().state.model
        val configuration = service<ConfigurationSettings>().state
        val requestBuilder: OpenAIChatCompletionRequest.Builder =
            OpenAIChatCompletionRequest.Builder(
                buildOpenAIMessages(
                    model = model,
                    callParameters = params,
                    referencedFiles = params.referencedFiles,
                    psiStructure = params.psiStructure,
                )
            )
                .setModel(model)
                .setMaxTokens(configuration.maxTokens)
                .setStream(true)
                .setTemperature(configuration.temperature.toDouble())
        return requestBuilder.build()
    }

    override fun createBasicCompletionRequest(
        systemPrompt: String,
        userPrompt: String,
        maxTokens: Int,
        stream: Boolean
    ): CompletionRequest {
        val model = service<OllamaSettings>().state.model
        return OpenAIRequestFactory.createBasicCompletionRequest(
            systemPrompt,
            userPrompt,
            model = model,
            isStream = stream
        )
    }
}
