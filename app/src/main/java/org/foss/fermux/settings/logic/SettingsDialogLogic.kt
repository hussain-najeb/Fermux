package org.foss.fermux.settings.logic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogLogic(
    title: String,
    text: String,
    onDismiss: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2C2F36))
                .padding(24.dp)
        ) {
            Text(title, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text("OK", color = Color(0xFF5669BD),
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onDismiss() }
            )
        }
    }
}










