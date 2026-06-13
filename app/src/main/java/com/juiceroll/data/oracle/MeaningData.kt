package com.juiceroll.data.oracle

/**
 * Static table data for Discover Meaning generator.
 * Reference: meaning-name-generator.md tables
 */
object MeaningData {
    /** Adjective words (column 1) - d20 */
    val adjectives: List<String> = listOf(
        "Ancient",      // 1
        "Betray",       // 2
        "Conceal",      // 3
        "Dangerous",    // 4
        "Helpful",      // 5
        "Loud",         // 6
        "Powerful",     // 7
        "Reveal",       // 8
        "Transform",    // 9
        "Unexpected",   // 10
        "Artificial",   // 11
        "Burning",      // 12
        "Communicate",  // 13
        "Deceive",      // 14
        "Dirty",        // 15
        "Disagreeable", // 16
        "Oppose",       // 17
        "Peaceful",     // 18
        "Reassuring",   // 19
        "Specialized"   // 20
    )

    /** Noun words (column 2) - d20 */
    val nouns: List<String> = listOf(
        "Burden",      // 1
        "Complexity",  // 2
        "Conflict",    // 3
        "Control",     // 4
        "Direction",   // 5
        "Happiness",   // 6
        "Memory",      // 7
        "Move",        // 8
        "Shadow",      // 9
        "Trust",       // 10
        "Assist",      // 11
        "Break",       // 12
        "Command",     // 13
        "Delay",       // 14
        "Duration",    // 15
        "Failure",     // 16
        "Fight",       // 17
        "Leave",       // 18
        "Sacrifice",   // 19
        "Threshold"    // 20
    )
}
