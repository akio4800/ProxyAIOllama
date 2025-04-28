package at.s2g.ai.toolwindow.chat.structure.data

import at.s2g.ai.ui.textarea.header.tag.TagDetails
import at.s2gplus.ai.psistructure.models.ClassStructure

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