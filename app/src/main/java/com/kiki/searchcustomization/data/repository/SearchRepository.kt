package com.kiki.searchcustomization.data.repository

import com.kiki.searchcustomization.data.Resource
import com.kiki.searchcustomization.data.dao.WartegDao
import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.di.IoDispatcher
import com.kiki.searchcustomization.util.algorithm.DistanceEvaluationFactor
import com.kiki.searchcustomization.util.algorithm.MultiFactorEvaluator
import com.kiki.searchcustomization.util.algorithm.PriceEvaluationFactor
import com.kiki.searchcustomization.util.algorithm.RatingEvaluationFactor
import com.kiki.searchcustomization.util.mapper.toSearchModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val dao: WartegDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val evaluator: MultiFactorEvaluator,
) {

    suspend fun searchWarteg(query: String): Flow<Resource<List<SearchModel>>> = channelFlow {
        send(Resource.Loading())
        try {
            val search = async { dao.searchWarteg(query) }
            search.await().collectLatest { list ->
                evaluator.apply {
                    addFactor(DistanceEvaluationFactor(), 3.4)
                    addFactor(PriceEvaluationFactor(), 3.3)
                    addFactor(RatingEvaluationFactor(), 3.3)
                }
                val score = list.map { warteg ->
                    evaluator.evaluate(warteg)
                }.sortedBy { it.score }
                val result = score.map {
                    it.toSearchModel()
                }
                send(Resource.Success(result))
            }
        } catch (e: Exception) {
            send(Resource.Error(e.message.toString()))
        }
    }.flowOn(ioDispatcher)

}