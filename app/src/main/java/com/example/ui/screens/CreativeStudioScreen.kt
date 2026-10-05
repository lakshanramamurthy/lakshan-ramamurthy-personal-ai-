package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ActiveGreen
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.BrandBorder
import com.example.ui.theme.BrandCharcoal
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JarvisViewModel

@Composable
fun CreativeStudioScreen(
    viewModel: JarvisViewModel,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("AI Art & Image", "AI Music & Scoring", "AI Video & Motion")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandCharcoal)
            .testTag("creative_studio_screen")
    ) {
        // Top Command Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xF01C1D21))
                .border(1.dp, BrandBorder, RoundedCornerShape(0.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(onClick = onOpenDrawer, modifier = Modifier.size(36.dp)) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                }

                Column {
                    Text(
                        text = "Creative Studio & Media Lab",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Imagen 3 Ultra • MusicFX Pro • Google Veo 2",
                        fontSize = 11.sp,
                        color = BrandAccent
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF282A30))
                    .border(1.dp, BrandBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Zero Latency",
                    fontSize = 11.sp,
                    color = ActiveGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = BrandSurface,
            contentColor = BrandAccent,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = BrandAccent
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.5.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) TextPrimary else TextMuted
                        )
                    }
                )
            }
        }

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> ImageStudioContent(viewModel)
                1 -> MusicStudioContent(viewModel)
                2 -> VideoStudioContent(viewModel)
            }
        }
    }
}

@Composable
private fun ImageStudioContent(viewModel: JarvisViewModel) {
    val prompt by viewModel.imagePrompt.collectAsState()
    val ratio by viewModel.selectedAspectRatio.collectAsState()
    val style by viewModel.selectedImageStyle.collectAsState()
    val isGenerating by viewModel.isGeneratingImage.collectAsState()
    val context = LocalContext.current

    val aspectRatios = listOf("16:9", "4:5", "1:1", "9:16")
    val styles = listOf("Minimalist Editorial", "Photorealistic", "Architectural", "Obsidian Luxury")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Image Canvas Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F24)),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CANVAS PREVIEW (Imagen 3 Ultra)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Aspect: $ratio • 4K UHD",
                        fontSize = 11.sp,
                        color = BrandAccent
                    )
                }

                // Rendered Asset
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF141416)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_studio_concept),
                        contentDescription = "Studio concept render",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (isGenerating) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xCC141416)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(color = BrandAccent)
                                Text("Synthesizing 4K UHD Concept...", fontSize = 12.sp, color = TextPrimary)
                            }
                        }
                    }
                }

                // Analysis bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lighting: Volumetric Amber • Dynamic Range: High",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF282A30))
                            .clickable {
                                Toast.makeText(context, "Image analyzed: Optimal $ratio compositional symmetry verified", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(13.dp))
                        Text("Analyze Image", fontSize = 11.sp, color = TextPrimary)
                    }
                }
            }
        }

        // Prompt input
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Creative Prompt", fontSize = 11.sp, color = TextMuted)
            OutlinedTextField(
                value = prompt,
                onValueChange = { viewModel.imagePrompt.value = it },
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF18191D),
                    unfocusedContainerColor = Color(0xFF18191D),
                    focusedBorderColor = BrandAccent,
                    unfocusedBorderColor = BrandBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Aspect Ratio Selector
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "ASPECT RATIO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                aspectRatios.forEach { r ->
                    val isSelected = ratio == r
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) BrandAccent else Color(0xFF1E1F24))
                            .border(1.dp, if (isSelected) BrandAccent else BrandBorder, RoundedCornerShape(6.dp))
                            .clickable { viewModel.selectedAspectRatio.value = r }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = r,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White else TextSecondary
                        )
                    }
                }
            }
        }

        // Style Selector
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "STYLE PRESET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                styles.forEach { s ->
                    val isSelected = style == s
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Color(0xFF282A30) else Color(0xFF1E1F24))
                            .border(1.dp, if (isSelected) BrandAccent else BrandBorder, RoundedCornerShape(6.dp))
                            .clickable { viewModel.selectedImageStyle.value = s }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = s,
                            fontSize = 11.sp,
                            color = if (isSelected) BrandAccent else TextSecondary
                        )
                    }
                }
            }
        }

        // Generate Button
        Button(
            onClick = { viewModel.generateStudioImage() },
            colors = ButtonDefaults.buttonColors(containerColor = BrandAccent),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("generate_image_button")
        ) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Synthesize Image ($ratio)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

@Composable
private fun MusicStudioContent(viewModel: JarvisViewModel) {
    val prompt by viewModel.musicPrompt.collectAsState()
    val engine by viewModel.selectedMusicEngine.collectAsState()
    val spatial by viewModel.selectedSpatialMode.collectAsState()
    val isGenerating by viewModel.isGeneratingMusic.collectAsState()
    val isPlaying by viewModel.isPlayingMusic.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Acoustic Player Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F24)),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MUSIC & ACOUSTIC SYNTHESIZER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Lossless 24-bit 96kHz",
                        fontSize = 11.sp,
                        color = ActiveGreen
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.toggleMusicPlayback() },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(BrandAccent)
                            .testTag("toggle_music_playback_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Playback",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Binaural Focus Alpha Flow",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "$engine • $spatial Mode • 40Hz Gamma",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                // Dynamic Acoustic Spectrum Waveform
                SpectrumWaveform(isPlaying = isPlaying)
            }
        }

        // Music Prompt
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Music Composition Prompt", fontSize = 11.sp, color = TextMuted)
            OutlinedTextField(
                value = prompt,
                onValueChange = { viewModel.musicPrompt.value = it },
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF18191D),
                    unfocusedContainerColor = Color(0xFF18191D),
                    focusedBorderColor = BrandAccent,
                    unfocusedBorderColor = BrandBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Engine & Spatial Mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "AUDIO ENGINE", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF282A30))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Text(text = engine, fontSize = 11.5.sp, color = TextPrimary)
                }
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "SPATIAL MODE", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF282A30))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Text(text = spatial, fontSize = 11.5.sp, color = TextPrimary)
                }
            }
        }

        // Compose Button
        Button(
            onClick = { viewModel.generateStudioMusic() },
            colors = ButtonDefaults.buttonColors(containerColor = BrandAccent),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("compose_music_button")
        ) {
            if (isGenerating) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Synthesizing Soundscape...", fontSize = 12.sp, color = Color.White)
            } else {
                Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Compose 96kHz Acoustic Score", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Composable
private fun VideoStudioContent(viewModel: JarvisViewModel) {
    val prompt by viewModel.videoPrompt.collectAsState()
    val model by viewModel.selectedVideoModel.collectAsState()
    val framerate by viewModel.selectedFramerate.collectAsState()
    val isGenerating by viewModel.isGeneratingVideo.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Video Preview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F24)),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MOTION SEQUENCE TIMELINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                    Text(text = "$model • $framerate", fontSize = 11.sp, color = BrandAccent)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF141416)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_studio_concept),
                        contentDescription = "Video motion sequence",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (isGenerating) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xCC141416)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                CircularProgressIndicator(color = BrandAccent)
                                Text("Rendering 60 FPS Scene...", fontSize = 12.sp, color = TextPrimary)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0x99141416)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(26.dp))
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Multi-camera consistency locked • Audio synced", fontSize = 11.sp, color = TextMuted)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF282A30))
                            .clickable {
                                Toast.makeText(context, "Voice-to-video narration attached using Nova archetype", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.GraphicEq, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(13.dp))
                        Text("Add Voice Narration", fontSize = 11.sp, color = TextPrimary)
                    }
                }
            }
        }

        // Prompt input
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Video Motion Directive", fontSize = 11.sp, color = TextMuted)
            OutlinedTextField(
                value = prompt,
                onValueChange = { viewModel.videoPrompt.value = it },
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF18191D),
                    unfocusedContainerColor = Color(0xFF18191D),
                    focusedBorderColor = BrandAccent,
                    unfocusedBorderColor = BrandBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.generateStudioVideo() },
                colors = ButtonDefaults.buttonColors(containerColor = BrandAccent),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("render_video_button")
            ) {
                Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Render 60 FPS Video", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Animating image concept into 60 FPS video clip", Toast.LENGTH_SHORT).show()
                    viewModel.generateStudioVideo()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282A30)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
            ) {
                Text("Animate Image", fontSize = 12.sp, color = TextPrimary)
            }
        }
    }
}

@Composable
private fun SpectrumWaveform(isPlaying: Boolean) {
    val transition = rememberInfiniteTransition(label = "spectrum")
    val animatedPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF141416))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val count = 28
        for (i in 0 until count) {
            val factor = ((i * 3 + (animatedPhase * 15).toInt()) % 20) + 6
            val barHeight = if (isPlaying) factor.dp else 4.dp
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(barHeight)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (i % 2 == 0) BrandAccent else BrandAccent.copy(alpha = 0.6f))
            )
        }
    }
}
