package ru.kuchanov.json_rpc.android

import ru.kuchanov.json_rpc.library.domain.JsonRpc

interface UserApi {

    @JsonRpc("getUser")
    fun getUser(@JsonRpc("id") id: Int): User
}