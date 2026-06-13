package com.juiceroll.data.oracle

/**
 * Static table data for Dungeon Generator.
 * Reference: Dungeon tables from juice-oracle docs
 */
object DungeonData {
    /** Next Area results - d10 */
    val dungeonAreaTypes: List<String> = listOf(
        "Passage",                       // 1
        "Small Chamber: 3 Doors",        // 2
        "Large Chamber: 3 Doors",        // 3
        "Small Chamber: 2 Doors",        // 4
        "Small Chamber: 1 Door",         // 5 (dead end!)
        "Locked Door",                   // 6
        "Known / Expected",              // 7
        "Exit / Stairs",                 // 8
        "Connection to Previous Area",   // 9
        "Passage"                        // 0/10
    )

    /** Passage details - d10 */
    val dungeonPassageTypes: List<String> = listOf(
        "Dead End",            // 1
        "Narrow Crawlspace",   // 2
        "Bridge",              // 3
        "Long",                // 4
        "Wide",                // 5
        "Expected",            // 6
        "Right Angle Turn",    // 7
        "Side Passage",        // 8
        "3-Way Intersection",  // 9
        "4-Way Intersection"   // 0/10
    )

    /** Room condition - d10 */
    val dungeonRoomConditions: List<String> = listOf(
        "Partially Collapsed",    // 1
        "Holes in Floor",         // 2
        "Flooded",                // 3
        "Ashes / Burned",         // 4
        "Damaged",                // 5
        "Expected",               // 6
        "Stripped Bare",          // 7
        "Used as Campsite",       // 8
        "Converted to Other Use", // 9
        "Pristine"                // 0/10
    )

    /** Dungeon types - d10 */
    val dungeonTypes: List<String> = listOf(
        "Catacombs",   // 1
        "Cavern",      // 2
        "Crypt",       // 3
        "Fortress",    // 4
        "Hideout",     // 5
        "Lair",        // 6
        "Mine",        // 7
        "Ruins",       // 8
        "Sanctuary",   // 9
        "Temple"       // 0/10
    )

    /** Dungeon name descriptions - d10 */
    val dungeonDescriptions: List<String> = listOf(
        "Bloodstained",  // 1
        "Chaotic",       // 2
        "Endless",       // 3
        "Fallen",        // 4
        "Forbidden",     // 5
        "Forgotten",     // 6
        "Shattered",     // 7
        "Shrouded",      // 8
        "Silent",        // 9
        "Unknown"        // 0/10
    )

    /** Dungeon name subjects - d10 */
    val dungeonSubjects: List<String> = listOf(
        "Blades",     // 1
        "Blight",     // 2
        "Darkness",   // 3
        "Fury",       // 4
        "Lies",       // 5
        "Madness",    // 6
        "Mist",       // 7
        "Prophecy",   // 8
        "Runes",      // 9
        "Terror"      // 0/10
    )

    /** Dungeon encounter types - d10 */
    val dungeonEncounterTypes: List<String> = listOf(
        "Monster",         // 1
        "Natural Hazard",  // 2
        "Challenge",       // 3
        "Immersion",       // 4
        "Safety",          // 5
        "Known",           // 6
        "Trap",            // 7
        "Feature",         // 8
        "Key",             // 9
        "Treasure"         // 0/10
    )

    /** Monster descriptors - Column 1 of Monster table (d10) */
    val dungeonMonsterDescriptors: List<String> = listOf(
        "Agile",        // 1
        "Beast",        // 2
        "Clothed",      // 3
        "Composite",    // 4
        "Decayed",      // 5
        "Elemental",    // 6
        "Inscribed",    // 7
        "Intimidating", // 8
        "Levitating",   // 9
        "Nightmarish"   // 0/10
    )

    /** Monster special abilities - Column 2 of Monster table (d10) */
    val dungeonMonsterAbilities: List<String> = listOf(
        "Climb",     // 1
        "Detect",    // 2
        "Drain",     // 3
        "Entangle",  // 4
        "Illusion",  // 5
        "Immune",    // 6
        "Magic",     // 7
        "Paralyze",  // 8
        "Pierce",    // 9
        "Ranged"     // 0/10
    )

    /** Trap actions - Column 1 of Trap table (d10) */
    val dungeonTrapActions: List<String> = listOf(
        "Ambush",    // 1
        "Collapse",  // 2
        "Divert",    // 3
        "Imitate",   // 4
        "Lure",      // 5
        "Obscure",   // 6
        "Summon",    // 7
        "Surprise",  // 8
        "Surround",  // 9
        "Trigger"    // 0/10
    )

    /** Trap subjects - Column 2 of Trap table (d10) */
    val dungeonTrapSubjects: List<String> = listOf(
        "Alarm",      // 1
        "Barrier",    // 2
        "Decay",      // 3
        "Denizen",    // 4
        "Fall",       // 5
        "Fire",       // 6
        "Light",      // 7
        "Path",       // 8
        "Poison",     // 9
        "Projectile"  // 0/10
    )

    /** Feature types - d10 */
    val dungeonFeatureTypes: List<String> = listOf(
        "Library",    // 1
        "Mural",      // 2
        "Mushrooms",  // 3
        "Prison",     // 4
        "Runes",      // 5
        "Shrine",     // 6
        "Storage",    // 7
        "Vault",      // 8
        "Well",       // 9
        "Workshop"    // 0/10
    )

    /** Trap procedure info */
    val dungeonTrapProcedure: String = """
Trap Procedure:
• Active Perception (10 min, @+): Pass = Avoid, Fail = Locate
• Passive Perception: Pass = Locate, Fail = Trigger
  - Avoid: Find and completely bypass the trap
  - Locate: Find the trap, must disarm or bypass
  - Trigger: Suffer the consequences""".trimIndent()
}
