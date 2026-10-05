package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
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
fun GeminiChatScreen(
    viewModel: JarvisViewModel,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isHighThinking by viewModel.isHighThinkingEnabled.collectAsState()
    val isGoogleSearch by viewModel.isGoogleSearchGrounding.collectAsState()
    val isGoogleMaps by viewModel.isGoogleMapsTelemetry.collectAsState()
    val voiceConfig by viewModel.voiceConfig.collectAsState()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandCharcoal)
            .testTag("gemini_chat_screen")
    ) {
        // Top Command Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xF01C1D21))
                .border(1.dp, BrandBorder, RoundedCornerShape(0.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onOpenDrawer,
                    modifier = Modifier.size(36.dp)
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
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Gemini 1.5 Pro Live",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(ActiveGreen)
                        )
                    }
                    Text(
                        text = "12ms Ultra-Low Latency • Voice: ${voiceConfig.selectedArchetype}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            // Grounding Badges / Toggles
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ChatFeatureToggle(
                    icon = Icons.Default.Psychology,
                    label = "Thinking",
                    isActive = isHighThinking,
                    onClick = { viewModel.isHighThinkingEnabled.value = !isHighThinking }
                )

                ChatFeatureToggle(
                    icon = Icons.Default.Search,
                    label = "Search",
                    isActive = isGoogleSearch,
                    onClick = { viewModel.isGoogleSearchGrounding.value = !isGoogleSearch }
                )

                ChatFeatureToggle(
                    icon = Icons.Default.Map,
                    label = "Maps",
                    isActive = isGoogleMaps,
                    onClick = { viewModel.isGoogleMapsTelemetry.value = !isGoogleMaps }
                )
            }
        }

        // Messages Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(message = msg)
            }
        }

        // Quick Suggestions Horizontal Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandSurface)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            QuickPromptPill(label = "Sync Q3 financial brief to Sheets") {
                viewModel.sendMessage("Sync Q3 financial brief to Sheets")
            }
            QuickPromptPill(label = "Draft board slides presentation") {
                viewModel.sendMessage("Draft board slides presentation in Google Slides")
            }
            QuickPromptPill(label = "Find Tokyo non-stop flights via Search") {
                viewModel.sendMessage("Find Tokyo non-stop flights via Google Search")
            }
            QuickPromptPill(label = "Generate 4K executive studio visual") {
                viewModel.sendMessage("Generate 4K executive studio visual at 16:9 ratio")
            }
            QuickPromptPill(label = "Compose binaural focus acoustic score") {
                viewModel.sendMessage("Compose binaural focus acoustic score with MusicFX")
            }
        }

        // Persistent Multi-modal Input Dock
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF101113))
                .border(1.dp, BrandBorder, RoundedCornerShape(0.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Media attachment button
            IconButton(
                onClick = {
                    Toast.makeText(context, "Asset attached: img_studio_concept.jpg", Toast.LENGTH_SHORT).show()
                    viewModel.sendMessage("Analyze the attached studio concept image for aspect ratio composition", "img_studio_concept.jpg")
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E1F24))
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Attach file",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Voice Record Trigger
            IconButton(
                onClick = {
                    Toast.makeText(context, "Listening with Nova Voiceprint...", Toast.LENGTH_SHORT).show()
                    viewModel.playArchetypeSample()
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (voiceConfig.isPlayingSample) BrandAccent else Color(0xFF1E1F24))
                    .testTag("voice_assistant_mic_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Assistant",
                    tint = if (voiceConfig.isPlayingSample) Color.White else BrandAccent,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Text Input
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        text = "Ask or instruct Jarvis...",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF18191D),
                    unfocusedContainerColor = Color(0xFF18191D),
                    focusedBorderColor = BrandAccent,
                    unfocusedBorderColor = BrandBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field")
            )

            // Send Button
            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        val text = inputText
                        inputText = ""
                        viewModel.sendMessage(text)
                    }
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BrandAccent)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.sender == "user"

    var showThinkingDetails by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (!isUser) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(BrandAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
                Text(
                    text = "Jarvis Chief of Staff",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandAccent
                )
                Text(
                    text = message.timestamp,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }

        // Thinking Steps Accordion
        if (message.isThinking) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F24)),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = BrandAccent,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Gemini Deep Reasoning In Progress...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }

                    message.thinkingSteps.forEach { step ->
                        Text(
                            text = "• $step",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isUser) Color(0xFF2E241E) else Color(0xFF1E1F24)
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isUser) BrandAccent.copy(alpha = 0.5f) else BrandBorder
                ),
                modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.95f)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = message.text,
                        fontSize = 13.5.sp,
                        color = TextPrimary,
                        lineHeight = 20.sp
                    )

                    // Rich Media Preview Cards
                    if (message.mediaType != null) {
                        MediaAttachmentCard(
                            type = message.mediaType,
                            title = message.mediaTitle ?: "Asset",
                            summary = message.mediaSummary ?: ""
                        )
                    }

                    // Grounding Sources Badges
                    if (message.groundingSources.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            message.groundingSources.forEach { source ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF282A30))
                                        .border(1.dp, BrandBorder, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = ActiveGreen,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = source,
                                        fontSize = 9.5.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaAttachmentCard(type: String, title: String, summary: String) {
    val icon = when (type) {
        "sheet" -> Icons.Default.TableChart
        "music" -> Icons.Default.MusicNote
        "video" -> Icons.Default.Movie
        else -> Icons.Default.AutoAwesome
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF141416))
            .border(1.dp, BrandBorder, RoundedCornerShape(8.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(BrandAccent.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(20.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = summary, fontSize = 10.5.sp, color = TextMuted)
        }
    }
}

@Composable
private fun ChatFeatureToggle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isActive) BrandAccent.copy(alpha = 0.2f) else Color(0xFF282A30))
            .border(
                1.dp,
                if (isActive) BrandAccent else BrandBorder,
                RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 7.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) BrandAccent else TextMuted,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = label,
            fontSize = 10.5.sp,
            color = if (isActive) TextPrimary else TextMuted
        )
    }
}

@Composable
private fun QuickPromptPill(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        fontSize = 11.sp,
        color = TextSecondary,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF202126))
            .border(1.dp, BrandBorder, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}
