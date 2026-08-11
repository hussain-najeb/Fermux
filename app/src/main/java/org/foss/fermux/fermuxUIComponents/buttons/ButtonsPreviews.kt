package org.foss.fermux.fermuxUIComponents.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.ui.theme.FermuxColors


// TODO. Add documentation like the other button to this one and the other as well

// Example for how the buttons will look like
@Preview
@Composable
fun Why() {
    var pressed by remember { mutableStateOf(false) } // To give the button an On/Off state.
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextWithIconButton(
            modifier = Modifier.defaultMinSize(minWidth = 70.dp), // To decide how big the button is.
            contentPadding = PaddingValues(9.dp), // To give text and icon a room in the button.
            icon = Icons.Default.ExpandMore, // Optional icon.
            iconRotation = if (pressed) 180f else 0f, // Optional icon animation.
            text = if (pressed) "show" else "hide", // Optional text given by caller for the button.
            onClick = { pressed = !pressed } // onClick for executing the desired effect.
        )

        Spacer(Modifier.height(20.dp))

        MainActionButton(
            modifier = Modifier.size(70.dp),
            image = painterResource(R.drawable.ic_launcher_background),
            componentSize = 50.dp, // The biggest the image can get.
            iconRotation = if (pressed) 180f else 0f,
            onClick = {pressed = !pressed },
        )

        Spacer(Modifier.height(20.dp))

        CancelButton(
            modifier = Modifier.size(45.dp),
            iconRotation = if (pressed) 360f else 0f,
            onClick = {pressed = !pressed },
        )

        Spacer(Modifier.height(20.dp))

        AppIconButton(
            icon = Icons.Default.ContentPaste,
            modifier = Modifier.size(50.dp),
            onClick = {pressed = !pressed}
        )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .verticalScroll(rememberScrollState())

            ) {

                SettingsSwitch(
                    checked = pressed,
                    onCheckedChange = {}
                )

            }
        }

    }
