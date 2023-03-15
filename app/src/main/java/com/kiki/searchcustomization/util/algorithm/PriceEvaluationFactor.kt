package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.util.PRICE

class PriceEvaluationFactor(private val prices: List<Double>) : EvaluationFactor {
    override val name = PRICE

    override fun evaluate(item: SearchModel): Double {
        return prices.normalize(this, item.menu.price)
    }
}