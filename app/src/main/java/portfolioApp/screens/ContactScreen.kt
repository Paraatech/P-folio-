package portfolioApp.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactScreen() {

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
            value = "Example: +1 555 010 1234",
            icon = { Icon(Icons.Default.Call, null) }
        )

        ContactCard(
            title = "Email",
            value = "Example: name@example.com",
            icon = { Icon(Icons.Default.Email, null) }
        )

        ContactCard(
            title = "LinkedIn",
            value = "Example: linkedin.com/in/your-name",
            icon = { Icon(Icons.Default.Person, null) }
        )

        ContactCard(
            title = "GitHub",
            value = "Example: github.com/your-username",
            icon = { Icon(Icons.Default.Public, null) }
        )

    }
}

@Composable
fun ContactCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit
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
