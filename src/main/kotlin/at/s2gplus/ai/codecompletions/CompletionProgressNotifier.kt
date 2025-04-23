package at.s2gplus.ai.codecompletions

import at.s2gplus.ai.CodeGPTKeys
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic

interface CompletionProgressNotifier {

    fun update()

    companion object {
        @JvmStatic
        val COMPLETION_PROGRESS_TOPIC =
            Topic.create("completionProgressTopic", CompletionProgressNotifier::class.java)

        @JvmStatic
        fun update(project: Project, loading: Boolean) {
            if (project.isDisposed) return

            CodeGPTKeys.COMPLETION_IN_PROGRESS.set(project, loading)

            project.messageBus.syncPublisher(COMPLETION_PROGRESS_TOPIC)?.update()
        }
    }
}