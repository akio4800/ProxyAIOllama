package at.s2g.ai.codecompletions

import at.s2g.ai.EncodingManager
import at.s2g.ai.settings.configuration.ConfigurationSettings
import at.s2g.ai.util.GitUtil
import at.s2gplus.ai.codecompletions.psi.CompletionContextService
import at.s2gplus.ai.psistructure.PsiStructureProvider
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import com.intellij.openapi.application.readAction
import com.intellij.openapi.components.service
import com.intellij.refactoring.suggested.startOffset


object InfillRequestUtil {

    suspend fun buildInfillRequest(
        request: InlineCompletionRequest,
        type: CompletionType
    ): InfillRequest {
        val caretOffset = readAction { request.editor.caretModel.offset }
        val infillRequestBuilder = InfillRequest.Builder(request.document, caretOffset, type)
            .fileDetails(
                InfillRequest.FileDetails(
                    request.document.text,
                    request.file.virtualFile.extension
                )
            )

        val project = request.editor.project ?: return infillRequestBuilder.build()
        if (service<ConfigurationSettings>().state.codeCompletionSettings.gitDiffEnabled) {
            val additionalContext = GitUtil.getCurrentChanges(project)
            if (!additionalContext.isNullOrEmpty()) {
                infillRequestBuilder.additionalContext(additionalContext)
            }
        }

        if (service<ConfigurationSettings>().state.codeCompletionSettings.contextAwareEnabled) {
            getInfillContext(request, caretOffset)?.let {
                infillRequestBuilder.context(it)
                infillRequestBuilder.addRepositoryName(it.getRepoName())
            }
        }

        if (service<ConfigurationSettings>().state.codeCompletionSettings.collectDependencyStructure) {
            val psiStructure = PsiStructureProvider().get(listOf(request.file))
            if (psiStructure.isNotEmpty()) {
                infillRequestBuilder.addDependenciesStructure(psiStructure)
                infillRequestBuilder.addRepositoryName(psiStructure.first().repositoryName)
            }
        }

        return infillRequestBuilder.build()
    }

    private fun getInfillContext(
        request: InlineCompletionRequest,
        caretOffset: Int
    ): InfillContext? {
        val infillContext =
            service<CompletionContextService>().findContext(request.editor, caretOffset)
                ?: return null
        val caretInEnclosingElement =
            caretOffset - infillContext.enclosingElement.psiElement.startOffset
        val entireText = infillContext.enclosingElement.psiElement.readText()
        val prefix = entireText.take(caretInEnclosingElement)
        val suffix =
            if (entireText.length < caretInEnclosingElement) "" else entireText.takeLast(
                entireText.length - caretInEnclosingElement
            )
        return truncateContext(prefix + suffix, infillContext)
    }

    private fun truncateContext(prompt: String, infillContext: InfillContext): InfillContext {
        var promptTokens = EncodingManager.getInstance().countTokens(prompt)
        val truncatedContextElements = infillContext.contextElements.takeWhile {
            promptTokens += it.tokens
            promptTokens <= MAX_PROMPT_TOKENS
        }.toSet()
        return InfillContext(infillContext.enclosingElement, truncatedContextElements)
    }
}