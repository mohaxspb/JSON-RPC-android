package ru.kuchanov.json_rpc.library.domain

import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcRequest
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcResponse

interface JsonRpcInterceptor {

    fun intercept(chain: Chain): JsonRpcResponse

    interface Chain {
        fun proceed(request: JsonRpcRequest): JsonRpcResponse

        fun request(): JsonRpcRequest
    }
}