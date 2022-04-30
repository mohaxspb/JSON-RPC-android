package ru.kuchanov.json_rpc.library.domain.protocol

data class JsonRpcError(
    val message: String,
    val code: Int,
    val data: Any?
)