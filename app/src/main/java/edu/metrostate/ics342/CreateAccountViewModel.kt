import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.ApiClient
import edu.metrostate.ics342.CreateUserRequest
import edu.metrostate.ics342.SharedPreferencesManager
import edu.metrostate.ics342.User
import kotlinx.coroutines.launch

class CreateAccountViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiClient.apiService

    fun createAccount(email: String, name: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val request = CreateUserRequest(email, name, password)
                Log.d("CreateAccount", "Request: $request")
                val response = apiService.registerUser("b3126378-88e8-4404-9acf-888875e0009f", request)
                Log.d("CreateAccount", "Response: $response")
                response?.let {
                    SharedPreferencesManager.saveUserId(getApplication(), it.id)
                    SharedPreferencesManager.saveUserToken(getApplication(), it.token)
                }
                onResult(response)
            } catch (e: retrofit2.HttpException) {
                Log.e("CreateAccount", "HTTP Error: ${e.code()} ${e.message()}")
                onResult(null)
            } catch (e: Exception) {
                Log.e("CreateAccount", "Error: ${e.message}")
                onResult(null)
            }
        }
    }
}
