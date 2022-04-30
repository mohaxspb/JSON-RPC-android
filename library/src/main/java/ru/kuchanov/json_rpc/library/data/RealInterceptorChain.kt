package ru.kuchanov.json_rpc.library.data

import ru.kuchanov.json_rpc.library.domain.JsonRpcClient
import ru.kuchanov.json_rpc.library.domain.JsonRpcInterceptor
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcRequest
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcResponse

data class RealInterceptorChain(
    private val client: JsonRpcClient,
    private val interceptors: List<JsonRpcInterceptor>,
    private val request: JsonRpcRequest,
    private val index: Int = 0
) : JsonRpcInterceptor.Chain {

    override fun proceed(request: JsonRpcRequest): JsonRpcResponse {
        // Call the next interceptor in the chain. Last one in chain is ServerCallInterceptor.
        val nextChain = copy(index = index + 1)
        val nextInterceptor = interceptors[index]
        return nextInterceptor.intercept(nextChain)
    }

    override fun request(): JsonRpcRequest = request

    override fun toString(): String {
        return "RealInterceptorChain(index=$index, interceptors=$interceptors)"
    }
}