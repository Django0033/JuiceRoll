package com.juiceroll.data.oracle

/**
 * Static table data for Interrupt/Plot Point generator.
 * Reference: interrupt-plot-point.md tables
 */
object InterruptPlotPointData {
    /** Categories (first d10 determines column) - mapped by ranges */
    val categories: Map<Int, String> = mapOf(
        1 to "Action",
        2 to "Action",
        3 to "Tension",
        4 to "Tension",
        5 to "Mystery",
        6 to "Mystery",
        7 to "Social",
        8 to "Social",
        9 to "Personal",
        0 to "Personal" // 10 = 0
    )

    /** Action events (1-2 column) - d10 */
    val actionEvents: List<String> = listOf(
        "Abduction",    // 1
        "Barrier",      // 2
        "Battle",       // 3
        "Chase",        // 4
        "Collateral",   // 5
        "Crash",        // 6
        "Culmination",  // 7
        "Distraction",  // 8
        "Harm",         // 9
        "Intensify"     // 0/10
    )

    /** Tension events (3-4 column) - d10 */
    val tensionEvents: List<String> = listOf(
        "Choice",       // 1
        "Depletion",    // 2
        "Enemy",        // 3
        "Intimidation", // 4
        "Night",        // 5
        "Public",       // 6
        "Recurrence",   // 7
        "Remote",       // 8
        "Shady",        // 9
        "Trapped"       // 0/10
    )

    /** Mystery events (5-6 column) - d10 */
    val mysteryEvents: List<String> = listOf(
        "Alternate",     // 1
        "Behavior",      // 2
        "Connected",     // 3
        "Information",   // 4
        "Intercept",     // 5
        "Lucky",         // 6
        "Reappearance",  // 7
        "Revelation",    // 8
        "Secret",        // 9
        "Source"         // 0/10
    )

    /** Social events (7-8 column) - d10 */
    val socialEvents: List<String> = listOf(
        "Agreement",      // 1
        "Gathering",      // 2
        "Government",     // 3
        "Inadequate",     // 4
        "Injustice",      // 5
        "Misbehave",      // 6
        "Outcast",        // 7
        "Outside",        // 8
        "Reinforcements", // 9
        "Savior"          // 0/10
    )

    /** Personal events (9-0 column) - d10 */
    val personalEvents: List<String> = listOf(
        "Animosity",   // 1
        "Connection",  // 2
        "Dependent",   // 3
        "Ethical",     // 4
        "Flee",        // 5
        "Friend",      // 6
        "Help",        // 7
        "Home",        // 8
        "Humiliation", // 9
        "Offer"        // 0/10
    )
}
