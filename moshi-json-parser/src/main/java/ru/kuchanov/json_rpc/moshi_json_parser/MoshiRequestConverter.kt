package ru.kuchanov.json_rpc.moshi_json_parser

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.kuchanov.json_rpc.library.domain.parser.RequestConverter
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcRequest

class MoshiRequestConverter(
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
) : RequestConverter {
    override fun convert(request: JsonRpcRequest): String {
        return moshi.adapter(JsonRpcRequest::class.java).toJson(request)
    }
}