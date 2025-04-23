package at.s2gplus.ai.psistructure.models

data class ParameterInfo(
    val name: String,
    val type: ClassName,
    val modifiers: List<String>,
)
