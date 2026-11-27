package portfolioApp.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import portfolioApp.model.Portfolio

@Composable
fun ResumePreviewContent(
    portfolio: Portfolio,
    onSave: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F3F5))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                "Resume Preview",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A70)
            )
            Text(
                "A4 portrait layout",
                fontSize = 13.sp,
                color = Color(0xFF555555)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(595f / 842f)
        ) {
            drawResumePage(
                portfolio = portfolio,
                canvas = drawContext.canvas.nativeCanvas,
                width = size.width,
                height = size.height
            )
        }

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A70))
        ) {
            Text("Save Resume (PDF)")
        }
        OutlinedButton(
            onClick = onShare,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share Resume")
        }
        Spacer(Modifier.height(20.dp))
    }
}
