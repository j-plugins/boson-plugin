package com.github.xepozz.boson.components.inlay

import com.github.xepozz.boson.index.IndexUtil
import com.intellij.codeInsight.hints.declarative.AboveLineIndentedPosition
import com.intellij.codeInsight.hints.declarative.HintFormat
import com.intellij.codeInsight.hints.declarative.InlayActionPayload
import com.intellij.codeInsight.hints.declarative.InlayHintsCollector
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.codeInsight.hints.declarative.InlayPayload
import com.intellij.codeInsight.hints.declarative.InlayPosition
import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.codeInsight.hints.declarative.StringInlayActionPayload
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.startOffset
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

            sink.addPresentation(
                AboveLineIndentedPosition(element.startOffset),
                null,
                null,
                HintFormat.default
            ) {
                this.text("As: $names")
            }
        }

    }
}