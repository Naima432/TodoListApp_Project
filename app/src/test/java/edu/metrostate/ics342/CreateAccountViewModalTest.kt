package edu.metrostate.ics342



import CreateAccountViewModel
import edu.metrostate.testingdemo.CoroutinesTestExtension
import edu.metrostate.testingdemo.InstantExecutorExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(CoroutinesTestExtension::class, InstantExecutorExtension::class)
class CreateAccountViewModelTest {

    @Test
    fun `createAccount should succeed when api call is successful`(): Unit = runTest {

        val mockUser = mockk<User>()
        val mockApiService = mockk<TodoApiService>()
        //val viewModel = CreateAccountViewModel(mockApiService)
        val viewModel = CreateAccountViewModel(mockk(relaxed = true))

        coEvery { mockApiService.registerUser(any(), any()) } returns mockUser


        var result: User? = null
        viewModel.createAccount("hajji@gmail.com", "Naima", "Hajji@123") {
            result = it
        }


        coVerify { mockApiService.registerUser(any(), any()) }
        assertEquals(mockUser, result)
    }

    @Test
    fun `createAccount should return null when api call fails`(): Unit = runTest {

        val mockApiService = mockk<TodoApiService>()
        val viewModel = CreateAccountViewModel(mockk(relaxed = true))

        coEvery { mockApiService.registerUser(any(), any()) } throws Exception("Network error")


        var result: User? = null
        viewModel.createAccount("test@gmail.com", "Test User", "password123") {
            result = it
        }


        coVerify { mockApiService.registerUser(any(), any()) }
        assertNull(result)
    }
}
