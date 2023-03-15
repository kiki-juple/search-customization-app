package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel

class MultiFactorEvaluator {
    fun evaluate(item: SearchModel, factors: List<EvaluationFactor>, weight: Double): Score {
        var totalScore = 0.0
        factors.forEach { factor ->
            val score = factor.evaluate(item) * weight
            totalScore += score
        }
        return Score(item, totalScore)
    }
}