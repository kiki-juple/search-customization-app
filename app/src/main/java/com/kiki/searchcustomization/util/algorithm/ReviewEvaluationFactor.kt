package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.util.REVIEW

class ReviewEvaluationFactor(private val reviews: List<Double>) : EvaluationFactor {
    override val name = REVIEW

    override fun evaluate(item: SearchModel): Double {
        return reviews.normalize(this, item.warteg.review.toDouble())
    }
}