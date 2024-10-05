package com.example.faceidapp

enum class BiometricAuthStatus(val id: Int) {
    Ready(id = 1),
    NotAvailable(id = -1),
    TemporaryNotAvailable(id = -2),
    AvailableButNotEnrolled(id = -3),
}