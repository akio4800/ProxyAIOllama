package at.s2g.ai.codecompletions.psi

import at.s2gplus.ai.codecompletions.InfillContext
import com.intellij.psi.PsiElement

interface LanguageContextFinder {
    /**
     * Determines relevant enclosing [PsiElement] and [PsiElement]s relevant to the context and returns their source code [PsiElement].
     */
    fun findContext(psiElement: PsiElement): InfillContext
}