package at.s2gplus.ai.toolwindow.chat.ui.textarea

import at.s2gplus.ai.EncodingManager
import at.s2gplus.ai.psistructure.ClassStructureSerializer
import at.s2gplus.ai.psistructure.models.ClassStructure
import at.s2gplus.ai.toolwindow.chat.structure.data.PsiStructureRepository
import at.s2gplus.ai.toolwindow.chat.structure.data.PsiStructureState
import at.s2gplus.ai.util.coroutines.CoroutineDispatchers
import at.s2gplus.ai.util.coroutines.DisposableCoroutineScope
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class PsiStructureTotalTokenProvider(
    parentDisposable: Disposable,
    private val classStructureSerializer: ClassStructureSerializer,
    private val encodingManager: EncodingManager,
    dispatchers: CoroutineDispatchers,
    psiStructureRepository: PsiStructureRepository,
    onPsiTokenHandled: (Int) -> Unit
) {

    private val coroutineScope = DisposableCoroutineScope()

    init {
        Disposer.register(parentDisposable, coroutineScope)
        psiStructureRepository.structureState
            .map { structureState ->
                when (structureState) {
                    is PsiStructureState.Content -> {
                        getPsiTokensCount(structureState.elements)
                    }

                    PsiStructureState.Disabled -> 0

                    is PsiStructureState.UpdateInProgress -> 0
                }
            }
            .flowOn(dispatchers.io())
            .onEach { psiTokens ->
                onPsiTokenHandled(psiTokens)
            }
            .launchIn(coroutineScope)
    }

    private fun getPsiTokensCount(psiStructureSet: Set<ClassStructure>): Int =
        psiStructureSet
            .joinToString(separator = "\n\n") { psiStructure ->
                classStructureSerializer.serialize(psiStructure)
            }
            .let { serializedPsiStructure ->
                encodingManager.countTokens(serializedPsiStructure)
            }
}