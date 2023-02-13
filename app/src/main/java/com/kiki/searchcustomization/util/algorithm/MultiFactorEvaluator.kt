package com.kiki.searchcustomization.util.algorithm

import android.util.Log
import com.kiki.searchcustomization.data.entity.SearchModel

class MultiFactorEvaluator {
    fun evaluate(item: SearchModel, factors: List<EvaluationFactor>, weight: Double): Score {
        var totalScore = 0.0
        factors.forEach { factor ->
            val score = factor.evaluate(item) * weight
            Log.d("score123", mapOf("${item.warteg.name} ${factor.name}" to score).toString())
            totalScore += score
        }
        Log.d("factor", "evaluate: $factors")
        Log.d("factor", "evaluate: $weight")
        return Score(item, totalScore)
    }
}