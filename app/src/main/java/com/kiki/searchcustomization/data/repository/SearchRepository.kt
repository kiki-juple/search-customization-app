package com.kiki.searchcustomization.data.repository

import android.util.Log
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
            Log.d(
                "weight",
                "${factors.distance}, ${factors.price}, ${factors.rating}, ${factors.review}"
            )
            evaluator.apply {
                addFactor(DistanceEvaluationFactor(distances), if (factors.distance) 1.0 else 0.0)
                addFactor(PriceEvaluationFactor(prices), if (factors.price) 1.0 else 0.0)
                addFactor(RatingEvaluationFactor(ratings), if (factors.rating) 1.0 else 0.0)
                addFactor(ReviewEvaluationFactor(reviews), if (factors.review) 1.0 else 0.0)
            }
            val score = list.map { warteg ->
                evaluator.evaluate(warteg)
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