package at.s2g.ai.actions

import at.s2gplus.ai.codecompletions.AcceptNextLineInlayAction
import at.s2gplus.ai.codecompletions.AcceptNextWordInlayAction
import at.s2gplus.ai.predictions.OpenPredictionAction
import at.s2gplus.ai.predictions.TriggerCustomPredictionAction
import com.intellij.codeInsight.inline.completion.session.InlineCompletionContext
import com.intellij.openapi.actionSystem.ActionPromoter
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext

class InlayActionPromoter : ActionPromoter {
    override fun promote(actions: List<AnAction>, context: DataContext): List<AnAction> {
        val editor = CommonDataKeys.EDITOR.getData(context) ?: return emptyList()

        actions.filterIsInstance<TriggerCustomPredictionAction>().takeIf { it.isNotEmpty() }?.let { return it }
        actions.filterIsInstance<OpenPredictionAction>().takeIf { it.isNotEmpty() }?.let { return it }

        if (InlineCompletionContext.getOrNull(editor) == null) {
            return emptyList()
        }

        actions.filterIsInstance<AcceptNextWordInlayAction>().takeIf { it.isNotEmpty() }?.let { return it }
        actions.filterIsInstance<AcceptNextLineInlayAction>().takeIf { it.isNotEmpty() }?.let { return it }
        return emptyList()
    }
}