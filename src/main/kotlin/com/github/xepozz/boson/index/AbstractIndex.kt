package com.github.xepozz.boson.index

import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileBasedIndexExtension
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor

abstract class AbstractIndex<T : Any> : FileBasedIndexExtension<String, T>() {
    abstract override fun getName(): ID<String, T>

    abstract override fun getInputFilter(): FileBasedIndex.InputFilter

    override fun dependsOnFileContent() = true

    abstract override fun getIndexer(): DataIndexer<String, T, FileContent>

    override fun getKeyDescriptor(): EnumeratorStringDescriptor = EnumeratorStringDescriptor.INSTANCE

    override fun getValueExternalizer(): DataExternalizer<T> = ObjectStreamDataExternalizer<T>()

    override fun getVersion() = 1
}

abstract class AbstractBosonJsonIndex<T : Any> : AbstractIndex<T>() {
    override fun getInputFilter() = FileBasedIndex.InputFilter { file -> file.name == "boson.json" }
}

abstract class AbstractPhpIndex<T : Any> : AbstractIndex<T>() {
    override fun getInputFilter() = FileBasedIndex.InputFilter { file -> file.extension == "php" }
}