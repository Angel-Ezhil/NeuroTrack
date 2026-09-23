package com.example.data.model

enum class UserRole(val displayName: String) {
    ATHLETE("Athlete"),
    COACH("Coach"),
    SPORTS_TRAINER("Sports Trainer"),
    RESEARCHER("Researcher")
}

data class User(
    val id: String = "user_demo_1",
    val fullName: String = "Dr. Marcus Vance",
    val email: String = "trainer@neurotrack.ai",
    val role: UserRole = UserRole.SPORTS_TRAINER,
    val organization: String = "Apex Performance Athletics",
    val isAuthenticated: Boolean = false
)
