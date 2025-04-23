package at.s2gplus.ai.psistructure.models

data class FieldStructure(
    val name: String,
    val type: ClassName,
    val modifiers: List<String>,
)
