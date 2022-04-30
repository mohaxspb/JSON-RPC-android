package ru.kuchanov.json_rpc.moshi_json_parser

import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcResponse
import ru.kuchanov.json_rpc.library.domain.parser.ResponseParser
import com.squareup.moshi.Moshi

class MoshiResponseParser(private val moshi: Moshi = Moshi.Builder().build()) : ResponseParser {
    override fun parse(data: ByteArray): JsonRpcResponse {
        val responseType = JsonRpcResponse::class.java
        val adapter = moshi.adapter(responseType)
        return adapter.fromJson(data.decodeToString())
            ?: throw IllegalStateException("Unexpectedly null json parse result for value: $data!")
    }
}