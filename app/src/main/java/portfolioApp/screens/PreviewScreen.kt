package portfolioApp.screens

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
import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import android.os.Environment
import android.os.Build
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color as AndroidColor
import android.provider.MediaStore
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Shader
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import coil.compose.AsyncImage
import androidx.core.content.FileProvider
import portfolioApp.viewmodel.PortfolioViewModel

@Composable
fun PreviewScreen(viewModel: PortfolioViewModel = viewModel()) {

    val portfolio = viewModel.portfolio.value
    val context = LocalContext.current

    val openPortfolioLink: (String) -> Unit = { url ->
        if (url.isNotBlank()) {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            runCatching { context.startActivity(intent) }
        }
    }

    // Professional color scheme
    val primaryBlue = Color(0xFF1E40AF)      // Deep professional blue
    val accentBlue = Color(0xFF3B82F6)       // Bright accent blue
    val successGreen = Color(0xFF059669)     // Professional green
    val backgroundColor = Color(0xFFF8FAFC)  // Clean light background
    val cardBackground = Color.White
    val textPrimary = Color(0xFF0F172A)      // Very dark blue for text
    val textSecondary = Color(0xFF64748B)    // Slate gray for secondary text
    val borderColor = Color(0xFFE2E8F0)      // Subtle border

    // Helper to create a circular cropped & scaled bitmap to avoid shader artifacts
    fun createCircularBitmap(srcBmp: Bitmap, diameter: Int): Bitmap {
        val size = kotlin.math.min(srcBmp.width, srcBmp.height)
        val x = (srcBmp.width - size) / 2
        val y = (srcBmp.height - size) / 2
        val squared = Bitmap.createBitmap(srcBmp, x, y, size, size)
        val scaled = Bitmap.createScaledBitmap(squared, diameter, diameter, true)
        val output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvasOut = Canvas(output)
        val paintOut = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
        }
        val shader = BitmapShader(scaled, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paintOut.shader = shader
        val radius = diameter / 2f
        canvasOut.drawCircle(radius, radius, radius, paintOut)
        return output
    }

    // Helper for wrapping text and drawing it
    fun drawWrappedText(canvas: Canvas, text: String, x: Float, y: Float, maxWidth: Float, paint: Paint): Float {
        var currentY = y
        val words = text.split(" ")
        var line = ""
        for (word in words) {
            val testLine = if (line.isEmpty()) word else "$line $word"
            val width = paint.measureText(testLine)
            if (width > maxWidth && line.isNotEmpty()) {
                canvas.drawText(line, x, currentY, paint)
                line = word
                currentY += paint.textSize * 1.2f
            } else {
                line = testLine
            }
        }
        canvas.drawText(line, x, currentY, paint)
        return currentY + paint.textSize * 1.2f
    }

    val generatePortfolioBitmap = {
        val width = 1200
        val height = 3000
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(AndroidColor.WHITE)

        val primaryColor = AndroidColor.parseColor("#1E40AF")
        val textColor = AndroidColor.parseColor("#1F2937")
        val secondaryTextColor = AndroidColor.parseColor("#4B5563")
        val borderColor = AndroidColor.parseColor("#E5E7EB")

        val namePaint = Paint().apply {
            isAntiAlias = true
            color = primaryColor
            textSize = 54f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }

        val professionPaint = Paint().apply {
            isAntiAlias = true
            color = secondaryTextColor
            textSize = 22f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.NORMAL)
        }

        val sectionHeaderPaint = Paint().apply {
            isAntiAlias = true
            color = primaryColor
            textSize = 26f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }

        val contentPaint = Paint().apply {
            isAntiAlias = true
            color = textColor
            textSize = 22f
        }

        val boldContentPaint = Paint().apply {
            isAntiAlias = true
            color = textColor
            textSize = 22f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }

        val datePaint = Paint().apply {
            isAntiAlias = true
            color = secondaryTextColor
            textSize = 20f
            textAlign = Paint.Align.RIGHT
        }

        val contactPaint = Paint().apply {
            isAntiAlias = true
            color = secondaryTextColor
            textSize = 18f
        }
        val rightContactPaint = Paint(contactPaint).apply {
            textAlign = Paint.Align.RIGHT
        }

        val linePaint = Paint().apply {
            color = borderColor
            strokeWidth = 3f
        }

        val leftMargin = 80f
        val rightMargin = 1120f
        val contentWidth = rightMargin - leftMargin
        var yPos = 100f

        // --- HEADER ---
        canvas.drawText(portfolio.fullName, leftMargin, yPos + 45f, namePaint)
        yPos += 56f

        val githubText = if (portfolio.github.isNotEmpty()) "GitHub: ${portfolio.github}" else null
        val linkedinText = if (portfolio.linkedin.isNotEmpty()) "LinkedIn: ${portfolio.linkedin}" else null
        val emailText = if (portfolio.email.isNotEmpty()) "Email: ${portfolio.email}" else null
        val phoneText = if (portfolio.phone.isNotEmpty()) "Mobile: ${portfolio.phone}" else null

        val leftLine = listOfNotNull(githubText, linkedinText).joinToString("  |  ")
        val rightLine = listOfNotNull(emailText, phoneText).joinToString("  |  ")

        if (leftLine.isNotEmpty()) {
            canvas.drawText(leftLine, leftMargin, yPos, contactPaint)
        }
        if (rightLine.isNotEmpty()) {
            canvas.drawText(rightLine, rightMargin, yPos, rightContactPaint)
        }
        yPos += 26f

        fun drawSection(title: String) {
            yPos += 8f
            canvas.drawText(title.uppercase(), leftMargin, yPos, sectionHeaderPaint)
            yPos += 16f
            canvas.drawLine(leftMargin, yPos, rightMargin, yPos, linePaint)
            yPos += 26f
        }

        fun drawBullets(text: String, indent: Float = leftMargin + 12f) {
            val lines = text.split(". ").filter { it.isNotBlank() }
            lines.forEach { line ->
                val clean = line.trim().removePrefix("• ").removePrefix("- ")
                val bulletText = "• $clean"
                yPos = drawWrappedText(canvas, bulletText, indent, yPos, contentWidth - 20f, contentPaint)
                yPos += 2f
            }
        }

        if (portfolio.skills.isNotEmpty()) {
            drawSection("Skills Summary")
            portfolio.skills.forEach { skill ->
                canvas.drawText(skill, leftMargin, yPos, contentPaint)
                yPos += 24f
            }
            yPos += 6f
        }

        val internshipItems = portfolio.achievements.filter { it.category.equals("Internship", ignoreCase = true) }
        if (internshipItems.isNotEmpty()) {
            drawSection("Internships")
            internshipItems.forEach { achievement ->
                val mainTitle = listOfNotNull(
                    achievement.title.takeIf { it.isNotEmpty() },
                    achievement.organization.takeIf { it.isNotEmpty() },
                    achievement.category.takeIf { it.isNotEmpty() }
                ).joinToString(" | ")
                canvas.drawText(mainTitle, leftMargin, yPos, boldContentPaint)
                if (achievement.issueDate.isNotEmpty()) {
                    canvas.drawText(achievement.issueDate, rightMargin, yPos, datePaint)
                }
                yPos += 26f
                if (achievement.description.isNotEmpty()) {
                    drawBullets(achievement.description)
                }
                yPos += 8f
            }
        }

        if (portfolio.projects.isNotEmpty()) {
            drawSection("Projects")
            portfolio.projects.forEach { project ->
                val titleText = project.title.ifEmpty { "Project" }
                canvas.drawText(titleText, leftMargin, yPos, boldContentPaint)
                if (project.liveDemo.isNotEmpty()) {
                    canvas.drawText(project.liveDemo, rightMargin, yPos, datePaint)
                }
                yPos += 26f
                if (project.technologies.isNotEmpty()) {
                    canvas.drawText(project.technologies, leftMargin + 8f, yPos, contentPaint)
                    yPos += 22f
                }
                if (project.description.isNotEmpty()) {
                    drawBullets(project.description)
                }
                yPos += 8f
            }
        }

        val trainingItems = portfolio.achievements.filter { it.category.equals("Course", ignoreCase = true) }
        if (trainingItems.isNotEmpty()) {
            drawSection("Training")
            trainingItems.forEach { achievement ->
                val title = listOfNotNull(
                    achievement.title.takeIf { it.isNotEmpty() },
                    achievement.organization.takeIf { it.isNotEmpty() },
                    achievement.category.takeIf { it.isNotEmpty() }
                ).joinToString(" | ")
                canvas.drawText(title, leftMargin, yPos, boldContentPaint)
                if (achievement.issueDate.isNotEmpty()) {
                    canvas.drawText(achievement.issueDate, rightMargin, yPos, datePaint)
                }
                yPos += 22f
                if (achievement.description.isNotEmpty()) {
                    drawBullets(achievement.description)
                }
                yPos += 8f
            }
        }

        val certItems = portfolio.achievements.filter { it.category.equals("Certificate", ignoreCase = true) }
        if (certItems.isNotEmpty()) {
            drawSection("Certificates")
            certItems.forEach { achievement ->
                val title = listOfNotNull(
                    achievement.title.takeIf { it.isNotEmpty() },
                    achievement.organization.takeIf { it.isNotEmpty() },
                    achievement.category.takeIf { it.isNotEmpty() }
                ).joinToString(" | ")
                canvas.drawText(title, leftMargin, yPos, boldContentPaint)
                if (achievement.issueDate.isNotEmpty()) {
                    canvas.drawText(achievement.issueDate, rightMargin, yPos, datePaint)
                }
                yPos += 22f
                if (achievement.description.isNotEmpty()) {
                    drawBullets(achievement.description)
                }
                yPos += 8f
            }
        }

        val otherAchievements = portfolio.achievements.filter {
            !it.category.equals("Internship", ignoreCase = true) &&
                !it.category.equals("Course", ignoreCase = true) &&
                !it.category.equals("Certificate", ignoreCase = true)
        }
        if (otherAchievements.isNotEmpty()) {
            drawSection("Achievements")
            otherAchievements.forEach { achievement ->
                val title = listOfNotNull(
                    achievement.title.takeIf { it.isNotEmpty() },
                    achievement.organization.takeIf { it.isNotEmpty() }
                ).joinToString(" | ")
                canvas.drawText(title, leftMargin, yPos, boldContentPaint)
                if (achievement.issueDate.isNotEmpty()) {
                    canvas.drawText(achievement.issueDate, rightMargin, yPos, datePaint)
                }
                yPos += 22f
                if (achievement.description.isNotEmpty()) {
                    drawBullets(achievement.description)
                }
                yPos += 8f
            }
        }

        if (portfolio.college.isNotEmpty()) {
            drawSection("Education")
            canvas.drawText(portfolio.college, leftMargin, yPos, boldContentPaint)
            if (portfolio.graduationYear.isNotEmpty()) {
                canvas.drawText(portfolio.graduationYear, rightMargin, yPos, datePaint)
            }
            yPos += 22f
            val degreeText = listOfNotNull(
                portfolio.degree.takeIf { it.isNotEmpty() },
                portfolio.branch.takeIf { it.isNotEmpty() }
            ).joinToString(" ")
            if (degreeText.isNotEmpty()) {
                canvas.drawText(degreeText, leftMargin + 8f, yPos, contentPaint)
                yPos += 20f
            }
        }

        val outputHeight = (yPos + 60f).toInt().coerceAtLeast(1697).coerceAtMost(height)
        Bitmap.createBitmap(bitmap, 0, 0, width, outputHeight)
    }

    val savePortfolioImage = {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val documentsDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "PortfolioApp"
            )
            if (!documentsDir.exists()) {
                documentsDir.mkdirs()
            }

            val pdfFileName = "Resume_${portfolio.fullName.replace(" ", "_")}_$timeStamp.pdf"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        "${Environment.DIRECTORY_DOCUMENTS}/PortfolioApp"
                    )
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
                val pdfUri = context.contentResolver.insert(
                    MediaStore.Files.getContentUri("external"),
                    values
                ) ?: error("Unable to create the PDF file")
                try {
                    val output = context.contentResolver.openOutputStream(pdfUri)
                        ?: error("Unable to open the PDF file")
                    output.use { writeResumePdf(portfolio, it) }
                    values.clear()
                    values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    check(context.contentResolver.update(pdfUri, values, null, null) > 0) {
                        "Unable to finish saving the PDF"
                    }
                } catch (e: Exception) {
                    context.contentResolver.delete(pdfUri, null, null)
                    throw e
                }
            } else {
                val pdfFile = File(documentsDir, pdfFileName)
                FileOutputStream(pdfFile).use { writeResumePdf(portfolio, it) }
            }

            Toast.makeText(
                context,
                "Resume saved as A4 PDF: Documents/PortfolioApp/$pdfFileName",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {
            Toast.makeText(context, "❌ Error saving portfolio: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    val savePortfolio = savePortfolioImage


    val sharePortfolio = {
        try {
            val shareText = """
Check out my resume!

👤 ${portfolio.fullName}
💼 ${portfolio.profession}

Generated with PortfolioApp
            """.trimIndent()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "Resume_${portfolio.fullName.replace(" ", "_")}_$timeStamp.pdf"
            val pdfFile = File(context.cacheDir, fileName)
            FileOutputStream(pdfFile).use { writeResumePdf(portfolio, it) }

            // Create share intent
            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                pdfFile
            )
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "application/pdf"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Resume"))

        } catch (e: Exception) {
            Toast.makeText(context, "Error sharing: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    if (false) LazyColumn(
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

                    Spacer(modifier = Modifier.height(32.dp))

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
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("🏆", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            achievement.title.ifEmpty { "Achievement" },
                                            fontWeight = FontWeight.Bold,
                                            color = textPrimary,
                                            fontSize = 14.sp
                                        )
                                        if (achievement.organization.isNotEmpty()) {
                                            Text(achievement.organization, color = textSecondary, fontSize = 12.sp)
                                        }
                                        if (achievement.issueDate.isNotEmpty()) {
                                            Text(achievement.issueDate, color = textSecondary, fontSize = 12.sp)
                                        }
                                        if (achievement.credentialUrl.isNotEmpty()) {
                                            TextButton(
                                                onClick = { openPortfolioLink(achievement.credentialUrl) },
                                                modifier = Modifier.padding(start = (-12).dp)
                                            ) {
                                                Text("View Certificate", color = accentBlue, fontSize = 12.sp)
                                            }
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
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.5.dp)
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

    ResumePreviewContent(
        portfolio = portfolio,
        onSave = savePortfolio,
        onShare = sharePortfolio
    )
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
