package com.example.car_dealership_android.data.local

import android.content.Context
import com.example.car_dealership_android.domain.model.User
import com.example.car_dealership_android.domain.model.UserRole
import com.example.car_dealership_android.domain.repository.SessionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionStorage @Inject constructor(
    @ApplicationContext context: Context
) : SessionRepository {
    private val prefs = context.getSharedPreferences("session_storage", Context.MODE_PRIVATE)

    override fun getToken(): String? = prefs.getString("token", null)

    override fun getUser(): User? {
        val id = prefs.getInt("user_id", -1)
        val username = prefs.getString("username", null) ?: return null
        val roleName = prefs.getString("role", null) ?: return null
        if (id == -1) return null
        val role = runCatching { UserRole.valueOf(roleName) }.getOrNull() ?: return null
        return User(id = id, username = username, role = role)
    }

    override fun getRole(): UserRole? = getUser()?.role

    override fun saveSession(token: String, user: User) {
        prefs.edit()
            .putString("token", token)
            .putInt("user_id", user.id)
            .putString("username", user.username)
            .putString("role", user.role.name)
            .apply()
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }
}
