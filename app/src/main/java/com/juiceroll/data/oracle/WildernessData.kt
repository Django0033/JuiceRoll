package com.juiceroll.data.oracle

/**
 * Static table data for Wilderness Generator.
 * Reference: wilderness-table.md
 */
object WildernessData {
    /** Full environment list (d10, rows 1-10) */
    val wildernessEnvironments: List<String> = listOf(
        "Arctic",     // 1
        "Mountains",  // 2
        "Cavern",     // 3
        "Hills",      // 4
        "Grassland",  // 5
        "Forest",     // 6
        "Swamp",      // 7
        "Water",      // 8
        "Coast",      // 9
        "Desert"      // 10
    )

    /** Type data: modifier for weather, name, and weather skew symbol */
    val wildernessTypes: List<Map<String, Any>> = listOf(
        mapOf("modifier" to 0, "name" to "Snowy", "skew" to "-"),     // 1
        mapOf("modifier" to 2, "name" to "Rocky", "skew" to "-"),     // 2
        mapOf("modifier" to 3, "name" to "Expansive", "skew" to "0"), // 3
        mapOf("modifier" to 2, "name" to "Windy", "skew" to "-"),     // 4
        mapOf("modifier" to 4, "name" to "Scrub", "skew" to "0"),     // 5
        mapOf("modifier" to 3, "name" to "Tropical", "skew" to "0"),  // 6
        mapOf("modifier" to 1, "name" to "Dark", "skew" to "+"),      // 7
        mapOf("modifier" to 3, "name" to "Exotic", "skew" to "+"),    // 8
        mapOf("modifier" to 4, "name" to "Sandy", "skew" to "0"),     // 9
        mapOf("modifier" to 4, "name" to "Arid", "skew" to "+")       // 10
    )

    /** Encounter types (d10) */
    val wildernessEncounters: List<Map<String, Any>> = listOf(
        mapOf("name" to "Natural Hazard", "isItalic" to true),    // 1
        mapOf("name" to "Monster", "isItalic" to true),           // 2
        mapOf("name" to "Weather", "isItalic" to true),           // 3
        mapOf("name" to "Challenge", "isItalic" to true),         // 4
        mapOf("name" to "Dungeon", "isItalic" to true),           // 5
        mapOf("name" to "River/Road", "isItalic" to false),       // 6
        mapOf("name" to "Feature", "isItalic" to true),           // 7
        mapOf("name" to "Settlement/Camp", "isItalic" to true, "partialItalic" to "Settlement"),   // 8
        mapOf("name" to "Advance Plot", "isItalic" to false),     // 9
        mapOf("name" to "Destination/Lost", "isItalic" to false)  // 10
    )

    /** Weather conditions (rows 1-10) */
    val wildernessWeatherTypes: List<String> = listOf(
        "Blizzard",       // 1
        "Snow Flurries",  // 2
        "Freezing Cold",  // 3
        "Thunder Storm",  // 4
        "Heavy Rain",     // 5
        "Light Rain",     // 6
        "Heavy Clouds",   // 7
        "High Winds",     // 8
        "Clear Skies",    // 9
        "Scorching Heat"  // 10
    )

    /** Monster level formulas (modifier + advantage type) by environment */
    val wildernessMonsterFormulas: List<Map<String, Any>> = listOf(
        mapOf("modifier" to 0, "advantage" to "-"),  // 1: Arctic +0@-
        mapOf("modifier" to 0, "advantage" to "0"),  // 2: Mountains +0@0
        mapOf("modifier" to 1, "advantage" to "-"),  // 3: Cavern +1@-
        mapOf("modifier" to 1, "advantage" to "0"),  // 4: Hills +1@0
        mapOf("modifier" to 3, "advantage" to "-"),  // 5: Grassland +3@-
        mapOf("modifier" to 2, "advantage" to "0"),  // 6: Forest +2@0
        mapOf("modifier" to 3, "advantage" to "+"),  // 7: Swamp +3@+
        mapOf("modifier" to 3, "advantage" to "0"),  // 8: Water +3@0
        mapOf("modifier" to 4, "advantage" to "-"),  // 9: Coast +4@-
        mapOf("modifier" to 4, "advantage" to "+")   // 10: Desert +4@+
    )

    /** Natural hazards (d10) */
    val wildernessNaturalHazards: List<String> = listOf(
        "Creature Tracks",  // 1
        "Dust Storm",       // 2
        "Flood",            // 3
        "Fog",              // 4
        "Rockslide",        // 5
        "Unstable Ground",  // 6
        "Crevice",          // 7
        "Escarpment",       // 8
        "River Crossing",   // 9
        "Thick Plants"      // 10
    )

    /** Wilderness features (d10) */
    val wildernessFeatures: List<String> = listOf(
        "Bones",     // 1
        "Cairn",     // 2
        "Chasm",     // 3
        "Circle",    // 4
        "Spring",    // 5
        "Grave",     // 6
        "Monument",  // 7
        "Tower",     // 8
        "Tree",      // 9
        "Well"       // 10
    )
}
