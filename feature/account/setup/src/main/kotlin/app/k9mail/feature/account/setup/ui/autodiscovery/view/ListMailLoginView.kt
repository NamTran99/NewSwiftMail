package app.k9mail.feature.account.setup.ui.autodiscovery.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.k9mail.core.ui.compose.common.annotation.PreviewDevices
import app.k9mail.core.ui.compose.designsystem.PreviewWithTheme
import app.k9mail.core.ui.compose.designsystem.atom.DividerHorizontal
import app.k9mail.core.ui.compose.designsystem.atom.Surface
import app.k9mail.core.ui.compose.designsystem.atom.text.TextTitleLarge
import app.k9mail.core.ui.compose.theme2.MainTheme
import app.k9mail.feature.account.setup.R

enum class MailState {
    GMAIL, OUTLOOK, YANDEX, OTHER
}

@Composable
internal fun ListMailLoginView(
    listMail: List<MailState>,
    onItemClick: (MailState) -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp)
            .imePadding(),
    ) {
        items(
            listMail,
            key = {
                it.name
            },
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    onItemClick.invoke(it)
                },
            ) {
                Surface(
                    modifier = Modifier
                        .height(100.dp),
                ) {
                    when (it) {
                        MailState.GMAIL -> {
                            Image(painter = painterResource(id = R.drawable.ic_mail), null)
                        }

                        MailState.OUTLOOK -> Image(painter = painterResource(id = R.drawable.ic_outlook), null)
                        MailState.YANDEX -> Image(painter = painterResource(id = R.drawable.ic_yandex), null)
                        MailState.OTHER -> TextTitleLarge(
                            text = stringResource(id = R.string.account_setup_other_server),
                            modifier = Modifier.wrapContentSize(),
                            style = MainTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = MainTheme.colors.primary
                        )
                    }
                }
                DividerHorizontal()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
@PreviewDevices
internal fun ColorContentPreview() {
    PreviewWithTheme {
        ListMailLoginView(
            listOf(MailState.GMAIL, MailState.OUTLOOK, MailState.YANDEX, MailState.OTHER), {},
        )
    }
}
