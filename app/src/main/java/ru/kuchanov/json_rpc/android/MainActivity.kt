package ru.kuchanov.json_rpc.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.kuchanov.json_rpc.android.databinding.ActivityMainBinding
import ru.kuchanov.json_rpc.library.data.JsonRpcClientImpl
import ru.kuchanov.json_rpc.library.data.createJsonRpcService
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiRequestConverter
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiResponseParser
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiResultParser

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"

        const val BASE_URL = "http://localhost:8080/"
    }

    lateinit var binding: ActivityMainBinding

    lateinit var userApi: UserApi

    private fun initJsonRpcLibrary() {
        val logger = HttpLoggingInterceptor.Logger { Log.d(TAG, it) }
        val loggingInterceptor =
            HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val jsonRpcClient = JsonRpcClientImpl(
            baseUrl = BASE_URL,
            okHttpClient = okHttpClient,
            requestConverter = MoshiRequestConverter(),
            responseParser = MoshiResponseParser()
        )

        userApi = createJsonRpcService(
            service = UserApi::class.java,
            client = jsonRpcClient,
            resultParser = MoshiResultParser(),
            interceptors = listOf()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initJsonRpcLibrary()

        binding.getUserButton.setOnClickListener {
            // TODO: launch request
        }
    }
}