# JSON-RPC Android

This is source code and example of Android client library for JSON-RPC server. It's Retrofit-like, so you can declare interface with methods to make JSON_RPC calls.

### Example of usage

1. Declare model for server response

```
data class User(
    val id: Int,
    val name: String
)
```

2. Create interface with JSON-RPC calls methods

```
interface UserApi {

    @JsonRpc("getUser")
    fun getUser(@JsonRpc("id") id: Int): User
}
```

3. Create instance of interface

```
val okHttpClient = OkHttpClient.Builder().build()

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
```

4. Call method:

```
try {
    val user = api.getUser(id)
} catch (e: Throwable) {
    e.printStackTrace()
    if (e is JsonRpcException) {

    } else {

    }
}
```