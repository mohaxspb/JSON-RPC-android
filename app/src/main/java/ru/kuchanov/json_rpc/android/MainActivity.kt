package ru.kuchanov.json_rpc.android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.kuchanov.json_rpc.android.databinding.ActivityMainBinding
import ru.kuchanov.json_rpc.library.data.JsonRpcClientImpl
import ru.kuchanov.json_rpc.library.data.createJsonRpcService
import ru.kuchanov.json_rpc.library.domain.JsonRpcException
import ru.kuchanov.json_rpc.library.domain.JsonRpcInterceptor
import ru.kuchanov.json_rpc.library.domain.protocol.JsonRpcResponse
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiRequestConverter
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiResponseParser
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiResultParser

class MainActivity : AppCompatActivity() {

    companion object {
        const val BASE_URL = "http://192.168.43.226:8080/"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var userApi: UserApi
    private lateinit var userApiWithInterceptor: UserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initJsonRpcLibrary()
        initJsonRpcLibraryWithInterceptor()

        binding.getUserButton.setOnClickListener {
            loadUser(1)
        }

        binding.getUserWithErrorButton.setOnClickListener {
            loadUser(42)
        }

        binding.getUserWithInterceptorButton.setOnClickListener {
            loadUser(42, userApiWithInterceptor)
        }
    }

    private fun loadUser(id: Int, api: UserApi = userApi) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val user = api.getUser(id)

                    withContext(Dispatchers.Main) {
                        binding.requestResponseTextView.text = user.toString()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (e is JsonRpcException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "JSON-RPC error with code ${e.code} and message ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()

                            binding.requestResponseTextView.text = e.toString()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                e.message ?: e.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun initJsonRpcLibrary() {
        val logger = HttpLoggingInterceptor.Logger { Log.d("JSON-RPC", it) }
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

    private fun initJsonRpcLibraryWithInterceptor() {
        val logger = HttpLoggingInterceptor.Logger { Log.d("JSON-RPC", it) }
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

        val interceptor = object : JsonRpcInterceptor {
            override fun intercept(chain: JsonRpcInterceptor.Chain): JsonRpcResponse {
                val initialResponse = chain.proceed(chain.request())
                return if (initialResponse.error != null) {
                    chain.proceed(chain.request().copy(params = mapOf("id" to 1)))
                } else {
                    initialResponse
                }
            }
        }

        userApiWithInterceptor = createJsonRpcService(
            service = UserApi::class.java,
            client = jsonRpcClient,
            resultParser = MoshiResultParser(),
            interceptors = listOf(interceptor)
        )
    }
}