package com.juiceroll.data.oracle

/**
 * Static table data for Pay the Price generator.
 * Reference: pay-the-price.md tables
 */
object PayThePriceData {
    /** Standard consequences - d10 */
    val consequences: List<String> = listOf(
        "Action has Unintended Effect",       // 1
        "Current Situation Worsens",          // 2
        "Delayed / Disadvantaged",            // 3
        "Forced to Act Against Intentions",   // 4
        "New Danger / Foe Revealed",          // 5
        "Person / Community Exposed to Danger", // 6
        "Separated from Person / Thing",      // 7
        "Something of Value Lost / Destroyed", // 8
        "Surprise Complication",              // 9
        "Trusted Person Betrays You"          // 0/10
    )

    /** Major plot twists (for critical failures) - d10 */
    val majorTwists: List<String> = listOf(
        "Actions Benefit Enemy",            // 1
        "Assumption Is False",              // 2
        "Dark Secret Revealed",             // 3
        "Enemy Gains New Allies",           // 4
        "Enemy Shares a Common Goal",       // 5
        "It was all a Diversion",           // 6
        "Secret Alliance Revealed",         // 7
        "Someone Returns Unexpectedly",     // 8
        "Unrelated Situations Connected",   // 9
        "You are too late"                  // 0/10
    )
}
