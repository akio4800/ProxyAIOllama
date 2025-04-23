package at.s2gplus.ai.ui.textarea

import at.s2gplus.ai.conversations.message.Message

fun interface TagProcessor {
    fun process(message: Message, promptBuilder: StringBuilder)
}
