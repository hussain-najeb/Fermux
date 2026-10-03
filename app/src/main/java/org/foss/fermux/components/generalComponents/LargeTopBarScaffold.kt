package org.foss.fermux.components.generalComponents


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.components.buttons.BackButton
import org.foss.fermux.components.buttons.HelperButton
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun LargeTopBarScaffold(
     modifier: Modifier = Modifier,
     title: String,
     titleSize: TextUnit = 25.sp,
     onBack: (() -> Unit)? = null,
     firstButton: (()  -> Unit)? = null,
     secondButton: (() -> Unit)? = null,
     firstImage: Int? = null,
     secondImage: Int? = null,
     snackbarHost: (@Composable () -> Unit)? = null,
     content: @Composable (PaddingValues) -> Unit
) {
     val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
          rememberTopAppBarState(),
          canScroll = { true }
     )

     Scaffold(
          modifier = modifier
               .fillMaxSize()
               .nestedScroll(scrollBehavior.nestedScrollConnection),
          containerColor = FermuxColors.background,
          snackbarHost = snackbarHost ?: {},
          topBar = {
               LargeTopAppBar(
                    modifier = Modifier
                         .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(
                         containerColor = FermuxColors.background,
                         scrolledContainerColor = FermuxColors.darkPurple,
                         navigationIconContentColor = Color.Unspecified,
                         titleContentColor = Color.Unspecified,
                         actionIconContentColor = Color.Unspecified
                    ),
                    scrollBehavior = scrollBehavior,
                    title = {
                         Text(
                              title,
                              fontFamily = FontFamily.Default,
                              fontWeight = FontWeight.W500,
                              fontSize = titleSize,
                              color = Color.White,
                              modifier = Modifier.padding(10.dp)
                         )
                    },
                    navigationIcon = {
                         if (onBack != null)
                         BackButton(
                              modifier = Modifier.padding(10.dp).size(44.dp),
                              onClick = onBack
                         )
                    },
                    actions = {
                         Row {
                              if (firstButton != null && firstImage != null)
                                   HelperButton(
                                        modifier = Modifier.padding(10.dp).size(44.dp),
                                        onClick = { firstButton.invoke() },
                                        image = firstImage
                                   )
                              if (secondButton != null && secondImage != null)
                              HelperButton(
                                   modifier = Modifier.padding(10.dp).size(44.dp),
                                   onClick = { secondButton.invoke() },
                                   image = secondImage
                              )
                         }
                    }
               )
          },
          content = content
     )
}
