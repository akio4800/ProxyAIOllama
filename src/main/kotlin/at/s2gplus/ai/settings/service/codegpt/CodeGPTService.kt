package at.s2gplus.ai.settings.service.codegpt

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Deprecated("dont use")
@Service(Service.Level.PROJECT)
class CodeGPTService private constructor(val project: Project) {

}