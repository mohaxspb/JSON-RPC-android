package ru.kuchanov.json_rpc.moshi_json_parser

import com.squareup.moshi.Moshi
import ru.kuchanov.json_rpc.library.domain.parser.RequestConverter
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcRequest

class MoshiRequestConverter(private val moshi: Moshi = Moshi.Builder().build()) : RequestConverter {
    override fun convert(request: JsonRpcRequest): String {
        return moshi.adapter(JsonRpcRequest::class.java).toJson(request)
    }
}