package com.example.travelbucket.data.remote

data class CountryResponse(
    val name: Name?,
    val population: Long?,
    val currencies: Map<String, Currency>?,
    val flags: Flags?,
    val region: String?
)

data class Name(
    val common: String?,
    val official: String?
)

data class Currency(
    val name: String?,
    val symbol: String?
)

data class Flags(
    val png: String?,
    val svg: String?
)
