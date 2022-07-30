package com.medprompt.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

/**
 * TopBar for our Scaffold on the home screen
 */
@Composable
fun AppBar(onNavigationIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "MedPrompt") },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle Menu"
                )
            }
        }
    )
}