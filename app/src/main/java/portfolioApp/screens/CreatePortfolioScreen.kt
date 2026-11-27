package portfolioApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

import androidx.lifecycle.viewmodel.compose.viewModel
import portfolioApp.viewmodel.PortfolioViewModel

@Composable
fun CreatePortfolioScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: PortfolioViewModel = viewModel()
) {
    val primaryBlue = Color(0xFF1E40AF)
    val accentBlue = Color(0xFF3B82F6)
    val surfaceColor = Color.White
    val backgroundColor = Color(0xFFF8FAFC)

    var fullName by remember { mutableStateOf(viewModel.portfolio.value.fullName) }
    var profession by remember { mutableStateOf(viewModel.portfolio.value.profession) }
    var about by remember { mutableStateOf(viewModel.portfolio.value.about) }
    var imageUri by remember {
        mutableStateOf<Uri?>(
            if (viewModel.portfolio.value.imageUri.isNotEmpty())
                Uri.parse(viewModel.portfolio.value.imageUri)
            else
                null
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = surfaceColor,
        unfocusedContainerColor = surfaceColor,
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
            text = "Create Your Portfolio",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Step 1 of 6 • Personal Information",
            color = Color(0xFF64748B),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        LinearProgressIndicator(
            progress = { 1f / 7f },
            modifier = Modifier.fillMaxWidth(),
            color = primaryBlue,
            trackColor = Color(0xFFE2E8F0)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable {
                            launcher.launch("image/*")
                        },
                    color = Color(0xFFE0EAFF),
                    shadowElevation = 3.dp
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        if (imageUri == null) {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = primaryBlue,
                                modifier = Modifier.size(60.dp)
                            )

                        } else {

                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Profile",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Tap to add profile photo",
                    color = primaryBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            placeholder = { Text("e.g. Your Name") },
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors
        )
        Spacer(modifier = Modifier.height(18.dp))
        OutlinedTextField(
            value = profession,
            onValueChange = { profession = it },
            label = { Text("Profession") },
            placeholder = { Text("e.g. Android Developer") },
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
            value = about,
            onValueChange = { about = it },
            label = { Text("About Yourself") },
            placeholder = {
                Text("e.g. Android developer skilled in Kotlin, Java, and Jetpack Compose")
            },
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors
        )

        Spacer(modifier = Modifier.height(35.dp))

        Button(
            onClick = {
                viewModel.updatePortfolio { portfolio ->
                    portfolio.copy(
                        fullName = fullName,
                        profession = profession,
                        about = about,
                        imageUri = imageUri?.toString() ?: ""
                    )
                }
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryBlue
            )
        ) {

            Text(
                text = "Next",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
