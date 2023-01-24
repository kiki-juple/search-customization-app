package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.util.PRICE

class PriceEvaluationFactor : EvaluationFactor {
    override val name = PRICE
    override fun evaluate(item: SearchModel): Double {
        val price = item.menu.price
        return when {
            price <= 10.0 -> 1.0
            price <= 20.0 -> 0.8
            else -> 0.5
        }
    }
}