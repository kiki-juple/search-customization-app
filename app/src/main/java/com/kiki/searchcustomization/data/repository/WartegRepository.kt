package com.kiki.searchcustomization.data.repository

import com.kiki.searchcustomization.data.Resource
import com.kiki.searchcustomization.data.dao.WartegDao
import com.kiki.searchcustomization.data.entity.WartegWithMenu
import com.kiki.searchcustomization.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WartegRepository @Inject constructor(
    private val dao: WartegDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    fun getAllWarteg(): Flow<Resource<List<WartegWithMenu>>> = channelFlow {
        send(Resource.Loading())
        dao.getWarteg().collectLatest { list ->
            send(Resource.Success(list.sortedBy { it.warteg.distance }))
        }
    }.flowOn(ioDispatcher)

    fun getTopWarteg(): Flow<Resource<List<WartegWithMenu>>> = channelFlow {
        send(Resource.Loading())
        dao.getTopWarteg().collectLatest {
            send(Resource.Success(it))
        }
    }.flowOn(ioDispatcher)

    fun getCheapestWarteg(): Flow<Resource<List<WartegWithMenu>>> = channelFlow {
        send(Resource.Loading())
        dao.getCheapestWarteg().collectLatest {
            send(Resource.Success(it))
        }
    }
}