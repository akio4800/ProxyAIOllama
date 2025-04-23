package at.s2gplus.ai.toolwindow.chat.structure.data

import at.s2gplus.ai.psistructure.models.ClassStructure
import at.s2gplus.ai.ui.textarea.header.tag.TagDetails

sealed class PsiStructureState {

    data class UpdateInProgress(
        val currentlyAnalyzedTags: Set<TagDetails>,
    ) : PsiStructureState()

    data object Disabled : PsiStructureState()

    data class Content(
        val currentlyAnalyzedTags: Set<TagDetails>,
        val elements: Set<ClassStructure>
    ) : PsiStructureState()
}