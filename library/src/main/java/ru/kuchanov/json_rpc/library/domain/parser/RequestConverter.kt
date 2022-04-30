package ru.kuchanov.json_rpc.library.domain.parser

import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcRequest

interface RequestConverter {
    fun convert(request: JsonRpcRequest): String
}