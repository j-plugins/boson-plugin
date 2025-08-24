package com.github.xepozz.boson.index

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID

private typealias ConfigIndexType = List<String>

class ConfigIndex : AbstractBosonJsonIndex<ConfigIndexType>() {
    companion object {
        val identity = ID.create<String, ConfigIndexType>("BosonPHP.Config")
    }

    override fun getName() = identity

    override fun getIndexer() = object : DataIndexer<String, ConfigIndexType, FileContent> {
        override fun map(inputData: FileContent): Map<String, ConfigIndexType> {
            val jsonFile = inputData.psiFile as? JsonFile ?: return emptyMap()
            val document = jsonFile.fileDocument
            val root = jsonFile.topLevelValue ?: return emptyMap()

            val associatedMap = root.children
                .mapNotNull { it as? JsonProperty }
                .associateBy { it.name }
                .mapValues { listOf(it.value.value!!.text.removeSurrounding("\"")) }

            return associatedMap
        }
    }
}