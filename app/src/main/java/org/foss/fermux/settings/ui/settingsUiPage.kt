package org.foss.fermux.settings.ui


import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.Icon
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.SettingsViewModel


@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen()

 {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity, factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
)
     val notificationState by settingsViewModel.notificationState.collectAsState()


     Column(modifier = Modifier
         .fillMaxSize()
         .background(Color(0xFF2a2b3d))

     ) {
         Text("General",
             fontFamily = FontFamily.Default,
             fontSize = 40.sp,
             color = Color(0xFF129ff9),
             modifier = Modifier
                 .padding(10.dp)
         )

         Spacer(Modifier.height(16.dp))

         ListItem(
             modifier = Modifier
                 .padding(5.dp)
                 .clip(RoundedCornerShape(8.dp))
                 .border(2.5.dp, Color(0x1A00FFAA), RoundedCornerShape(8.dp))
                 .border(1.5.dp, Color(0x5500FFAA), RoundedCornerShape(8.dp))
                 .border(0.8.dp, Color(0xAA00FFAA), RoundedCornerShape(8.dp))
             ,
             headlineContent = {Text("Download Notifications")},
             colors = ListItemDefaults.colors(Color(0xFF1f2034)),
             supportingContent = {Text("Notify me of downloaded files")},
             leadingContent = {Icon(Icons.Default.Notifications, contentDescription = null)},
             trailingContent = {
                 Switch(
                     checked = notificationState,
                     onCheckedChange = {settingsViewModel.setNotificationState(it)}
                 )
             }
         )





     }




}