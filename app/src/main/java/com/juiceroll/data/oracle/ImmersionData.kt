package com.juiceroll.data.oracle

/**
 * Static table data for Immersion generator.
 * Reference: immersion.md tables
 */
object ImmersionData {
    /** Senses categories (first d10 determines column) */
    val senseCategories: Map<Int, String> = mapOf(
        1 to "See",
        2 to "See",
        3 to "See",
        4 to "Hear",
        5 to "Hear",
        6 to "Hear",
        7 to "Smell",
        8 to "Smell",
        9 to "Feel",
        0 to "Feel" // 10 = 0
    )

    /** See details - d10 */
    val seeDetails: List<String> = listOf(
        "Broken",    // 1
        "Colorful",  // 2
        "Discarded", // 3
        "Edible",    // 4
        "Liquid",    // 5
        "Natural",   // 6
        "Odd",       // 7
        "Round",     // 8
        "Shiny",     // 9
        "Written"    // 0/10
    )

    /** Hear details - d10 */
    val hearDetails: List<String> = listOf(
        "Dripping",   // 1
        "Fire",       // 2
        "Footsteps",  // 3
        "Growling",   // 4
        "Laughter",   // 5
        "Music",      // 6
        "Scratching", // 7
        "Silence",    // 8
        "Talking",    // 9
        "Wind"        // 0/10
    )

    /** Smell details - d10 */
    val smellDetails: List<String> = listOf(
        "Alcohol", // 1
        "Blood",   // 2
        "Smoke",   // 3
        "Cooking", // 4
        "Decay",   // 5
        "Dust",    // 6
        "Flowers", // 7
        "Leather", // 8
        "Oil",     // 9
        "Soil"     // 0/10
    )

    /** Feel details - d10 */
    val feelDetails: List<String> = listOf(
        "Cold",     // 1
        "Damp",     // 2
        "Flexible", // 3
        "Furry",    // 4
        "Rough",    // 5
        "Sharp",    // 6
        "Slippery", // 7
        "Smooth",   // 8
        "Sticky",   // 9
        "Warm"      // 0/10
    )

    /** Where? locations - d10 */
    val whereLocations: List<String> = listOf(
        "Above",            // 1
        "Behind",           // 2
        "In Front",         // 3
        "In The Air",       // 4
        "In The Distance",  // 5
        "In The Next Room", // 6
        "In The Shadows",   // 7
        "Next To You",      // 8
        "On The Ground",    // 9
        "Under"             // 0/10
    )

    /** Negative emotions - d10 */
    val negativeEmotions: List<String> = listOf(
        "Despair",    // 1
        "Panic",      // 2
        "Fear",       // 3
        "Disgust",    // 4
        "Anger",      // 5
        "Sadness",    // 6
        "Arrogance",  // 7
        "Confusion",  // 8
        "Apathy",     // 9
        "Deja Vu"     // 0/10
    )

    /** Positive emotions (opposites) - d10 */
    val positiveEmotions: List<String> = listOf(
        "Hope",         // 1
        "Relief",       // 2
        "Courage",      // 3
        "Desire",       // 4
        "Calm",         // 5
        "Joy",          // 6
        "Selflessness", // 7
        "Clarity",      // 8
        "Nostalgia",    // 9
        "Awe"           // 0/10
    )

    /** Causes - d10 */
    val causes: List<String> = listOf(
        "help is on the way",          // 1
        "it is getting closer",        // 2
        "it may be valuable",          // 3
        "of a childhood event",        // 4
        "of a recent memory",          // 5
        "the source is unknown",       // 6
        "then it is suddenly gone",    // 7
        "you recognize it",            // 8
        "you were warned about it",    // 9
        "you weren't expecting it"     // 0/10
    )
}
