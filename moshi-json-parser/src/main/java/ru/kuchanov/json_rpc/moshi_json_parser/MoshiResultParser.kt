package ru.kuchanov.json_rpc.moshi_json_parser

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.kuchanov.json_rpc.library.domain.parser.ResultParser
import java.lang.reflect.Type

class MoshiResultParser(
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
) : ResultParser {
    override fun <T> parse(type: Type, data: Any): T {
        return moshi.adapter<T>(type).fromJsonValue(data)
            ?: throw IllegalStateException("Unexpectedly null json parse result for value: $data!")
    }
}