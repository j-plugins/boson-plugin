package com.github.xepozz.boson.components

import com.github.xepozz.boson.index.IndexUtil
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlElementDescriptor

class WebComponentsTagDescriptorProvider : XmlElementDescriptorProvider {
    override fun getDescriptor(xmlTag: XmlTag): XmlElementDescriptor? {
        val xmlTag = xmlTag as? HtmlTag ?: return null
        val tagName = xmlTag.name

        val phpClass = IndexUtil.findClasses(xmlTag.project, tagName).firstOrNull()

        return WebComponentsTagDescriptor(tagName, phpClass ?: return null)
    }
}

