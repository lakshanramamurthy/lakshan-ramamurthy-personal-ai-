package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CreativeEngineItem
import com.example.model.ScreenDestination
import com.example.ui.theme.ActiveGreen
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.BrandBorder
import com.example.ui.theme.BrandBorderHover
import com.example.ui.theme.BrandCard
import com.example.ui.theme.BrandCharcoal
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JarvisViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudioSettingsScreen(
    viewModel: JarvisViewModel,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val profile by viewModel.profile.collectAsState()
    val voiceConfig by viewModel.voiceConfig.collectAsState()
    val creativeEngines by viewModel.creativeEngines.collectAsState()
    val systemConfig by viewModel.systemConfig.collectAsState()

    var legalNameInput by remember(profile.legalName) { mutableStateOf(profile.legalName) }
    var handleInput by remember(profile.systemHandle) { mutableStateOf(profile.systemHandle) }
    var titleInput by remember(profile.preferredTitle) { mutableStateOf(profile.preferredTitle) }
    var emailInput by remember(profile.encryptedEmail) { mutableStateOf(profile.encryptedEmail) }

    var isLanguageMenuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandCharcoal)
            .testTag("studio_settings_screen")
    ) {
        // Sticky Header / Top Command Bar matching snapshot
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                IconButton(
                    onClick = onOpenDrawer,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("open_sidebar_drawer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Sidebar",
                        tint = TextPrimary
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "JARVIS SETTINGS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted,
                            letterSpacing = 0.5.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Profile & AI Studio",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Studio Configuration",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        // Status Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x2210B981))
                                .border(1.dp, ActiveGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(ActiveGreen)
                            )
                            Text(
                                text = "Gemini 1.5 Pro: Connected (12ms)",
                                fontSize = 10.5.sp,
                                color = ActiveGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Top Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { viewModel.resetDefaults() },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF222328))
                        .border(1.dp, BrandBorder, RoundedCornerShape(8.dp))
                        .testTag("reset_defaults_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Reset Defaults",
                        tint = TextSecondary,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Button(
                    onClick = {
                        viewModel.updateProfile(legalNameInput, handleInput, titleInput, emailInput)
                        viewModel.saveChanges()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandAccent),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("save_changes_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save Changes",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Save",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        // Sub Navigation Chips & Quick Telemetry
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandSurface)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubNavPill(
                icon = Icons.Default.Badge,
                label = "Profile & Identity",
                hasDot = true,
                isActive = true
            )
            SubNavPill(
                icon = Icons.Default.GraphicEq,
                label = "Voice & Assistant",
                badge = "Live"
            )
            SubNavPill(
                icon = Icons.Default.AutoAwesome,
                label = "Creative Engines",
                badge = "6 Active"
            )
            SubNavPill(
                icon = Icons.Default.Key,
                label = "API Keys & Providers"
            )
            SubNavPill(
                icon = Icons.Default.Hub,
                label = "System Architecture",
                badge = "v3.4"
            )
        }

        // Main Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Neural Compute Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x30222328)),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NEURAL COMPUTE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${systemConfig.neuralComputeScore}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandAccent
                        )
                    }

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF2A2B30))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.72f)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(BrandAccent)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Autonomous agents running under zero-egress hardware enclaves with local state caching.",
                            fontSize = 11.5.sp,
                            color = TextMuted,
                            modifier = Modifier.weight(1f)
                        )

                        // Multi-language picker
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF202126))
                                    .border(1.dp, BrandBorder, RoundedCornerShape(6.dp))
                                    .clickable { isLanguageMenuOpen = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language",
                                    tint = BrandAccent,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = systemConfig.selectedLanguage,
                                    fontSize = 10.5.sp,
                                    color = TextPrimary
                                )
                            }

                            DropdownMenu(
                                expanded = isLanguageMenuOpen,
                                onDismissRequest = { isLanguageMenuOpen = false }
                            ) {
                                systemConfig.multiLanguageList.forEach { lang ->
                                    DropdownMenuItem(
                                        text = { Text(lang, fontSize = 12.sp) },
                                        onClick = {
                                            viewModel.selectLanguage(lang)
                                            isLanguageMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // SECTION 1: Profile & Identity
            // ==========================================
            SettingsSectionCard(
                title = "Profile & Executive Identity",
                description = "Control how Jarvis recognizes, addresses, and formats briefs for you.",
                badgeText = "Tier: ${profile.tier}",
                testTag = "section_profile_identity"
            ) {
                // Identity Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(BrandAccent, Color(0xFF222328))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.initials,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        IconButton(
                            onClick = { viewModel.generateStylizedPersona() },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF141416))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Avatar synthesis",
                                tint = BrandAccent,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = profile.legalName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Verified Principal",
                                fontSize = 10.sp,
                                color = ActiveGreen,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ActiveGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "Neural Voiceprint ID: ${profile.voiceprintId}",
                            fontSize = 11.5.sp,
                            color = TextMuted
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "Generate Stylized Persona",
                                fontSize = 11.sp,
                                color = TextPrimary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF282A30))
                                    .clickable { viewModel.generateStylizedPersona() }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Text(
                                text = "Export Identity Spec",
                                fontSize = 11.sp,
                                color = TextMuted,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF202126))
                                    .clickable { viewModel.exportIdentitySpec() }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Profile Fields Grid
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    JarvisTextField(
                        label = "Full Legal Name",
                        value = legalNameInput,
                        onValueChange = { legalNameInput = it },
                        testTag = "input_legal_name"
                    )

                    JarvisTextField(
                        label = "System Handle",
                        value = handleInput,
                        onValueChange = { handleInput = it },
                        testTag = "input_system_handle"
                    )

                    JarvisTextField(
                        label = "Preferred Executive Title",
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        testTag = "input_executive_title"
                    )

                    JarvisTextField(
                        label = "Encrypted Routing Email",
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        testTag = "input_routing_email"
                    )
                }
            }

            // ==========================================
            // SECTION 2: Voice & Gemini Assistant Engine
            // ==========================================
            SettingsSectionCard(
                title = "Gemini AI Voice Engine & Acoustics",
                description = "Ultra-low latency streaming voice assistant powered by multimodal Gemini architecture.",
                headerAction = {
                    Switch(
                        checked = voiceConfig.isVoiceEnabled,
                        onCheckedChange = { viewModel.toggleVoiceEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = BrandAccent,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = Color(0xFF2A2B30)
                        ),
                        modifier = Modifier.testTag("toggle_voice_enabled")
                    )
                },
                testTag = "section_voice_assistant"
            ) {
                // Voice Mode Selector Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val isGemini = voiceConfig.selectedEngine == "Gemini 1.5 Pro Live Voice"
                    EngineOptionCard(
                        title = "Gemini 1.5 Pro Live Voice",
                        subtitle = "Native multi-speaker reasoning with audio interruptibility and contextual tone matching.",
                        isActive = isGemini,
                        badge = if (isGemini) "Active" else null,
                        icon = Icons.Default.Bolt,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectVoiceEngine("Gemini 1.5 Pro Live Voice") }
                    )

                    val isNeural = voiceConfig.selectedEngine == "Jarvis Neural Studio Synth"
                    EngineOptionCard(
                        title = "Jarvis Neural Studio Synth",
                        subtitle = "High-fidelity localized neural synthesizer for confidential, offline ambient tasks.",
                        isActive = isNeural,
                        badge = if (isNeural) "Active" else null,
                        icon = Icons.Default.Tune,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectVoiceEngine("Jarvis Neural Studio Synth") }
                    )
                }

                // Voice Archetype Selector Grid
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "SELECT EXECUTIVE VOICE ARCHETYPE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        viewModel.voiceArchetypes.forEach { archetype ->
                            val isSelected = voiceConfig.selectedArchetype == archetype.name
                            ArchetypeCard(
                                archetype = archetype,
                                isSelected = isSelected,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.selectVoiceArchetype(archetype.name) }
                            )
                        }
                    }
                }

                // Wave Visualizer & Testing Bar
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F24)),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            IconButton(
                                onClick = {
                                    if (voiceConfig.isPlayingSample) viewModel.stopAudioSample()
                                    else viewModel.playArchetypeSample()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BrandAccent)
                                    .testTag("play_voice_sample_button")
                            ) {
                                Icon(
                                    imageVector = if (voiceConfig.isPlayingSample) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play voice sample",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Voice Sample: ${voiceConfig.selectedArchetype} Briefing",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "\"Good morning, Lakshan. Tokyo markets closed up 1.4%...\"",
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Animated Waveform
                        WaveformVisualizer(isPlaying = voiceConfig.isPlayingSample)

                        Text(
                            text = "${voiceConfig.speed}x",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF282A30))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Sliders & Wake Word Config
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Cadence & Speed", fontSize = 11.sp, color = TextMuted)
                            Text(text = "${String.format("%.2f", voiceConfig.speed)}x", fontSize = 11.sp, color = TextPrimary)
                        }
                        Slider(
                            value = voiceConfig.speed,
                            onValueChange = { viewModel.updateSpeed(it) },
                            valueRange = 0.8f..1.4f,
                            colors = SliderDefaults.colors(
                                thumbColor = BrandAccent,
                                activeTrackColor = BrandAccent,
                                inactiveTrackColor = Color(0xFF2A2B30)
                            ),
                            modifier = Modifier.testTag("slider_voice_speed")
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Pitch Modulation", fontSize = 11.sp, color = TextMuted)
                            Text(text = "${voiceConfig.pitchModulation} Semi", fontSize = 11.sp, color = TextPrimary)
                        }
                        Slider(
                            value = voiceConfig.pitchModulation.toFloat(),
                            onValueChange = { viewModel.updatePitch(it.toInt()) },
                            valueRange = -6f..6f,
                            colors = SliderDefaults.colors(
                                thumbColor = BrandAccent,
                                activeTrackColor = BrandAccent,
                                inactiveTrackColor = Color(0xFF2A2B30)
                            ),
                            modifier = Modifier.testTag("slider_pitch_modulation")
                        )
                    }
                }
            }

            // ==========================================
            // SECTION 3: Creative & Generative AI Engines
            // ==========================================
            SettingsSectionCard(
                title = "Generative Creative Capabilities",
                description = "Modular AI studio engines for direct autonomous creation, design, nutrition, and coding.",
                badgeText = "Studio Mode: Autonomous",
                testTag = "section_creative_engines"
            ) {
                creativeEngines.forEach { engine ->
                    CreativeEngineCard(
                        engine = engine,
                        onToggle = { viewModel.toggleCreativeEngine(engine.id) },
                        onPrimaryOptionSelected = { viewModel.updateCreativeEngineOptions(engine.id, primary = it) },
                        onSecondaryOptionSelected = { viewModel.updateCreativeEngineOptions(engine.id, secondary = it) },
                        onAction = {
                            viewModel.navigateTo(ScreenDestination.CREATIVE_STUDIO)
                        }
                    )
                }
            }

            // ==========================================
            // SECTION 4: API Keys & Model Providers
            // ==========================================
            SettingsSectionCard(
                title = "API Credentials & Model Connectors",
                description = "High-throughput keys authenticated directly with Google Cloud and partner inference endpoints.",
                headerAction = {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Provider added to Zero-Trust enclave", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282A30)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Provider", fontSize = 11.sp, color = TextPrimary)
                    }
                },
                testTag = "section_api_management"
            ) {
                // Gemini Live API Key Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F24)),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(ActiveGreen))
                                Text(
                                    text = "Gemini 1.5 Pro & Live Multimodal API",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Verified: 12ms",
                                    fontSize = 10.sp,
                                    color = ActiveGreen,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ActiveGreen.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                                Text(
                                    text = "Quota: Unlimited",
                                    fontSize = 10.sp,
                                    color = TextMuted,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF282A30))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Key Masked
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF141416))
                                    .border(1.dp, BrandBorder, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "••••••••••••••••••••••••-992014-a92f",
                                    fontSize = 11.5.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextPrimary
                                )
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString("sk-gemini-live-992014-a92f-x0928f82k1940"))
                                        Toast.makeText(context, "API Key copied to clipboard", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Key",
                                        tint = TextMuted,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    Toast.makeText(context, "Connection verified: 12ms latency OK", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282A30)),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Test", fontSize = 11.sp, color = TextPrimary)
                            }
                        }
                    }
                }

                // Additional Providers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProviderStatusPill(
                        icon = Icons.Default.CloudDone,
                        name = "Anthropic Claude (Sonnet 3.5)",
                        status = "Active",
                        modifier = Modifier.weight(1f)
                    )
                    ProviderStatusPill(
                        icon = Icons.Default.GraphicEq,
                        name = "ElevenLabs Voice Pipeline",
                        status = "Active",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ==========================================
            // SECTION 5: System Architecture & Workflow
            // ==========================================
            SettingsSectionCard(
                title = "System Architecture & Workflow Node Graph",
                description = "Visualize agent routing, runtime sandbox constraints, and autonomous decision memory.",
                headerAction = {
                    Button(
                        onClick = { viewModel.navigateTo(ScreenDestination.AUTONOMOUS_TASKS) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282A30)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AccountTree, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Flow Graph", fontSize = 11.sp, color = BrandAccent)
                    }
                },
                testTag = "section_system_architecture"
            ) {
                // Visual Abstract Wire
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E1F24))
                        .border(1.dp, BrandBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NodeBox(
                        icon = Icons.Default.Mic,
                        title = "Multi-Modal Ingest",
                        subtitle = "Voice, Telemetry, Git",
                        tint = BrandAccent
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(BrandBorder)
                    )
                    NodeBox(
                        icon = Icons.Default.Hub,
                        title = "Jarvis Neural Router",
                        subtitle = "Executive Intent Gate",
                        tint = TextPrimary,
                        isCore = true
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(BrandBorder)
                    )
                    NodeBox(
                        icon = Icons.Default.Bolt,
                        title = "Autonomous Actions",
                        subtitle = "PRs, Media, Briefs",
                        tint = ActiveGreen
                    )
                }

                // Architecture Toggles
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ArchitectureToggleRow(
                        title = "Zero-Latency Enclave",
                        subtitle = "Pre-fetch predictive models across Google Workspace",
                        checked = systemConfig.zeroLatencyEnclave,
                        onCheckedChange = { viewModel.toggleZeroLatencyEnclave(it) },
                        testTag = "toggle_zero_latency"
                    )

                    ArchitectureToggleRow(
                        title = "Executive Memory Lock",
                        subtitle = "Encrypted zero-knowledge long-term recall in cloud enclave",
                        checked = systemConfig.executiveMemoryLock,
                        onCheckedChange = { viewModel.toggleExecutiveMemoryLock(it) },
                        testTag = "toggle_memory_lock"
                    )

                    ArchitectureToggleRow(
                        title = "Strict Airgap Mode",
                        subtitle = "Disable 3rd-party telemetry outside approved enclaves",
                        checked = systemConfig.strictAirgapMode,
                        onCheckedChange = { viewModel.toggleStrictAirgapMode(it) },
                        testTag = "toggle_airgap_mode"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    description: String,
    badgeText: String? = null,
    headerAction: @Composable (() -> Unit)? = null,
    testTag: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0x35222328)),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = description,
                        fontSize = 11.5.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                if (badgeText != null) {
                    Text(
                        text = badgeText,
                        fontSize = 10.5.sp,
                        color = TextPrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF282A30))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                } else if (headerAction != null) {
                    headerAction()
                }
            }

            HorizontalDivider(color = BrandBorder.copy(alpha = 0.5f), thickness = 1.dp)

            content()
        }
    }
}

@Composable
private fun JarvisTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    testTag: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(text = label, fontSize = 11.sp, color = TextMuted)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF18191D),
                unfocusedContainerColor = Color(0xFF18191D),
                focusedBorderColor = BrandAccent,
                unfocusedBorderColor = BrandBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
        )
    }
}

@Composable
private fun EngineOptionCard(
    title: String,
    subtitle: String,
    isActive: Boolean,
    badge: String?,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isActive) Color(0xFF282A30) else Color(0xFF1E1F24))
            .border(
                1.dp,
                if (isActive) BrandAccent.copy(alpha = 0.5f) else BrandBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isActive) BrandAccent else TextMuted,
                    modifier = Modifier.size(16.dp)
                )
                if (badge != null) {
                    Text(
                        text = badge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(BrandAccent)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 10.5.sp,
                color = TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ArchetypeCard(
    archetype: com.example.model.VoiceArchetype,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFF282A30) else Color(0xFF1C1D21))
            .border(
                1.dp,
                if (isSelected) BrandAccent else BrandBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = archetype.name,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Icon(
                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (isSelected) BrandAccent else TextMuted,
                    modifier = Modifier.size(13.dp)
                )
            }
            Text(text = archetype.tone, fontSize = 9.5.sp, color = TextMuted)
            Text(text = archetype.pitchLabel, fontSize = 9.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun WaveformVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val animatedHeight by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_height"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        val heights = if (isPlaying) {
            listOf(6f, animatedHeight, 14f, 22f - animatedHeight, 18f, animatedHeight * 0.7f, 8f)
        } else {
            listOf(6f, 10f, 6f, 14f, 8f, 12f, 6f)
        }

        heights.forEach { h ->
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height(h.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(BrandAccent)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CreativeEngineCard(
    engine: CreativeEngineItem,
    onToggle: () -> Unit,
    onPrimaryOptionSelected: (String) -> Unit,
    onSecondaryOptionSelected: (String) -> Unit,
    onAction: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F24)),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = engine.title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = engine.subtitle,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
                Switch(
                    checked = engine.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BrandAccent,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = Color(0xFF2A2B30)
                    )
                )
            }

            // Options Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EngineOptionSelector(
                    label = engine.primaryLabel,
                    currentValue = engine.primaryValue,
                    options = engine.primaryOptions,
                    onSelected = onPrimaryOptionSelected,
                    modifier = Modifier.weight(1f)
                )
                EngineOptionSelector(
                    label = engine.secondaryLabel,
                    currentValue = engine.secondaryValue,
                    options = engine.secondaryOptions,
                    onSelected = onSecondaryOptionSelected,
                    modifier = Modifier.weight(1f)
                )
            }

            // Tags
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                engine.tags.forEach { tag ->
                    Text(
                        text = tag,
                        fontSize = 10.sp,
                        color = TextSecondary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF282A30))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Card Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = engine.footerMetric,
                    fontSize = 10.5.sp,
                    color = TextMuted
                )
                Row(
                    modifier = Modifier
                        .clickable(onClick = onAction)
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = engine.actionLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandAccent
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = BrandAccent,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EngineOptionSelector(
    label: String,
    currentValue: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = label, fontSize = 10.sp, color = TextMuted)
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF282A30))
                    .border(1.dp, BrandBorder, RoundedCornerShape(6.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentValue,
                    fontSize = 11.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt, fontSize = 11.5.sp) },
                        onClick = {
                            onSelected(opt)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NodeBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    tint: Color,
    isCore: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isCore) Color(0xFF282A30) else Color(0xFF1A1B20))
            .border(1.dp, if (isCore) BrandAccent else BrandBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Text(
            text = title,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(top = 2.dp)
        )
        Text(text = subtitle, fontSize = 9.sp, color = TextMuted)
    }
}

@Composable
private fun ArchitectureToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E1F24))
            .border(1.dp, BrandBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = subtitle, fontSize = 10.5.sp, color = TextMuted)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BrandAccent,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = Color(0xFF2A2B30)
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}

@Composable
private fun ProviderStatusPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    name: String,
    status: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E1F24))
            .border(1.dp, BrandBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = TextMuted, modifier = Modifier.size(15.dp))
            Text(text = name, fontSize = 11.sp, color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ActiveGreen))
    }
}

@Composable
private fun SubNavPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    badge: String? = null,
    hasDot: Boolean = false,
    isActive: Boolean = false
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isActive) Color(0xFF282A30) else Color(0xFF1E1F24))
            .border(1.dp, if (isActive) BrandAccent.copy(alpha = 0.5f) else BrandBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) BrandAccent else TextMuted,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isActive) TextPrimary else TextSecondary,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
        )
        if (hasDot) {
            Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(BrandAccent))
        } else if (badge != null) {
            Text(
                text = badge,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = BrandAccent,
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(BrandAccent.copy(alpha = 0.15f))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            )
        }
    }
}
