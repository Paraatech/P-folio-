package com.parastech.firstapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parastech.firstapp.model.Project
import com.parastech.firstapp.viewmodel.PortfolioViewModel

@Composable
fun ProjectsBuilderScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: PortfolioViewModel = viewModel()
) {

    val projects = remember {
        mutableStateListOf<Project>().apply {
            if (viewModel.portfolio.value.projects.isNotEmpty()) {
                addAll(viewModel.portfolio.value.projects)
            } else {
                add(Project())
            }
        }
    }

    val purple = Color(0xFF6C63FF)
    val background = Color(0xFFF5F7FB)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Text(
                text = "Step 4 of 6",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "💼 My Projects",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        items(projects.size) { index ->

            val project = projects[index]

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Project ${index + 1}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PortfolioField(
                        value = project.title,
                        onValueChange = {
                            projects[index] =
                                project.copy(title = it)
                        },
                        label = "Project Name"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PortfolioField(
                        value = project.description,
                        onValueChange = {
                            projects[index] =
                                project.copy(description = it)
                        },
                        label = "Description"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PortfolioField(
                        value = project.technologies,
                        onValueChange = {
                            projects[index] =
                                project.copy(technologies = it)
                        },
                        label = "Technologies Used"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PortfolioField(
                        value = project.github,
                        onValueChange = {
                            projects[index] =
                                project.copy(github = it)
                        },
                        label = "GitHub Link",
                        keyboardType = KeyboardType.Uri
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PortfolioField(
                        value = project.liveDemo,
                        onValueChange = {
                            projects[index] =
                                project.copy(liveDemo = it)
                        },
                        label = "Project URL (Optional)",
                        keyboardType = KeyboardType.Uri
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (projects.size > 1) {

                        TextButton(
                            onClick = {
                                projects.removeAt(index)
                            }
                        ) {

                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Remove Project",
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
                    projects.add(Project())
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purple
                )
            ) {

                Icon(
                    Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Add Another Project")

            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedButton(
                    onClick = onBack
                ) {
                    Text("← Back")
                }

                Button(
                    onClick = {
                        viewModel.updatePortfolio { portfolio ->
                            portfolio.copy(projects = projects.toMutableList())
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
fun PortfolioField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
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