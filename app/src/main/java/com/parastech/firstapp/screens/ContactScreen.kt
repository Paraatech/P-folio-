package com.parastech.firstapp.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@Composable
fun ContactScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FB))
            .padding(16.dp)
    ) {

        Text(
            text = "Let's Connect",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Feel free to reach out anytime.",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        ContactCard(
            title = "Phone",
            value = "8930316879",
            icon = { Icon(Icons.Default.Call, null) }
        ) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:8930316879")
            context.startActivity(intent)
        }

        ContactCard(
            title = "Email",
            value = "Parasw2005@gmail.com",
            icon = { Icon(Icons.Default.Email, null) }
        ) {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:Parasw2005@gmail.com")
            context.startActivity(intent)
        }

        ContactCard(
            title = "LinkedIn",
            value = "linkedin.com/in/paras-wadhwa18",
            icon = { Icon(Icons.Default.Person, null) }
        ) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://www.linkedin.com/in/paras-wadhwa18".toUri()
            )
            context.startActivity(intent)
        }

        ContactCard(
            title = "GitHub",
            value = "github.com/Paraatech",
            icon = { Icon(Icons.Default.Public, null) }
        ) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://github.com/Paraatech".toUri()
            )
            context.startActivity(intent)
        }

    }
}

@Composable
fun ContactCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            icon()

            Column {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = value,
                    color = Color.Gray
                )

            }

        }

    }

}