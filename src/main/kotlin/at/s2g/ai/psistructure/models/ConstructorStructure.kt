package at.s2g.ai.psistructure.models

data class ConstructorStructure(
    val parameters: List<ParameterInfo>,
    val modifiers: List<String>,
)
