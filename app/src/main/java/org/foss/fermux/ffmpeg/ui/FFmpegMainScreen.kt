package org.foss.fermux.ffmpeg.ui


import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment


@Composable
fun ConverterScreen() {

    var pickedFile by remember { mutableStateOf<Uri?>(null) }

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri -> pickedFile = uri }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF181825))
    )

    {

        Card(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp)),
            colors = CardColors(
                contentColor = Color.Unspecified,
                containerColor = Color(0xFF1f2034),
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified),
            border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
            content =
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        ElevatedButton(
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonColors(
                                contentColor = Color(0xFF1f2034),
                                containerColor = Color(0xFF383b4d),
                                disabledContentColor = Color.Unspecified,
                                disabledContainerColor = Color.Unspecified),

                            modifier = Modifier
                            .padding(5.dp)
                            .size(85.dp),

                            onClick = {  }

                        ) {
                            Icon(imageVector = Icons.Default.Upload,
                                tint = Color(0xFFE7E7E2),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(33.dp)

                            )
                        }

                    }
                }
        )











    }
}