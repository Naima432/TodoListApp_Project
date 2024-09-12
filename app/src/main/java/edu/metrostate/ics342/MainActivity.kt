package edu.metrostate.ics342

//import createAccountViewModel



import CreateAccountViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.metrostate.ics342.ui.theme.ICS342Theme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("MainActivity", "onCreate called")
        setContent {




            TodoListNavigation()

                //Content()

        }
    }
}

@Composable
fun TodoListNavigation(startDestination: String = "login"){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination ){
        composable("login"){
            LoginScreen (navController)}
        composable("createAccount"){
            CreateAccountScreen(navController)
        }
        composable("todoList"){ Content(navController)}
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(navController: NavHostController, viewModel: TodoListViewModel = viewModel()){
    var bottomSheet by remember {
        mutableStateOf(false)
    }

    val todoItems by viewModel.todos.observeAsState(emptyList())
    var newItem by remember {
        mutableStateOf("")
    }
    var showErrorMessage by remember {
        mutableStateOf(false)
    }
    val error by viewModel.error.observeAsState()
    var showAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(error) {
        error?.let {
             showAlertDialog = true
            viewModel.clearError()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchTodos()
    }
    ICS342Theme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "ToDo",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        Button(onClick ={
                            navController.navigate("login")
                        },
                                colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7B47D8),
                        )



                        ) {
                            Text("Logout", color = Color.Black)
                        }
                    },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFFA887E2),
                            titleContentColor = Color.Black


                        )


                    )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { bottomSheet = true },
                    shape = FloatingActionButtonDefaults.smallShape,
                    containerColor = Color(0xFF7B47D8)
                    ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = "" )


                }
            }
            ) {innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                Log.d("Content", "Rendering TodoList with items: $todoItems")
                TodoList(todoItems, viewModel)

               //Text(text = "List Item")
                //TodoList(todoItems = todoItems)
            }
            if(bottomSheet){
                ModalBottomSheet(
                    onDismissRequest = { bottomSheet = false },
                    modifier = Modifier.fillMaxHeight(0.75f)


                ) {
                    BottomsheetContent(

                        userText = newItem,
                        showErrorMessage =showErrorMessage ,
                        onNewItemChange = {
                                newItem = it
                            showErrorMessage = false

                        } ,
                        onSaveClick = {
                            if (newItem.isNotBlank()){
                                Log.d("Content", "Creating new todo with description: $newItem")
                                viewModel.createTodo(
                                    description = newItem
                                )
                                newItem =""
                                bottomSheet = false
                                showErrorMessage = false
                            }else{
                                showErrorMessage = true
                            }
                        },
                        onCancelClick = {
                            newItem = ""
                            bottomSheet = false
                            showErrorMessage = false
                        },

                        )

                    }
                }
            if (showAlertDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showAlertDialog = false
                        viewModel.clearError()
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showAlertDialog = false
                                viewModel.clearError()
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    text = { Text(error ?: "Unknown error") }
                )
            }
        }
    }
}






@Composable
fun BottomsheetContent(
    userText: String,
    showErrorMessage: Boolean,
    onNewItemChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit

){
    Column(modifier = Modifier
        //.background(Color(0xFFFCB7CE))
        .padding()) {
        OutlinedTextField(
            value = userText,
            onValueChange = onNewItemChange,
            label = {
                Text(text = stringResource(id = R.string.new_todo))
            },
            trailingIcon = {
                IconButton(onClick = { onNewItemChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "")



                }
            },
            isError = showErrorMessage,
            modifier = Modifier.fillMaxWidth()

            )
        if(showErrorMessage){
            Text(text = stringResource(id = R.string.error_message),
                color = Color.Red,
                modifier = Modifier.padding(top=4.dp))
        }

    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        ) {
        Button(
            onClick = onSaveClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF593794))
        ) {
            Text(text = stringResource(id = R.string.save))

        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row (modifier = Modifier.fillMaxWidth()){
        OutlinedButton(onClick = onCancelClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }

    }


    }

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = viewModel()){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            when {
                email.isBlank() || password.isBlank() -> {
                    errorMessage = "Please enter both email and password"
                    //showDialog = true
                }

                else -> {
                    viewModel.loginUser(email, password) { user ->
                        if (user != null) {
                            navController.navigate("todoList")
                        } else {
                            errorMessage = "Login failed.Please Enter The Correct Email and Password."
                                //showDialog = true
                        }
                    }
                }
            }

        }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Create an Account",
            color = Color.Blue,
            modifier = Modifier.clickable {
                navController.navigate("createAccount")
            }
        )
    }
}

@Composable
fun CreateAccountScreen(navController: NavHostController, viewModel: CreateAccountViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            text = { Text(errorMessage) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            when {
                email.isBlank() || name.isBlank() || password.isBlank() -> {
                    errorMessage = "All fields are required"
                    showDialog = true
                }
                password.length < 8 -> {
                    errorMessage = "Password must be at least 8 characters long"
                    showDialog = true
                }
                password != confirmPassword -> {
                    errorMessage = "Passwords do not match"
                    showDialog = true
                }
                else -> {
                    viewModel.createAccount(email, name, password) { user ->
                        if (user != null) {
                            navController.navigate("todoList")
                        } else {
                            errorMessage = "Account creation failed. Please try again."
                            showDialog = true
                        }
                    }
                }
            }
        }) {
            Text("Create Account")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text("Log In")
        }
    }
}




@Composable
fun TodoList(todoItems: List<TodoItem>, viewModel: TodoListViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(Color(0xFFD3BEFA))
    ) {
        items(todoItems) { todo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(8.dp)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = todo.description,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    color = Color.Black
                )
                var isChecked by remember { mutableStateOf(todo.isCompleted) }
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        viewModel.completeTodo(todo.id, todo.description, isChecked)
                    },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF39009C))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    ICS342Theme {
        Content(rememberNavController())
    }
}