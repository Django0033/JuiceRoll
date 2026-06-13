package com.juiceroll.ui.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.*

// ═══════════════════════════════════════════════════════════════
// Settlement Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun SettlementNameDisplay(
    result: SettlementNameResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = result.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun SettlementDetailDisplay(
    result: SettlementDetailResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = result.detailType, value = "${result.result} (${result.roll})")
            result.subResult?.let { DetailRow(label = "Sub", value = "$it (${result.subRoll})") }
        }
    }
}

@Composable
fun EstablishmentCountDisplay(
    result: EstablishmentCountResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Establishments", value = "${result.count}")
    }
}

@Composable
fun EstablishmentNameDisplay(
    result: EstablishmentNameResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "${result.colorEmoji} ${result.name}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun SettlementPropertiesDisplay(
    result: SettlementPropertiesResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "${result.property1Name} (${result.intensity1}) + ${result.property2Name} (${result.intensity2})",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun SimpleNpcDisplay(
    result: SimpleNpcResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Personality", value = result.personality)
            DetailRow(label = "Need", value = result.need)
            DetailRow(label = "Motive", value = result.motive)
        }
    }
}

@Composable
fun MultiEstablishmentDisplay(
    result: MultiEstablishmentResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.count} establishments",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            result.establishments.forEach { name ->
                Text(
                    text = "\u2022 $name",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
fun FullSettlementDisplay(
    result: FullSettlementResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Establishment", value = result.establishment)
            DetailRow(label = "News", value = result.news)
        }
    }
}

@Composable
fun CompleteSettlementDisplay(
    result: CompleteSettlementResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.settlementType}: ${result.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Establishments", value = result.establishmentNames.joinToString(", "))
            DetailRow(label = "News", value = result.news)
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Object Treasure & Item Creation
// ═══════════════════════════════════════════════════════════════

@Composable
fun ObjectTreasureDisplay(
    result: ObjectTreasureResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.quality} ${result.material} ${result.itemType}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Category", value = result.category)
        }
    }
}

@Composable
fun ItemCreationDisplay(
    result: ItemCreationResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "${result.property1Name} (${result.property1Intensity}) + ${result.property2Name} (${result.property2Intensity})",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Dungeon Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun DungeonNameDisplay(
    result: DungeonNameResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = result.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun DungeonAreaDisplay(
    result: DungeonAreaResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.areaType} (${result.phase})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Roll", value = "${result.chosenRoll} [${result.roll1}, ${result.roll2}]")
            if (result.isDoubles) {
                Text(
                    text = "Doubles!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
fun DungeonDetailDisplay(
    result: DungeonDetailResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = result.detailType, value = "${result.result} (${result.roll})")
    }
}

@Composable
fun FullDungeonAreaDisplay(
    result: FullDungeonAreaResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.areaType,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Condition", value = result.conditionResult ?: "none")
        }
    }
}

@Composable
fun DungeonMonsterDisplay(
    result: DungeonMonsterResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "${result.descriptor} ${result.ability}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun DungeonTrapDisplay(
    result: DungeonTrapResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "${result.action} ${result.subject}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
fun DungeonEncounterDisplay(
    result: DungeonEncounterResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Encounter", value = result.encounterType)
    }
}

@Composable
fun TwoPassAreaDisplay(
    result: TwoPassAreaResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.areaType}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Roll", value = "${result.chosenRoll} [${result.roll1}, ${result.roll2}]")
            if (result.isDoubles) {
                Text(
                    text = "Doubles! Phase change",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
fun TrapProcedureDisplay(
    result: TrapProcedureResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.trapAction} ${result.trapSubject}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
            DetailRow(label = "DC", value = "${result.dc}")
        }
    }
}

@Composable
fun LocationDisplay(
    result: LocationResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Position", value = "(${result.row}, ${result.column})")
    }
}

@Composable
fun AbstractIconDisplay(
    result: AbstractIconResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Icon", value = "Row ${result.rowLabel}, Col ${result.colLabel}")
    }
}

@Composable
fun NameDisplay(
    result: NameResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = result.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Challenge Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun FullChallengeDisplay(
    result: FullChallengeResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Physical", value = "${result.physicalSkill} (DC ${result.physicalDc}, roll ${result.physicalRoll})")
            DetailRow(label = "Mental", value = "${result.mentalSkill} (DC ${result.mentalDc}, roll ${result.mentalRoll})")
        }
    }
}

@Composable
fun DcDisplay(
    result: DcResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "DC", value = "${result.dc} (${result.method})")
            DetailRow(label = "Roll", value = "${result.roll}")
        }
    }
}

@Composable
fun QuickDcDisplay(
    result: QuickDcResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "DC", value = "${result.dc}")
            DetailRow(label = "Roll", value = "${result.rawSum} (${result.dice.joinToString(", ")})")
        }
    }
}

@Composable
fun ChallengeSkillDisplay(
    result: ChallengeSkillResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "${result.challengeType}", value = "${result.skill} (DC ${result.suggestedDc})")
    }
}

@Composable
fun PercentageChanceDisplay(
    result: PercentageChanceResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Chance", value = "${result.minPercent}-${result.maxPercent}%")
            DetailRow(label = "Roll", value = "${result.roll}")
            val success = result.roll <= result.percent
            Text(
                text = if (success) "Success!" else "Failure",
                style = MaterialTheme.typography.bodyMedium,
                color = if (success) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Immersion Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun SensoryDetailDisplay(
    result: SensoryDetailResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "You ${result.sense} something ${result.detail} ${result.where}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun EmotionalAtmosphereDisplay(
    result: EmotionalAtmosphereResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "It causes ${result.selectedEmotion} because ${result.cause}",
            style = MaterialTheme.typography.bodyMedium,
            color = if (result.isPositive) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
fun FullImmersionDisplay(
    result: FullImmersionResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            result.sensory?.let { SensoryDetailDisplay(it) }
            Spacer(modifier = Modifier.height(2.dp))
            result.emotional?.let { EmotionalAtmosphereDisplay(it) }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Wilderness Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun WildernessAreaDisplay(
    result: WildernessAreaResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "${result.typeName} ${result.environment}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun WildernessEncounterDisplay(
    result: WildernessEncounterResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.encounter,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            if (result.wasLost) {
                Text(
                    text = "Lost!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
fun WildernessWeatherDisplay(
    result: WildernessWeatherResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Weather", value = "${result.weather} (${result.weatherRow})")
    }
}

@Composable
fun WildernessDetailDisplay(
    result: WildernessDetailResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = result.detailType, value = "${result.result} (${result.roll})")
    }
}

// ═══════════════════════════════════════════════════════════════
// Monster Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun MonsterLevelDisplay(
    result: MonsterLevelResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Level", value = "${result.monsterLevel}")
    }
}

@Composable
fun MonsterEncounterDisplay(
    result: MonsterEncounterResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.monster,
                style = MaterialTheme.typography.bodyMedium,
                color = if (result.isDeadly) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Difficulty", value = result.difficulty)
        }
    }
}

@Composable
fun FullMonsterEncounterDisplay(
    result: FullMonsterEncounterResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Difficulty", value = result.difficulty)
            result.bossMonster?.let {
                Text(
                    text = "BOSS: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            result.monsters.forEach { monster ->
                Text(
                    text = "${monster.count}x ${monster.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
fun MonsterTracksDisplay(
    result: MonsterTracksResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = result.tracks,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}
