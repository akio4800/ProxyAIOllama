package at.s2g.ai.psistructure.models

data class ParameterInfo(
    val name: String,
    val type: ClassName,
    val modifiers: List<String>,
)
