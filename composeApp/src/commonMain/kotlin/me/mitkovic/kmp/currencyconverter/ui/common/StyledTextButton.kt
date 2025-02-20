package me.mitkovic.kmp.currencyconverter.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing

@Composable
fun StyledTextButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .clip(RoundedCornerShape(MaterialTheme.spacing.medium)) // Applies rounded corners
                .padding(horizontal = MaterialTheme.spacing.extraSmall),
        // Adds padding to maintain spacing
        shape = RoundedCornerShape(MaterialTheme.spacing.extraMedium), // Further specifies the shape
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Sets the background color
                contentColor = MaterialTheme.colorScheme.onPrimary, // Sets the text/icon color
            ),
    ) {
        Text(
            text = buttonText,
            style = textStyle,
        )
    }
}
