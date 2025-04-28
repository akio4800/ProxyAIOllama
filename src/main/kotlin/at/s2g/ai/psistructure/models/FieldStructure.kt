package at.s2g.ai.psistructure.models

data class FieldStructure(
    val name: String,
    val type: ClassName,
    val modifiers: List<String>,
)
