package ru.kuchanov.json_rpc.android

import ru.kuchanov.json_rpc.library.domain.JsonRpc

interface UserApi {

    @JsonRpc("user")
    fun getUser(@JsonRpc("id") id: Int): User
}