package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AutonomousTask
import com.example.model.ChatMessage
import com.example.model.ConnectorItem
import com.example.model.CreativeEngineItem
import com.example.model.ExecutiveProfile
import com.example.model.ScreenDestination
import com.example.model.SystemArchitectureConfig
import com.example.model.VoiceArchetype
import com.example.model.VoiceConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JarvisViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(ScreenDestination.HOME_FRONT_PAGE)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _profile = MutableStateFlow(ExecutiveProfile())
    val profile: StateFlow<ExecutiveProfile> = _profile.asStateFlow()

    private val _voiceConfig = MutableStateFlow(VoiceConfig())
    val voiceConfig: StateFlow<VoiceConfig> = _voiceConfig.asStateFlow()

    private val _creativeEngines = MutableStateFlow(createInitialEngines())
    val creativeEngines: StateFlow<List<CreativeEngineItem>> = _creativeEngines.asStateFlow()

    private val _connectors = MutableStateFlow(createInitialConnectors())
    val connectors: StateFlow<List<ConnectorItem>> = _connectors.asStateFlow()

    private val _chatMessages = MutableStateFlow(createInitialChatMessages())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _autonomousTasks = MutableStateFlow(createInitialTasks())
    val autonomousTasks: StateFlow<List<AutonomousTask>> = _autonomousTasks.asStateFlow()

    private val _systemConfig = MutableStateFlow(SystemArchitectureConfig())
    val systemConfig: StateFlow<SystemArchitectureConfig> = _systemConfig.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // Creative studio sub-states
    val imagePrompt = MutableStateFlow("Cinematic executive sky lounge overlooking Tokyo, dusk amber lighting, hyper-minimalist")
    val selectedAspectRatio = MutableStateFlow("16:9")
    val selectedImageStyle = MutableStateFlow("Minimalist Editorial")
    val isGeneratingImage = MutableStateFlow(false)

    val musicPrompt = MutableStateFlow("Ambient binaural focus soundscape with subtle analog synthesis and warm frequencies")
    val selectedMusicEngine = MutableStateFlow("MusicFX Pro")
    val selectedSpatialMode = MutableStateFlow("Binaural Focus")
    val isGeneratingMusic = MutableStateFlow(false)
    val isPlayingMusic = MutableStateFlow(false)

    val videoPrompt = MutableStateFlow("Slow fluid camera push through glowing obsidian neural data pipeline, 4K HDR")
    val selectedVideoModel = MutableStateFlow("Google Veo 2")
    val selectedFramerate = MutableStateFlow("60 FPS Smooth")
    val isGeneratingVideo = MutableStateFlow(false)

    // Chat controls
    val isHighThinkingEnabled = MutableStateFlow(true)
    val isGoogleSearchGrounding = MutableStateFlow(true)
    val isGoogleMapsTelemetry = MutableStateFlow(false)
    val isLowLatencyActive = MutableStateFlow(true)

    val voiceArchetypes = listOf(
        VoiceArchetype("Nova", "Warm & Executive", "Resonant Baritone", "Good morning, Lakshan. Tokyo markets closed up 1.4%. Your Q3 financial brief has been synced with Google Sheets."),
        VoiceArchetype("Kore", "Calm & Precise", "Crisp Articulation", "All autonomous agents are operating under verified zero-egress enclaves. System latency is measured at 12ms."),
        VoiceArchetype("Fenrir", "Deep & Confident", "Authoritative Pitch", "Linear triage completed. 3 critical blockers addressed and auto-PR synthesized."),
        VoiceArchetype("Custom Studio", "Clone & Modulate", "Upload Sample", "Jarvis neural synthesizer calibrated to your personalized vocal parameters.")
    )

    private var sampleAudioJob: Job? = null

    fun navigateTo(screen: ScreenDestination) {
        _currentScreen.value = screen
    }

    fun updateProfile(legalName: String, handle: String, title: String, email: String) {
        _profile.update {
            it.copy(
                legalName = legalName,
                systemHandle = handle,
                preferredTitle = title,
                encryptedEmail = email
            )
        }
        emitToast("Profile updated successfully")
    }

    fun generateStylizedPersona() {
        emitToast("Synthesizing stylized executive persona with Gemini 1.5 Ultra...")
    }

    fun exportIdentitySpec() {
        emitToast("Identity spec encrypted & exported to secure storage")
    }

    fun toggleVoiceEnabled(enabled: Boolean) {
        _voiceConfig.update { it.copy(isVoiceEnabled = enabled) }
    }

    fun selectVoiceEngine(engine: String) {
        _voiceConfig.update { it.copy(selectedEngine = engine) }
    }

    fun selectVoiceArchetype(archetype: String) {
        _voiceConfig.update { it.copy(selectedArchetype = archetype) }
        playArchetypeSample(archetype)
    }

    fun updateSpeed(speed: Float) {
        _voiceConfig.update { it.copy(speed = speed) }
    }

    fun updatePitch(pitch: Int) {
        _voiceConfig.update { it.copy(pitchModulation = pitch) }
    }

    fun updateWakeWord(wakeWord: String) {
        _voiceConfig.update { it.copy(wakeWord = wakeWord) }
    }

    fun playArchetypeSample(archetype: String = _voiceConfig.value.selectedArchetype) {
        sampleAudioJob?.cancel()
        _voiceConfig.update { it.copy(isPlayingSample = true) }
        sampleAudioJob = viewModelScope.launch {
            delay(4000)
            _voiceConfig.update { it.copy(isPlayingSample = false) }
        }
    }

    fun stopAudioSample() {
        sampleAudioJob?.cancel()
        _voiceConfig.update { it.copy(isPlayingSample = false) }
    }

    fun toggleCreativeEngine(id: String) {
        _creativeEngines.update { list ->
            list.map { engine ->
                if (engine.id == id) engine.copy(isEnabled = !engine.isEnabled) else engine
            }
        }
    }

    fun updateCreativeEngineOptions(id: String, primary: String? = null, secondary: String? = null) {
        _creativeEngines.update { list ->
            list.map { engine ->
                if (engine.id == id) {
                    engine.copy(
                        primaryValue = primary ?: engine.primaryValue,
                        secondaryValue = secondary ?: engine.secondaryValue
                    )
                } else engine
            }
        }
    }

    fun toggleConnector(id: String) {
        _connectors.update { list ->
            list.map { item ->
                if (item.id == id) {
                    val newState = !item.isConnected
                    item.copy(
                        isConnected = newState,
                        statusLabel = if (newState) "Connected (Realtime)" else "Disconnected",
                        lastSync = if (newState) "Just now" else "Offline"
                    )
                } else item
            }
        }
    }

    fun triggerConnectorAutomation(connectorId: String, workflowName: String) {
        viewModelScope.launch {
            emitToast("Executing '$workflowName'...")
            delay(800)
            _connectors.update { list ->
                list.map {
                    if (it.id == connectorId) it.copy(lastSync = "Just now") else it
                }
            }
            // Add an autonomous task record
            val newTask = AutonomousTask(
                id = "task-${System.currentTimeMillis()}",
                title = workflowName,
                summary = "Automated execution orchestrated across $connectorId",
                connector = connectorId,
                status = "COMPLETED",
                timeAgo = "Just now",
                executionLog = "200 OK • Payload encrypted • Zero data leakage"
            )
            _autonomousTasks.update { listOf(newTask) + it }
            emitToast("'$workflowName' successfully executed & synced")
        }
    }

    fun toggleZeroLatencyEnclave(enabled: Boolean) {
        _systemConfig.update { it.copy(zeroLatencyEnclave = enabled) }
    }

    fun toggleExecutiveMemoryLock(enabled: Boolean) {
        _systemConfig.update { it.copy(executiveMemoryLock = enabled) }
    }

    fun toggleStrictAirgapMode(enabled: Boolean) {
        _systemConfig.update { it.copy(strictAirgapMode = enabled) }
    }

    fun selectLanguage(lang: String) {
        _systemConfig.update { it.copy(selectedLanguage = lang) }
        emitToast("Language switched to $lang")
    }

    fun resetDefaults() {
        _profile.value = ExecutiveProfile()
        _voiceConfig.value = VoiceConfig()
        _systemConfig.value = SystemArchitectureConfig()
        emitToast("Configurations reset to factory defaults")
    }

    fun saveChanges() {
        emitToast("All studio settings securely encrypted & saved (12ms)")
    }

    fun sendMessage(userText: String, attachedMediaUri: String? = null) {
        if (userText.isBlank() && attachedMediaUri == null) return

        val userMsg = ChatMessage(
            id = "msg-${System.currentTimeMillis()}",
            sender = "user",
            text = userText,
            timestamp = "Just now",
            mediaTitle = if (attachedMediaUri != null) "Attached Multimedia Asset" else null
        )
        _chatMessages.update { it + userMsg }

        viewModelScope.launch {
            // Add thinking indicator if high thinking mode is on
            val jarvisMsgId = "msg-${System.currentTimeMillis() + 1}"
            val thinkingSteps = if (isHighThinkingEnabled.value) {
                listOf(
                    "Analyzing query intent against connected Google Workspace...",
                    "Checking real-time Google Search & Grounding index...",
                    "Validating zero-egress neural sandbox constraints...",
                    "Synthesizing structured executive brief..."
                )
            } else emptyList()

            val thinkingMsg = ChatMessage(
                id = jarvisMsgId,
                sender = "jarvis",
                text = "...",
                timestamp = "Thinking",
                isThinking = true,
                thinkingSteps = thinkingSteps
            )
            _chatMessages.update { it + thinkingMsg }

            delay(1200)

            // Formulate intelligent response based on query
            val responseText: String
            var mediaType: String? = null
            var mediaTitle: String? = null
            var mediaSummary: String? = null
            val groundingSources = mutableListOf<String>()

            val lower = userText.lowercase()
            when {
                lower.contains("sheet") || lower.contains("financial") || lower.contains("q3") -> {
                    responseText = "I have queried Google Sheets and synchronized your Q3 Financial brief. Revenue is tracking +18.4% above quarterly guidance with $2.4M Net ARR expansion. The live spreadsheet has been reconciled with Google Drive."
                    mediaType = "sheet"
                    mediaTitle = "Q3 2026 Executive Financial Model.gsheet"
                    mediaSummary = "Google Sheets • Live Synced • 12 Sheets Reconciled"
                    groundingSources.addAll(listOf("Google Sheets API", "Enterprise Cloud Enclave"))
                }
                lower.contains("slide") || lower.contains("deck") || lower.contains("presentation") -> {
                    responseText = "Generated 14-slide executive keynote in Google Slides using 'Dark Executive Luxury' typography. Included dynamic revenue charts, roadmap deliverables, and AI telemetry."
                    mediaType = "slide"
                    mediaTitle = "Board Strategy Deck Q4.gslides"
                    mediaSummary = "Google Slides • 14 Slides • Dark Obsidian Theme"
                    groundingSources.addAll(listOf("Google Slides API", "Google Drive"))
                }
                lower.contains("image") || lower.contains("art") || lower.contains("visual") -> {
                    responseText = "Rendered high-fidelity concept visual using Imagen 3 Ultra at 16:9 cinematic aspect ratio. Image assets are preserved in local persistent storage."
                    mediaType = "image"
                    mediaTitle = "Studio Executive Vision.png"
                    mediaSummary = "Imagen 3 Ultra • 4K UHD • 16:9 Aspect Ratio"
                    groundingSources.add("Imagen 3 Generative Engine")
                }
                lower.contains("music") || lower.contains("sound") || lower.contains("audio") -> {
                    responseText = "Composed 3-minute lossless binaural soundscape using MusicFX Pro. Acoustic frequencies tuned to 40Hz gamma waves for sustained executive deep work."
                    mediaType = "music"
                    mediaTitle = "Binaural Focus Alpha Flow (96kHz).wav"
                    mediaSummary = "MusicFX Pro • Binaural Focus • 24-bit 96kHz"
                    groundingSources.add("MusicFX Neural Synthesizer")
                }
                lower.contains("video") || lower.contains("animate") -> {
                    responseText = "Generated seamless text-to-video scene using Google Veo 2 at 60 FPS smooth with synchronized voice-to-video narration."
                    mediaType = "video"
                    mediaTitle = "Executive Briefing Sequence.mp4"
                    mediaSummary = "Google Veo 2 • 60 FPS • Multi-Camera Consistency"
                    groundingSources.addAll(listOf("Google Veo 2 API", "Gemini Acoustics"))
                }
                lower.contains("tokyo") || lower.contains("flight") || lower.contains("travel") -> {
                    responseText = "Retrieved live flight telemetry and route data via Google Search and Google Maps. Non-stop options via ANA and JAL depart at 11:20 AM with 98% on-time rating."
                    groundingSources.addAll(listOf("Google Search Realtime Grounding", "Google Flights & Maps API"))
                }
                else -> {
                    responseText = "Understood, Lakshan. I've processed your directive through the Gemini 1.5 Pro multimodal reasoning engine with verified 12ms latency. All 12 Google and Cloud connectors remain actively synchronized."
                    groundingSources.addAll(listOf("Gemini 1.5 Pro Neural Core", "Autonomous Zero-Egress Enclave"))
                }
            }

            _chatMessages.update { list ->
                list.map {
                    if (it.id == jarvisMsgId) {
                        it.copy(
                            text = responseText,
                            timestamp = "Just now",
                            isThinking = false,
                            groundingSources = groundingSources,
                            mediaType = mediaType,
                            mediaTitle = mediaTitle,
                            mediaSummary = mediaSummary
                        )
                    } else it
                }
            }
        }
    }

    fun generateStudioImage() {
        viewModelScope.launch {
            isGeneratingImage.value = true
            delay(1500)
            isGeneratingImage.value = false
            emitToast("Image generated successfully at ${selectedAspectRatio.value} ratio")
        }
    }

    fun generateStudioMusic() {
        viewModelScope.launch {
            isGeneratingMusic.value = true
            delay(1400)
            isGeneratingMusic.value = false
            isPlayingMusic.value = true
            emitToast("Audio track composed with ${selectedMusicEngine.value} (${selectedSpatialMode.value})")
        }
    }

    fun toggleMusicPlayback() {
        isPlayingMusic.value = !isPlayingMusic.value
    }

    fun generateStudioVideo() {
        viewModelScope.launch {
            isGeneratingVideo.value = true
            delay(1800)
            isGeneratingVideo.value = false
            emitToast("Video motion sequence rendered with ${selectedVideoModel.value} (${selectedFramerate.value})")
        }
    }

    private fun emitToast(msg: String) {
        viewModelScope.launch {
            _toastEvent.emit(msg)
        }
    }

    companion object {
        fun createInitialEngines(): List<CreativeEngineItem> = listOf(
            CreativeEngineItem(
                id = "art_concept",
                title = "AI Art & Concept Studio",
                subtitle = "Visual generation & moodboarding",
                isEnabled = true,
                primaryLabel = "Core Engine",
                primaryValue = "Imagen 3 Ultra",
                primaryOptions = listOf("Imagen 3 Ultra", "Midjourney v6.1 API", "SDXL Turbo Latent"),
                secondaryLabel = "Default Ratio",
                secondaryValue = "16:9 Cinematic",
                secondaryOptions = listOf("16:9 Cinematic", "4:5 Editorial Portrait", "1:1 Square Art", "9:16 Mobile"),
                tags = listOf("Architectural", "Photorealistic", "Minimalist Editorial"),
                footerMetric = "Max Resolution: 4K UHD",
                actionLabel = "Launch Canvas"
            ),
            CreativeEngineItem(
                id = "music_acoustic",
                title = "AI Music & Acoustic Scoring",
                subtitle = "Dynamic soundscapes & intros",
                isEnabled = true,
                primaryLabel = "Audio Engine",
                primaryValue = "MusicFX Pro",
                primaryOptions = listOf("MusicFX Pro", "Suno v4 API", "Meta AudioCraft"),
                secondaryLabel = "Spatial Mode",
                secondaryValue = "Binaural Focus",
                secondaryOptions = listOf("Binaural Focus", "Dolby Atmos Stems", "Lo-Fi Flow State"),
                tags = listOf("Ambient Focus", "Keynote Scores", "Podcast Openers"),
                footerMetric = "Lossless 24-bit 96kHz",
                actionLabel = "Compose Track"
            ),
            CreativeEngineItem(
                id = "video_motion",
                title = "AI Video & Motion Studio",
                subtitle = "Text-to-briefings & product renders",
                isEnabled = true,
                primaryLabel = "Video Model",
                primaryValue = "Google Veo 2",
                primaryOptions = listOf("Google Veo 2", "Runway Gen-3 Alpha", "Kling 1.5 HD"),
                secondaryLabel = "Framerate",
                secondaryValue = "60 FPS Smooth",
                secondaryOptions = listOf("60 FPS Smooth", "24 FPS Cinematic"),
                tags = listOf("4K Scene Render", "Executive Briefings", "3D Mockups"),
                footerMetric = "Multi-Camera Consistency",
                actionLabel = "Open Timeline"
            ),
            CreativeEngineItem(
                id = "culinary_nutrition",
                title = "AI Culinary & Food Crafting",
                subtitle = "Telemetry-based gourmet nutrition",
                isEnabled = true,
                primaryLabel = "Dietary Protocol",
                primaryValue = "Longevity & Clean Keto",
                primaryOptions = listOf("Longevity & Clean Keto", "High Protein Mediterranean", "Executive Fasting Sync"),
                secondaryLabel = "Bio-Sync",
                secondaryValue = "Oura + Whoop Live",
                secondaryOptions = listOf("Oura + Whoop Live", "Apple Health Vitals", "Manual Biometrics"),
                tags = listOf("Gourmet Pairing", "Private Chef Briefs", "Smart Pantry Sync"),
                footerMetric = "Calibrated to 2,350 kcal/day",
                actionLabel = "View Menu Plan"
            ),
            CreativeEngineItem(
                id = "multimedia_remaster",
                title = "Multimedia Remastering & Editing",
                subtitle = "1-click cutdowns & keynote styling",
                isEnabled = true,
                primaryLabel = "Voice Isolation",
                primaryValue = "Deep Studio De-Noise",
                primaryOptions = listOf("Deep Studio De-Noise", "Spatial Acoustics EQ"),
                secondaryLabel = "Slide Re-styler",
                secondaryValue = "Dark Executive Luxury",
                secondaryOptions = listOf("Dark Executive Luxury", "Minimalist Swiss Grid"),
                tags = listOf("Auto-Captions", "Audio Mastering", "Keynote Vectorizer"),
                footerMetric = "Instant Render Queue (0 idle)",
                actionLabel = "Batch Process"
            ),
            CreativeEngineItem(
                id = "code_orchestration",
                title = "Autonomous Code & Orchestration",
                subtitle = "Full-stack dev & auto-PR synthesis",
                isEnabled = true,
                primaryLabel = "Reasoning Engine",
                primaryValue = "Claude 3.5 Sonnet / Gemini Pro",
                primaryOptions = listOf("Claude 3.5 Sonnet / Gemini Pro", "OpenAI o1 Reasoning", "DeepSeek R1 Architecture"),
                secondaryLabel = "Git Target",
                secondaryValue = "GitHub Enterprise (Org)",
                secondaryOptions = listOf("GitHub Enterprise (Org)", "GitLab Self-Hosted"),
                tags = listOf("Auto-PR Reviewer", "Docker Enclaves", "Microservice Scaffolding"),
                footerMetric = "Security: Sandboxed Zero-Trust",
                actionLabel = "Open Terminal"
            )
        )

        fun createInitialConnectors(): List<ConnectorItem> = listOf(
            ConnectorItem(
                id = "google_drive",
                name = "Google Drive",
                category = "Google Workspace",
                description = "Encrypted cloud asset storage, frontend builds, document indexing",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "1m ago",
                supportedAutomations = listOf("Sync Frontend Build Assets", "Auto-Catalog Executive Reports", "Enclave Vault Backup")
            ),
            ConnectorItem(
                id = "google_sheets",
                name = "Google Sheets",
                category = "Google Workspace",
                description = "Dynamic KPI calculation, automated financial reporting, live cell sync",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "2m ago",
                supportedAutomations = listOf("Sync Q3 Financials", "Reconcile Portfolio Metrics", "Generate Burn Rate Model")
            ),
            ConnectorItem(
                id = "gmail",
                name = "Gmail",
                category = "Google Workspace",
                description = "Executive briefing dispatch, thread summarization, draft approval",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "4m ago",
                supportedAutomations = listOf("Send Morning Executive Digest", "Draft Follow-ups for Key Clients", "Filter VIP Inquiries")
            ),
            ConnectorItem(
                id = "google_docs",
                name = "Google Docs",
                category = "Google Workspace",
                description = "Executive strategy whitepapers, meeting minutes auto-synthesis",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "8m ago",
                supportedAutomations = listOf("Synthesize Board Strategy Memo", "Draft Legal Review NDA", "Convert Transcripts to Docs")
            ),
            ConnectorItem(
                id = "google_slides",
                name = "Google Slides",
                category = "Google Workspace",
                description = "Automated keynote generator, vector chart rendering, presentation sync",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "12m ago",
                supportedAutomations = listOf("Draft Board Slides Q4", "Stylize Pitch Deck", "Export PDF Keynote")
            ),
            ConnectorItem(
                id = "google_tasks",
                name = "Google Tasks",
                category = "Google Workspace",
                description = "Zero-friction action item extraction, priority escalation",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "5m ago",
                supportedAutomations = listOf("Extract Action Items from Chat", "Escalate Critical Due Dates", "Sync Calendar Reminders")
            ),
            ConnectorItem(
                id = "google_chats",
                name = "Google Chats",
                category = "Google Workspace",
                description = "Cross-team autonomous announcements, asynchronous updates",
                isConnected = false,
                statusLabel = "Ready to Connect",
                lastSync = "Offline",
                supportedAutomations = listOf("Broadcast Release Notes", "Ping Engineering on Incidents", "Share Daily Standup Digest")
            ),
            ConnectorItem(
                id = "google_meet",
                name = "Google Meet",
                category = "Google Workspace",
                description = "Real-time meeting transcripts, speaker intelligence, instant room links",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "15m ago",
                supportedAutomations = listOf("Generate Instant Meet Link", "Extract Meeting Action Plan", "Realtime Speaker Audio Sync")
            ),
            ConnectorItem(
                id = "google_keep",
                name = "Google Keep",
                category = "Google Workspace",
                description = "Instant voice memo capture, quick executive scratchpad",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "18m ago",
                supportedAutomations = listOf("Capture Voice Idea Note", "Sync Checklist to Keep", "Pin Critical Passcodes")
            ),
            ConnectorItem(
                id = "google_search",
                name = "Google Search Data",
                category = "Intelligence & Grounding",
                description = "Live real-time search grounding, financial market indices, news digest",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "Live Grounding Active",
                supportedAutomations = listOf("Ground Market Intelligence", "Verify Flight Schedules", "Realtime News Extraction")
            ),
            ConnectorItem(
                id = "google_maps",
                name = "Google Maps Data",
                category = "Intelligence & Grounding",
                description = "Live venue telemetry, itinerary optimization, real-time transit ETA",
                isConnected = true,
                statusLabel = "Connected (Realtime)",
                lastSync = "Location Telemetry Active",
                supportedAutomations = listOf("Calculate Executive Transit ETA", "Verify Airport Departure Route", "Locate Private Venues")
            ),
            ConnectorItem(
                id = "encrypted_cloud",
                name = "Encrypted Cloud Enclave",
                category = "Enterprise Cloud",
                description = "Zero-egress hardware enclaves with AES-256 local state caching",
                isConnected = true,
                statusLabel = "Active Enclave (Zero-Egress)",
                lastSync = "Synced (500 GB)",
                supportedAutomations = listOf("Rotate Hardware Keys", "Backup Neural Cache", "Verify Cryptographic Ledger")
            )
        )

        fun createInitialChatMessages(): List<ChatMessage> = listOf(
            ChatMessage(
                id = "msg-1",
                sender = "jarvis",
                text = "Good morning, Lakshan. I'm connected to your executive workspace with verified 12ms ultra-low latency. Gemini 1.5 Pro Live Voice engine is armed and 11 connectors are actively synchronized.",
                timestamp = "09:00 AM",
                groundingSources = listOf("Gemini 1.5 Pro Neural Core", "Executive Memory Lock")
            ),
            ChatMessage(
                id = "msg-2",
                sender = "user",
                text = "Jarvis, pull our Q3 financial sync brief and prepare the board slides draft.",
                timestamp = "09:01 AM"
            ),
            ChatMessage(
                id = "msg-3",
                sender = "jarvis",
                text = "Reconciled Google Sheets with Google Drive. Operating margins expanded by +4.2% and annual recurring revenue crossed $14.8M. I have structured the presentation deck with dark executive styling.",
                timestamp = "09:01 AM",
                mediaType = "sheet",
                mediaTitle = "Q3 Financial Summary & Forecast.gsheet",
                mediaSummary = "Google Sheets • 12 sheets verified • Auto-reconciled with Drive",
                groundingSources = listOf("Google Sheets API", "Google Drive API")
            )
        )

        fun createInitialTasks(): List<AutonomousTask> = listOf(
            AutonomousTask(
                id = "task-1",
                title = "Q3 Financial sync brief",
                summary = "Reconciled revenue metrics across Sheets & encrypted Drive storage",
                connector = "Google Sheets + Drive",
                status = "COMPLETED",
                timeAgo = "12m ago",
                executionLog = "Read 1,420 rows • Formula validation complete • 0 discrepancies"
            ),
            AutonomousTask(
                id = "task-2",
                title = "Flight options for Tokyo",
                summary = "Monitored real-time Google Flights and ANA non-stop availability",
                connector = "Google Search + Maps",
                status = "COMPLETED",
                timeAgo = "45m ago",
                executionLog = "Grounding query returned 6 non-stop flights • ETA calculated"
            ),
            AutonomousTask(
                id = "task-3",
                title = "Sync board slides draft",
                summary = "Generated 14-slide executive presentation with dark luxury layout",
                connector = "Google Slides",
                status = "COMPLETED",
                timeAgo = "1h ago",
                executionLog = "Vector graphics compiled • Slide restyler applied"
            ),
            AutonomousTask(
                id = "task-4",
                title = "Linear bug triage workflow",
                summary = "Autonomous agent reviewed 8 PRs and updated issue priorities",
                connector = "Autonomous Code",
                status = "COMPLETED",
                timeAgo = "2h ago",
                executionLog = "Sandboxed Zero-Trust review • 8 commits inspected"
            )
        )
    }
}
