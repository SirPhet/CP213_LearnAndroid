import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// --- ส่วนที่ 1: Data Model ---
data class PokedexResponse(
    val pokemon_entries: List<PokemonEntry>
)

data class PokemonEntry(
    val entry_number: Int,
    val pokemon_species: PokemonSpecies
)

data class PokemonSpecies(
    val name: String,
    val url: String
)

// --- ส่วนที่ 2: API Interface ---
interface PokemonApiService {
    @GET("pokedex/2/")
    suspend fun getKantoPokedex(): PokedexResponse
}

// --- ส่วนที่ 3: Singleton Instance ---
object PokemonNetwork {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val api: PokemonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApiService::class.java)
    }
}