package org.foss.fermux.fermuxUIComponents.generalComponents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.fermuxUIComponents.buttons.FermuxBackButton
import org.foss.fermux.ui.theme.FermuxColors

/**
 * The standard Fermux page shell: a collapsing [LargeTopAppBar] with a back
 * button, wired up to a [Scaffold] with the app's background color and
 * nested-scroll behavior already configured.
 * Use this for any top-level page that needs the large collapsing title
 * back button pattern (Settings, History, etc.) instead of re-wiring
 * Scaffold/LargeTopAppBar/scrollBehavior by hand each time.
 */
@Composable
fun FermuxLargeTopBarScaffold(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
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
        topBar = {
            Column {
                LargeTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = FermuxColors.fermuxBackground,
                        scrolledContainerColor = FermuxColors.fermuxBackground,
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
                            fontSize = 35.sp,
                            color = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    },
                    navigationIcon = {
                        FermuxBackButton(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            modifier = Modifier.padding(10.dp).size(44.dp),
                            contentPadding = PaddingValues(3.dp),
                            onClick = onBack
                        )
                  }
                )
                FermuxDivider()
            }
        },
        content = content
    )
}
