package ai.lingopulse.ui.feature.setting

import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.setting.SettingScreenUiState
import ai.lingopulse.presentation.setting.model.UiLanguage
import ai.lingopulse.presentation.setting.model.UiLanguageLevel
import ai.lingopulse.ui.core.common.model.ScreenEventHandler
import ai.lingopulse.ui.core.theme.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kodein.emoji.compose.m2.TextWithPlatformEmoji

@Composable
fun SettingScreen(
    state: SettingScreenUiState,
    eventHandler: ScreenEventHandler
) {
    when (state) {
        is SettingScreenUiState.ScreenLoaded -> {
            SettingContent(
                categories = state.categories,
                languages = state.languages,
                languageLevels = state.languageLevels,
                eventHandler = eventHandler
            )
        }
    }
}

@Composable
private fun SettingContent(
    categories: List<UiCategory>,
    languages: List<UiLanguage>,
    languageLevels: List<UiLanguageLevel>,
    eventHandler: ScreenEventHandler
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PaddingXLarge)
        ) {
            item {
                Spacer(modifier = Modifier.height(PaddingXXLarge))
            }

            // Welcome Section
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(PaddingMedium)
                ) {
                    Text(
                        text = "Welcome to the Lingo Pulse app",
                        fontSize = FontSizeXXLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryColor,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Let's get to know each other",
                        fontSize = FontSizeLarge,
                        color = TextSecondaryColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Categories Section
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(PaddingMedium)
                ) {
                    Text(
                        text = "Choose your interests",
                        fontSize = FontSizeLarge,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimaryColor,
                        textAlign = TextAlign.Center
                    )
                    
                    CategoryRow(
                        categories = categories,
                        eventHandler = eventHandler
                    )
                }
            }

            // Language Dropdown Section
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(PaddingMedium)
                ) {
                    Text(
                        text = "What is your preferred language?",
                        fontSize = FontSizeLarge,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimaryColor,
                        textAlign = TextAlign.Center
                    )
                    
                    LanguageDropdown(
                        languages = languages,
                        eventHandler = eventHandler
                    )
                }
            }

            // Language Level Section
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(PaddingMedium)
                ) {
                    Text(
                        text = "What is your language level?",
                        fontSize = FontSizeLarge,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimaryColor,
                        textAlign = TextAlign.Center
                    )
                    
                    LanguageLevelRow(
                        languageLevels = languageLevels,
                        eventHandler = eventHandler
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(PaddingXXLarge))
            }
        }

        // Sticky Confirm Button
        Button(
            onClick = {
                eventHandler.onEvent(SettingScreenEvent.OnConfirmClicked)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge, vertical = PaddingSuperLarge),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PrimaryColor,
                contentColor = ToolbarColor
            ),
            shape = RoundedCornerShape(CornerSmall),
            contentPadding = PaddingValues(PaddingMedium)
        ) {
            Text(
                text = "Confirm and open feeds",
                fontSize = FontSizeMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CategoryRow(
    categories: List<UiCategory>,
    eventHandler: ScreenEventHandler
) {
    FlowRow(
        modifier = Modifier
            .widthIn(max = 640.dp)
            .padding(horizontal = PaddingMedium),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(PaddingXXTiny)
    ) {
        categories.forEach { category ->
            OutlinedButton(
                onClick = {
                    eventHandler.onEvent(
                        SettingScreenEvent.OnCategoryClicked(category)
                    )
                },
                shape = RoundedCornerShape(CornerSmall),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (category.isSelected) TextSecondaryColor else ToolbarColor,
                    contentColor = TextSecondaryColor
                ),
                border = BorderStroke(0.5.dp, TextSecondaryColor),
                modifier = Modifier.padding(horizontal = PaddingXXTiny)
            ) {
                TextWithPlatformEmoji(
                    modifier = Modifier.padding(PaddingXXTiny),
                    text = category.category,
                    color = if (category.isSelected) ToolbarColor else TextSecondaryColor,
                )
            }
        }
    }
}

@Composable
private fun LanguageLevelRow(
    languageLevels: List<UiLanguageLevel>,
    eventHandler: ScreenEventHandler
) {
    FlowRow(
        modifier = Modifier
            .widthIn(max = 640.dp)
            .padding(horizontal = PaddingMedium),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(PaddingXXTiny)
    ) {
        languageLevels.forEach { level ->
            OutlinedButton(
                onClick = {
                    eventHandler.onEvent(
                        SettingScreenEvent.OnLanguageLevelClicked(level)
                    )
                },
                shape = RoundedCornerShape(CornerSmall),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (level.isSelected) TextSecondaryColor else ToolbarColor,
                    contentColor = TextSecondaryColor
                ),
                border = BorderStroke(0.5.dp, TextSecondaryColor),
                modifier = Modifier.padding(horizontal = PaddingXXTiny)
            ) {
                Text(
                    modifier = Modifier.padding(PaddingXXTiny),
                    text = level.level,
                    color = if (level.isSelected) ToolbarColor else TextSecondaryColor,
                )
            }
        }
    }
}

@Composable
private fun LanguageDropdown(
    languages: List<UiLanguage>,
    eventHandler: ScreenEventHandler
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLanguage = languages.find { it.isSelected }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(CornerSmall),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ToolbarColor,
                contentColor = TextSecondaryColor
            ),
            border = BorderStroke(0.5.dp, TextSecondaryColor),
            contentPadding = PaddingValues(PaddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextWithPlatformEmoji(
                    text = selectedLanguage?.name ?: "Select a language",
                    color = TextSecondaryColor
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = TextSecondaryColor
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    onClick = {
                        eventHandler.onEvent(SettingScreenEvent.OnLanguageClicked(language))
                        expanded = false
                    }
                ) {
                    TextWithPlatformEmoji(
                        text = language.name,
                        color = TextPrimaryColor
                    )
                }
            }
        }
    }
}
