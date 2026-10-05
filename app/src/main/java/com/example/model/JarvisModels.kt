package com.example.model

data class ExecutiveProfile(
    val legalName: String = "Lakshan Ramamurthy",
    val systemHandle: String = "@lakshan",
    val preferredTitle: String = "Founder & Principal",
    val encryptedEmail: String = "lakshan@enterprise.ai",
    val tier: String = "Chief Executive",
    val voiceprintId: String = "#NV-88219-LXR",
    val initials: String = "LR"
)

data class VoiceArchetype(
    val name: String,
    val tone: String,
    val pitchLabel: String,
    val sampleSnippet: String
)

data class VoiceConfig(
    val isVoiceEnabled: Boolean = true,
    val selectedEngine: String = "Gemini 1.5 Pro Live Voice",
    val selectedArchetype: String = "Nova",
    val speed: Float = 1.05f,
    val pitchModulation: Int = -2,
    val wakeWord: String = "Hey Jarvis (Standard)",
    val isPlayingSample: Boolean = false
)

data class CreativeEngineItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val isEnabled: Boolean = true,
    val primaryLabel: String,
    val primaryValue: String,
    val primaryOptions: List<String>,
    val secondaryLabel: String,
    val secondaryValue: String,
    val secondaryOptions: List<String>,
    val tags: List<String>,
    val footerMetric: String,
    val actionLabel: String
)

data class ConnectorItem(
    val id: String,
    val name: String,
    val category: String, // "Google Workspace", "Intelligence & Grounding", "Enterprise Cloud"
    val description: String,
    val isConnected: Boolean,
    val statusLabel: String,
    val lastSync: String,
    val supportedAutomations: List<String>
)

data class ChatMessage(
    val id: String,
    val sender: String, // "user" or "jarvis"
    val text: String,
    val timestamp: String,
    val isThinking: Boolean = false,
    val thinkingSteps: List<String> = emptyList(),
    val groundingSources: List<String> = emptyList(),
    val mediaType: String? = null, // "image", "music", "video", "sheet", "doc", "slide"
    val mediaTitle: String? = null,
    val mediaSummary: String? = null
)

data class AutonomousTask(
    val id: String,
    val title: String,
    val summary: String,
    val connector: String,
    val status: String, // "COMPLETED", "EXECUTING", "SCHEDULED"
    val timeAgo: String,
    val executionLog: String
)

data class SystemArchitectureConfig(
    val zeroLatencyEnclave: Boolean = true,
    val executiveMemoryLock: Boolean = true,
    val strictAirgapMode: Boolean = false,
    val neuralComputeScore: Float = 99.98f,
    val selectedLanguage: String = "English (US)",
    val encryptedStorageAllocated: String = "500 GB",
    val encryptedStorageUsed: String = "4.2 GB",
    val multiLanguageList: List<String> = listOf(
        "English (US)",
        "French (Français)",
        "German (Deutsch)",
        "Japanese (日本語)",
        "Spanish (Español)",
        "Arabic (العربية)",
        "Chinese (Mandarin)"
    )
)

enum class ScreenDestination {
    HOME_FRONT_PAGE,
    STUDIO_SETTINGS,
    CONNECTORS_HUB,
    GEMINI_CHAT,
    CREATIVE_STUDIO,
    AUTONOMOUS_TASKS
}
