package com.kiki.searchcustomization.data.repository

import com.kiki.searchcustomization.data.dao.WartegDao
import com.kiki.searchcustomization.data.entity.Factor
import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.di.IoDispatcher
import com.kiki.searchcustomization.util.algorithm.*
import com.kiki.searchcustomization.util.mapper.toSearchModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val dao: WartegDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val evaluator: MultiFactorEvaluator,
) {

    fun search(query: String, factors: Factor) = dao.searchWarteg(query).map { list ->
        if (list.isNotEmpty()) {
            val distances = list.map {
                it.warteg.distance ?: 0.0
            }
            val prices = list.map {
                it.menu.price
            }
            val ratings = list.map {
                it.warteg.rating
            }
            val reviews = list.map {
                it.warteg.review.toDouble()
            }
            val factor = mutableListOf<EvaluationFactor>()
            if (factors.distance) factor.add(DistanceEvaluationFactor(distances))
            if (factors.price) factor.add(PriceEvaluationFactor(prices))
            if (factors.rating) factor.add(RatingEvaluationFactor(ratings))
            if (factors.review) factor.add(ReviewEvaluationFactor(reviews))
            val weight = 1.0 / factor.size
            val score = list.map { warteg ->
                evaluator.evaluate(warteg, factor, weight)
            }.sortedByDescending { it.score }
            val result = score.map {
                it.toSearchModel()
            }
            return@map result
        } else {
            return@map emptyList<SearchModel>()
        }
    }.flowOn(ioDispatcher)
}