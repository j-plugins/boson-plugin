package com.github.xepozz.boson.components.usage

import com.github.xepozz.boson.index.IndexUtil
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiReference
import com.intellij.psi.search.UsageSearchContext
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import com.jetbrains.php.lang.psi.elements.PhpClass

class WebComponentsReferenceSearcher : QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>(true) {
    override fun processQuery(
        queryParameters: ReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference>
    ) {
        val element = queryParameters.elementToSearch as? PhpClass ?: return

        val names = IndexUtil
            .findAliases(element.project, element.fqn)
            .distinct()

        for (attrName in names) {
            queryParameters.optimizer.searchWord(
                attrName,
                queryParameters.getEffectiveSearchScope(),
                UsageSearchContext.IN_FOREIGN_LANGUAGES,
                true,
                element
            )
        }
    }
}