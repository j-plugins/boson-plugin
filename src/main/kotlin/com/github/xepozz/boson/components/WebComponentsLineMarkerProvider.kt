package com.github.xepozz.boson.components

import com.github.xepozz.boson.PHPSdk
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.jetbrains.php.PhpClassHierarchyUtils
import com.jetbrains.php.lang.psi.elements.PhpClass

class WebComponentsLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val element = element as? PhpClass ?: return

        var isWebComponent = false
        for (classReference in element.extendsList.referenceElements) {
            val phpClass = classReference.resolve() as? PhpClass ?: continue

            PhpClassHierarchyUtils.processSuperClasses(
                phpClass,
                true,
                true
            ) {
                if (it.fqn == PHPSdk.WEB_COMPONENT) {
                    isWebComponent = true
                    return@processSuperClasses false
                }

                true
            }
        }
        if (!isWebComponent) return

        result.add(
            NavigationGutterIconBuilder
                .create(AllIcons.General.Web)
                .setTargets(element.containingFile)
                .setNamer { element.containingFile.name }
                .createLineMarkerInfo(element)
        )
    }
}