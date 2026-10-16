package org.foss.fermux.components.downloaderComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.R
import org.foss.fermux.components.buttons.FilterButton
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SeqCard(
  modifier: Modifier,
  title: String,


) {

  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val iconBackground by animateColorAsState(
    targetValue = if (isPressed) FermuxColors.deepBlue else FermuxColors.warmPurple,
    animationSpec = tween(delayMillis = 10),
    label = "color of main icons page"
  )

  val titleColor by animateColorAsState(
    targetValue = if (isPressed) FermuxColors.offWhiteTextColor else FermuxColors.white,
    label = "color of main title of main page"
  )

  val descriptionBackground by animateColorAsState(
    targetValue = if (isPressed) FermuxColors.fermuxSurface else FermuxColors.darkPurple,
    label = "color of main background of the description surface"
  )

  val size by animateFloatAsState(
    targetValue = if (isPressed) 0.98f else 1f,
    animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
    label = ""
  )


  Surface(
    modifier = modifier
      .fillMaxWidth()
      .graphicsLayer {
        scaleY = size
        scaleX = size
      }
      .clickable(
        interactionSource = interactionSource,
        indication = null
      ) {}
      .height(100.dp),
    color = descriptionBackground,
    shape = RoundedCornerShape(12.dp),
    border = BorderStroke(1.dp, FermuxColors.warmPurple),
    tonalElevation = 12.dp,
    shadowElevation = 12.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth()
    ) {
      Surface(
        modifier = Modifier
          .fillMaxHeight()
          .align(Alignment.CenterVertically),
        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
        color = iconBackground
      ) {
        AsyncImage()
      }
      Surface(
        modifier = Modifier.fillMaxSize(),
        color = descriptionBackground,
      ) {
        Row(modifier = Modifier.padding(2.dp).fillMaxSize()) {
          Column(
            modifier = Modifier
              .weight(1f)
              .padding(10.dp)
          ) {
            Text(
              text = title,
              fontSize = 18.sp,
              fontStyle = FontStyle.Italic,
              fontFamily = FontFamily.Default,
              fontWeight = FontWeight.SemiBold,
              minLines = 1,
              maxLines = 5,
              overflow = TextOverflow.Ellipsis,
              color = titleColor
            )
          }
            FilterButton(
              modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 10.dp),
              onClick = { buttonOnClick?.invoke() },
              image = R.drawable.info,
            )
        }
      }
    }
  }
}