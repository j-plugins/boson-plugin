package com.github.xepozz.boson.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.PhpClass

object IndexUtil {
    fun getAllAliases(project: Project): List<String> {
        return FileBasedIndex.getInstance()
            .getAllKeys(WebComponentsDefinitionIndex.identity, project)
            .toList()
    }

    fun findAliases(project: Project, fqn: String): List<String> {
        return FileBasedIndex.getInstance()
            .getValues(WebComponentsAliasesIndex.identity, fqn, GlobalSearchScope.allScope(project))
            .flatten()
    }

    fun findClasses(project: Project, alias: String): List<PhpClass> {
        val fileBasedIndex = FileBasedIndex.getInstance()
        val phpIndex = PhpIndex.getInstance(project)

        return fileBasedIndex
            .getValues(WebComponentsDefinitionIndex.identity, alias, GlobalSearchScope.allScope(project))
            .flatten()
            .map { phpIndex.getClassesByFQN(it) }
            .flatten()
    }
}