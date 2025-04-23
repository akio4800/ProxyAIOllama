package at.s2gplus.ai.psistructure.models

data class ConstructorStructure(
    val parameters: List<ParameterInfo>,
    val modifiers: List<String>,
)
