package com.example.imageloaderapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.imageloaderapp.data.api.ImageApiFactory
import com.example.imageloaderapp.data.mapper.ImageMapper
import com.example.imageloaderapp.data.paging.ImagePagingSource
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.entity.result.RequestError
import com.example.imageloaderapp.domain.entity.result.RequestResult
import com.example.imageloaderapp.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class ImageRepositoryImpl() : ImageRepository {

    private val apiService = ImageApiFactory.apiService
    private val mapper = ImageMapper()

    override fun getImageList(): Flow<RequestResult<PagingData<Image>>> = flow {
        try {
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 1,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { ImagePagingSource() }
            )

            pager.flow.collect { pagingData ->
                emit(RequestResult.Success(pagingData))
            }

        } catch (e: Exception) {
            emit(exceptionRequest(e))
        }
    }



    override suspend fun getImage(id: Int): RequestResult<Image> =
        withContext(Dispatchers.IO) {
            val response = try {
                apiService.getImage(id)
            } catch (e: Exception) {
                return@withContext exceptionRequest(e)
            }

            return@withContext if (response.isSuccessful) {
                val image = response.body() ?: throw RuntimeException("Response body is null")
                RequestResult.Success(mapper.mapDtoToEntity(image))
            } else {
                when (val code = response.code()) {
                    in 500..599 -> RequestResult.Error(RequestError.ServerError)
                    else -> throw RuntimeException("Unknown error code: $code")
                }
            }
        }

    private fun <T> exceptionRequest(e: Exception): RequestResult<T> =
        when (e) {
            is UnknownHostException -> RequestResult.Error(RequestError.NetworkError)
            is SocketTimeoutException -> RequestResult.Error(RequestError.NetworkError)
            is SSLHandshakeException -> RequestResult.Error(RequestError.ServerError)
            else -> throw e
        }
}