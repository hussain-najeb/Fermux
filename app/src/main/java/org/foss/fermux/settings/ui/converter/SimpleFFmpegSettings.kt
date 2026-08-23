package org.foss.fermux.settings.ui.converter

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.AudioBitrateSlider
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.ResolutionSelect
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.ui.theme.FermuxColors


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
	var resolutionSelector by remember { mutableStateOf(false) }


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
			title = "Normalize Audio",
			description = "Audio normalization is uniformly adjusting a recording's overall volume so its peak or average loudness hits a specific target level",
			image = R.drawable.normalize_audio,
			content = {
				SettingsSwitch(
					checked = normalizeAudio,
					onCheckedChange = {
						ffmpegSettingsViewModel.setNormalizeAudio(it)
					}
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
        SettingListInfo(
        	title = "Video Compression",
        	description = "Video compression re-encodes the video instead of copying it as-is, trading conversion speed for a smaller file size",
        	image = R.drawable.video_compression,
        	content = {
        		SettingsSwitch(
        			checked = enableVideoCompression,
        			onCheckedChange = {
        				ffmpegSettingsViewModel.setEnableVideoCompression(it)
        				}
        			)
        		}
        	),
        SettingListInfo(
        	title = "Video Resolution",
        	description = "Edit the video resolution for the selected media prior to using the converter",
        	image = R.drawable.video_resolution,
        	onClick = { resolutionSelector = !resolutionSelector },
        	content = {
                ResolutionSelect(
                	expanded = resolutionSelector
                	)
        		}
        	),
	)

LargeTopBarScaffold(
          title = "Settings",
          onBack = { navController.popBackStack() }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
          ) {

simpleFFmpegSetting.forEach { option ->
    SettingLists(
    	title = option.title,
    	description = option.description,
    	image = option.image,
    	icon = option.icon,
    	onClick = { option.onClick?.invoke() },
    	content = option.content,
    	trailingContent = option.trailingContent
    			)
 			}
		}
	}
}