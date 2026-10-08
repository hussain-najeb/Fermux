package org.foss.fermux.components.settingsComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.components.generalComponents.SettingAlertDialog
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun TileOptions(
     title: String,
     description: String,
     shape: Shape = RoundedCornerShape(1.dp),
     icon: ImageVector? = null,
     image: Int? = null,
     onClick: () -> Unit,

     dialogShow: Boolean? = false,
     dialogTitle: String? = null,
     dialogDescription: String? = null,
     specialDescription: AnnotatedString? = null,
     dialogImage: Int? = null,
     dialogContent: @Composable (() -> Unit)? = null,

     liner: Boolean? = null,
     content: @Composable (() -> Unit)? = null,
     leadingContent: @Composable (() -> Unit)? = null,
     trailingContent: @Composable (() -> Unit)? = null
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     var dialogShower by remember { mutableStateOf(false) }

     val surfaceColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveButton else FermuxColors.fermuxInActiveButton
     )
     val contentColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.downriver else FermuxColors.white,
          animationSpec = tween(400),
          label = "Fermux Container Colors",
     )

     Column(
          modifier = Modifier
               .fillMaxSize()
               .padding(start = 5.dp, end = 5.dp)
     ) {
          Surface(
               modifier = Modifier.padding(2.dp),
               shape = shape,
               contentColor = contentColor,
               interactionSource = interactionSource,
               color = surfaceColor,
               onClick = {
                    onClick.invoke()
                    if (dialogShow == true) {
                         dialogShower = true
                    }
               }
          ) {
               Row(
                    modifier = Modifier
                         .fillMaxWidth()
                         .height(IntrinsicSize.Min)
                         .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
               ) {
                    icon?.let {
                         Icon(
                              imageVector = icon,
                              contentDescription = null,
                              modifier = Modifier
                                   .padding(end = 16.dp)
                                   .size(28.dp)
                         )
                    }

                    if (image != null) {
                         Icon(
                              painter = painterResource(id = image),
                              contentDescription = null,
                              modifier = Modifier
                                   .padding(end = 16.dp)
                                   .size(28.dp)
                         )
                    }
                    leadingContent?.invoke()
                    Column(
                         modifier = Modifier
                              .weight(1f)
                              .padding(start = if (icon == null && image == null && leadingContent == null) 12.dp else 5.dp)
                    ) {
                         Text(
                              text = title,
                              fontSize = 18.sp,
                              modifier = Modifier.padding(1.dp),
                              maxLines = 1,
                              style = MaterialTheme.typography.titleLarge
                         )
                         Spacer(modifier = Modifier.height(2.dp))

                         Text(
                              text = description,
                              modifier = Modifier.padding(1.dp),
                              maxLines = 4,
                              style = MaterialTheme.typography.bodyMedium
                         )
                    }
                    if (liner == true) VerticalDivider(
                         modifier = Modifier
                              .fillMaxHeight()
                              .padding(start = 4.dp, end = 4.dp),
                         thickness = 0.7.dp,
                         color = FermuxColors.fermuxHelperBorder,
                    )
                         content?.invoke()
               }
          }
          trailingContent?.invoke()
     }

     if (dialogShower) {
          SettingAlertDialog(
               onDismissRequest = { dialogShower = false },
               title = dialogTitle,
               description = dialogDescription,
               specialDescription = specialDescription,
               settingImage = dialogImage,
               dialogContent = dialogContent
          )
     }
}