package com.parastech.firstapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
import android.widget.Toast
import android.os.Environment
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color as AndroidColor
import android.media.MediaScannerConnection
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import coil.compose.AsyncImage
import androidx.core.content.FileProvider
import com.parastech.firstapp.viewmodel.PortfolioViewModel

@Composable
fun PreviewScreen(viewModel: PortfolioViewModel = viewModel()) {

    val portfolio = viewModel.portfolio.value
    val context = LocalContext.current

    // Professional color scheme
    val primaryBlue = Color(0xFF1E40AF)      // Deep professional blue
    val accentBlue = Color(0xFF3B82F6)       // Bright accent blue
    val successGreen = Color(0xFF059669)     // Professional green
    val backgroundColor = Color(0xFFF8FAFC)  // Clean light background
    val cardBackground = Color.White
    val textPrimary = Color(0xFF0F172A)      // Very dark blue for text
    val textSecondary = Color(0xFF64748B)    // Slate gray for secondary text
    val borderColor = Color(0xFFE2E8F0)      // Subtle border

    val savePortfolioImage = {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "Portfolio_${portfolio.fullName.replace(" ", "_")}_$timeStamp.jpg"

            // Create bitmap for portfolio image
            val width = 1200
            val height = 1800
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Background
            canvas.drawColor(AndroidColor.parseColor("#F8FAFC"))

            val paint = Paint().apply {
                isAntiAlias = true
                color = AndroidColor.parseColor("#1E40AF")
                textSize = 48f
                textAlign = Paint.Align.CENTER
            }

            val smallPaint = Paint().apply {
                isAntiAlias = true
                color = AndroidColor.parseColor("#64748B")
                textSize = 32f
                textAlign = Paint.Align.CENTER
            }

            val textPaint = Paint().apply {
                isAntiAlias = true
                color = AndroidColor.parseColor("#0F172A")
                textSize = 28f
            }

            var yPosition = 100f

            // Draw title
            canvas.drawText("PROFESSIONAL PORTFOLIO", width / 2f, yPosition, paint)
            yPosition += 80f

            // Draw name
            paint.color = AndroidColor.parseColor("#1E40AF")
            canvas.drawText(portfolio.fullName.ifEmpty { "Your Name" }, width / 2f, yPosition, paint)
            yPosition += 60f

            // Draw profession
            smallPaint.textAlign = Paint.Align.CENTER
            canvas.drawText(portfolio.profession.ifEmpty { "Your Profession" }, width / 2f, yPosition, smallPaint)
            yPosition += 100f

            // Draw sections
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 36f
            textPaint.color = AndroidColor.parseColor("#1E40AF")

            // About section
            canvas.drawText("📝 ABOUT", 60f, yPosition, textPaint)
            yPosition += 50f
            textPaint.color = AndroidColor.parseColor("#0F172A")
            textPaint.textSize = 28f
            canvas.drawText(portfolio.about.take(50).ifEmpty { "About yourself" }, 60f, yPosition, textPaint)
            yPosition += 80f

            // Skills section
            textPaint.color = AndroidColor.parseColor("#1E40AF")
            textPaint.textSize = 36f
            canvas.drawText("💻 SKILLS", 60f, yPosition, textPaint)
            yPosition += 50f
            textPaint.color = AndroidColor.parseColor("#0F172A")
            textPaint.textSize = 28f
            portfolio.skills.take(3).forEach { skill ->
                canvas.drawText("• $skill", 80f, yPosition, textPaint)
                yPosition += 50f
            }
            yPosition += 30f

            // Contact section
            textPaint.color = AndroidColor.parseColor("#1E40AF")
            textPaint.textSize = 36f
            canvas.drawText("📞 CONTACT", 60f, yPosition, textPaint)
            yPosition += 50f
            textPaint.color = AndroidColor.parseColor("#0F172A")
            textPaint.textSize = 28f
            if (portfolio.email.isNotEmpty()) {
                canvas.drawText("📧 ${portfolio.email.take(30)}", 80f, yPosition, textPaint)
                yPosition += 50f
            }
            if (portfolio.phone.isNotEmpty()) {
                canvas.drawText("📱 ${portfolio.phone}", 80f, yPosition, textPaint)
                yPosition += 50f
            }

            // Save image to Pictures gallery
            val picturesDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PortfolioApp")
            if (!picturesDir.exists()) {
                picturesDir.mkdirs()
            }

            val imageFile = File(picturesDir, fileName)
            FileOutputStream(imageFile).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, output)
            }

            // Notify gallery
            MediaScannerConnection.scanFile(context, arrayOf(imageFile.absolutePath), null, null)

            // Save text to Documents folder
            val portfolioFileName = "Portfolio_${portfolio.fullName.replace(" ", "_")}_$timeStamp.txt"
            val portfolioContent = """
                ╔════════════════════════════════════════════════════════════════╗
                ║                    PROFESSIONAL PORTFOLIO                      ║
                ╚════════════════════════════════════════════════════════════════╝
                
                👤 PERSONAL INFORMATION
                ────────────────────────────────────────────────────────────────
                Name: ${portfolio.fullName}
                Profession: ${portfolio.profession}
                Location: ${portfolio.location}
                
                📝 ABOUT ME
                ────────────────────────────────────────────────────────────────
                ${portfolio.about}
                
                💻 SKILLS
                ────────────────────────────────────────────────────────────────
                ${portfolio.skills.takeIf { it.isNotEmpty() }?.joinToString("\n") { "  • $it" } ?: "  • No skills added"}
                
                🎓 EDUCATION
                ────────────────────────────────────────────────────────────────
                University: ${portfolio.college}
                Degree: ${portfolio.degree}
                Branch: ${portfolio.branch}
                Graduation Year: ${portfolio.graduationYear}
                
                💼 PROJECTS
                ────────────────────────────────────────────────────────────────
                ${portfolio.projects.takeIf { it.isNotEmpty() }?.mapIndexed { idx, project ->
                    """
                    Project ${idx + 1}: ${project.title}
                    Description: ${project.description}
                    Technologies: ${project.technologies}
                    GitHub: ${project.github}
                    Live Demo: ${project.liveDemo}
                    """.trimIndent()
                }?.joinToString("\n") ?: "  • No projects added"}
                
                🏆 ACHIEVEMENTS
                ────────────────────────────────────────────────────────────────
                ${portfolio.achievements.takeIf { it.isNotEmpty() }?.mapIndexed { idx, achievement ->
                    """
                    Achievement ${idx + 1}: ${achievement.title}
                    Organization: ${achievement.organization}
                    Date: ${achievement.issueDate}
                    """.trimIndent()
                }?.joinToString("\n") ?: "  • No achievements added"}
                
                📞 CONTACT INFORMATION
                ────────────────────────────────────────────────────────────────
                Email: ${portfolio.email}
                Phone: ${portfolio.phone}
                GitHub: ${portfolio.github}
                LinkedIn: ${portfolio.linkedin}
                
                ════════════════════════════════════════════════════════════════
                Generated on: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(Date())}
                ════════════════════════════════════════════════════════════════
            """.trimIndent()

            val documentsDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "PortfolioApp")
            if (!documentsDir.exists()) {
                documentsDir.mkdirs()
            }

            val textFile = File(documentsDir, portfolioFileName)
            FileOutputStream(textFile).use { output ->
                output.write(portfolioContent.toByteArray())
            }

            Toast.makeText(context, "✅ Portfolio saved!\n📸 Image: $fileName\n📄 Text: $portfolioFileName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "❌ Error saving portfolio: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    val savePortfolio = savePortfolioImage


    val sharePortfolio = {
        try {
            val shareText = """
╔════════════════════════════════════════════════════════════════╗
║              CHECK OUT MY PROFESSIONAL PORTFOLIO!             ║
╚════════════════════════════════════════════════════════════════╝

👤 ${portfolio.fullName}
💼 ${portfolio.profession}

${if (portfolio.about.isNotEmpty()) "📝 About: ${portfolio.about}\n" else ""}
${if (portfolio.skills.isNotEmpty()) "💻 Skills: ${portfolio.skills.joinToString(", ")}\n" else ""}
📧 Email: ${portfolio.email}
📱 Phone: ${portfolio.phone}
🌐 GitHub: ${portfolio.github}

🔗 Download the PortfolioApp to create your own!

#Portfolio #Professional #Developer #Portfolio
            """.trimIndent()

            // Create an image version of the portfolio and save to Pictures
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "Portfolio_${portfolio.fullName.replace(" ", "_")}_$timeStamp.jpg"

            val width = 1200
            val height = 1800
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Background
            canvas.drawColor(AndroidColor.parseColor("#F8FAFC"))

            val titlePaint = Paint().apply {
                isAntiAlias = true
                color = AndroidColor.parseColor("#1E40AF")
                textSize = 56f
                textAlign = Paint.Align.CENTER
            }

            val namePaint = Paint().apply {
                isAntiAlias = true
                color = AndroidColor.parseColor("#0F172A")
                textSize = 44f
                textAlign = Paint.Align.CENTER
            }

            var yPos = 140f
            canvas.drawText("PROFESSIONAL PORTFOLIO", width / 2f, yPos, titlePaint)
            yPos += 100f
            canvas.drawText(portfolio.fullName.ifEmpty { "Your Name" }, width / 2f, yPos, namePaint)
            yPos += 80f
            canvas.drawText(portfolio.profession.ifEmpty { "Your Profession" }, width / 2f, yPos, titlePaint)
            yPos += 120f

            val smallPaint = Paint().apply {
                isAntiAlias = true
                color = AndroidColor.parseColor("#0F172A")
                textSize = 32f
                textAlign = Paint.Align.LEFT
            }

            val leftPadding = 60f
            if (portfolio.about.isNotEmpty()) {
                canvas.drawText("About: ${portfolio.about.take(120)}", leftPadding, yPos, smallPaint)
                yPos += 60f
            }
            if (portfolio.skills.isNotEmpty()) {
                canvas.drawText("Skills: ${portfolio.skills.take(5).joinToString(", ")}", leftPadding, yPos, smallPaint)
                yPos += 60f
            }

            // Save image to Pictures
            val picturesDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PortfolioApp")
            if (!picturesDir.exists()) {
                picturesDir.mkdirs()
            }

            val imageFile = File(picturesDir, fileName)
            FileOutputStream(imageFile).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, output)
            }

            // Make available to gallery
            MediaScannerConnection.scanFile(context, arrayOf(imageFile.absolutePath), null, null)

            // Create share intent with both text and image
            val uri = androidx.core.content.FileProvider.getUriForFile(context, context.packageName + ".fileprovider", imageFile)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Portfolio"))

        } catch (e: Exception) {
            Toast.makeText(context, "Error sharing: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Text(
                "Step 6 of 6",
                color = textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "🎉 Portfolio Preview",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue
            )

            Text(
                "Your professional portfolio",
                color = textSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

        }

        item {

            Card(
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = primaryBlue.copy(alpha = 0.15f)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                if (portfolio.fullName.isNotEmpty()) accentBlue
                                else Color(0xFFDEEAF8)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (portfolio.imageUri.isNotEmpty()) {
                            AsyncImage(
                                model = portfolio.imageUri,
                                contentDescription = "Profile Photo",
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                portfolio.fullName.take(1).uppercase(),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        portfolio.fullName.ifEmpty { "Your Name" },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        portfolio.profession.ifEmpty { "Your Profession" },
                        color = accentBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (portfolio.location.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "📍 ${portfolio.location}",
                            color = textSecondary,
                            fontSize = 14.sp
                        )
                    }

                }

            }

        }

        item {

            SectionCard(
                title = "About Me",
                backgroundColor = cardBackground,
                primaryColor = primaryBlue,
                borderColor = borderColor
            ) {

                Text(
                    portfolio.about.ifEmpty { "Fill About yourself" },
                    color = textPrimary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )

            }

        }

        item {

            SectionCard(
                title = "Skills",
                backgroundColor = cardBackground,
                primaryColor = primaryBlue,
                borderColor = borderColor
            ) {

                if (portfolio.skills.isNotEmpty()) {
                    portfolio.skills.forEach { skill ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(accentBlue)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(skill, color = textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                } else {
                    val skills = listOf("Kotlin", "Jetpack Compose", "Firebase", "Android Studio")
                    skills.forEach { skill ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(accentBlue)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(skill, color = textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

            }

        }
        item {

            SectionCard(
                title = "Projects",
                backgroundColor = cardBackground,
                primaryColor = primaryBlue,
                borderColor = borderColor
            ) {

                if (portfolio.projects.isNotEmpty()) {
                    portfolio.projects.forEach { project ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF1F5F9)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {

                                Text(
                                    project.title.ifEmpty { "📁 Project" },
                                    fontWeight = FontWeight.Bold,
                                    color = primaryBlue,
                                    fontSize = 15.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(project.description.ifEmpty { "Project description" }, color = textSecondary, fontSize = 13.sp)

                                if (project.technologies.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Tech: ${project.technologies}", color = accentBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }

                                if (project.github.isNotEmpty() || project.liveDemo.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        if (project.github.isNotEmpty()) {
                                            TextButton(onClick = { }, modifier = Modifier.height(32.dp)) {
                                                Text("🔗 GitHub", fontSize = 12.sp, color = accentBlue)
                                            }
                                        }
                                        if (project.liveDemo.isNotEmpty()) {
                                            TextButton(onClick = { }, modifier = Modifier.height(32.dp)) {
                                                Text("🌐 URL", fontSize = 12.sp, color = accentBlue)
                                            }
                                        }
                                    }
                                }

                            }

                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF1F5F9)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {

                            Text(
                                "📁 Portfolio Builder",
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue,
                                fontSize = 15.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Built using Kotlin and Jetpack Compose.", color = textSecondary, fontSize = 13.sp)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(onClick = { }, modifier = Modifier.height(32.dp)) {
                                    Text("🔗 GitHub", fontSize = 12.sp, color = accentBlue)
                                }
                                TextButton(onClick = { }, modifier = Modifier.height(32.dp)) {
                                    Text("🌐 URL", fontSize = 12.sp, color = accentBlue)
                                }
                            }

                        }

                    }
                }

            }

        }

        item {

            SectionCard(
                title = "Achievements",
                backgroundColor = cardBackground,
                primaryColor = primaryBlue,
                borderColor = borderColor
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0FDF4)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {

                        if (portfolio.achievements.isNotEmpty()) {
                            portfolio.achievements.forEach { achievement ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🏆", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(achievement.title.ifEmpty { "Achievement" }, fontWeight = FontWeight.Bold, color = textPrimary, fontSize = 14.sp)
                                        if (achievement.organization.isNotEmpty()) {
                                            Text(achievement.organization, color = textSecondary, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🏆", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Google Android Certificate", fontWeight = FontWeight.Bold, color = textPrimary, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🏆", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Hackathon Finalist", fontWeight = FontWeight.Bold, color = textPrimary, fontSize = 14.sp)
                            }
                        }

                    }

                }

            }

        }

        item {

            SectionCard(
                title = "Education",
                backgroundColor = cardBackground,
                primaryColor = primaryBlue,
                borderColor = borderColor
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F9FF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {

                        Text(
                            portfolio.college.ifEmpty { "EDUCATION/College" },
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            portfolio.degree.ifEmpty { "Branch" } +
                            (if (portfolio.branch.isNotEmpty()) " - ${portfolio.branch}" else ""),
                            color = textPrimary,
                            fontSize = 14.sp
                        )

                    }

                }

            }

        }

        item {

            SectionCard(
                title = "Contact",
                backgroundColor = cardBackground,
                primaryColor = primaryBlue,
                borderColor = borderColor
            ) {

                if (portfolio.email.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = accentBlue,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(portfolio.email, color = textPrimary, fontSize = 14.sp)

                    }
                }

                if (portfolio.phone.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = accentBlue,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(portfolio.phone, color = textPrimary, fontSize = 14.sp)

                    }
                }

                if (portfolio.github.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Public,
                            contentDescription = null,
                            tint = accentBlue,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(portfolio.github, color = textPrimary, fontSize = 14.sp)

                    }
                }

                if (portfolio.email.isEmpty() && portfolio.phone.isEmpty() && portfolio.github.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = accentBlue,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text("john@email.com", color = textPrimary, fontSize = 14.sp)

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = accentBlue,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text("+91 9876543210", color = textPrimary, fontSize = 14.sp)

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Public,
                            contentDescription = null,
                            tint = accentBlue,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text("github.com/johndoe", color = textPrimary, fontSize = 14.sp)

                    }
                }

            }

        }
        item {

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = savePortfolio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentBlue
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Save Portfolio", color = Color.White, fontWeight = FontWeight.SemiBold)

            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = sharePortfolio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = accentBlue
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Share Portfolio", color = accentBlue, fontWeight = FontWeight.SemiBold)

            }

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(40.dp))

        }

    }

}

@Composable
fun SectionCard(
    title: String,
    backgroundColor: Color = Color.White,
    primaryColor: Color = Color(0xFF1E40AF),
    borderColor: Color = Color(0xFFE2E8F0),
    content: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .background(borderColor)
            )

            Spacer(modifier = Modifier.height(16.dp))

            content()

        }

    }

}