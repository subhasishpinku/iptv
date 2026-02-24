/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bacbpl.iptv.jetStram.presentation.screens.profile

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bacbpl.iptv.JetStreamActivity
import com.bacbpl.iptv.data.util.StringConstants
import com.bacbpl.iptv.jetStram.presentation.screens.dashboard.rememberChildPadding
import com.bacbpl.iptv.ui.activities.StartScreen
import com.bacbpl.iptv.utils.UserSession
import com.bacbpl.iptv.ui.activities.otpscreen.data.User
import com.bacbpl.iptv.utils.getUserName
import com.bacbpl.iptv.utils.getUserEmail
import com.bacbpl.iptv.utils.getUserMobile

@Immutable
data class AccountsSectionData(
    val title: String,
    val value: String? = null,
    val onClick: () -> Unit = {}
)
@Composable
fun AccountsSection() {
    val context = LocalContext.current
    val isLoggedIn by UserSession.isLoggedIn.collectAsState()
    val userName by UserSession.userName.collectAsState()
    val userEmail by UserSession.userEmail.collectAsState()
    val userMobile by UserSession.userMobile.collectAsState()

    val childPadding = rememberChildPadding()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Update session when composable is first composed
    LaunchedEffect(Unit) {
        UserSession.updateSession(context)
    }

    if (!isLoggedIn) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(childPadding.start),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Please login to view profile",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    } else {
        val accountsSectionListItems = remember(userName, userEmail, userMobile) {
            listOf(
                AccountsSectionData(
                    title = "Name",
                    value = userName ?: "User"
                ),
                AccountsSectionData(
                    title = "Email",
                    value = userEmail ?: "Email not set"
                ),
                AccountsSectionData(
                    title = "Mobile",
                    value = userMobile ?: "Mobile not set"
                ),
                AccountsSectionData(
                    title = "Log Out",
                    onClick = {
                        UserSession.clearSession(context)
                        val intent = Intent(context, StartScreen::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                ),
                AccountsSectionData(
                    title = "Change Password",
                    value = "Change"
                ),
                AccountsSectionData(
                    title = "Add New Account",
                ),
                AccountsSectionData(
                    title = "View Subscriptions"
                ),
                AccountsSectionData(
                    title = "Delete Account",
                    onClick = { showDeleteDialog = true }
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = childPadding.start),
                columns = GridCells.Fixed(2),
                content = {
                    items(accountsSectionListItems.size) { index ->
                        AccountsSelectionItem(
                            modifier = Modifier.focusRequester(focusRequester),
                            key = index,
                            accountsSectionData = accountsSectionListItems[index]
                        )
                    }
                }
            )
        }

        AccountsSectionDeleteDialog(
            showDialog = showDeleteDialog,
            onDismissRequest = { showDeleteDialog = false },
            modifier = Modifier.width(428.dp)
        )
    }
}