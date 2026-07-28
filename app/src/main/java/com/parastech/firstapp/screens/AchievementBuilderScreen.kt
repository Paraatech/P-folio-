package com.parastech.firstapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parastech.firstapp.model.Achievement
import com.parastech.firstapp.viewmodel.PortfolioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementBuilderScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: PortfolioViewModel = viewModel()
) {

    val purple = Color(0xFF6C63FF)

    val achievements = remember {
        mutableStateListOf<Achievement>().apply {
            if (viewModel.portfolio.value.achievements.isNotEmpty()) {
                addAll(viewModel.portfolio.value.achievements)
            } else {
                add(Achievement())
            }
        }
    }

    val categories = listOf(
        "Certificate",
        "Internship",
        "Hackathon",
        "Course",
        "Publication",
        "Open Source",
        "Other"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Text(
                "Step 5 of 6",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "🏆 Achievements",
                fontSize = 28.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        items(achievements.size) { index ->

            val achievement = achievements[index]

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Achievement ${index + 1}",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AchievementField(
                        value = achievement.title,
                        onValueChange = {
                            achievements[index] =
                                achievement.copy(title = it)
                        },
                        label = "Achievement Title"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    var expanded by remember {
                        mutableStateOf(false)
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {

                        OutlinedTextField(
                            value = achievement.category,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("Category")
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = purple,
                                unfocusedBorderColor = Color.LightGray,
                                cursorColor = purple
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            categories.forEach { category ->

                                DropdownMenuItem(
                                    text = {
                                        Text(category)
                                    },
                                    onClick = {
                                        achievements[index] =
                                            achievement.copy(category = category)
                                        expanded = false
                                    }
                                )

                            }

                        }

                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AchievementField(
                        value = achievement.organization,
                        onValueChange = {
                            achievements[index] =
                                achievement.copy(
                                    organization = it
                                )
                        },
                        label = "Organization"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AchievementField(
                        value = achievement.issueDate,
                        onValueChange = {
                            achievements[index] =
                                achievement.copy(
                                    issueDate = it
                                )
                        },
                        label = "Issue Date"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AchievementField(
                        value = achievement.credentialUrl,
                        onValueChange = {
                            achievements[index] =
                                achievement.copy(
                                    credentialUrl = it
                                )
                        },
                        label = "Credential URL (Optional)"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AchievementField(
                        value = achievement.description,
                        onValueChange = {
                            achievements[index] =
                                achievement.copy(
                                    description = it
                                )
                        },
                        label = "Description"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (achievements.size > 1) {

                        TextButton(
                            onClick = {
                                achievements.removeAt(index)
                            }
                        ) {

                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                "Remove Achievement",
                                color = Color.Red
                            )

                        }

                    }

                }

            }

        }
        item {

            Button(
                onClick = {
                    achievements.add(Achievement())
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purple
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Add Another Achievement")

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedButton(
                    onClick = onBack
                ) {
                    Text("← Back")
                }

                Button(
                    onClick = {
                        viewModel.updatePortfolio { portfolio ->
                            portfolio.copy(achievements = achievements.toMutableList())
                        }
                        onNext()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple
                    )
                ) {
                    Text("Next →")
                }

            }

            Spacer(modifier = Modifier.height(30.dp))

        }

    }

}

@Composable
fun AchievementField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFF6C63FF),
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color(0xFF6C63FF),
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color(0xFF6C63FF)
        )
    )

}