package ru.kuchanov.json_rpc.library.domain

import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcRequest
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcResponse

interface JsonRpcClient {
    fun call(jsonRpcRequest: JsonRpcRequest): JsonRpcResponse
}