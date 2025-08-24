package com.github.xepozz.boson.components.inlay

import com.github.xepozz.boson.index.IndexUtil
import com.intellij.codeInsight.hints.declarative.EndOfLinePosition
import com.intellij.codeInsight.hints.declarative.HintFormat
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.codeInsight.hints.declarative.InlayPosition
import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.php.lang.psi.elements.PhpClass

class WebComponentsInlayHintsProvider : InlayHintsProvider {
    override fun createCollector(
        file: PsiFile,
        editor: Editor
    ) = object : SharedBypassCollector {
        override fun collectFromElement(
            element: PsiElement,
            sink: InlayTreeSink
        ) {
            if (element !is PhpClass) return

            val names = IndexUtil
                .findAliases(element.project, element.fqn)
                .distinct()
                .joinToString(", ")

            if (names.isEmpty()) return

            val textOffset = element.textOffset
            val position = try {
                val a = Class.forName("com.intellij.codeInsight.hints.declarative.AboveLineIndentedPosition")
                a.constructors.first().newInstance(textOffset, 0, 0) as InlayPosition
            } catch (ignored: ClassNotFoundException) {
//                InlineInlayPosition(textOffset, true)
                EndOfLinePosition(element.containingFile.text.substring(0, textOffset).count { it == '\n' }, 0)
            }

            sink.addPresentation(
                position,
                null,
                null,
                HintFormat.default
            ) {
                this.text("As: $names")
            }
        }

    }
}