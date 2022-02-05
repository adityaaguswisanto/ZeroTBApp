package com.zerotb.zerotb.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zerotb.zerotb.data.network.MyApi
import com.zerotb.zerotb.data.responses.data.Pill
import javax.inject.Inject

class PillPaging @Inject constructor(
    private val api: MyApi,
    private val token: String,
    private val id: Int
) : PagingSource<Int, Pill>() {

    override fun getRefreshKey(state: PagingState<Int, Pill>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pill> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.pill(token, nextPageNumber, id)
            val pill = response.data
            LoadResult.Page(
                data = response.data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (pill.isEmpty()) null else nextPageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}