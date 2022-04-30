package ru.kuchanov.json_rpc.android

import ru.kuchanov.json_rpc.library.domain.JsonRpc

interface UserApi {

    @JsonRpc("user")
    fun getUser(
        @JsonRpc("contact") contact: String,
        @JsonRpc("type") type: Int
    ): User
}