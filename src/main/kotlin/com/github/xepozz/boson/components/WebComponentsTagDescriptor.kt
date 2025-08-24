package com.github.xepozz.boson.components

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlElementDescriptor
import org.jetbrains.annotations.NonNls
import com.intellij.psi.impl.source.xml.XmlDescriptorUtil;
import com.intellij.psi.impl.source.html.dtd.HtmlNSDescriptorImpl;

class WebComponentsTagDescriptor(private val myName: String?, private val myDeclaration: PsiElement) : XmlElementDescriptor {
    override fun getQualifiedName() = myName

    override fun getDefaultName() = myName

    override fun getElementsDescriptors(context: XmlTag?): Array<XmlElementDescriptor?>? {
        return XmlDescriptorUtil.getElementsDescriptors(context)
    }

    override fun getElementDescriptor(childTag: XmlTag?, contextTag: XmlTag?): XmlElementDescriptor? {
        return XmlDescriptorUtil.getElementDescriptor(childTag, contextTag)
    }

    override fun getAttributesDescriptors(context: XmlTag?): Array<XmlAttributeDescriptor> {
        return HtmlNSDescriptorImpl.getCommonAttributeDescriptors(context)
    }

    override fun getAttributeDescriptor(attribute: XmlAttribute): XmlAttributeDescriptor? {
        return getAttributeDescriptor(attribute.name, attribute.parent)
    }

    override fun getAttributeDescriptor(attributeName: @NonNls String, context: XmlTag?) =
        getAttributesDescriptors(context).find { attributeName == it.name }

    override fun getDeclaration() = myDeclaration

    override fun getName(context: PsiElement?) = myName

    override fun getName() = myName

    override fun getNSDescriptor() = null

    override fun getTopGroup() = null

    override fun getDefaultValue() = null

    override fun getContentType() = XmlElementDescriptor.CONTENT_TYPE_ANY

    override fun init(element: PsiElement?) {
    }
}