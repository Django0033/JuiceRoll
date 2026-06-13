package com.juiceroll.data.oracle

/**
 * Static data tables for the Next Scene preset.
 * Focus table used when altering scenes.
 */
object NextSceneData {
    /** Focus table - d10 (from quest.md Focus column) */
    val focuses: List<String> = listOf(
        "Enemy",       // 1
        "Monster",     // 2
        "Event",       // 3
        "Environment", // 4
        "Community",   // 5
        "Person",      // 6
        "Information", // 7
        "Location",    // 8
        "Object",      // 9
        "Ally"         // 0/10
    )
}
