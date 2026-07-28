package com.parastech.firstapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent


import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun ProjectsScreen() {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                "My Projects",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C63FF)
            )
        }

        item {
            ProjectCard(
                title = "Portfolio App",
                description = "A modern Android portfolio application built using Kotlin and Jetpack Compose.",
                tech = "Kotlin • Jetpack Compose",
                icon = {
                    Icon(
                        Icons.Default.Code,
                        contentDescription = null,
                        tint = Color(0xFF6C63FF)

                    )
                },onViewDetails = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://github.com/Paraatech/P-folio-".toUri()
                    )
                    context.startActivity(intent)
                }
            )
        }

        item {
            ProjectCard(
                title = "Beauty Bronze",
                description = "E-Commerce app for salon products.",
                tech = "Compose • Firebase",
                icon = {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFF6C63FF)
                    )
                },
                onViewDetails = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://paraatech.github.io/BeautyBronzeProfessional/".toUri()
                    )
                    context.startActivity(intent)
                }
            )
        }

        item {
            ProjectCard(
                title = "Smart Dustbin",
                description = "An Arduino-based smart dustbin that automatically opens its lid using an ultrasonic sensor when a hand is detected.",
                tech = "Arduino • Ultrasonic Sensor • Servo Motor",
                icon = {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color(0xFF6C63FF)
                    )
                },
                onViewDetails = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://www.linkedin.com/posts/paras-wadhwa18_project-01-project-description-smart-ugcPost-7269035259210383361-XHeu/?utm_source=share&utm_medium=member_desktop&rcm=ACoAAFSWoroBP0G_FGq9bWUF6O5-5_aKsutDY90".toUri()
                    )
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun ProjectCard(
    title: String,
    description: String,
    tech: String,
    icon: @Composable () -> Unit,
    onViewDetails: (() -> Unit)? = null
){

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                icon()

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(description)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                tech,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.Green
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text("Completed")
            }

            if (onViewDetails != null) {

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onViewDetails,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(" View Details")
                }
            }
        }

    }

}