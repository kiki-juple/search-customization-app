package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel

interface EvaluationFactor {
    val name: String
    fun evaluate(item: SearchModel): Double
}