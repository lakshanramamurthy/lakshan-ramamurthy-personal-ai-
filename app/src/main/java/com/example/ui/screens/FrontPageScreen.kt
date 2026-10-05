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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.ActiveGreen
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.BrandBorder
import com.example.ui.theme.BrandCard
import com.example.ui.theme.BrandCharcoal
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JarvisViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FrontPageScreen(
    viewModel: JarvisViewModel,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var promptInput by remember { mutableStateOf("") }
    var activeMode by remember { mutableStateOf("Chat") } // "Chat", "Autonomous Agent", "Deep Research"
    var selectedModel by remember { mutableStateOf("Jarvis 3.5 Ultra") }
    var isModelMenuOpen by remember { mutableStateOf(false) }
    var isGreetingAlternate by remember { mutableStateOf(false) }

    val profile by viewModel.profile.collectAsState()
    val voiceConfig by viewModel.voiceConfig.collectAsState()
    val context = LocalContext.current

    val modelsList = listOf("Jarvis 3.5 Ultra", "Sonnet 5.5 Medium", "Gemini 1.5 Pro Deep")

    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val ambientGlowAlpha by pulseTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient_glow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandCharcoal)
            .testTag("front_page_screen")
    ) {
        // Subtle Ambient Glow centered at top-stage
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .size(width = 340.dp, height = 180.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            BrandAccent.copy(alpha = ambientGlowAlpha),
                            Color(0xFF3A4D6B).copy(alpha = ambientGlowAlpha * 0.8f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ==========================================
            // Top Command & Header Bar
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xD9141416))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("front_page_sidebar_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Sidebar",
                            tint = TextPrimary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(ActiveGreen)
                        )
                        Text(
                            text = "Jarvis Neural Core 3.5 Active",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            Toast.makeText(context, "No unread alerts • All enclaves zero-egress verified", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.navigateTo(ScreenDestination.STUDIO_SETTINGS)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Studio Settings",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // User Profile / Google Sign-in Verified Avatar Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF222328))
                            .border(1.dp, BrandBorder, RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.navigateTo(ScreenDestination.STUDIO_SETTINGS)
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(BrandAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.initials,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Verified Principal",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }

            // Top Ambient Breadcrumb / Terminal Location
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(BrandAccent)
                    )
                    Text(
                        text = "EXECUTIVE HUB",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "18:42 GMT+1 • Zurich Terminal",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // Center Stage: Headline Greeting & Console
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 1. Headline Greeting with 8-Pointed Spark Mark
                Row(
                    modifier = Modifier
                        .clickable { isGreetingAlternate = !isGreetingAlternate }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 8-Pointed Star Spark Emblem (warm coral terracotta)
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(BrandAccent, Color(0xFFF3AE79))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Jarvis Emblem",
                            tint = BrandCharcoal,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = if (!isGreetingAlternate) {
                            "Good evening, Lakshan. What shall Jarvis handle today?"
                        } else {
                            "lakshan ramamurthy returns!"
                        },
                        fontSize = 22.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Normal,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )
                }

                // 2. Main Command / Prompt Console Container
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1D21)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("prompt_console_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Text Area
                        OutlinedTextField(
                            value = promptInput,
                            onValueChange = { promptInput = it },
                            placeholder = {
                                Text(
                                    text = "Ask Jarvis anything, execute live multi-tool workflows, or instruct a brief...",
                                    fontSize = 13.5.sp,
                                    color = TextMuted,
                                    lineHeight = 20.sp
                                )
                            },
                            minLines = 3,
                            maxLines = 6,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("front_page_prompt_input")
                        )

                        // Bottom Actions Row Inside Console
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left Controls: Attachment & Mode Switcher
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Attachment button
                                IconButton(
                                    onClick = {
                                        Toast.makeText(context, "Context file attached: Q3_Financials.gsheet", Toast.LENGTH_SHORT).show()
                                        promptInput = "Analyze the attached Q3 financials spreadsheet and summarize key margins"
                                    },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF25262C))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Attach Context File",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Mode Pill Switcher (Chat | Autonomous | Deep Research)
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF222328))
                                        .padding(2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    listOf("Chat", "Cowork", "Research").forEach { mode ->
                                        val isSelected = activeMode == mode
                                        Text(
                                            text = mode,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else TextMuted,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (isSelected) BrandAccent else Color.Transparent)
                                                .clickable { activeMode = mode }
                                                .padding(horizontal = 9.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            // Right Controls: Model Pill, Voice, Audio Wave, Send Trigger
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Model Switcher Dropdown Pill
                                Box {
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0xFF25262C))
                                            .clickable { isModelMenuOpen = true }
                                            .padding(horizontal = 8.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(ActiveGreen)
                                        )
                                        Text(
                                            text = selectedModel,
                                            fontSize = 10.5.sp,
                                            color = TextPrimary
                                        )
                                        Icon(
                                            imageVector = Icons.Default.ExpandMore,
                                            contentDescription = null,
                                            tint = TextMuted,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = isModelMenuOpen,
                                        onDismissRequest = { isModelMenuOpen = false }
                                    ) {
                                        modelsList.forEach { model ->
                                            DropdownMenuItem(
                                                text = { Text(model, fontSize = 11.5.sp) },
                                                onClick = {
                                                    selectedModel = model
                                                    isModelMenuOpen = false
                                                }
                                            )
                                        }
                                    }
                                }

                                // Voice Command Mic
                                IconButton(
                                    onClick = {
                                        Toast.makeText(context, "Streaming voice session active via Gemini Live", Toast.LENGTH_SHORT).show()
                                        viewModel.playArchetypeSample()
                                    },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (voiceConfig.isPlayingSample) BrandAccent else Color(0xFF25262C))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Voice Command",
                                        tint = if (voiceConfig.isPlayingSample) Color.White else TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Send Trigger Button (Circular)
                                IconButton(
                                    onClick = {
                                        val query = if (promptInput.isNotBlank()) promptInput else "Good evening, Jarvis. Prepare my executive brief for today."
                                        promptInput = ""
                                        viewModel.sendMessage(query)
                                        viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                                    },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(BrandAccent)
                                        .testTag("front_page_send_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowUpward,
                                        contentDescription = "Execute Query",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Quick Action Button Pills
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionPill(
                        emoji = "✏️",
                        label = "Write / Draft Brief",
                        onClick = {
                            promptInput = "Draft a brief for the team summarizing our Q3 product deliverables: "
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    QuickActionPill(
                        emoji = "🧠",
                        label = "Learn / Deep Research",
                        onClick = {
                            promptInput = "Perform deep analytical research on autonomous AI agent enclaves and latency: "
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    QuickActionPill(
                        emoji = "</>",
                        label = "Code / Run GitHub",
                        onClick = {
                            promptInput = "Review recent PRs and run GitHub deployment checks for the workspace: "
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    QuickActionPill(
                        emoji = "📅",
                        label = "From Calendar",
                        onClick = {
                            promptInput = "Summarize conflicts and prep notes from today's calendar."
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    QuickActionPill(
                        emoji = "✉️",
                        label = "From Gmail",
                        onClick = {
                            promptInput = "Extract high-priority action items from my unread VIP emails."
                        }
                    )
                }

                // 4. Connected Pipelines & Workspaces Quick Access Strip
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "CONNECTED PIPELINES & WORKSPACES",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PipelineChip(label = "Google Calendar", isLive = true) {
                            viewModel.sendMessage("Check Google Calendar for meeting conflicts today")
                            viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        PipelineChip(label = "Gmail / Workspace", isLive = true) {
                            viewModel.sendMessage("Extract key VIP threads from Gmail")
                            viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        PipelineChip(label = "Google Drive", isLive = true) {
                            viewModel.navigateTo(ScreenDestination.CONNECTORS_HUB)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        PipelineChip(label = "Google Sheets", isLive = true) {
                            viewModel.sendMessage("Sync and open the Q3 financial summary model")
                            viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        PipelineChip(label = "Slack", isLive = true) {
                            viewModel.navigateTo(ScreenDestination.CONNECTORS_HUB)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        PipelineChip(label = "Notion", isLive = true) {
                            viewModel.navigateTo(ScreenDestination.CONNECTORS_HUB)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        PipelineChip(label = "+ Connect Linear", isLive = false) {
                            viewModel.navigateTo(ScreenDestination.CONNECTORS_HUB)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        PipelineChip(label = "+ Connect GitHub", isLive = false) {
                            viewModel.navigateTo(ScreenDestination.CONNECTORS_HUB)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // Bottom Quiet Telemetry / State Footer
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Security",
                        tint = TextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Enterprise Guardrail Encrypted • End-to-End Private Session",
                        fontSize = 10.5.sp,
                        color = TextMuted
                    )
                }

                Text(
                    text = "Latency: 12ms • Token Pool: Active",
                    fontSize = 10.5.sp,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun QuickActionPill(
    emoji: String,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF202126))
            .border(1.dp, BrandBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = emoji, fontSize = 12.sp)
        Text(text = label, fontSize = 11.5.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PipelineChip(
    label: String,
    isLive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E1F24))
            .border(1.dp, BrandBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (isLive) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(ActiveGreen)
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isLive) TextPrimary else TextMuted
        )
    }
}
