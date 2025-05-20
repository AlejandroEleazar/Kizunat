package com.example.kizunat.AppScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Calendar
import compose.icons.fontawesomeicons.regular.User
import compose.icons.fontawesomeicons.solid.Calendar
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.User

enum class BottomBarIcon {
    HOME, MENU, PROFILE
}


@Composable
fun CustomScaffold(
    navigateToHome: () -> Unit,
    navigateToMenu: () -> Unit,
    navigateToProfile: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    var selectedIcon by remember { mutableStateOf(BottomBarIcon.HOME) }

    Scaffold(
        bottomBar = {
            CustomBottomBar(
                selectedIcon = selectedIcon,
                onIconSelected = { selectedIcon = it },
                navigateToHome,
                navigateToProfile,
                navigateToMenu
            )
        },
        content = content
    )
}


@Composable
fun CustomBottomBar(
    selectedIcon: BottomBarIcon,
    onIconSelected: (BottomBarIcon) -> Unit,
    navigateToHome: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToMenu: () -> Unit
) {
    val icons: List<ImageVector> = when (selectedIcon) {
        BottomBarIcon.HOME -> listOf(
            FontAwesomeIcons.Solid.Home,
            FontAwesomeIcons.Regular.Calendar,
            FontAwesomeIcons.Regular.User
        )

        BottomBarIcon.MENU -> listOf(
            FontAwesomeIcons.Solid.Home,
            FontAwesomeIcons.Solid.Calendar,
            FontAwesomeIcons.Regular.User
        )

        BottomBarIcon.PROFILE -> listOf(
            FontAwesomeIcons.Solid.Home,
            FontAwesomeIcons.Regular.Calendar,
            FontAwesomeIcons.Solid.User
        )
    }

    BottomAppBar(
        containerColor = Color(0xffffffff),
        actions = {
            Spacer(Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    onIconSelected(BottomBarIcon.HOME)
                    navigateToHome()
                }) {
                    Icon(
                        icons[0],
                        contentDescription = "home",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                        onIconSelected(BottomBarIcon.MENU)
                        navigateToMenu()
                    }
                ) {
                    Icon(
                        icons[1],
                        contentDescription = "Menu",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    onIconSelected(BottomBarIcon.PROFILE)
                    navigateToProfile()
                }) {
                    Icon(
                        icons[2],
                        contentDescription = "Profile",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))
        }
    )
}
