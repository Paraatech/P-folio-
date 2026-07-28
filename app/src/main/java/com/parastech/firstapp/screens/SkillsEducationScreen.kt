package com.parastech.firstapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.FlowRow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parastech.firstapp.viewmodel.PortfolioViewModel

@Composable
fun SkillsEducationScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: PortfolioViewModel = viewModel()
) {

    val suggestedSkills = listOf(
        "Kotlin","Java","Android","Jetpack Compose",
        "Firebase","Python","C++","React",
        "HTML","CSS","JavaScript","SQL"
    )

    val selectedSkills = remember { mutableStateListOf<String>().apply { addAll(viewModel.portfolio.value.skills) } }

    var customSkill by remember { mutableStateOf("") }

    var college by remember { mutableStateOf(viewModel.portfolio.value.college) }
    var degree by remember { mutableStateOf(viewModel.portfolio.value.degree) }
    var branch by remember { mutableStateOf(viewModel.portfolio.value.branch) }
    var year by remember { mutableStateOf(viewModel.portfolio.value.graduationYear) }

    val tfColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        cursorColor = Color(0xFF6C63FF),
        focusedBorderColor = Color(0xFF6C63FF),
        unfocusedBorderColor = Color.LightGray,
        focusedLabelColor = Color(0xFF6C63FF),
        unfocusedLabelColor = Color.Gray
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F7FF))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            "Skills & Education",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            "Step 3 of 6",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { 3f / 6f },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF6C63FF)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            "Suggested Skills",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(220.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(suggestedSkills) { skill ->

                FilterChip(
                    selected = selectedSkills.contains(skill),
                    onClick = {
                        if (selectedSkills.contains(skill))
                            selectedSkills.remove(skill)
                        else
                            selectedSkills.add(skill)
                    },
                    label = {
                        Text(
                            text = skill,
                            color = if (selectedSkills.contains(skill))
                                Color.White
                            else
                                Color.Black
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Black,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Selected Skills",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            selectedSkills.forEach { skill ->

                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = skill,
                            color = Color.White
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                selectedSkills.remove(skill)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFF6C63FF),
                        labelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = customSkill,
            onValueChange = { customSkill = it },
            label = { Text("Add Custom Skill") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (customSkill.isNotBlank()) {
                    selectedSkills.add(customSkill)
                    customSkill = ""
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C63FF)
            )
        ) {
            Text("Add Skill")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            "Education",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = college,
            onValueChange = { college = it },
            label = { Text("College / University") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = degree,
            onValueChange = { degree = it },
            label = { Text("Degree") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = branch,
            onValueChange = { branch = it },
            label = { Text("Branch") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Graduation Year") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null)
                Spacer(Modifier.width(6.dp))
                Text("Back")
            }

            Button(
                onClick = {
                    viewModel.updatePortfolio { portfolio ->
                        portfolio.copy(
                            skills = selectedSkills.toMutableList(),
                            college = college,
                            degree = degree,
                            branch = branch,
                            graduationYear = year
                        )
                    }
                    onNext()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C63FF)
                )
            ) {
                Text("Next")
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Default.ArrowForward, null)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}