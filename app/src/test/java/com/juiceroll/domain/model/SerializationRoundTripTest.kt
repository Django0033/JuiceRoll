package com.juiceroll.domain.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SerializationRoundTripTest(private val testCase: RoundTripCase) {

    data class RoundTripCase(
        val name: String,
        val result: RollResult,
    )

    companion object {
        private val json = rollResultJson

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): List<RoundTripCase> = listOf(
            // ── Basic (3) ──
            RoundTripCase("StandardRollResult", RollResult.StandardRollResult(
                values = listOf(3, 5, 2), sides = 6, total = 10,
            )),
            RoundTripCase("FateRollResult", RollResult.FateRollResult(
                values = listOf(1, -1, 0, 1), total = 1,
            )),
            RoundTripCase("InvalidExpression", RollResult.InvalidExpression(
                expression = "2d20+1",
            )),

            // ── Oracle (7) ──
            RoundTripCase("FateCheckResult", FateCheckResult(
                likelihood = "Likely",
                fateDice = listOf(1, 0),
                fateSum = 1,
                intensity = 3,
                outcome = FateCheckOutcome.YES,
            )),
            RoundTripCase("FateCheckResult with trigger", FateCheckResult(
                likelihood = "Unlikely",
                fateDice = listOf(1, -1),
                fateSum = 0,
                intensity = 5,
                outcome = FateCheckOutcome.YES_BUT,
                specialTrigger = SpecialTrigger.RANDOM_EVENT,
            )),
            RoundTripCase("ExpectationCheckResult", ExpectationCheckResult(
                fateDice = listOf(1, 0),
                fateSum = 1,
                outcome = ExpectationOutcome.EXPECTED,
            )),
            RoundTripCase("ScaleResult", ScaleResult(
                fateDice = listOf(1, 0),
                fateSum = 1,
                intensity = 3,
                total = 4,
                modifier = "+10%",
                multiplier = 1.1,
            )),
            RoundTripCase("NextSceneResult", NextSceneResult(
                fateDice = listOf(1, -1),
                fateSum = 0,
                sceneType = "normal",
            )),
            RoundTripCase("NextSceneWithFollowUpResult", NextSceneWithFollowUpResult(
                sceneType = "alterAdd",
                fateDice = listOf(1, -1),
                fateSum = 0,
                focusResult = FocusResult(roll = 5, focus = "Ally"),
            )),
            RoundTripCase("FocusResult", FocusResult(
                roll = 7, focus = "Enemy",
            )),
            RoundTripCase("DiscoverMeaningResult", DiscoverMeaningResult(
                adjectiveRoll = 10, adjective = "Powerful",
                nounRoll = 15, noun = "Memory",
            )),

            // ── Random Event (4) ──
            RoundTripCase("RandomEventResult", RandomEventResult(
                focusRoll = 5, focus = "NPC",
                modifierRoll = 3, modifier = "Change",
                ideaRoll = 7, idea = "Secret",
            )),
            RoundTripCase("RandomEventFocusResult", RandomEventFocusResult(
                focusRoll = 5, focus = "NPC",
            )),
            RoundTripCase("IdeaResult", IdeaResult(
                modifierRoll = 3, modifier = "Change",
                ideaRoll = 7, idea = "Secret",
                ideaCategory = "Idea",
            )),
            RoundTripCase("SingleTableResult", SingleTableResult(
                roll = 10, result = "Aid", tableName = "NPC Action",
            )),

            // ── NPC & Dialog (9) ──
            RoundTripCase("NpcActionResult", NpcActionResult(
                column = "action", roll = 12, result = "Attack",
            )),
            RoundTripCase("MotiveWithFollowUpResult", MotiveWithFollowUpResult(
                roll = 8, motive = "Focus",
            )),
            RoundTripCase("SimpleNpcProfileResult", SimpleNpcProfileResult(
                personalityRoll = 5, personality = "Brave",
                needRoll = 7, need = "Protection",
                motiveRoll = 3, motive = "History",
            )),
            RoundTripCase("NpcProfileResult", NpcProfileResult(
                primaryPersonalityRoll = 5, primaryPersonality = "Brave",
                secondaryPersonalityRoll = 8, secondaryPersonality = "Cautious",
                needRoll = 7, need = "Protection",
                motiveRoll = 3, motive = "History",
            )),
            RoundTripCase("DualPersonalityResult", DualPersonalityResult(
                primaryRoll = 5, primary = "Brave",
                secondaryRoll = 8, secondary = "Cautious",
            )),
            RoundTripCase("ComplexNpcResult", ComplexNpcResult(
                primaryPersonalityRoll = 5, primaryPersonality = "Brave",
                needRoll = 7, need = "Protection",
                needSkew = "primitive",
                needAllRolls = listOf(7, 3),
                motiveRoll = 3, motive = "Focus",
            )),
            RoundTripCase("InformationResult", InformationResult(
                typeRoll = 25, topicRoll = 80,
                informationType = "Rumor", topic = "Lost Treasure",
            )),
            RoundTripCase("CompanionResponseResult", CompanionResponseResult(
                roll = 65, response = "I'll help",
                skew = "none", allRolls = listOf(65),
            )),
            RoundTripCase("DialogTopicResult", DialogTopicResult(
                roll = 42, topic = "Weather",
            )),

            // ── Story (7) ──
            RoundTripCase("PayThePriceResult", PayThePriceResult(
                roll = 15, result = "You suffer a setback",
            )),
            RoundTripCase("PayThePriceResult twist", PayThePriceResult(
                isMajorTwist = true, roll = 1, result = "Major plot twist!",
            )),
            RoundTripCase("InterruptPlotPointResult", InterruptPlotPointResult(
                categoryRoll = 3, category = "Action",
                eventRoll = 7, event = "Ambush",
            )),
            RoundTripCase("QuestResult", QuestResult(
                objectiveRoll = 1, objective = "Find",
                description = "Ancient", descriptionRoll = 3,
                focusRoll = 5, focus = "Artifact",
                prepositionRoll = 2, preposition = "in",
                locationRoll = 8, location = "Temple",
            )),
            RoundTripCase("DetailResult", DetailResult(
                detailType = "Color", roll = 7, result = "Crimson",
                emoji = "🔴",
            )),
            RoundTripCase("PropertyResult", PropertyResult(
                propertyRoll = 5, property = "Strong",
                intensityRoll = 4,
            )),
            RoundTripCase("DualPropertyResult", DualPropertyResult(
                property1Roll = 5, property1Name = "Strong", intensity1 = 4,
                property2Roll = 8, property2Name = "Light", intensity2 = 2,
            )),
            RoundTripCase("DetailWithFollowUpResult", DetailWithFollowUpResult(
                detailType = "Detail", roll = 7, result = "History",
                followUpResult = "War",
            )),

            // ── World (23) ──
            RoundTripCase("SettlementNameResult", SettlementNameResult(
                prefixRoll = 3, prefix = "River",
                suffixRoll = 7, suffix = "ton",
            )),
            RoundTripCase("SettlementDetailResult", SettlementDetailResult(
                detailType = "Establishment", roll = 12, result = "Inn",
            )),
            RoundTripCase("EstablishmentCountResult", EstablishmentCountResult(
                count = 5, dice = listOf(3, 2), settlementType = "village",
            )),
            RoundTripCase("EstablishmentNameResult", EstablishmentNameResult(
                colorRoll = 4, color = "Red", shortColor = "Red",
                colorEmoji = "🔴", objectRoll = 7, `object` = "Dragon",
                name = "Red Dragon",
            )),
            RoundTripCase("SettlementPropertiesResult", SettlementPropertiesResult(
                property1Roll = 5, property1Name = "Wealthy", intensity1 = 3,
                property2Roll = 8, property2Name = "Ancient", intensity2 = 5,
            )),
            RoundTripCase("SimpleNpcResult", SimpleNpcResult(
                name = "Elena", personality = "Brave",
                need = "Protection", motive = "Defend",
            )),
            RoundTripCase("MultiEstablishmentResult", MultiEstablishmentResult(
                count = 3, establishments = listOf("Inn", "Blacksmith", "Temple"),
            )),
            RoundTripCase("FullSettlementResult", FullSettlementResult(
                name = "Riverton", establishment = "Inn", news = "Festival",
            )),
            RoundTripCase("CompleteSettlementResult", CompleteSettlementResult(
                settlementType = "village", name = "Riverton",
                establishmentNames = listOf("Inn", "Blacksmith"),
                news = "Festival",
            )),
            RoundTripCase("ObjectTreasureResult", ObjectTreasureResult(
                category = "Treasure", quality = "Fine",
                material = "Silver", itemType = "Coins",
                rolls = listOf(1, 15, 8, 3),
            )),
            RoundTripCase("ItemCreationResult", ItemCreationResult(
                property1Name = "Sharp", property1Intensity = 5,
                property2Name = "Light", property2Intensity = 2,
            )),
            RoundTripCase("DungeonNameResult", DungeonNameResult(
                typeRoll = 3, dungeonType = "Tomb",
                descriptionRoll = 7, descriptionWord = "Forgotten",
                subjectRoll = 2, subject = "King",
            )),
            RoundTripCase("DungeonAreaResult", DungeonAreaResult(
                phase = "entering", roll1 = 3, roll2 = 5, chosenRoll = 3,
                areaType = "Chamber", isDoubles = false,
            )),
            RoundTripCase("DungeonDetailResult", DungeonDetailResult(
                detailType = "Condition", roll = 7, result = "Musty",
            )),
            RoundTripCase("FullDungeonAreaResult", FullDungeonAreaResult(
                areaType = "Great Hall", conditionResult = "Crumbling",
            )),
            RoundTripCase("DungeonMonsterResult", DungeonMonsterResult(
                descriptorRoll = 5, descriptor = "Fanged",
                abilityRoll = 8, ability = "Venom",
            )),
            RoundTripCase("DungeonTrapResult", DungeonTrapResult(
                actionRoll = 4, action = "Crush",
                subjectRoll = 9, subject = "Spikes",
            )),
            RoundTripCase("DungeonEncounterResult", DungeonEncounterResult(
                encounterType = "Monster",
            )),
            RoundTripCase("TwoPassAreaResult", TwoPassAreaResult(
                roll1 = 3, roll2 = 7, chosenRoll = 3,
                areaType = "Corridor", isDoubles = false,
            )),
            RoundTripCase("TrapProcedureResult", TrapProcedureResult(
                trapAction = "Crush", trapSubject = "Spikes",
                dcRoll = 12, dcRolls = listOf(12), dc = 12,
            )),
            RoundTripCase("LocationResult", LocationResult(
                roll = 42, row = 3, column = 2,
            )),
            RoundTripCase("AbstractIconResult", AbstractIconResult(
                d10Roll = 7, d6Roll = 4, rowLabel = 7, colLabel = 4,
            )),
            RoundTripCase("NameResult", NameResult(
                rolls = listOf(5, 12, 8),
                syllables = listOf("El", "dan"),
                name = "Eldan",
            )),

            // ── Basic (16) ──
            RoundTripCase("FullChallengeResult", FullChallengeResult(
                physicalRoll = 12, physicalSkill = "Athletics", physicalDc = 15,
                mentalRoll = 8, mentalSkill = "Arcana", mentalDc = 12,
                dcMethod = "standard",
            )),
            RoundTripCase("DcResult", DcResult(
                roll = 14, dc = 15, method = "standard",
            )),
            RoundTripCase("QuickDcResult", QuickDcResult(
                dice = listOf(3, 5), rawSum = 8, dc = 14,
            )),
            RoundTripCase("ChallengeSkillResult", ChallengeSkillResult(
                challengeType = "physical", roll = 12,
                skill = "Athletics", suggestedDc = 15,
            )),
            RoundTripCase("PercentageChanceResult", PercentageChanceResult(
                roll = 45, minPercent = 1, maxPercent = 50, percent = 50,
            )),
            RoundTripCase("SensoryDetailResult", SensoryDetailResult(
                senseRoll = 3, sense = "see",
                detailRoll = 7, detail = "glimmering",
                whereRoll = 5, where = "in the shadows",
            )),
            RoundTripCase("EmotionalAtmosphereResult", EmotionalAtmosphereResult(
                emotionRoll = 4,
                negativeEmotion = "Fear", positiveEmotion = "Hope",
                selectedEmotion = "Fear", isPositive = false,
                causeRoll = 6, cause = "approaching danger",
            )),
            RoundTripCase("FullImmersionResult", FullImmersionResult(
                sensory = SensoryDetailResult(
                    senseRoll = 3, sense = "see",
                    detailRoll = 7, detail = "glimmering",
                    whereRoll = 5, where = "in the shadows",
                ),
                emotional = EmotionalAtmosphereResult(
                    emotionRoll = 4,
                    negativeEmotion = "Fear", positiveEmotion = "Hope",
                    selectedEmotion = "Fear", isPositive = false,
                    causeRoll = 6, cause = "approaching danger",
                ),
            )),
            RoundTripCase("WildernessAreaResult", WildernessAreaResult(
                envRoll = 5, environment = "Forest",
                typeRoll = 3, typeName = "Dense",
                typeModifier = 1,
            )),
            RoundTripCase("WildernessEncounterResult", WildernessEncounterResult(
                roll = 7, encounter = "Wolf pack",
            )),
            RoundTripCase("WildernessWeatherResult", WildernessWeatherResult(
                baseRoll = 3, weatherRow = 4, weather = "Rain",
            )),
            RoundTripCase("WildernessDetailResult", WildernessDetailResult(
                detailType = "Flora", roll = 5, result = "Mushrooms",
            )),
            RoundTripCase("MonsterLevelResult", MonsterLevelResult(
                baseRoll = 4, modifier = 2,
                advantageType = "@", monsterLevel = 6,
            )),
            RoundTripCase("MonsterEncounterResult", MonsterEncounterResult(
                diceResults = listOf(7), row = 6, difficulty = "medium",
                monster = "Goblin",
            )),
            RoundTripCase("FullMonsterEncounterResult", FullMonsterEncounterResult(
                diceResults = listOf(7), row = 6, difficulty = "medium",
                monsters = listOf(MonsterCountData(code = "gob", name = "Goblin", count = 3)),
            )),
            RoundTripCase("MonsterTracksResult", MonsterTracksResult(
                diceResults = listOf(5), row = 4, tracks = "Large footprints",
                modifier = 4,
            )),

            // ── Ironsworn (6) ──
            RoundTripCase("IronswornActionResult", IronswornActionResult(
                actionDie = 5, challengeDice = listOf(3, 8),
                statBonus = 2, adds = 1,
                actionScore = 8, outcome = IronswornOutcome.WEAK_HIT,
                isMatch = false,
            )),
            RoundTripCase("IronswornProgressResult", IronswornProgressResult(
                progressScore = 7, challengeDice = listOf(4, 9),
                outcome = IronswornOutcome.WEAK_HIT,
            )),
            RoundTripCase("IronswornOracleResult", IronswornOracleResult(
                oracleRoll = 65, oracleTable = "Location",
            )),
            RoundTripCase("IronswornYesNoResult", IronswornYesNoResult(
                roll = 45, odds = IronswornOdds.FIFTY_FIFTY,
                isYes = false,
            )),
            RoundTripCase("IronswornCursedOracleResult", IronswornCursedOracleResult(
                oracleRoll = 65, cursedDie = 10,
                isCursed = true,
            )),
            RoundTripCase("IronswornMomentumBurnResult", IronswornMomentumBurnResult(
                actionDie = 3, challengeDice = listOf(7, 9),
                momentumValue = 8, statBonus = 2, adds = 0,
                originalActionScore = 5, originalOutcome = IronswornOutcome.MISS,
                burnedOutcome = IronswornOutcome.WEAK_HIT,
                wasUpgraded = true,
            )),
        )
    }

    @Test
    fun roundTrip() {
        val encoded = json.encodeToString<RollResult>(testCase.result)
        assertTrue(
            "JSON should contain className discriminator for ${testCase.name}",
            encoded.contains("\"className\"")
        )
        val decoded = json.decodeFromString<RollResult>(encoded)
        assertEquals(
            "${testCase.name}: decoded type should match",
            testCase.result::class, decoded::class
        )
        assertEquals(
            "${testCase.name}: decoded timestamp should be non-zero",
            testCase.result.timestamp, decoded.timestamp
        )
    }

    @Test
    fun roundTripTypeMatch() {
        val encoded = json.encodeToString<RollResult>(testCase.result)
        val decoded = json.decodeFromString<RollResult>(encoded)
        assertEquals(
            "${testCase.name}: RollType should match",
            testCase.result.type, decoded.type
        )
    }
}

class SerializationEdgeCaseTest {

    private val json = rollResultJson

    /**
     * kotlinx.serialization throws on unknown polymorphic class discriminators
     * even with ignoreUnknownKeys=true (which only works for field-level unknowns).
     * Per-session deserialization in SessionRepositoryImpl wraps decodeFromString
     * in try-catch to handle this gracefully, producing an empty list on failure.
     * This test verifies that the throw behavior is as expected, and callers
     * are responsible for catching at the repository boundary.
     */
    @Test
    fun unknownDiscriminatorThrowsByDesign() {
        val jsonStr = """[
            {"className": "UnknownType", "type": "UNKNOWN", "description": "?", "diceResults": [], "total": 0, "timestamp": 1000}
        ]"""
        try {
            json.decodeFromString<List<RollResult>>(jsonStr)
            fail("Should have thrown for unknown discriminator")
        } catch (e: kotlinx.serialization.SerializationException) {
            // Expected — kotlinx.serialization does not skip unknown polymorphic discriminators
        }
    }

    @Test
    fun knownTypesRoundTripInList() {
        val jsonStr = """[
            {"className": "StandardRollResult", "values": [3, 5], "sides": 6, "total": 8, "dropped": [], "advantage": false, "disadvantage": false, "type": "STANDARD", "description": "Standard Roll", "diceResults": [3, 5], "timestamp": 1000, "interpretation": null},
            {"className": "FateRollResult", "values": [1, -1, 0, 1], "total": 1, "type": "FATE", "description": "Fate Roll", "diceResults": [1, -1, 0, 1], "timestamp": 3000, "interpretation": null}
        ]"""
        val decoded = json.decodeFromString<List<RollResult>>(jsonStr)
        assertEquals("Should decode 2 known types", 2, decoded.size)
        assertEquals(RollResult.StandardRollResult::class, decoded[0]::class)
        assertEquals(RollResult.FateRollResult::class, decoded[1]::class)
    }

    @Test
    fun valueObjectSerialization() {
        val data = FateDiceData(dice = listOf(1, -1))
        val jsonStr = Json.encodeToString(FateDiceData.serializer(), data)
        assertTrue("FateDiceData should serialize", jsonStr.isNotEmpty())
        val decoded = Json.decodeFromString(FateDiceData.serializer(), jsonStr)
        assertEquals(data.dice, decoded.dice)
        assertEquals(data.sum, decoded.sum)
    }

    @Test
    fun listRoundTrip() {
        val results = listOf(
            RollResult.StandardRollResult(values = listOf(3, 5), sides = 6, total = 8),
            RollResult.FateRollResult(values = listOf(1, -1, 0, 1), total = 1),
            FateCheckResult(
                likelihood = "Likely", fateDice = listOf(1, 0),
                fateSum = 1, intensity = 3,
                outcome = FateCheckOutcome.YES,
            ),
        )
        val encoded = json.encodeToString(results)
        val decoded = json.decodeFromString<List<RollResult>>(encoded)
        assertEquals("All subtypes should round-trip", results.size, decoded.size)
        for (i in results.indices) {
            assertEquals("Item $i type should match", results[i]::class, decoded[i]::class)
        }
    }
}
