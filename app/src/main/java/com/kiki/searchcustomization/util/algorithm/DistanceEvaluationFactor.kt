package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.util.DISTANCE

class DistanceEvaluationFactor(private val distances: List<Double>) : EvaluationFactor {
    override val name = DISTANCE

    override fun evaluate(item: SearchModel): Double {
        return distances.normalize(this, item.warteg.distance ?: 0.0)
    }
}