package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ConnectorItem
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
fun ConnectorsScreen(
    viewModel: JarvisViewModel,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val connectors by viewModel.connectors.collectAsState()
    val context = LocalContext.current
    val activeCount = connectors.count { it.isConnected }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandCharcoal)
            .testTag("connectors_hub_screen")
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
                    Text(
                        text = "Connectors & Automation Hub",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "$activeCount of ${connectors.size} Services Live • Encrypted Hardware Enclaves",
                        fontSize = 11.sp,
                        color = ActiveGreen
                    )
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, "All connected Google & cloud services synced", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(34.dp)
                    .testTag("sync_all_connectors_button")
            ) {
                Icon(
                    imageVector = Icons.Default.WifiTethering,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sync All", fontSize = 11.5.sp, color = Color.White)
            }
        }

        // Connectors List grouped by category
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Summary Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x30222328)),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BrandAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = BrandAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Zero-Trust Enterprise Interconnect",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "All Google Workspace data (Drive, Sheets, Gmail, Docs, Slides, Tasks, Meet, Keep) and telemetry flow through encrypted local memory channels.",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            // Google Workspace Connectors
            Text(
                text = "GOOGLE WORKSPACE & PRODUCTIVITY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )

            connectors.filter { it.category == "Google Workspace" }.forEach { item ->
                ConnectorCard(
                    connector = item,
                    icon = getConnectorIcon(item.id),
                    onToggle = { viewModel.toggleConnector(item.id) },
                    onRunAutomation = { workflow ->
                        viewModel.triggerConnectorAutomation(item.id, workflow)
                    }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Intelligence & Grounding Connectors
            Text(
                text = "INTELLIGENCE, SEARCH & REALTIME TELEMETRY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )

            connectors.filter { it.category == "Intelligence & Grounding" }.forEach { item ->
                ConnectorCard(
                    connector = item,
                    icon = getConnectorIcon(item.id),
                    onToggle = { viewModel.toggleConnector(item.id) },
                    onRunAutomation = { workflow ->
                        viewModel.triggerConnectorAutomation(item.id, workflow)
                    }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Enterprise Cloud Storage
            Text(
                text = "ENCRYPTED CLOUD ENCLAVE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )

            connectors.filter { it.category == "Enterprise Cloud" }.forEach { item ->
                ConnectorCard(
                    connector = item,
                    icon = Icons.Default.Lock,
                    onToggle = { viewModel.toggleConnector(item.id) },
                    onRunAutomation = { workflow ->
                        viewModel.triggerConnectorAutomation(item.id, workflow)
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ConnectorCard(
    connector: ConnectorItem,
    icon: ImageVector,
    onToggle: () -> Unit,
    onRunAutomation: (String) -> Unit
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF282A30)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (connector.isConnected) BrandAccent else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = connector.name,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (connector.isConnected) ActiveGreen else Color.Gray)
                            )
                        }
                        Text(
                            text = connector.description,
                            fontSize = 11.sp,
                            color = TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Switch(
                    checked = connector.isConnected,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BrandAccent,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = Color(0xFF2A2B30)
                    )
                )
            }

            HorizontalDivider(color = BrandBorder.copy(alpha = 0.5f), thickness = 1.dp)

            // Automations Action Buttons
            Text(
                text = "AVAILABLE AUTOMATED WORKFLOWS:",
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted,
                letterSpacing = 0.4.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                connector.supportedAutomations.take(2).forEach { workflow ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF282A30))
                            .border(1.dp, BrandBorder, RoundedCornerShape(6.dp))
                            .clickable(enabled = connector.isConnected) {
                                onRunAutomation(workflow)
                            }
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (connector.isConnected) BrandAccent else TextMuted,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = workflow,
                            fontSize = 10.5.sp,
                            color = if (connector.isConnected) TextPrimary else TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

private fun getConnectorIcon(id: String): ImageVector {
    return when (id) {
        "google_drive" -> Icons.Default.Lock
        "google_sheets" -> Icons.Default.TableChart
        "gmail" -> Icons.Default.Email
        "google_docs" -> Icons.Default.Description
        "google_slides" -> Icons.Default.Slideshow
        "google_tasks" -> Icons.Default.FormatListBulleted
        "google_chats" -> Icons.Default.Chat
        "google_meet" -> Icons.Default.Videocam
        "google_keep" -> Icons.Default.Lightbulb
        "google_search" -> Icons.Default.Search
        "google_maps" -> Icons.Default.Map
        else -> Icons.Default.Lock
    }
}
