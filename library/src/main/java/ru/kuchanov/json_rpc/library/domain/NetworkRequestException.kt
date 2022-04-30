package ru.kuchanov.json_rpc.library.domain

class NetworkRequestException(
    override val message: String?,
    override val cause: Throwable
) : RuntimeException(message, cause)