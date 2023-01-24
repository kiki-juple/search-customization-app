package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel

class MultiFactorEvaluator {
    private val factors = mutableListOf<EvaluationFactor>()
    private val weight = mutableMapOf<String, Double>()

    fun addFactor(factor: EvaluationFactor, weight: Double) {
        factors.add(factor)
        this.weight[factor.name] = weight
    }

    fun evaluate(item: SearchModel): Score {
        var totalScore = 0.0
        for (factor in factors) {
            val score = factor.evaluate(item) * weight[factor.name]!!
            totalScore += score
        }
        return Score(item, totalScore)
    }
}