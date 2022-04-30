package ru.kuchanov.json_rpc.library.data

import ru.kuchanov.json_rpc.library.domain.JsonRpcClient
import ru.kuchanov.json_rpc.library.domain.JsonRpcInterceptor
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcResponse

class ServerCallInterceptor(private val client: JsonRpcClient) : JsonRpcInterceptor {

    override fun intercept(chain: JsonRpcInterceptor.Chain): JsonRpcResponse {
        println("intercept: $chain")
        val realChain = chain as RealInterceptorChain
        val request = realChain.request()
        return client.call(request)
    }
}