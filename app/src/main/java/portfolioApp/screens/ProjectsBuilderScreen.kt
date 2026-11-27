package portfolioApp.screens

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
import portfolioApp.model.Project
import portfolioApp.viewmodel.PortfolioViewModel

@Composable
fun ProjectsBuilderScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: PortfolioViewModel = viewModel()
) {
    val primaryBlue = Color(0xFF1E40AF)
    val background = Color(0xFFF8FAFC)

    val projects = remember {
        mutableStateListOf<Project>().apply {
            if (viewModel.portfolio.value.projects.isNotEmpty()) {
                addAll(viewModel.portfolio.value.projects)
            } else {
                add(Project())
            }
        }
    }

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
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "💼 My Projects",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
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
                        label = "Project Name",
                        placeholder = if (index == 0) {
                            "e.g. Portfolio Android Application | GitHub"
                        } else {
                            "e.g. Beauty Bronze Professionals | GitHub"
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PortfolioField(
                        value = project.description,
                        onValueChange = {
                            projects[index] =
                                project.copy(description = it)
                        },
                        label = "Description",
                        placeholder = if (index == 0) {
                            "e.g. Built an Android app to showcase projects and achievements."
                        } else {
                            "e.g. Built a full-stack website for business services."
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PortfolioField(
                        value = project.technologies,
                        onValueChange = {
                            projects[index] =
                                project.copy(technologies = it)
                        },
                        label = "Technologies Used",
                        placeholder = if (index == 0) {
                            "e.g. Android Studio | Kotlin | Jetpack Compose"
                        } else {
                            "e.g. HTML | CSS | JavaScript | Node.js | MongoDB"
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PortfolioField(
                        value = project.github,
                        onValueChange = {
                            projects[index] =
                                project.copy(github = it)
                        },
                        label = "GitHub Link",
                        placeholder = "e.g. https://github.com/your-username/project",
                        keyboardType = KeyboardType.Uri
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PortfolioField(
                        value = project.liveDemo,
                        onValueChange = {
                            projects[index] =
                                project.copy(liveDemo = it)
                        },
                        label = "Date (Optional)",
                        placeholder = if (index == 0) "e.g. Jun 2026" else "e.g. May 2026"
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
                    containerColor = primaryBlue
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
                        containerColor = primaryBlue
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
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        placeholder = if (placeholder.isNotEmpty()) {
            { Text(placeholder) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFF1E40AF),
            unfocusedBorderColor = Color(0xFFE2E8F0),
            focusedLabelColor = Color(0xFF1E40AF),
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color(0xFF1E40AF)
        )
    )

}
