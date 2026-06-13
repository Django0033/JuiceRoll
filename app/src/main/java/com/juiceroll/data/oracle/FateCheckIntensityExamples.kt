package com.juiceroll.data.oracle

/**
 * Example consequences/prompts for each Intensity value (1-6) for Fate Check "Because" results.
 * Reference: fate-check.md
 */
object FateCheckIntensityExamples {
    /** Example consequences for each intensity value. */
    val examples: Map<Int, String> = mapOf(
        1 to "Minimal: The reason is barely relevant, a trivial detail or weak justification.",
        2 to "Minor: The reason is small, a slight influence or minor factor.",
        3 to "Mundane: The reason is ordinary, a typical or expected cause.",
        4 to "Moderate: The reason is significant, a clear and meaningful cause.",
        5 to "Major: The reason is dramatic, a strong and impactful justification.",
        6 to "Maximum: The reason is overwhelming, a total or game-changing cause."
    )

    /** Example narrative prompts for each Intensity value. */
    val prompts: Map<Int, String> = mapOf(
        1 to "Because... it barely matters. (e.g. a passing comment, a weak wind, a minor distraction)",
        2 to "Because... something small tipped the scales. (e.g. a subtle clue, a minor obstacle)",
        3 to "Because... it was the expected outcome. (e.g. routine, average, what you thought would happen)",
        4 to "Because... a clear reason stands out. (e.g. a strong clue, a direct cause, a meaningful event)",
        5 to "Because... something dramatic happened. (e.g. a major twist, a powerful force, a big reveal)",
        6 to "Because... the reason is overwhelming. (e.g. fate intervenes, a total reversal, a game-changer)"
    )
}
