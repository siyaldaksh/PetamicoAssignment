package com.assignment.petamicoassignment.ui.userlist

import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.assignment.petamicoassignment.ui.network.Api
import com.assignment.petamicoassignment.ui.network.UserDataServer
import retrofit2.HttpException
import java.io.IOException



class UserPagingSource (val service : Api): PagingSource<Int, UserDataServer>(){


    companion object{
        private const val STARTING_PAGE_INDEX = 1
        private const val NETWORK_PAGE_SIZE = 6

    }




    override fun getRefreshKey(state: PagingState<Int, UserDataServer>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserDataServer> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = service.getUserData(position)

            val list = response.data

            val nextKey = if (list.isNullOrEmpty()) {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = list,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }


}