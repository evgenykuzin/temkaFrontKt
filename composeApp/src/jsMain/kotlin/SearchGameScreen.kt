import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*

@Composable
fun SearchGameScreen(
    onBack: () -> Unit,
) {
    val gamesList = remember { mutableStateListOf<Game>() }
    LaunchedEffect(Unit) {
        try {
            gamesList.addAll(GameClient.fetchGames())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    WebAppBackButton(onClick = onBack)
    Div(
        attrs = {
            classes(FriendsListStyle.Container)
        }
    ) {
        if (gamesList.isEmpty()) {
            H4 {
                Text("Игры не найдены...")
            }
        } else {
            gamesList.forEach {
                ChooseGameUI(it)
            }
        }
    }
}

var chosenGame = mutableStateOf<Game?>(null)

@Composable
fun GameInfoScreen(
    onBack: () -> Unit,
) {
    WebAppBackButton(onClick = onBack)
    Div(
        attrs = {
            classes(FriendsListStyle.Container)
        }
    ) {
      chosenGame.value?.let {
          GameUI(it)
      }
    }
}

@Composable
fun ChooseGameUI(game: Game) {
    Div(
        attrs = {
            classes(FriendsListStyle.FriendBlock)//TODO GameBlock
            onClick {
                chosenGame.value = game
                currentScreen = Screen.GAME_INFO
            }
        }
    ) {
        Text(game.name)
    }
}

@Composable
fun GameUI(game: Game) {
    Div(
        attrs = {
            classes(FriendsListStyle.FriendBlock)//TODO GameBlock
        }
    ) {
        Text(game.name)
        Li {
            game.users.forEach { opponent ->
                Ul {
                    OpponentUI(opponent)
                }
            }
        }
    }
}

@Composable
fun OpponentUI(user: UserTemki) {
    Div(
        attrs = {
            classes(FriendsListStyle.FriendBlock) //TODO OpponentBlock
        }
    ) {
        Text("${user.username} (${user.balance})")
    }
}