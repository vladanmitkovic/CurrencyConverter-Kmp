package me.mitkovic.kmp.currencyconverter.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onDecimalClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val buttons =
        listOf(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            ".",
            "0",
            "<-",
        )

    val scopeModifier =
        Modifier
            .padding(horizontal = MaterialTheme.spacing.small) // Add padding around the keypad
            .fillMaxWidth() // Fill the width of its parent

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = scopeModifier) {
        for (i in buttons.indices step 3) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth() // Fill the width of its parent
                        .padding(vertical = MaterialTheme.spacing.extraSmall),
                // Add padding between rows
                horizontalArrangement = Arrangement.SpaceBetween, // Distribute space evenly between the buttons
            ) {
                for (j in i until i + 3) {
                    StyledTextButton(
                        buttonText = buttons[j],
                        onClick = {
                            when (buttons[j]) {
                                "." -> onDecimalClick()
                                "<-" -> onDeleteClick()
                                else -> onNumberClick(buttons[j])
                            }
                        },
                        modifier =
                            Modifier
                                .weight(1f) // Make each button take equal space
                                .padding(horizontal = MaterialTheme.spacing.extraSmall) // Add padding between buttons
                                .height(MaterialTheme.spacing.numericButton),
                    )
                }
            }
        }
    }
}
