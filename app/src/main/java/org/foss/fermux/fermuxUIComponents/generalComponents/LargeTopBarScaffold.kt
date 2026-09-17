package org.foss.fermux.fermuxUIComponents.generalComponents


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.fermuxUIComponents.buttons.BackButton
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun LargeTopBarScaffold(
     title: String,
     onBack: () -> Unit,
     modifier: Modifier = Modifier,
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
          containerColor = FermuxColors.fermuxBackground,
          snackbarHost = snackbarHost ?: {},
          topBar = {
               LargeTopAppBar(
                    modifier = Modifier
                         .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(
                         containerColor = FermuxColors.fermuxBackground,
                         scrolledContainerColor = FermuxColors.fermuxSaturatedComponents,
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
                              fontSize = 25.sp,
                              color = Color.White,
                              modifier = Modifier.padding(10.dp)
                         )
                    },
                    navigationIcon = {
                         BackButton(
                              modifier = Modifier.padding(10.dp).size(44.dp),
                              onClick = onBack
                         )
                    }
               )
          },
          content = content
     )
}
