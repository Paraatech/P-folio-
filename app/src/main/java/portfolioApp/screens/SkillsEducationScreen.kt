package portfolioApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import portfolioApp.viewmodel.PortfolioViewModel

@Composable
fun SkillsEducationScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: PortfolioViewModel = viewModel()
) {
    val primaryBlue = Color(0xFF1E40AF)
    val backgroundColor = Color(0xFFF8FAFC)

    val skillLabels = listOf("Languages", "Frameworks", "Tools/Platforms", "Backend", "Soft Skills")
    val skillExamples = listOf(
        "e.g. C, C++, Java",
        "e.g. HTML, CSS, JavaScript, React (basic), Bootstrap",
        "e.g. LeetCode, GeeksforGeeks, Git, GitHub",
        "e.g. Node.js (basic), MySQL, MongoDB, REST APIs",
        "e.g. Problem-solving, Team Collaboration, Continuous Learning"
    )
    val savedSkills = viewModel.portfolio.value.skills
    val standardSkillPrefixes = skillLabels.map { "$it:" }
    val skillDetails = remember {
        mutableStateListOf<String>().apply {
            skillLabels.forEachIndexed { index, label ->
                val prefix = "$label:"
                val saved = savedSkills.firstOrNull { it.startsWith(prefix, ignoreCase = true) }
                    ?.substringAfter(':')
                    ?.trim()
                add(saved.orEmpty())
            }
        }
    }
    var additionalSkills by remember {
        mutableStateOf(
            savedSkills.filterNot { skill ->
                standardSkillPrefixes.any { skill.startsWith(it, ignoreCase = true) }
            }.joinToString("\n")
        )
    }

    var college by remember { mutableStateOf(viewModel.portfolio.value.college) }
    var degree by remember { mutableStateOf(viewModel.portfolio.value.degree) }
    var branch by remember { mutableStateOf(viewModel.portfolio.value.branch) }
    var year by remember { mutableStateOf(viewModel.portfolio.value.graduationYear) }
    var collegeLocation by remember { mutableStateOf(viewModel.portfolio.value.collegeLocation) }
    var secondarySchool by remember { mutableStateOf(viewModel.portfolio.value.secondarySchool) }
    var secondaryEducation by remember { mutableStateOf(viewModel.portfolio.value.secondaryEducation) }
    var secondaryEducationDates by remember { mutableStateOf(viewModel.portfolio.value.secondaryEducationDates) }
    var secondaryEducationLocation by remember { mutableStateOf(viewModel.portfolio.value.secondaryEducationLocation) }
    var highSchool by remember { mutableStateOf(viewModel.portfolio.value.highSchool) }
    var highSchoolEducation by remember { mutableStateOf(viewModel.portfolio.value.highSchoolEducation) }
    var highSchoolDates by remember { mutableStateOf(viewModel.portfolio.value.highSchoolDates) }
    var highSchoolLocation by remember { mutableStateOf(viewModel.portfolio.value.highSchoolLocation) }

    val tfColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        cursorColor = primaryBlue,
        focusedBorderColor = primaryBlue,
        unfocusedBorderColor = Color(0xFFE2E8F0),
        focusedLabelColor = primaryBlue,
        unfocusedLabelColor = Color.Gray
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            "Skills & Education",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Text(
            "Step 3 of 6",
            color = Color(0xFF64748B),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { 3f / 6f },
            modifier = Modifier.fillMaxWidth(),
            color = primaryBlue,
            trackColor = Color(0xFFE2E8F0)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text("Skills Summary", fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))

        skillLabels.forEachIndexed { index, label ->
            OutlinedTextField(
                value = skillDetails[index],
                onValueChange = { skillDetails[index] = it },
                label = { Text(label) },
                placeholder = { Text(skillExamples[index]) },
                modifier = Modifier.fillMaxWidth(),
                colors = tfColors
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        OutlinedTextField(
            value = additionalSkills,
            onValueChange = { additionalSkills = it },
            label = { Text("Additional Skills (optional)") },
            placeholder = { Text("Add other skills, one per line") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

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
            placeholder = { Text("e.g. Example University") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = degree,
            onValueChange = { degree = it },
            label = { Text("Degree") },
            placeholder = { Text("e.g. Bachelor of Technology in Computer Science") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = branch,
            onValueChange = { branch = it },
            label = { Text("CGPA") },
            placeholder = { Text("e.g. CGPA - 7.52") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Graduation Year") },
            placeholder = { Text("e.g. Aug 2024 - Present") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = collegeLocation,
            onValueChange = { collegeLocation = it },
            label = { Text("University Location") },
            placeholder = { Text("e.g. City, Country") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Previous Education",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = secondarySchool,
            onValueChange = { secondarySchool = it },
            label = { Text("Intermediate School") },
            placeholder = { Text("e.g. Example Senior Secondary School") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = secondaryEducation,
            onValueChange = { secondaryEducation = it },
            label = { Text("Intermediate Result") },
            placeholder = { Text("e.g. Intermediate – 75%") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = secondaryEducationLocation,
            onValueChange = { secondaryEducationLocation = it },
            label = { Text("Intermediate Location") },
            placeholder = { Text("e.g. City, State") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = secondaryEducationDates,
            onValueChange = { secondaryEducationDates = it },
            label = { Text("Intermediate Dates") },
            placeholder = { Text("e.g. Mar 2023 – Mar 2024") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = highSchool,
            onValueChange = { highSchool = it },
            label = { Text("High School") },
            placeholder = { Text("e.g. Example High School") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = highSchoolEducation,
            onValueChange = { highSchoolEducation = it },
            label = { Text("High School Result") },
            placeholder = { Text("e.g. Matriculation – 93.2%") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = highSchoolLocation,
            onValueChange = { highSchoolLocation = it },
            label = { Text("High School Location") },
            placeholder = { Text("e.g. City, State") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = highSchoolDates,
            onValueChange = { highSchoolDates = it },
            label = { Text("High School Dates") },
            placeholder = { Text("e.g. Mar 2022 – Mar 2023") },
            modifier = Modifier.fillMaxWidth(),
            colors = tfColors
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                Spacer(Modifier.width(6.dp))
                Text("Back")
            }

            Button(
                onClick = {
                    viewModel.updatePortfolio { portfolio ->
                        portfolio.copy(
                            skills = (
                                skillLabels.mapIndexedNotNull { index, label ->
                                    skillDetails[index].takeIf(String::isNotBlank)?.let {
                                        "$label: ${it.trim()}"
                                    }
                                } + additionalSkills.lines()
                                    .map(String::trim)
                                    .filter(String::isNotBlank)
                            ).toMutableList(),
                            college = college,
                            degree = degree,
                            branch = branch,
                            graduationYear = year,
                            collegeLocation = collegeLocation,
                            secondarySchool = secondarySchool,
                            secondaryEducation = secondaryEducation,
                            secondaryEducationDates = secondaryEducationDates,
                            secondaryEducationLocation = secondaryEducationLocation,
                            highSchool = highSchool,
                            highSchoolEducation = highSchoolEducation,
                            highSchoolDates = highSchoolDates,
                            highSchoolLocation = highSchoolLocation
                        )
                    }
                    onNext()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryBlue
                )
            ) {
                Text("Next")
                Spacer(Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
