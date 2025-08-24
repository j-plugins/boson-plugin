package com.github.xepozz.boson.components

import com.github.xepozz.boson.index.IndexUtil
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlTagNameProvider

class WebComponentsTagNameProvider : XmlTagNameProvider {
    override fun addTagNameVariants(elements: MutableList<LookupElement?>, xmlTag: XmlTag, prefix: String?) {
        val xmlTag = xmlTag as? HtmlTag ?: return

        val componentNames = IndexUtil.getAllAliases(xmlTag.project)

        componentNames
            .forEach {
                LookupElementBuilder
                    .create(xmlTag, it)
                    .withIcon(AllIcons.Nodes.Tag)
                    .apply { elements.add(this) }
            }
    }
}