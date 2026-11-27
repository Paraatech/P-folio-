package portfolioApp.screens

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import portfolioApp.model.Achievement
import portfolioApp.model.Portfolio
import portfolioApp.model.Project
import java.io.ByteArrayOutputStream
import java.io.File

@RunWith(AndroidJUnit4::class)
class ResumePdfRendererTest {
    @Test
    fun rendersDenseResumeAsOneA4PortraitPage() {
        val portfolio = Portfolio(
            fullName = "Paras Wadhwa",
            email = "Parasw2005@gmail.com",
            phone = "+91-8930316879",
            linkedin = "www.linkedin.com/in/paras-wadhwa18",
            github = "https://github.com/Paraatech",
            skills = mutableListOf(
                "Languages: C, C++, Java",
                "Frameworks: HTML, CSS, JavaScript, React (basic), Bootstrap",
                "Tools/Platforms: LeetCode, GeeksforGeeks, Git, GitHub",
                "Backend: Node.js, MySQL, MongoDB, REST APIs",
                "Soft Skills: Problem-solving, Team Collaboration, Continuous Learning"
            ),
            college = "Lovely Professional University",
            degree = "Bachelor of Technology in Computer Science and Engineering",
            branch = "CGPA - 7.52",
            graduationYear = "Aug 2024 - Present",
            location = "Punjab, India",
            secondarySchool = "IPS Sr. sec. school",
            secondaryEducation = "Intermediate – 75%",
            secondaryEducationDates = "Mar 2023 – Mar 2024",
            secondaryEducationLocation = "Rohtak, Haryana",
            highSchool = "Gvm High School",
            highSchoolEducation = "Matriculation – 93.2%",
            highSchoolDates = "Mar 2022 – Mar 2023",
            highSchoolLocation = "Rohtak, Haryana",
            projects = mutableListOf(
                Project(
                    title = "Portfolio Android Application | GitHub",
                    description = "Developed a professional personal portfolio Android application to showcase projects, achievements, certifications and contact information.",
                    technologies = "Android Studio | Kotlin | Jetpack Compose | Material 3",
                    liveDemo = "Jun 2026"
                ),
                Project(
                    title = "Beauty Bronze Professionals | GitHub",
                    description = "Developed a full-stack website for business services, products and customer interaction.",
                    technologies = "HTML | CSS | JavaScript | Node.js | MongoDB",
                    liveDemo = "May 2026"
                )
            ),
            achievements = mutableListOf(
                Achievement(
                    title = "Software Development Intern",
                    category = "Internship",
                    organization = "Humble Coders",
                    issueDate = "Jun 2026 - Jul 2026",
                    description = "Worked on software development tasks in a professional environment. Applied programming and web development concepts to practical projects."
                ),
                Achievement(
                    title = "C++ and JAVA Training",
                    category = "Course",
                    organization = "INFOSYS SPRINGBOARD",
                    issueDate = "Jun 2025 - Jul 2025",
                    description = "Gained hands-on experience in C++ and Java. Improved understanding of API design, problem-solving, and version control."
                ),
                Achievement(
                    title = "AI Database Foundation",
                    category = "Certificate",
                    organization = "ORACLE",
                    issueDate = "May 2026"
                ),
                Achievement(
                    title = "Solved 100+ coding problems",
                    category = "Other",
                    description = "Participated in coding events and collaborated with teams to solve real-world problems."
                )
            )
        )
        val output = ByteArrayOutputStream()

        writeResumePdf(portfolio, output)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val pdfFile = File(context.cacheDir, "resume-renderer-test.pdf")
        try {
            pdfFile.writeBytes(output.toByteArray())
            ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY).use { descriptor ->
                PdfRenderer(descriptor).use { renderer ->
                    assertEquals(1, renderer.pageCount)
                    val page = renderer.openPage(0)
                    try {
                        assertEquals(595, page.width)
                        assertEquals(842, page.height)
                    } finally {
                        page.close()
                    }
                }
            }
        } finally {
            pdfFile.delete()
        }
    }
}
