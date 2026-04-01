package com.kim.shoppinglist

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val index: Int,
    var name: String,
    var quantity: Int,
    var isBought: Boolean = false
)


@Composable
fun ShoppingListApp(){
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf<ShoppingItem?>(null) }
    var shoppingItems by remember{ mutableStateOf(listOf<ShoppingItem>())} //list all item object
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    var editDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 70.dp),
        verticalArrangement = if(shoppingItems.isNotEmpty()) Arrangement.Top else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text("Shopping List App", style = MaterialTheme.typography.titleLarge)
        Text("by kim :P", style = MaterialTheme.typography.titleSmall)

        Spacer(modifier = Modifier.height(10.dp))

        if(shoppingItems.isNotEmpty()){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .weight(1f),
        ){
            items(shoppingItems){

                ShoppingListItem(it, {
                    selectedItem = it
                    itemName = it.name
                    itemQuantity = it.quantity.toString()
                    editDialog = true
                }, {
                    shoppingItems -= it
                    Toast.makeText(context, "I'm ${it.name} and you killed me!", Toast.LENGTH_SHORT).show()
                })
            }
        }}

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
    {
        Button(onClick = {
            showDialog = true
        }) {
            Text("Add Item")
            Icon(Icons.Default.Add, contentDescription = "Add")
        }

        if(shoppingItems.isNotEmpty()){
            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                shoppingItems = emptyList()
                Toast.makeText(context, "Deleted All Items", Toast.LENGTH_SHORT).show()
            }) {
                Text("Delete All Items")
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
    }

    if(showDialog){
        AlertDialog(onDismissRequest = {
            showDialog = false
            itemName = ""
            itemQuantity = ""},

            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(onClick = {
                        showDialog = false
                        itemName = ""
                        itemQuantity = ""
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(onClick = {

                        if(itemName.isBlank()){
                            Toast.makeText(context, "Enter item name", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if(itemQuantity.isBlank()){
                            Toast.makeText(context, "Enter item quantity", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val quantity = itemQuantity.toIntOrNull()?: 0

                        if(quantity > 0) {
                            val nextIndex = (shoppingItems.maxOfOrNull { it.index } ?: 0) + 1

                            val newItem = ShoppingItem(
                                nextIndex,
                                itemName,
                                itemQuantity.toInt()
                            )

                            shoppingItems += newItem
                            Toast.makeText(context, "Item added :)", Toast.LENGTH_SHORT).show()

                            showDialog = false
                            itemName = ""
                            itemQuantity = ""
                        } else{
                            Toast.makeText(context, "Invalid Item Quantity", Toast.LENGTH_SHORT).show()
                            }

                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Confirm")
                    }
                }
            },
            title = {Text("Add Shopping Item", style = MaterialTheme.typography.titleMedium)},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Enter item name")})

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Enter item quantity")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                     )

                }
            })
    }

    if(editDialog) {

        AlertDialog(
            onDismissRequest = {
                editDialog = false
                itemName = ""
                itemQuantity = ""
            },

            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(onClick = {
                        editDialog = false
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(onClick = {

                        if(itemName.isBlank()){
                            Toast.makeText(context, "Enter item name", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if(itemQuantity.isBlank()){
                            Toast.makeText(context, "Enter item quantity", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val quantity = itemQuantity.toIntOrNull()?: 0

                        if(quantity > 0) {

                            shoppingItems = shoppingItems.map {
                                if (it.index == selectedItem!!.index)
                                    it.copy(name = itemName, quantity = itemQuantity.toIntOrNull() ?: 0)
                                else it
                            }
                            editDialog = false
                            Toast.makeText(context, "Item edited :)", Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(context, "Invalid Item Quantity", Toast.LENGTH_SHORT).show()
                        }

                        itemName = ""
                        itemQuantity = ""
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Confirm")
                    }
                }
            },

            title = { Text("Edit Item") },

            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it},
                        label = { Text("Item Name") }
                    )

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it},
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        )
    }
}


@Composable
fun ShoppingListItem(
  item: ShoppingItem,
  editItemUnit: () -> Unit,
  deleteItemUnit: () -> Unit
){
// TODO - edit button
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        Row{
            Text(item.quantity.toString(), style = MaterialTheme.typography.titleLarge)

            Icon(Icons.Default.ArrowForward, contentDescription = "Forward", modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 10.dp))

            if(item.name.length > 8) {
                Text(item.name.take(10) + "...", style = MaterialTheme.typography.titleLarge)
            }else{
                Text(item.name, style = MaterialTheme.typography.titleLarge)
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceEvenly)
        {
            IconButton(onClick = editItemUnit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = deleteItemUnit) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}