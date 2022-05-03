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
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiRequestConverter
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiResponseParser
import ru.kuchanov.json_rpc.moshi_json_parser.MoshiResultParser

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"

        const val BASE_URL = "http://localhost:8080/"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var userApi: UserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initJsonRpcLibrary()

        binding.getUserButton.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val user = userApi.getUser(42)

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
    }

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
}