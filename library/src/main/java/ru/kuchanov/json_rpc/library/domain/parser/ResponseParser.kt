package ru.kuchanov.json_rpc.library.domain.parser

import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcResponse

interface ResponseParser {
    fun parse(data: ByteArray): JsonRpcResponse
}