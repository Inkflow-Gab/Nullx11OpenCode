package dev.opencode.mobile.ui.sessionchat.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.opencode.mobile.domain.command.ServerCommand
import dev.opencode.mobile.ui.components.OpenCodeBottomSheet
import dev.opencode.mobile.ui.components.OpenCodeListItem
import dev.opencode.mobile.ui.components.OpenCodeTextField

/** Searchable library of skills currently advertised by the connected server. */
@Composable
internal fun SessionChatSkillLibrarySheet(
    visible: Boolean,
    skills: List<ServerCommand>,
    onDismiss: () -> Unit,
    onUseSkill: (ServerCommand) -> Unit,
) {
    var query by remember(visible) { mutableStateOf("") }
    val filtered = remember(skills, query) {
        skills.filter { skill ->
            query.isBlank() || skill.name.contains(query.trim(), ignoreCase = true) ||
                skill.description.orEmpty().contains(query.trim(), ignoreCase = true)
        }
    }
    OpenCodeBottomSheet(
        visible = visible,
        title = "Skill library",
        onDismissRequest = onDismiss,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text(
                text = "Choose a server skill to add it to your prompt. Skill files are managed on the connected server.",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search skills") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            )
            if (filtered.isEmpty()) {
                Text(
                    text = if (skills.isEmpty()) "No skills are available from this server." else "No matching skills.",
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                filtered.forEach { skill ->
                    OpenCodeListItem(
                        title = "/${skill.name}",
                        subtitle = skill.description ?: "Server skill",
                        onClick = { onUseSkill(skill) },
                    )
                }
            }
        }
    }
}
