package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.ScreenDestination
import com.example.ui.components.SidebarContent
import com.example.ui.screens.AutonomousTasksScreen
import com.example.ui.screens.ConnectorsScreen
import com.example.ui.screens.CreativeStudioScreen
import com.example.ui.screens.FrontPageScreen
import com.example.ui.screens.GeminiChatScreen
import com.example.ui.screens.StudioSettingsScreen
import com.example.ui.theme.BrandCharcoal
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.JarvisViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                JarvisApp()
            }
        }
    }
}

@Composable
fun JarvisApp(viewModel: JarvisViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    BackHandler(enabled = drawerState.isOpen || currentScreen != ScreenDestination.HOME_FRONT_PAGE) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else if (currentScreen != ScreenDestination.HOME_FRONT_PAGE) {
            viewModel.navigateTo(ScreenDestination.HOME_FRONT_PAGE)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BrandCharcoal
            ) {
                SidebarContent(
                    viewModel = viewModel,
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BrandCharcoal)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            when (currentScreen) {
                ScreenDestination.HOME_FRONT_PAGE -> {
                    FrontPageScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
                ScreenDestination.STUDIO_SETTINGS -> {
                    StudioSettingsScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
                ScreenDestination.CONNECTORS_HUB -> {
                    ConnectorsScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
                ScreenDestination.GEMINI_CHAT -> {
                    GeminiChatScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
                ScreenDestination.CREATIVE_STUDIO -> {
                    CreativeStudioScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
                ScreenDestination.AUTONOMOUS_TASKS -> {
                    AutonomousTasksScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
            }
        }
    }
}
