package com.juiceroll.data.oracle

/**
 * Static table data for Monster Encounter generator.
 * Reference: Monster tables from wilderness exploration
 */
object MonsterEncounterData {
    /** Monster table rows (index 0-11) */
    val monsterTable: List<List<String>> = listOf(
        // Row 1 (index 0)
        listOf("+ Wolf", "- Ice Mephit", "- Winter Wolf", "Yeti", "Werebear"),
        // Row 2 (index 1)
        listOf("+ Skeleton", "- Warhorse S", "- Wight", "- Nightmare", "Wraith"),
        // Row 3 (index 2)
        listOf("+ Drow", "- G Spider", "- Quaggoth", "- Phase Spider", "Drider"),
        // Row 4 (index 3)
        listOf("+ Goblin", "- Worg", "+ Hobgoblin", "+ Bugbear", "Hob C"),
        // Row 5 (index 4)
        listOf("Orc", "- Orog", "Orc EoG", "- Troll", "Orc WC"),
        // Row 6* (index 5)
        listOf("Kobold", "+ G Weasel", "+ W Kobold", "+ Stirge", "Y Dragon"),
        // Row 7 (index 6)
        listOf("Lizardfolk", "G Lizard", "L Shaman", "- G Crocodile", "L King"),
        // Row 8 (index 7)
        listOf("+ Zombie", "Ghoul", "- Mummy", "Ogre Z", "V Spawn"),
        // Row 9 (index 8)
        listOf("Yuan-ti PB", "- Cockatrice", "- Yuan-ti M", "Basilisk", "Medusa"),
        // Row 0 (index 9)
        listOf("Gnoll", "- G Hyena", "Gnoll PL", "+ Jackalwere", "Lamia"),
        // Row * (index 10)
        listOf("+ T Blight", "+ N Blight", "+ V Blight", "- S Mound", "G Hag"),
        // Row ** (index 11)
        listOf("+ Bandit", "Thug", "Scout", "- Veteran", "Bandit C")
    )

    /** Full monster names for display */
    val monsterFullNames: Map<String, String> = mapOf(
        "+ Wolf" to "Wolf (\u00BD CR)",
        "- Ice Mephit" to "Ice Mephit (2\u00D7 CR)",
        "- Winter Wolf" to "Winter Wolf (2\u00D7 CR)",
        "Yeti" to "Yeti",
        "Werebear" to "Werebear",
        "+ Skeleton" to "Skeleton (\u00BD CR)",
        "- Warhorse S" to "Warhorse Skeleton (2\u00D7 CR)",
        "- Wight" to "Wight (2\u00D7 CR)",
        "- Nightmare" to "Nightmare (2\u00D7 CR)",
        "Wraith" to "Wraith",
        "+ Drow" to "Drow (\u00BD CR)",
        "- G Spider" to "Giant Spider (2\u00D7 CR)",
        "- Quaggoth" to "Quaggoth (2\u00D7 CR)",
        "- Phase Spider" to "Phase Spider (2\u00D7 CR)",
        "Drider" to "Drider",
        "+ Goblin" to "Goblin (\u00BD CR)",
        "- Worg" to "Worg (2\u00D7 CR)",
        "+ Hobgoblin" to "Hobgoblin (\u00BD CR)",
        "+ Bugbear" to "Bugbear (\u00BD CR)",
        "Hob C" to "Hobgoblin Captain",
        "Orc" to "Orc",
        "- Orog" to "Orog (2\u00D7 CR)",
        "Orc EoG" to "Orc Eye of Gruumsh",
        "- Troll" to "Troll (2\u00D7 CR)",
        "Orc WC" to "Orc War Chief",
        "Kobold" to "Kobold",
        "+ G Weasel" to "Giant Weasel (\u00BD CR)",
        "+ W Kobold" to "Winged Kobold (\u00BD CR)",
        "+ Stirge" to "Stirge (\u00BD CR)",
        "Y Dragon" to "Young Dragon",
        "Lizardfolk" to "Lizardfolk",
        "G Lizard" to "Giant Lizard",
        "L Shaman" to "Lizardfolk Shaman",
        "- G Crocodile" to "Giant Crocodile (2\u00D7 CR)",
        "L King" to "Lizard King",
        "+ Zombie" to "Zombie (\u00BD CR)",
        "Ghoul" to "Ghoul",
        "- Mummy" to "Mummy (2\u00D7 CR)",
        "Ogre Z" to "Ogre Zombie",
        "V Spawn" to "Vampire Spawn",
        "Yuan-ti PB" to "Yuan-ti Pureblood",
        "- Cockatrice" to "Cockatrice (2\u00D7 CR)",
        "- Yuan-ti M" to "Yuan-ti Malison (2\u00D7 CR)",
        "Basilisk" to "Basilisk",
        "Medusa" to "Medusa",
        "Gnoll" to "Gnoll",
        "- G Hyena" to "Giant Hyena (2\u00D7 CR)",
        "Gnoll PL" to "Gnoll Pack Lord",
        "+ Jackalwere" to "Jackalwere (\u00BD CR)",
        "Lamia" to "Lamia",
        "+ T Blight" to "Twig Blight (\u00BD CR)",
        "+ N Blight" to "Needle Blight (\u00BD CR)",
        "+ V Blight" to "Vine Blight (\u00BD CR)",
        "- S Mound" to "Shambling Mound (2\u00D7 CR)",
        "G Hag" to "Green Hag",
        "+ Bandit" to "Bandit (\u00BD CR)",
        "Thug" to "Thug",
        "Scout" to "Scout",
        "- Veteran" to "Veteran (2\u00D7 CR)",
        "Bandit C" to "Bandit Captain"
    )

    /** Environment-based monster formulas */
    val environmentFormulas: List<Map<String, Any>> = listOf(
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

    /** Environment names for display */
    val environmentNames: List<String> = listOf(
        "Arctic", "Mountains", "Cavern", "Hills", "Grassland",
        "Forest", "Swamp", "Water", "Coast", "Desert"
    )
}
