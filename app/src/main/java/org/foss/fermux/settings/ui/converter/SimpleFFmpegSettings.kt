package org.foss.fermux.settings.ui.converter

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.AudioBitrateSlider
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo


@Composable
fun SimpleFFmpegSetting(
	navController: NavHostController,
	@SuppressLint("ContextCastToActivity") ffmpegSettingsViewModel: FFmpegSettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity) 
	) {

	val audioBitrate by ffmpegSettingsViewModel.audioBitrate.collectAsStateWithLifecycle()
	val normalizeAudio by ffmpegSettingsViewModel.normalizeAudio.collectAsStateWithLifecycle()
	val monoDownmix by ffmpegSettingsViewModel.monoDownmix.collectAsStateWithLifecycle()
	val enableVideoCompression by ffmpegSettingsViewModel.enableVideoCompression.collectAsStateWithLifecycle()
	val videoResolution by ffmpegSettingsViewModel.videoResolution.collectAsStateWithLifecycle()
	val videoCrf by ffmpegSettingsViewModel.videoCrf.collectAsStateWithLifecycle() // for the CRF icon icon = Icons.Default.Tune
	val useHardwareEncoder by ffmpegSettingsViewModel.useHardwareEncoder.collectAsStateWithLifecycle()
	val threadLimit by ffmpegSettingsViewModel.threadLimit.collectAsStateWithLifecycle()

	var audioBitrateExpansion by remember { mutableStateOf(false) }


	val simpleFFmpegSetting = listOf(
		SettingListInfo(
			title = "Audio Bitrate",
			description = "Audio bitrate is the amount of data processed for each second of sound",
			image = R.drawable.edit_audio,
			onClick = { audioBitrateExpansion = !audioBitrateExpansion },
			trailingContent = {
				AudioBitrateSlider(
					expanded = audioBitrateExpansion
				)
			}
		),
		SettingListInfo(
			title = "Mono Downmix",
			description = "Mono downmix blends multi-channel or stereo audio into one single channel",
			image = R.drawable.headphones,
			content = {
				SettingsSwitch(
					checked = monoDownmix,
					onCheckedChange = { ffmpegSettingsViewModel.setMonoDownmix(it) }
				)
			}
		),



	)










}