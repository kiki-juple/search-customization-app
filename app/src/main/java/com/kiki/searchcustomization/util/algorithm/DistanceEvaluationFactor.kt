package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.util.DISTANCE

class DistanceEvaluationFactor : EvaluationFactor {
    override val name = DISTANCE
    override fun evaluate(item: SearchModel): Double {
        val distance = item.warteg.distance ?: 0.0
        return when {
            distance <= 1.0 -> 1.0
            distance <= 5.0 -> 0.8
            else -> 0.5
        }
    }
}