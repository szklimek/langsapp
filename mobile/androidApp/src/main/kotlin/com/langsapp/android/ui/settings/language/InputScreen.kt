package com.langsapp.android.ui.settings.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.langsapp.android.logging.Log
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.settings.language.LanguageSettingsAction
import com.langsapp.settings.language.LanguageSettingsState

@Composable
internal fun InputScreen(
    actionSender: ActionSender<Action>,
    state: LanguageSettingsState.Input,
) {
    Column {
        Text("Language settings")
        Spacer(Modifier.height(16.dp))

        val chips = remember { state.availableLanguages.map { it.code } }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(all = 16.dp),
        ) {
            Text(text = "Learn language")
            val learnLanguageState = rememberChipSelectorState(
                chips = chips,
                selectedChips = state.learnLanguage?.let { listOf(it.code) } ?: emptyList(),
            )
            ChipsSelector(
                state = learnLanguageState,
                modifier = Modifier.fillMaxWidth(),
                onChipTapped = {
                    val learnLanguage = learnLanguageState.selectedChips.firstOrNull()
                    Log.d("Learn language chip tapped, selected: $learnLanguage")
                    if (learnLanguage != null) {
                        Log.d("Learn language selected: $learnLanguage")
                        actionSender.sendAction(
                            LanguageSettingsAction.LearnLanguageChanged(
                                state.availableLanguages.first { it.code == learnLanguage },
                            ),
                        )
                    } else {
                        Log.d("Learn language deselected")
                    }
                },
            )

            Spacer(Modifier.height(16.dp))

            Text(text = "Base language")
            val baseLanguageState = rememberChipSelectorState(
                chips = chips,
                selectedChips = state.baseLanguage?.let { listOf(it.code) } ?: emptyList(),
            )
            ChipsSelector(
                state = baseLanguageState,
                modifier = Modifier.fillMaxWidth(),
                onChipTapped = {
                    val baseLanguage = baseLanguageState.selectedChips.firstOrNull()
                    Log.d("Base language chip tapped, selected: $baseLanguage")
                    if (baseLanguage != null) {
                        Log.d("Base language selected: $baseLanguage")
                        actionSender.sendAction(
                            LanguageSettingsAction.BaseLanguageChanged(
                                state.availableLanguages.first { it.code == baseLanguage },
                            ),
                        )
                    } else {
                        Log.d("Base language deselected")
                    }
                },
            )

            Spacer(Modifier.height(16.dp))

            Text(text = "Support language")
            val supportLanguageState = rememberChipSelectorState(
                chips = chips,
                selectedChips = state.supportLanguage?.let { listOf(it.code) } ?: emptyList(),
            )
            ChipsSelector(
                state = supportLanguageState,
                modifier = Modifier.fillMaxWidth(),
                onChipTapped = {
                    val supportLanguage = supportLanguageState.selectedChips.firstOrNull()
                    Log.d("Support language chip tapped, selected: $supportLanguage")
                    if (supportLanguage != null) {
                        Log.d("Support language selected: $supportLanguage")
                        actionSender.sendAction(
                            LanguageSettingsAction.SupportLanguageChanged(
                                state.availableLanguages.first { it.code == supportLanguage },
                            ),
                        )
                    } else {
                        Log.d("Support language deselected")
                    }
                },
            )

            Button(
                onClick = {
                    Log.d("Confirm tapped")
                    actionSender.sendAction(LanguageSettingsAction.ConfirmTapped)
                },
                enabled = state.isInputCorrect,
            ) {
                Text("Confirm")
            }
        }
    }
}

enum class SelectionMode(val index: Int) {
    Single(0),
    Multiple(1),
    ;

    companion object {
        fun fromIndex(index: Int) = entries.firstOrNull { it.index == index } ?: Single
    }
}

@Stable
interface ChipSelectorState {
    val chips: List<String>
    val selectedChips: List<String>

    fun onChipClick(chip: String)
    fun isSelected(chip: String): Boolean
}

class ChipSelectorStateImpl(
    override val chips: List<String>,
    selectedChips: List<String> = emptyList(),
    val mode: SelectionMode = SelectionMode.Single,
) : ChipSelectorState {
    override var selectedChips by mutableStateOf(selectedChips)

    override fun onChipClick(chip: String) {
        if (mode == SelectionMode.Single) {
            if (!selectedChips.contains(chip)) {
                selectedChips = listOf(chip)
            }
        } else {
            selectedChips = if (selectedChips.contains(chip)) {
                selectedChips - chip
            } else {
                selectedChips + chip
            }
        }
    }

    override fun isSelected(chip: String): Boolean = selectedChips.contains(chip)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChipSelectorStateImpl

        if (chips != other.chips) return false
        if (mode != other.mode) return false
        if (selectedChips != other.selectedChips) return false

        return true
    }

    override fun hashCode(): Int {
        var result = chips.hashCode()
        result = 31 * result + mode.hashCode()
        result = 31 * result + selectedChips.hashCode()
        return result
    }

    companion object {
        val saver = Saver<ChipSelectorStateImpl, List<*>>(
            save = { state ->
                buildList {
                    add(state.chips.size)
                    addAll(state.chips)
                    add(state.selectedChips.size)
                    addAll(state.selectedChips)
                    add(state.mode.index)
                }
            },
            restore = { items ->
                var index = 0
                val chipsSize = items[index++] as Int
                val chips = List(chipsSize) {
                    items[index++] as String
                }
                val selectedSize = items[index++] as Int
                val selectedChips = List(selectedSize) {
                    items[index++] as String
                }
                val mode = SelectionMode.fromIndex(items[index] as Int)
                ChipSelectorStateImpl(
                    chips = chips,
                    selectedChips = selectedChips,
                    mode = mode,
                )
            },
        )
    }
}

@Composable
fun rememberChipSelectorState(
    chips: List<String>,
    selectedChips: List<String> = emptyList(),
    mode: SelectionMode = SelectionMode.Single,
): ChipSelectorState {
    if (chips.isEmpty()) error("No chips provided")
    if (mode == SelectionMode.Single && selectedChips.size > 1) {
        error("Single choice can only have 1 pre-selected chip")
    }

    return rememberSaveable(
        saver = ChipSelectorStateImpl.saver,
    ) {
        ChipSelectorStateImpl(
            chips,
            selectedChips,
            mode,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipsSelector(
    onChipTapped: () -> Unit,
    state: ChipSelectorState,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
    ) {
        state.chips.forEach { chip ->
            FilterChip(
                selected = state.isSelected(chip),
                onClick = {
                    state.onChipClick(chip)
                    onChipTapped()
                },
                label = {
                    Text(chip)
                },
            )
        }
    }
}
