package portfolioApp.screens

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import portfolioApp.model.Achievement
import portfolioApp.model.Portfolio
import portfolioApp.model.Project
import java.io.OutputStream

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842
private const val SIDE_MARGIN = 25f
private const val BOTTOM_MARGIN = 24f

private data class ResumePaints(
    val name: Paint,
    val section: Paint,
    val text: Paint,
    val bold: Paint,
    val date: Paint,
    val italic: Paint,
    val muted: Paint,
    val link: Paint,
    val rule: Paint
)

fun writeResumePdf(portfolio: Portfolio, output: OutputStream) {
    val document = PdfDocument()
    try {
        val page = document.startPage(
            PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        )
        drawResumePage(portfolio, page.canvas, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat())
        document.finishPage(page)
        document.writeTo(output)
    } finally {
        document.close()
    }
}

fun drawResumePage(portfolio: Portfolio, canvas: Canvas, width: Float, height: Float) {
    val measure = Canvas()
    val contentBottom = drawResume(portfolio, measure, PAGE_WIDTH.toFloat())
    val scale = minOf(
        width / PAGE_WIDTH,
        height / PAGE_HEIGHT,
        width / PAGE_WIDTH * (PAGE_HEIGHT - BOTTOM_MARGIN) / contentBottom
    )
    val pageWidth = PAGE_WIDTH * scale
    val pageHeight = PAGE_HEIGHT * scale
    val left = (width - pageWidth) / 2f
    val top = (height - pageHeight) / 2f

    canvas.drawColor(android.graphics.Color.WHITE)
    canvas.save()
    canvas.translate(left, top)
    canvas.scale(scale, scale)
    drawResume(portfolio, canvas, PAGE_WIDTH.toFloat())
    canvas.restore()
}

private fun drawResume(portfolio: Portfolio, canvas: Canvas, width: Float): Float {
    val navy = android.graphics.Color.rgb(30, 58, 112)
    val textColor = android.graphics.Color.rgb(34, 34, 34)
    val mutedColor = android.graphics.Color.rgb(75, 75, 75)
    val paints = ResumePaints(
        name = resumePaint(navy, 19f, bold = true),
        section = resumePaint(navy, 9.5f, bold = true),
        text = resumePaint(textColor, 8f),
        bold = resumePaint(textColor, 8f, bold = true),
        date = resumePaint(textColor, 7.5f, italic = true, align = Paint.Align.RIGHT),
        italic = resumePaint(textColor, 7.5f, italic = true, align = Paint.Align.RIGHT),
        muted = resumePaint(mutedColor, 7.2f),
        link = resumePaint(android.graphics.Color.rgb(45, 105, 190), 8f, bold = true),
        rule = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = navy
            strokeWidth = 1.4f
        }
    )
    val left = SIDE_MARGIN
    val right = width - SIDE_MARGIN
    val contentWidth = right - left
    var y = 31f

    y = drawWrapped(
        canvas,
        portfolio.fullName.ifBlank { "Your Name" },
        left,
        y,
        contentWidth,
        paints.name,
        1.08f
    )
    y += 1f

    val leftContacts = listOfNotNull(
        portfolio.github.takeIf(String::isNotBlank)?.let { "GitHub: " to it },
        portfolio.linkedin.takeIf(String::isNotBlank)?.let { "LinkedIn: " to it }
    )
    val rightContacts = listOfNotNull(
        portfolio.email.takeIf(String::isNotBlank)?.let { "Email: " to it },
        portfolio.phone.takeIf(String::isNotBlank)?.let { "Mobile: " to it }
    )
    val contactPaint = paints.muted
    contactPaint.textSize = 7f
    val contactLinkPaint = resumePaint(
        android.graphics.Color.rgb(45, 105, 190),
        7f,
        bold = true
    )
    val contactGap = 3f
    val leftLabelWidth = leftContacts.maxOfOrNull { contactPaint.measureText(it.first) } ?: 0f
    val rightLabelWidth = rightContacts.maxOfOrNull { contactPaint.measureText(it.first) } ?: 0f
    val rightValueWidth = rightContacts.maxOfOrNull { contactLinkPaint.measureText(it.second) } ?: 0f
    val leftValueX = left + leftLabelWidth + contactGap
    val rightLabelX = right - rightValueWidth - contactGap - rightLabelWidth
    val contactRowHeight = contactPaint.textSize * 1.35f
    val contactRows = maxOf(leftContacts.size, rightContacts.size)
    repeat(contactRows) { index ->
        leftContacts.getOrNull(index)?.let { (label, value) ->
            canvas.drawText(label, left, y, contactPaint)
            canvas.drawText(value, leftValueX, y, contactLinkPaint)
        }
        rightContacts.getOrNull(index)?.let { (label, value) ->
            contactPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText(label, rightLabelX + rightLabelWidth, y, contactPaint)
            contactPaint.textAlign = Paint.Align.LEFT
            contactLinkPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText(value, right, y, contactLinkPaint)
            contactLinkPaint.textAlign = Paint.Align.LEFT
            contactPaint.textAlign = Paint.Align.LEFT
        }
        y += contactRowHeight
    }
    y += 4f

    fun section(title: String, content: () -> Unit) {
        y += 4f
        y += paints.section.textSize
        canvas.drawText(title.uppercase(), left, y, paints.section)
        y += 3f
        canvas.drawLine(left, y, right, y, paints.rule)
        y += 8f
        content()
    }

    fun bulletText(text: String) {
        text.split(Regex("(?<=[.!?])\\s+|\\n"))
            .map(String::trim)
            .filter(String::isNotBlank)
            .forEach { sentence ->
                canvas.drawText("•", left + 14f, y, paints.text)
                y = drawWrapped(
                    canvas,
                    sentence,
                    left + 23f,
                    y,
                    contentWidth - 23f,
                    paints.text,
                    1.2f
                )
                y += 0.5f
            }
    }

    fun titleDate(title: String, date: String) {
        if (title.isBlank() && date.isBlank()) return
        val dateWidth = if (date.isBlank()) 0f else paints.date.measureText(date) + 10f
        val titleWidth = (contentWidth - dateWidth).coerceAtLeast(40f)
        val titleBottom = if (title.isNotBlank()) {
            drawWrapped(canvas, title, left, y, titleWidth, paints.bold, 1.15f)
        } else {
            y
        }
        if (date.isNotBlank()) {
            canvas.drawText(date, right, y, paints.date)
        }
        y = maxOf(titleBottom, y + paints.bold.textSize * 1.15f) + 1f
    }

    fun linkedTitleDate(title: String, linkLabel: String, date: String) {
        val fullTitle = listOf(title, linkLabel).filter(String::isNotBlank).joinToString(" | ")
        if (fullTitle.isBlank() && date.isBlank()) return
        val dateWidth = if (date.isBlank()) 0f else paints.date.measureText(date) + 10f
        val titleWidth = (contentWidth - dateWidth).coerceAtLeast(40f)
        val separatorWidth =
            if (title.isNotBlank() && linkLabel.isNotBlank()) paints.bold.measureText(" | ") else 0f
        val combinedWidth = paints.bold.measureText(title) + separatorWidth +
            paints.link.measureText(linkLabel)

        if (combinedWidth <= titleWidth) {
            val baseline = y
            var x = left
            if (title.isNotBlank()) {
                canvas.drawText(title, x, y, paints.bold)
                x += paints.bold.measureText(title)
            }
            if (title.isNotBlank() && linkLabel.isNotBlank()) {
                canvas.drawText(" | ", x, y, paints.bold)
                x += paints.bold.measureText(" | ")
            }
            if (linkLabel.isNotBlank()) canvas.drawText(linkLabel, x, y, paints.link)
            if (date.isNotBlank()) canvas.drawText(date, right, y, paints.date)
            y += paints.bold.textSize * 1.3f
        } else {
            val baseline = y
            val titleBottom = drawWrapped(canvas, title, left, y, titleWidth, paints.bold, 1.15f)
            y = titleBottom
            if (linkLabel.isNotBlank()) {
                canvas.drawText(linkLabel, left, y, paints.link)
                y += paints.link.textSize * 1.3f
            }
            if (date.isNotBlank()) canvas.drawText(date, right, baseline, paints.date)
        }
        y += 1f
    }

    fun drawAchievement(achievement: Achievement) {
        val title = listOf(achievement.title, achievement.organization)
            .filter(String::isNotBlank)
            .joinToString(" | ")
        if (
            achievement.category.equals("Internship", true) ||
            achievement.category.equals("Course", true) ||
            achievement.category.equals("Certificate", true)
        ) {
            linkedTitleDate(title, "Certificate", achievement.issueDate)
            if (achievement.description.isNotBlank()) bulletText(achievement.description)
        } else if (
            achievement.organization.isBlank() &&
            achievement.issueDate.isBlank()
        ) {
            bulletText(listOf(title, achievement.description)
                .filter(String::isNotBlank)
                .joinToString(". "))
        } else {
            titleDate(title, achievement.issueDate)
            if (achievement.credentialUrl.isNotBlank()) {
                y = drawWrapped(
                    canvas,
                    achievement.credentialUrl,
                    left + 8f,
                    y,
                    contentWidth - 8f,
                    paints.link,
                    1.15f
                )
            }
            if (achievement.description.isNotBlank()) bulletText(achievement.description)
        }
        y += 2f
    }

    val internships = portfolio.achievements.filter { it.category.equals("Internship", true) }
    val courses = portfolio.achievements.filter { it.category.equals("Course", true) }
    val certificates = portfolio.achievements.filter { it.category.equals("Certificate", true) }
    val otherAchievements = portfolio.achievements.filter {
        !it.category.equals("Internship", true) &&
            !it.category.equals("Course", true) &&
            !it.category.equals("Certificate", true)
    }

    val skills = portfolio.skills.filter(String::isNotBlank)
    if (skills.isNotEmpty()) {
        section("Skills Summary") {
            val labelColumnWidth = (skills.mapNotNull { skill ->
                val separator = skill.indexOf(':')
                if (separator > 0) {
                    paints.bold.measureText("${skill.substring(0, separator).trim()}:")
                } else {
                    null
                }
            }.maxOrNull() ?: 0f) + 8f

            skills.forEach {
                val separator = it.indexOf(':')
                if (separator > 0) {
                    val label = "${it.substring(0, separator).trim()}:"
                    canvas.drawText(label, left, y, paints.bold)
                    y = drawWrapped(
                        canvas,
                        it.substring(separator + 1).trim(),
                        left + labelColumnWidth,
                        y,
                        (contentWidth - labelColumnWidth).coerceAtLeast(40f),
                        paints.text,
                        1.25f
                    )
                } else {
                    y = drawWrapped(canvas, it, left, y, contentWidth, paints.text, 1.25f)
                }
            }
        }
    }

    if (internships.isNotEmpty()) {
        section("Internships") {
            internships.forEach(::drawAchievement)
        }
    }

    if (portfolio.projects.isNotEmpty()) {
        section("Projects") {
            portfolio.projects.forEach { project: Project ->
                val hasGithubLabel = project.github.isNotBlank() ||
                    project.title.substringAfterLast("|", "").trim().equals("GitHub", true)
                val title = if (hasGithubLabel && project.title.contains("|")) {
                    project.title.substringBeforeLast("|").trim()
                } else {
                    project.title.ifBlank { "Project" }
                }
                if (hasGithubLabel) {
                    linkedTitleDate(title, "GitHub", project.liveDemo)
                } else {
                    titleDate(title, project.liveDemo)
                }
                if (project.description.isNotBlank()) bulletText(project.description)
                if (project.technologies.isNotBlank()) {
                    canvas.drawText("TECH STACK:", left + 8f, y, paints.bold)
                    val stackLabelWidth = paints.bold.measureText("TECH STACK: ")
                    y = drawWrapped(
                        canvas,
                        project.technologies,
                        left + 8f + stackLabelWidth,
                        y,
                        contentWidth - 8f - stackLabelWidth,
                        paints.text,
                        1.2f
                    )
                }
                y += 2f
            }
        }
    }

    if (courses.isNotEmpty()) {
        section("Training") {
            courses.forEach(::drawAchievement)
        }
    }

    if (certificates.isNotEmpty()) {
        section("Certificates") {
            certificates.forEach(::drawAchievement)
        }
    }

    if (otherAchievements.isNotEmpty()) {
        section("Achievements") {
            otherAchievements.forEach(::drawAchievement)
        }
    }

    if (
        portfolio.college.isNotBlank() ||
        portfolio.degree.isNotBlank() ||
        portfolio.branch.isNotBlank() ||
        portfolio.secondarySchool.isNotBlank() ||
        portfolio.highSchool.isNotBlank()
    ) {
        section("Education") {
            y = educationEntry(
                canvas, portfolio.college, portfolio.collegeLocation,
                portfolio.degree, portfolio.branch, portfolio.graduationYear,
                left, right, contentWidth, paints, y
            )
            y = educationEntry(
                canvas, portfolio.secondarySchool, portfolio.secondaryEducationLocation,
                "", portfolio.secondaryEducation, portfolio.secondaryEducationDates,
                left, right, contentWidth, paints, y
            )
            y = educationEntry(
                canvas, portfolio.highSchool, portfolio.highSchoolLocation,
                "", portfolio.highSchoolEducation, portfolio.highSchoolDates,
                left, right, contentWidth, paints, y
            )
        }
    }

    return y
}

private fun resumePaint(
    color: Int,
    size: Float,
    bold: Boolean = false,
    italic: Boolean = false,
    align: Paint.Align = Paint.Align.LEFT
) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    this.color = color
    textSize = size
    textAlign = align
    typeface = Typeface.create(
        Typeface.SANS_SERIF,
        when {
            bold && italic -> Typeface.BOLD_ITALIC
            bold -> Typeface.BOLD
            italic -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }
    )
}

private fun educationEntry(
    canvas: Canvas,
    institution: String,
    location: String,
    qualification: String,
    result: String,
    dates: String,
    left: Float,
    right: Float,
    contentWidth: Float,
    paints: ResumePaints,
    baseline: Float
): Float {
    if (
        institution.isBlank() && location.isBlank() &&
        qualification.isBlank() && result.isBlank() && dates.isBlank()
    ) return baseline
    var y = baseline
    if (institution.isNotBlank()) {
        y = drawWrapped(canvas, institution, left, baseline, contentWidth * 0.68f, paints.bold, 1.15f)
        if (location.isNotBlank()) canvas.drawText(location, right, baseline, paints.italic)
    }
    if (qualification.isNotBlank()) {
        val qualificationBaseline = y
        y = drawWrapped(canvas, qualification, left, y, contentWidth * 0.68f, paints.text, 1.15f)
        if (dates.isNotBlank()) canvas.drawText(dates, right, qualificationBaseline, paints.italic)
    } else if (dates.isNotBlank()) {
        canvas.drawText(dates, right, y, paints.italic)
    }
    if (result.isNotBlank()) {
        y = drawWrapped(canvas, result, left, y, contentWidth, paints.text, 1.15f)
    }
    return y + 2f
}

private fun drawWrapped(
    canvas: Canvas,
    text: String,
    x: Float,
    baseline: Float,
    maxWidth: Float,
    paint: Paint,
    lineHeightMultiplier: Float
): Float {
    val words = mutableListOf<String>()
    text.trim().split(Regex("\\s+")).filter(String::isNotBlank).forEach { word ->
        if (paint.measureText(word) <= maxWidth) {
            words += word
        } else {
            var remaining = word
            while (remaining.isNotEmpty()) {
                val count = paint.breakText(remaining, true, maxWidth, null)
                if (count <= 0) break
                words += remaining.substring(0, count)
                remaining = remaining.substring(count)
            }
        }
    }
    if (words.isEmpty()) return baseline

    val lineHeight = paint.textSize * lineHeightMultiplier
    var y = baseline
    var line = ""
    words.forEach { word ->
        val candidate = if (line.isEmpty()) word else "$line $word"
        if (line.isNotEmpty() && paint.measureText(candidate) > maxWidth) {
            canvas.drawText(line, x, y, paint)
            y += lineHeight
            line = word
        } else {
            line = candidate
        }
    }
    canvas.drawText(line, x, y, paint)
    return y + lineHeight
}
