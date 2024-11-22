package com.example.imageloaderapp.data.repository

import com.example.imageloaderapp.data.api.ImageApiFactory
import com.example.imageloaderapp.data.mapper.ImageMapper
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.entity.result.RequestError
import com.example.imageloaderapp.domain.entity.result.RequestResult
import com.example.imageloaderapp.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class ImageRepositoryImpl() : ImageRepository {

    private val apiService = ImageApiFactory.apiService
    private val mapper = ImageMapper()

    override suspend fun getImageList(): RequestResult<List<Image>> =
        withContext(Dispatchers.IO) {
            val response = try {
                apiService.getImageList()
            } catch (e: Exception) {
                return@withContext exceptionRequest(e)
            }

            return@withContext if (response.isSuccessful) {
                val imageList = response.body() ?: throw RuntimeException("Response body is null")
                RequestResult.Success(imageList.map { mapper.mapDtoToEntity(it) })
            } else {
                when (val code = response.code()) {
                    in 500..599 -> RequestResult.Error(RequestError.ServerError)
                    else -> throw RuntimeException("Unknown error code: $code")
                }
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