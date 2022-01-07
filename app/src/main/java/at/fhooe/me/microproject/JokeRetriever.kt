package at.fhooe.me.microproject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeRetriever {
    private val networkInterface : NetworkInterface
    companion object {
        var BaseURl = "https://v2.jokeapi.dev/joke/"
    }

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BaseURl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun getJoke() : ModelJoke {
        return networkInterface.getJoke()
    }
}