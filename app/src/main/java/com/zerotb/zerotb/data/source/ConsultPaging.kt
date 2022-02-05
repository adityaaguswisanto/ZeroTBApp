package com.zerotb.zerotb.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zerotb.zerotb.data.network.MyApi
import com.zerotb.zerotb.data.responses.data.Consult
import javax.inject.Inject

class ConsultPaging @Inject constructor(
    private val api: MyApi,
    private val token: String
) : PagingSource<Int, Consult>() {

    override fun getRefreshKey(state: PagingState<Int, Consult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Consult> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.consult(token, nextPageNumber)
            val consult = response.data
            LoadResult.Page(
                data = response.data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (consult.isEmpty()) null else nextPageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}