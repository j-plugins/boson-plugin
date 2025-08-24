package com.github.xepozz.boson.index

import com.jetbrains.php.codeInsight.controlFlow.PhpControlFlowUtil
import com.jetbrains.php.codeInsight.controlFlow.PhpInstructionProcessor
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpCallInstruction
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.elements.ClassConstantReference
import com.jetbrains.php.lang.psi.elements.ClassReference
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

object ComponentsDefinitionProcessor {
    val addComponentsTypes = listOf(
        "#P#P#C\\Boson\\Application.webview.components",
        "#P#C\\Boson\\Application.webview"
    )

    fun process(phpFile: PhpFile): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        PhpControlFlowUtil.processFile(phpFile, object : PhpInstructionProcessor() {
            override fun processPhpCallInstruction(instruction: PhpCallInstruction): Boolean {
                val anchor = instruction.anchor
                val functionReference = instruction.functionReference
                when (functionReference.name) {
                    "add", "defineComponent" -> {
                        val parameters = functionReference.parameters
                        if (parameters.size != 2
                            || parameters.getOrNull(0) !is StringLiteralExpression
                            || parameters.getOrNull(1) !is ClassConstantReference
                        ) {
                            return true
                        }
                        (anchor as? MethodReference)
                            ?.classReference
                            ?.apply {
                                type
                                    .filterOutIntermediateTypes()
                                    .types
                                    .intersect(addComponentsTypes)
                                    .isNotEmpty()
                                    .takeIf { it }
                                    .apply {
                                        val first =
                                            parameters.getOrNull(0) as? StringLiteralExpression ?: return@apply
                                        val second =
                                            parameters.getOrNull(1) as? ClassConstantReference ?: return@apply
                                        val classReference =
                                            second.classReference as? ClassReference ?: return@apply
                                        val classFqn = classReference.fqn ?: return@apply

                                        result.add(Pair(first.contents, classFqn))
                                    }
                            }

                        return true
                    }

                    else -> return true
                }
            }
        })

        return result
    }
}