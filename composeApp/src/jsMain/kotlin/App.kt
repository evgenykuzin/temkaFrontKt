import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.inmo.tgbotapi.webapps.webApp

// Don't use in real code
enum class Screen {
    CLICKER,
    FRIENDS_LIST,
    SEARCH_GAME,
    GAME_INFO
}

// Don't use in real code
var currentScreen by mutableStateOf(Screen.CLICKER)

@Composable
fun App() {
    when (currentScreen) {
        Screen.CLICKER -> ClickerScreen {
            currentScreen = Screen.FRIENDS_LIST
        }

        Screen.FRIENDS_LIST -> FriendsListScreen {
            currentScreen = Screen.CLICKER
        }

        Screen.SEARCH_GAME -> SearchGameScreen {
            currentScreen = Screen.CLICKER
        }
        Screen.GAME_INFO -> GameInfoScreen {
            currentScreen = Screen.SEARCH_GAME
        }
    }
}