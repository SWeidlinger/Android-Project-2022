package at.fhooe.me.microproject

import retrofit2.http.GET

interface NetworkInterface {
    //https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,racist,sexist,explicit
    @GET("Any?blacklistFlags=nsfw,racist,sexist,explicit")
    suspend fun getJoke(): ModelJoke
}