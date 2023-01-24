package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.util.RATING

class RatingEvaluationFactor : EvaluationFactor {
    override val name = RATING
    override fun evaluate(item: SearchModel): Double {
        return item.warteg.rating / 5
    }
}