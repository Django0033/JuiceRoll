package com.juiceroll.data.oracle

/**
 * Static data tables for the Dialog Generator preset.
 * The Dialog Grid is a 5x5 grid-based mini-game for generating NPC conversations.
 */
object DialogGeneratorData {
    /** The 5x5 Dialog Grid */
    val grid: List<List<String>> = listOf(
        // Row 0 (Past)
        listOf("Fact", "Denial", "Query", "Denial", "Action"),
        // Row 1 (Past)
        listOf("Want", "Query", "Need", "Query", "Fact"),
        // Row 2 (Present) - Center row
        listOf("Action", "Need", "Fact", "Action", "Denial"),
        // Row 3 (Present)
        listOf("Need", "Query", "Denial", "Query", "Want"),
        // Row 4 (Present)
        listOf("Query", "Support", "Query", "Support", "Need")
    )

    /** Dialog fragment descriptions for each type */
    val fragmentDescriptions: Map<String, String> = mapOf(
        "Fact" to "NPC states a fact or observation",
        "Query" to "NPC asks a question",
        "Need" to "NPC expresses a need or requirement",
        "Want" to "NPC expresses a desire or wish",
        "Action" to "NPC describes or suggests an action",
        "Denial" to "NPC denies, refuses, or disagrees",
        "Support" to "NPC offers support or agreement"
    )
}
