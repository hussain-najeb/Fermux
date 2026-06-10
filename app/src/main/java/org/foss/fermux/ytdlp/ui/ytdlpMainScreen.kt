package org.foss.fermux.ytdlp.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.settings.ui.SettingsScreen


// Add unique Font for the Download screen



@Composable
fun DownloaderScreen(navigationController: NavHostController) {



    var currentPage by remember { mutableStateOf("download") }


    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF282c34))
    ){







       Image(
            painter = painterResource(
                R.drawable.icon_sidebar_toggle_active),
contentDescription = "Side bar to toggle default",
modifier = Modifier
    .offset(x = 2.dp, y = 640.dp)
    .padding(top = 12.dp)
    .size(53.dp)
    .clickable {}


             )


        Row(
            modifier = Modifier
                .fillMaxSize()

        )
        {

            // Sidebar Column

            Column(



                modifier = Modifier
                    .padding(top = 180.dp)
                    .height(320.dp)
                    .width(70.dp)
                    .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                    .background(Color(0xFF1a1d24))
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                        ambientColor = Color(0xFF111420),
                        spotColor = Color(0xFF272d45)


                    )


            ) {


                Spacer(modifier = Modifier.height(10.dp))


                ToggleButton(
                    modifier = Modifier
                        .offset(x = 6.dp),
                    checked = currentPage == "download",
                    onCheckedChange = {currentPage = "download"},
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ToggleButton(
                    modifier = Modifier
                    .offset(x = 6.dp),
                    checked = currentPage == "Download List",
                    onCheckedChange = {currentPage = "Download List"},
                ) {
                    Icon (
                        imageVector = Icons.Default.Movie,
                        contentDescription = "Download List",
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ToggleButton(
                    modifier = Modifier
                        .offset(x = 6.dp),
                    checked = currentPage == "Audio List",
                    onCheckedChange = {currentPage = "Audio List"},
                ) {
                    Icon(
                        imageVector = Icons.Default.LibraryMusic,
                        contentDescription = "Audio List",
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ToggleButton(
                    modifier = Modifier
                    .offset(x = 6.dp),
                    checked = currentPage == "Settings",
                    onCheckedChange = {currentPage = "Settings"},
                ) {
                    Icon (
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))


               IconButton(
                   modifier = Modifier
                           .offset(x = 6.dp),
                   onClick = { navigationController.popBackStack() }
               ) {
                   Icon(
                       imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                       contentDescription = "Home",
                   )
               }
               }


// Content Column

            //Also — imePadding() and verticalScroll
            // aren't on this Column yet,
            // but you don't need them until the TextField actually opens a
            // keyboard and pushes content.
            // Tomorrow's problem.

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF282c34))


            ) {

                when (currentPage) {
                    "download" -> DownloadContent()
                    "Audio List" -> AudioScreen()
                    "Download List" -> DownloadList()
                    "Favourite Links" -> FavouriteLinks()
                    "Settings" -> SettingsScreen()
                }


            }


        }
    }
}

@Composable
fun DownloadContent() {



    var downloadUrl by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .fillMaxSize()

    )

        {

        OutlinedTextField(
modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
            value = downloadUrl,
            onValueChange = { txt -> downloadUrl = txt },
            label = { Text("Type URL here") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
            )

        )
        Box {
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 30.dp),

                onClick = {
                    downloadUrl = ""
                }) {
                Text("Download")
            }
        }
    }
}


@Composable
fun AudioScreen() {



}

@Composable
fun DownloadList() {

}

@Composable
fun FavouriteLinks() {

}





















