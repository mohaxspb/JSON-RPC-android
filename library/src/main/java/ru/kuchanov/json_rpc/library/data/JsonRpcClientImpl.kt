package ru.kuchanov.json_rpc.library.data

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.kuchanov.json_rpc.library.domain.JsonRpcClient
import ru.kuchanov.json_rpc.library.domain.NetworkRequestException
import ru.kuchanov.json_rpc.library.domain.TransportException
import ru.kuchanov.json_rpc.library.domain.parser.RequestConverter
import ru.kuchanov.json_rpc.library.domain.parser.ResponseParser
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcRequest
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcResponse

class JsonRpcClientImpl(
    private val baseUrl: String,
    private val okHttpClient: OkHttpClient,
    private val requestConverter: RequestConverter,
    private val responseParser: ResponseParser
) : JsonRpcClient {

    override fun call(jsonRpcRequest: JsonRpcRequest): JsonRpcResponse {
        val requestBody = requestConverter.convert(jsonRpcRequest).toByteArray().toRequestBody()
        val request = Request.Builder()
            .post(requestBody)
            .url(baseUrl)
            .build()
        val response = try {
            okHttpClient.newCall(request).execute()
        } catch (e: Exception) {
            throw NetworkRequestException(
                message = "Network error: ${e.message}",
                cause = e
            )
        }
        return if (response.isSuccessful) {
            response.body?.let { responseParser.parse(it.bytes()) }
                ?: throw IllegalStateException("Response body is null")
        } else {
            throw TransportException(
                httpCode = response.code,
                message = "HTTP ${response.code}. ${response.message}",
                response = response,
            )
        }
    }
}