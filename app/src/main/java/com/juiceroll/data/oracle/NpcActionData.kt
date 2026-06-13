package com.juiceroll.data.oracle

/**
 * Static table data for NPC Action generator.
 * Reference: npc-action.md tables
 */
object NpcActionData {
    /** Personality traits - d10 (0-9 mapped to 1-10) */
    val npcPersonalities: List<String> = listOf(
        "Cautious",      // 1
        "Curious",       // 2
        "Careless",      // 3
        "Organized",     // 4
        "Reserved",      // 5
        "Outgoing",      // 6
        "Critical",      // 7
        "Compassionate", // 8
        "Confident",     // 9
        "Sensitive"      // 0/10
    )

    /** NPC needs - d10 */
    val npcNeeds: List<String> = listOf(
        "Sustenance",   // 1
        "Shelter",      // 2
        "Recovery",     // 3
        "Security",     // 4
        "Stability",    // 5
        "Friendship",   // 6
        "Acceptance",   // 7
        "Status",       // 8
        "Recognition",  // 9
        "Fulfillment"   // 0/10
    )

    /** Motive/Topic - d10 */
    val npcMotives: List<String> = listOf(
        "History",      // 1
        "Family",       // 2
        "Experience",   // 3
        "Flaws",        // 4
        "Reputation",   // 5
        "Superiors",    // 6
        "Wealth",       // 7
        "Equipment",    // 8
        "Treasure",     // 9
        "Focus"         // 0/10
    )

    /** Actions - d10 */
    val npcActions: List<String> = listOf(
        "Ambiguous Action", // 1
        "Talks",            // 2
        "Continues",        // 3
        "Act: PC Interest", // 4
        "Next Most Logical",// 5
        "Gives Something",  // 6
        "End Encounter",    // 7
        "Act: Self Interest",// 8
        "Takes Something",  // 9
        "Enters Combat"     // 0/10
    )

    /** Combat actions - d10 */
    val npcCombatActions: List<String> = listOf(
        "Defend",      // 1
        "Shift Focus", // 2
        "Seize",       // 3
        "Intimidate",  // 4
        "Advantage",   // 5
        "Coordinate",  // 6
        "Lure",        // 7
        "Destroy",     // 8
        "Precision",   // 9
        "Power"        // 0/10
    )

    /** Focus entries that require sub-rolls (italic in the original table) */
    val npcItalicFocuses: Set<String> = setOf(
        "Monster", "Event", "Environment", "Person", "Location", "Object"
    )
}
