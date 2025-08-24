package com.github.xepozz.boson.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.elements.PhpClass

private typealias WebComponentsDefinitionIndexType = List<String>

class WebComponentsDefinitionIndex : AbstractPhpIndex<WebComponentsDefinitionIndexType>() {
    companion object {
        val identity = ID.create<String, WebComponentsDefinitionIndexType>("BosonPHP.WebComponents.Definition")
    }

    override fun getName() = identity

    override fun getIndexer() = object : DataIndexer<String, WebComponentsDefinitionIndexType, FileContent> {
        override fun map(inputData: FileContent): Map<String, WebComponentsDefinitionIndexType> {
            val phpFile = inputData.psiFile as? PhpFile ?: return emptyMap()

            val result = ComponentsDefinitionProcessor.process(phpFile)
            return result.groupBy { it.first }.mapValues { it.value.map { it.second } }
        }
    }
}