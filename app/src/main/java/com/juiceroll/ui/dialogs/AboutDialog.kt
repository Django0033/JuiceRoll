package com.juiceroll.ui.dialogs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.JuiceOrange
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Surface

/**
 * Dialog displaying information about Juice Oracle and the app.
 *
 * Shows app name, version, description, credits for the original
 * Juice Oracle creator, and information about the app developer.
 * Provides links to the Juice Oracle itch.io page, developer GitHub,
 * and the project source code.
 */
@Composable
fun AboutJuiceDialog(
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocalDrink,
                    contentDescription = null,
                    tint = JuiceOrange,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "About Juice",
                    style = MaterialTheme.typography.titleLarge,
                    color = Parchment,
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                // What is Juice?
                SectionHeaderWithIcon(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "What is Juice?",
                    color = Gold,
                )
                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF2A2420),
                    border = BorderStroke(1.dp, ParchmentDark.copy(alpha = 0.15f)),
                ) {
                    Text(
                        text = "Juice is a solo roleplaying oracle designed to be travel-friendly " +
                            "and minimal, yet versatile and complete. It is a dense collection of " +
                            "generic tables that can be used to enhance your roleplaying experience.\n\n" +
                            "There is a large focus on immersion and NPC interaction, as these are " +
                            "often the most enjoyable parts of a solo session.\n\n" +
                            "If you are familiar with the Mythic system, this oracle will feel " +
                            "intuitive. It provides alternative variations of major Mythic components " +
                            "such as Fate Checks, Meaning Tables, Random Events, and Scene Transitions.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Parchment.copy(alpha = 0.85f),
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Juice Oracle Creator
                SectionHeaderWithIcon(
                    icon = Icons.Filled.Person,
                    title = "Juice Oracle Creator",
                    color = JuiceOrange,
                )
                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = JuiceOrange.copy(alpha = 0.06f),
                    border = BorderStroke(1.dp, JuiceOrange.copy(alpha = 0.2f)),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = JuiceOrange,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "thunder9861",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = JuiceOrange,
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "The Juice Oracle was created by thunder9861, who has been " +
                                "iterating on this oracle for a long time to bring together the " +
                                "best concepts from various solo roleplaying systems.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Parchment.copy(alpha = 0.85f),
                            lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://thunder9861.itch.io/juice-oracle"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, JuiceOrange.copy(alpha = 0.4f)),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = null,
                                tint = JuiceOrange,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Get the Juice Oracle PDFs", color = JuiceOrange)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // App Developer
                SectionHeaderWithIcon(
                    icon = Icons.Filled.Code,
                    title = "App Developer",
                    color = Info,
                )
                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Info.copy(alpha = 0.06f),
                    border = BorderStroke(1.dp, Info.copy(alpha = 0.2f)),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Brush,
                                contentDescription = null,
                                tint = Info,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "John Kordich",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Info,
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This app was independently created by John Kordich, an artist " +
                                "and software engineer who loves all types of games. Built just for " +
                                "fun to make the Juice Oracle more accessible!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Parchment.copy(alpha = 0.85f),
                            lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/johnkord"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, Info.copy(alpha = 0.4f)),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = null,
                                    tint = Info,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("GitHub", color = Info)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/johnkord/juice-roll"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, Info.copy(alpha = 0.4f)),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Code,
                                    contentDescription = null,
                                    tint = Info,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Source", color = Info)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer
                Text(
                    text = "Happy adventuring!",
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic,
                    color = Gold.copy(alpha = 0.8f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = ParchmentDark)
            }
        },
        containerColor = Surface,
        titleContentColor = Parchment,
        textContentColor = Parchment.copy(alpha = 0.9f),
        shape = RoundedCornerShape(16.dp),
    )
}
