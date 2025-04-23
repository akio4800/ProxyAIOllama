package at.s2gplus.ai.toolwindow.chat

import com.intellij.openapi.project.Project
import at.s2gplus.ai.ReferencedFile
import at.s2gplus.ai.conversations.message.Message
import at.s2gplus.ai.ui.textarea.TagProcessorFactory
import at.s2gplus.ai.ui.textarea.header.tag.TagDetails

class MessageBuilder(private val project: Project, private val text: String) {
    private val message = Message("")
    private var inlayContent: String = ""

    fun withInlays(appliedTags: List<TagDetails>): MessageBuilder {
        if (appliedTags.isNotEmpty()) {
            inlayContent = processTags(message, appliedTags)
        }
        return this
    }

    fun withReferencedFiles(referencedFiles: List<ReferencedFile>): MessageBuilder {
        if (referencedFiles.isNotEmpty()) {
            message.referencedFilePaths = referencedFiles.map { it.filePath() }
        }
        return this
    }

    fun withImage(attachedImagePath: String): MessageBuilder {
        message.imageFilePath = attachedImagePath
        return this
    }

    fun build(): Message {
        message.prompt = buildString {
            append(text)
            if (inlayContent.isNotBlank()) {
                append("\n")
                append(inlayContent)
            }
        }.trim()
        return message
    }

    private fun processTags(
        message: Message,
        tags: List<TagDetails>
    ): String = buildString {
        tags
            .map {
                TagProcessorFactory.getProcessor(project, it)
            }
            .forEach { it.process(message, this) }
    }
}
