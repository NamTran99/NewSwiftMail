package app.k9mail.core.ui.compose.designsystem.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodySmall
import androidx.compose.material3.Button as Material3Button

@Composable
fun ButtonUnderlineText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    enabled: Boolean = true,
) {
    Material3Button(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .size(10.dp)
            .background(Color.Blue)
         ,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        enabled = enabled,
    ) {
        TextBodySmall(
            text = text,
            textStyle = { textStyle -> textStyle.copy(textDecoration = TextDecoration.Underline) },
            color = color,
        )
    }
}
