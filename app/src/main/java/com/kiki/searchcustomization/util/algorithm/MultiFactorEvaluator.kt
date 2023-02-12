package com.kiki.searchcustomization.util.algorithm

import android.util.Log
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
        factors.forEach { factor ->
            val score = if (factor is PriceEvaluationFactor) {
                factor.evaluate(item) * weight[factor.name]!!
            } else {
                factor.evaluate(item) * weight[factor.name]!!
            }
            Log.d("score123", mapOf("${item.warteg.name} ${factor.name}" to score).toString())
            totalScore += score
        }
        return Score(item, totalScore)
    }
}