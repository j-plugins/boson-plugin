package com.github.xepozz.boson.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.rml.dfa.ir.IrProject
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.jetbrains.php.lang.psi.PhpFile

private typealias WebComponentsAliasesIndexType = List<String>

class WebComponentsAliasesIndex : AbstractPhpIndex<WebComponentsAliasesIndexType>() {
    companion object {
         val identity = ID.create<String, WebComponentsAliasesIndexType>("BosonPHP.WebComponents.Aliases")
    }

    override fun getName() = identity

    override fun getIndexer() = object : DataIndexer<String, WebComponentsAliasesIndexType, FileContent> {
        override fun map(inputData: FileContent): Map<String, WebComponentsAliasesIndexType> {
            val phpFile = inputData.psiFile as? PhpFile ?: return emptyMap()

            val result = ComponentsDefinitionProcessor.process(phpFile)
            return result.groupBy { it.second }.mapValues { it.value.map { it.first } }
        }
    }

}