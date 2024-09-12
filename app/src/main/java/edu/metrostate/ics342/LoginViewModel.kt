package edu.metrostate.ics342

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiClient.apiService

    fun loginUser(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = apiService.loginUser("b3126378-88e8-4404-9acf-888875e0009f", LoginUserRequest(email, password))
                user?.let {
                    Log.d("LoginViewModel", "Saving User ID: ${it.id}, Token: ${it.token}")
                    SharedPreferencesManager.saveUserId(getApplication(), it.id)
                    SharedPreferencesManager.saveUserToken(getApplication(), it.token)
                }
                onResult(user)
            } catch (e: retrofit2.HttpException) {
                Log.e("Login", "HTTP Error: ${e.code()} ${e.message()}")
                onResult(null)
            } catch (e: Exception) {
                Log.e("Login", "Error logging in: ${e.message}")
                onResult(null)
            }
        }
    }
}
