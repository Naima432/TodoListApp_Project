package edu.metrostate.ics342

import edu.metrostate.testingdemo.CoroutinesTestExtension
import edu.metrostate.testingdemo.InstantExecutorExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
//import org.junit.Test

import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class, InstantExecutorExtension::class)
class LoginViewModelTest {

    @Test
    fun `loginUser should succeed with correct credentials`() = runTest {

        val mockUser = mockk<User>()
        val mockApiService = mockk<TodoApiService>()
        coEvery { mockApiService.loginUser(any(), any()) } returns mockUser
        //if I pass mockAPIservice I will have to change the parameter type of LoginviewModal
        //val viewModel = LoginViewModel(mockApiService)
        val viewModel = LoginViewModel(mockk(relaxed = true))

        coEvery { mockApiService.loginUser(any(), any()) } returns mockUser


        var result: User? = null
        viewModel.loginUser("hajji@gmail.com", "Hajji@123") {
            result = it
        }


        coVerify { mockApiService.loginUser(any(), any()) }
        assertEquals(mockUser, result)
    }

    @Test
    fun `loginUser should return null with incorrect credentials`() = runTest {

        val mockApiService = mockk<TodoApiService>()
        val viewModel = LoginViewModel(mockk(relaxed = true))

        coEvery { mockApiService.loginUser(any(), any()) } throws Exception("Invalid credentials")


        var result: User? = null
        viewModel.loginUser("test@gmail.com", "wrongpassword") {
            result = it
        }

        coVerify { mockApiService.loginUser(any(), any()) }
        assertNull(result)
    }
}
