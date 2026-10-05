package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.ActiveGreen
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.BrandBorder
import com.example.ui.theme.BrandBorderHover
import com.example.ui.theme.BrandCard
import com.example.ui.theme.BrandCharcoal
import com.example.ui.theme.BrandSidebar
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JarvisViewModel

@Composable
fun SidebarContent(
    viewModel: JarvisViewModel,
    modifier: Modifier = Modifier,
    onCloseDrawer: () -> Unit = {}
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val connectors by viewModel.connectors.collectAsState()
    val activeConnectorsCount = connectors.count { it.isConnected }
    val profile by viewModel.profile.collectAsState()

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(BrandSidebar)
            .border(width = 1.dp, color = BrandBorder, shape = RoundedCornerShape(0.dp))
            .testTag("jarvis_sidebar_drawer")
    ) {
        // Top App Brand Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Spark Logo
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(BrandAccent, Color(0xFFFDE68A))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Jarvis Emblem",
                        tint = BrandCharcoal,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "Jarvis",
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }

            IconButton(
                onClick = onCloseDrawer,
                modifier = Modifier
                    .size(28.dp)
                    .testTag("sidebar_toggle_button")
            ) {
                Icon(
                    imageVector = Icons.Default.MenuOpen,
                    contentDescription = "Toggle Sidebar",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // New Chat Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF202126))
                    .border(1.dp, BrandBorder, RoundedCornerShape(8.dp))
                    .clickable {
                        viewModel.navigateTo(ScreenDestination.HOME_FRONT_PAGE)
                        onCloseDrawer()
                    }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("sidebar_new_chat_button"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Chat",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "New Chat",
                    color = TextPrimary,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Navigation Items
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SidebarNavItem(
                icon = Icons.Default.AutoAwesome,
                label = "Executive Home",
                isSelected = currentScreen == ScreenDestination.HOME_FRONT_PAGE,
                testTag = "nav_home_front_page",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.HOME_FRONT_PAGE)
                    onCloseDrawer()
                }
            )

            SidebarNavItem(
                icon = Icons.Default.Settings,
                label = "Studio Configuration",
                isSelected = currentScreen == ScreenDestination.STUDIO_SETTINGS,
                testTag = "nav_studio_settings",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.STUDIO_SETTINGS)
                    onCloseDrawer()
                }
            )

            SidebarNavItem(
                icon = Icons.AutoMirrored.Filled.Chat,
                label = "Gemini Assistant",
                badgeText = "Live",
                badgeColor = BrandAccent,
                isSelected = currentScreen == ScreenDestination.GEMINI_CHAT,
                testTag = "nav_gemini_chat",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                    onCloseDrawer()
                }
            )

            SidebarNavItem(
                icon = Icons.Default.AutoAwesome,
                label = "Creative Engines",
                badgeText = "6 Active",
                badgeColor = BrandAccent,
                isSelected = currentScreen == ScreenDestination.CREATIVE_STUDIO,
                testTag = "nav_creative_studio",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.CREATIVE_STUDIO)
                    onCloseDrawer()
                }
            )

            SidebarNavItem(
                icon = Icons.Default.Link,
                label = "Connectors",
                badgeText = "$activeConnectorsCount Active",
                badgeColor = ActiveGreen,
                isSelected = currentScreen == ScreenDestination.CONNECTORS_HUB,
                testTag = "nav_connectors_hub",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.CONNECTORS_HUB)
                    onCloseDrawer()
                }
            )

            SidebarNavItem(
                icon = Icons.Default.Terminal,
                label = "Autonomous Tasks",
                badgeText = "PRO",
                badgeColor = BrandAccent,
                isSelected = currentScreen == ScreenDestination.AUTONOMOUS_TASKS,
                testTag = "nav_autonomous_tasks",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.AUTONOMOUS_TASKS)
                    onCloseDrawer()
                }
            )

            SidebarNavItem(
                icon = Icons.Default.Folder,
                label = "Projects",
                isSelected = false,
                testTag = "nav_projects",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.CREATIVE_STUDIO)
                    onCloseDrawer()
                }
            )

            SidebarNavItem(
                icon = Icons.Default.Bolt,
                label = "Artifacts",
                isSelected = false,
                testTag = "nav_artifacts",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.AUTONOMOUS_TASKS)
                    onCloseDrawer()
                }
            )

            SidebarNavItem(
                icon = Icons.Default.Tune,
                label = "Customize",
                isSelected = false,
                testTag = "nav_customize",
                onClick = {
                    viewModel.navigateTo(ScreenDestination.STUDIO_SETTINGS)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Recent Chats Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CHATS AND TASKS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
            }

            // Quick Prompt 1: Workspace Memory
            RecentTaskItem(
                title = "Import workspace memory",
                badge = "Try",
                isAccent = true,
                onClick = {
                    viewModel.sendMessage("Import workspace memory from Google Drive and Google Sheets")
                    viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                    onCloseDrawer()
                }
            )

            RecentTaskItem(
                title = "Q3 Financial sync brief",
                onClick = {
                    viewModel.sendMessage("Show me the Q3 financial sync brief reconciled with Google Sheets")
                    viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                    onCloseDrawer()
                }
            )

            RecentTaskItem(
                title = "Flight options for Tokyo",
                onClick = {
                    viewModel.sendMessage("Find optimal non-stop flight options for Tokyo using Google Search grounding")
                    viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                    onCloseDrawer()
                }
            )

            RecentTaskItem(
                title = "Sync board slides draft",
                onClick = {
                    viewModel.sendMessage("Sync board slides draft with Google Slides in Dark Luxury theme")
                    viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                    onCloseDrawer()
                }
            )

            RecentTaskItem(
                title = "Linear bug triage workflow",
                onClick = {
                    viewModel.sendMessage("Run linear bug triage workflow and synthesize auto-PRs")
                    viewModel.navigateTo(ScreenDestination.GEMINI_CHAT)
                    onCloseDrawer()
                }
            )
        }

        // Bottom User Profile (Alex Rivera / Lakshan Ramamurthy)
        HorizontalDivider(color = BrandBorder, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF101113))
                .clickable {
                    viewModel.navigateTo(ScreenDestination.STUDIO_SETTINGS)
                    onCloseDrawer()
                }
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .testTag("sidebar_profile_button"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(BrandAccent, Color(0xFF4338CA))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.initials,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = profile.legalName,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Executive Mode",
                        fontSize = 10.5.sp,
                        color = TextMuted
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Expand Profile",
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun SidebarNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    badgeText: String? = null,
    badgeColor: Color = BrandAccent,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Color(0x33222328) else Color.Transparent
    val textColor = if (isSelected) TextPrimary else TextSecondary
    val iconColor = if (isSelected) BrandAccent else TextMuted

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(17.dp)
            )
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (badgeText != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(badgeColor.copy(alpha = 0.15f))
                    .border(1.dp, badgeColor.copy(alpha = 0.35f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor
                )
            }
        }
    }
}

@Composable
private fun RecentTaskItem(
    title: String,
    badge: String? = null,
    isAccent: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(if (isAccent) BrandAccent else Color(0xFF64748B))
            )
            Text(
                text = title,
                fontSize = 12.5.sp,
                color = if (isAccent) BrandAccent else TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(BrandAccent.copy(alpha = 0.15f))
                    .border(1.dp, BrandAccent.copy(alpha = 0.4f), RoundedCornerShape(3.dp))
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandAccent
                )
            }
        }
    }
}
