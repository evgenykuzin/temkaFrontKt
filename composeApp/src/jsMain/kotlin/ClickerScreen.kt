import androidx.compose.runtime.*
import dev.inmo.kslog.common.LogLevel
import dev.inmo.kslog.common.log
import dev.inmo.kslog.common.logger
import dev.inmo.micro_utils.coroutines.invokeOnFirst
import dev.inmo.tgbotapi.webapps.invoice.InvoiceStatus
import dev.inmo.tgbotapi.webapps.invoice.statusTyped
import dev.inmo.tgbotapi.webapps.webApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.*
import teck.ellow.clicker.Res
import kotlin.time.Duration.Companion.seconds

// Don't use in real code!

var score = mutableStateOf(0L)

@Composable
fun ClickerScreen(
    onFriendsList: () -> Unit,
) {


    // Don't use in real code!
    LaunchedEffect(Unit) {
        score.value = TapClient.fetchCurrentScore()
    }
    // Don't use in real code!
    LaunchedEffect(Unit) {
        while (true) {
            if (score.value != 0L) {
                try {
                    TapClient.sendCurrentScore(score.value)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            delay(1.seconds)
        }
    }
    WebAppMainButton(
        text = "Сп?исок друзей",
        hideOnDispose = false,
        onClick = onFriendsList
    )
    Div(
        attrs = {
            classes(ClickerStyles.MainContainer)
        }
    ) {
        UserInfo()
        Div(
            attrs = {
                classes(ClickerStyles.ClickerBlock)
            }
        ) {
            H2 {
                Text("Score: ${score.value}")
            }
            Img(
                src = Res.images.click_item.fileUrl,
                attrs = {
                    classes(ClickerStyles.MainImage)
                    onClick {
                        score.value = score.value.inc()
                        println("currentScore: ${score.value}")
                        logger.log(LogLevel.INFO, "CurrentScore: ${score.value}")
                    }

                }
            )
            //val coroutineScope = rememberCoroutineScope()
            Button(
                attrs = {
                    classes(ClickerStyles.PayButton) //TODO SearchGame
                    onClick {
                        currentScreen = Screen.SEARCH_GAME
                        logger.log(LogLevel.INFO, "SCREAN: ${currentScreen}")
                    }
                }
            ) {
                Text("Найти темку..")
            }
        }
    }
}

@Composable
fun UserInfo() {
    Div(
        attrs = {
            classes(ClickerStyles.UserInfo)
        }
    ) {
        Div(
            attrs = {
                classes(ClickerStyles.UserBlock)
            }
        ) {
            H5(
                attrs = {
                    classes(ClickerStyles.UserInfoName)
                }
            ) {
                Text("Username: ")
                Text(webApp.initDataUnsafe.user?.username ?: "no username")
            }
            Span(
                attrs = {
                    classes(ClickerStyles.UserInfoId)
                }
            ) {
                Text("ID: ")
                Text(webApp.initDataUnsafe.user?.id?.toString() ?: "no id")
            }
        }
        PayButton()
    }
}

@Composable
fun PayButton() {
    val coroutineScope = rememberCoroutineScope()
    Button(
        attrs = {
            classes(ClickerStyles.PayButton)
            onClick {
                coroutineScope.launch {
                    val link = TapClient.fetchUpgradeLink()
                    webApp.openInvoice(link) {
                        when (it.statusTyped) {
                            InvoiceStatus.Cancelled -> webApp.showAlert(message = "Оплата отменена")
                            InvoiceStatus.Failed -> webApp.showAlert(message = "Ошибка оплаты")
                            InvoiceStatus.Paid -> webApp.showAlert(message = "Оплата принята")
                            else -> {}
                        }
                    }
                }
            }
        }
    ) {
        Text("Upgrade")
    }
}