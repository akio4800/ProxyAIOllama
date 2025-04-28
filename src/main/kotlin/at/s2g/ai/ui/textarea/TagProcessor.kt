package at.s2g.ai.ui.textarea

import at.s2g.ai.conversations.message.Message

fun interface TagProcessor {
    fun process(message: Message, promptBuilder: StringBuilder)
}
