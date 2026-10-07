package org.foss.fermux.components.generalComponents

import android.view.WindowManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SettingAlertDialog(
     onDismissRequest: () -> Unit,
     title: String,
     description: String,
     settingImage: Int
) {
     Dialog(
          onDismissRequest = onDismissRequest
     ) {
          val view = (LocalView.current.parent as? DialogWindowProvider)?.window
          SideEffect {
               view?.apply {
                    addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    setDimAmount(0.6f)
               }
          }

          Surface(
               modifier = Modifier
                    .height(230.dp)
                    .fillMaxWidth()
                    .padding(0.dp),
               shape = RoundedCornerShape(12.dp),
               color = FermuxColors.warmPurple,
               border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder),
          ) {
               Column(
                    modifier = Modifier.fillMaxSize()
               ) {
                    Box(
                         modifier = Modifier
                              .fillMaxWidth()
                              .padding(8.dp),
                         contentAlignment = Alignment.TopCenter
                    ) {
                         Row(horizontalArrangement = Arrangement.Center) {
                              Icon(
                                   painter = painterResource(id = settingImage),
                                   contentDescription = null,
                                   tint = FermuxColors.white,
                                   modifier = Modifier
                                        .padding(4.dp)
                                        .size(28.dp)
                              )
                              Text(
                                   text = title,
                                   fontSize = 22.sp,
                                   fontStyle = FontStyle.Normal,
                                   fontWeight = FontWeight.SemiBold,
                                   color = FermuxColors.white,
                                   modifier = Modifier.padding(4.dp)
                              )
                         }
                    }
                    Box(
                         modifier = Modifier
                              .fillMaxWidth()
                              .padding(7.dp),
                         contentAlignment = Alignment.CenterStart
                    ) {
                         Text(
                              text = description,
                              fontSize = 18.sp,
                              fontStyle = FontStyle.Normal,
                              fontWeight = FontWeight.SemiBold,
                              color = FermuxColors.white
                         )
                    }
               }
          }
     }
}

@Preview
@Composable
fun test12() {
     Column(
          modifier = Modifier.fillMaxSize()
     ) {
          SettingAlertDialog(
               onDismissRequest = {},
               settingImage = R.drawable.eraser,
               title = "Setting name",
               description = "Soemthing Soemthing Soemthing Soemthing Soemthing Soemthing Soemthing Soemthing Soemthing Skmfo Soemthing w-kfw0e Soemthing mr0i Soemthing Soemthing Soemthing Soemthing "
          )
     }
}