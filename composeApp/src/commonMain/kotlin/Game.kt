import dev.inmo.tgbotapi.types.chat.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Game(
    val name: String,
    val users: List<UserTemki>
)

@Serializable
data class UserTemki(
    var id: String? = null,
    var telegramId: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var username: String? = null,
    var phone: String? = null,
    var balance: Double = 0.0,
    var credit: Int = 0,
    var deleted: Boolean = false,
)

@Serializable
data class GamesSearchRs(
   val games: List<Game>
)

object GameClient {

    private const val SERVER_URL = "http://localhost:8081/games"

    private val client: HttpClient = HttpClient(TapClientEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        //authConfig()
    }

    suspend fun fetchGames(): List<Game> {
        return client
            .get("$SERVER_URL/search")
            .body<GamesSearchRs>()::games tryOrElse listOf()
    }
}

