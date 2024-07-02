package edu.metrostate.ics342

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.metrostate.ics342.ui.theme.ICS342Theme
import org.w3c.dom.Text


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

                Content()

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(){
    var bottomSheet by remember {
        mutableStateOf(false)
    }

    val todoItems = remember {
        mutableStateListOf<TodoItem>()
    }
    var newItem by remember {
        mutableStateOf("")
    }
    var showErrorMessage by remember {
        mutableStateOf(false)
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
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFFE0A3B8),
                            titleContentColor = Color.Black


                        )

                            
                    )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { bottomSheet = true },
                    shape = FloatingActionButtonDefaults.smallShape,
                    containerColor = Color(0xFFD87093)
                    ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = "" )

                    
                }
            }
            ) {innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                TodoList(todoItems)
 
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
                                todoItems.add(TodoItem(newItem))
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE01056))
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
fun TodoList(todoItems: List<TodoItem>){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .background(Color(0xFFFCB7CE))

    ) {
        items(todoItems){todo ->
            Row (modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(8.dp)
                .padding(horizontal = 12.dp)
            ){
                Text(todo.name,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    color = Color.Black



                )
                var isChecked by remember {
                    mutableStateOf(todo.isCompleted)
                }
                Checkbox(
                   // modifier = Modifier.background(Color()),
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        todo.isCompleted = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFCF1252))

                )

            }





        }
    }
}


@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    ICS342Theme {
        Content()
    }
}