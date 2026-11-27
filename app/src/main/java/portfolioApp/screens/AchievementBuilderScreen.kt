package portfolioApp.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import portfolioApp.model.Achievement
import portfolioApp.viewmodel.PortfolioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementBuilderScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: PortfolioViewModel = viewModel()
) {

    val primaryBlue = Color(0xFF1E40AF)
    val backgroundColor = Color(0xFFF8FAFC)

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

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Text(
                "Step 5 of 6",
                color = Color(0xFF64748B),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "🏆 Achievements",
                fontSize = 28.sp,
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        items(achievements.size) { index ->
 
            val achievement = achievements[index]
            val certificateLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->
                if (uri != null) {
                    achievements[index] = achievement.copy(credentialUrl = uri.toString())
                }
            }
 
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
                        label = "Achievement Title",
                        placeholder = when (achievement.category) {
                            "Internship" -> "e.g. Software Development Intern"
                            "Course" -> "e.g. C++ and JAVA Training"
                            "Certificate" -> if (index == 1) {
                                "e.g. C++ Course Completion"
                            } else {
                                "e.g. AI Database Foundation"
                            }
                            else -> "e.g. Solved 100+ coding problems"
                        }
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
                                focusedBorderColor = primaryBlue,
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                cursorColor = primaryBlue,
                                focusedLabelColor = primaryBlue,
                                unfocusedLabelColor = Color.Gray
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
                        label = "Organization",
                        placeholder = when (achievement.category) {
                            "Internship" -> "e.g. Example Company"
                            "Course" -> "e.g. Example Training Provider"
                            "Certificate" -> if (index == 1) {
                                "e.g. Example Learning Platform"
                            } else {
                                "e.g. Example Organization"
                            }
                            else -> "e.g. Organization or institution"
                        }
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
                        label = "Issue Date",
                        placeholder = when (achievement.category) {
                            "Internship" -> "e.g. Jun 2026 - Jul 2026"
                            "Course" -> "e.g. Jun 2025 - Jul 2025"
                            "Certificate" -> if (index == 1) {
                                "e.g. Jun 2026"
                            } else {
                                "e.g. May 2026"
                            }
                            else -> "e.g. May 2026"
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            certificateLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryBlue
                        )
                    ) {
                        Text(
                            if (achievement.credentialUrl.isNotBlank()) "Replace Certificate Photo" else "Upload Certificate Photo"
                        )
                    }

                    if (achievement.credentialUrl.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        AsyncImage(
                            model = Uri.parse(achievement.credentialUrl),
                            contentDescription = "Certificate preview",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(achievement.credentialUrl)).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                runCatching { context.startActivity(intent) }
                            }
                        ) {
                            Text("Open certificate link")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AchievementField(
                        value = achievement.credentialUrl,
                        onValueChange = {
                            achievements[index] =
                                achievement.copy(
                                    credentialUrl = it
                                )
                        },
                        label = "Credential URL / Uploaded File Link",
                        placeholder = "e.g. https://example.com/certificate"
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
                        label = "Description",
                        placeholder = when (achievement.category) {
                            "Internship" -> "e.g. Worked on software development tasks and practical projects."
                            "Course" -> "e.g. Gained hands-on experience in C++ and Java."
                            "Certificate" -> "e.g. Learned Oracle Database concepts and SQL."
                            else -> "e.g. Describe this achievement and its impact."
                        }
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
                    containerColor = primaryBlue
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
fun AchievementField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = ""
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
