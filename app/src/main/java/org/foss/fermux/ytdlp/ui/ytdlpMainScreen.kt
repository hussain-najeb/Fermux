package org.foss.fermux.ytdlp.ui


import android.content.ClipData
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.R
import kotlin.collections.listOf


// Add unique Font for the Download screen



@Composable
fun DownloaderScreen(navigationController: NavHostController) {


    var currentPage by remember { mutableStateOf("download") }


    Row(
        modifier = Modifier

        .fillMaxSize()

    )
    {

        Image(painter = painterResource(R.drawable.icon_sidebar_toggle_default),
            contentDescription = "Side bar to toggle default",
            modifier = Modifier
                .size(53.dp)
                .clickable {}


        )
        // Sidebar Column

        Column(
            modifier = Modifier
                .padding(top = 70.dp)
                .height(460.dp)
                .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd  = 20.dp) )
                .background(Color(0xFF1a1d24))
                .width(60.dp)
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                    ambientColor = Color(0xFF111420),
                    spotColor = Color(0xFF272d45)


                )


        ) {





            Spacer(modifier = Modifier.height(10.dp))


Image( painter =
    painterResource(if (currentPage == "download")
        R.drawable.icon_download_active
    else R.drawable.icon_download_default

    ),
    contentDescription = "idle download",
    modifier = Modifier
        .offset(x = 2.dp)
        .size(53.dp)
        .clickable { currentPage = "download" }
        .clip(RoundedCornerShape(8.dp))
        .shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
            ambientColor = Color(0xFF171B2B),
            spotColor = Color(0xFF272d45)
        )
)



            Spacer(modifier = Modifier.height(14.dp))

            Image(painter = painterResource(R.drawable.icon_fermux_default),
                contentDescription = "Back Home",
                modifier = Modifier
                    .offset(x = 2.dp)
                    .size(53.dp)
                .clickable { navigationController.popBackStack() }
                .clip(RoundedCornerShape(8.dp))

                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = Color(0xFF171B2B),
                    spotColor = Color(0xFF272d45)
                )
            )





        }


// Content Column

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(color = Color(0xFF282c34)
                )


        ) {

            when (currentPage) {
                "download" -> DownloadContent()
                "Audio List" -> AudioScreen()
                "Download List" -> DownloadList()
                "Favourite Links" -> FavouriteLinks()
            }


        }


    }
}

@Composable
fun DownloadContent() {

    var downloadUrl by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .offset(x = 5.dp, y = 10.dp),
        value = downloadUrl,
        onValueChange = { txt -> downloadUrl = txt },
        label = { Text("Type URL here") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send,
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false
        )

    )
    Box(
        modifier = Modifier


        )

    {
        Button(
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


@Composable
fun AudioScreen() {

}

@Composable
fun DownloadList() {

}

@Composable
fun FavouriteLinks() {

}





















