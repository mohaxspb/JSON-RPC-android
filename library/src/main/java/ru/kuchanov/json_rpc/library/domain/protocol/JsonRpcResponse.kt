package ru.kuchanov.json_rpc.library.domain.protocol


data class JsonRpcResponse(
    val id: Long,
    val result: Any?,
    val error: JsonRpcError?
)