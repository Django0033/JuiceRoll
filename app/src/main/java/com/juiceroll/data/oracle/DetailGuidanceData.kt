package com.juiceroll.data.oracle

/**
 * Guidance data for Detail modifier results.
 * Reference: Details section of juice_081425_instructions-1.md
 */
object DetailGuidanceData {
    /** Detail modifier categories. */
    enum class DetailModifierCategory {
        EMOTION, PC, THREAD, NPC, FOLLOW_UP
    }

    /** Guidance data for a specific detail modifier result. */
    data class DetailModifierGuidance(
        val result: String,
        val category: DetailModifierCategory,
        val isPositive: Boolean,
        val description: String,
        val prompt: String,
        val followUpRoll: String? = null,
        val examples: List<String>,
        val skewNote: String? = null
    )

    /** Map of detail modifier results to their guidance. */
    val detailModifierGuidance: Map<String, DetailModifierGuidance> = mapOf(
        "Negative Emotion" to DetailModifierGuidance(
            result = "Negative Emotion",
            category = DetailModifierCategory.EMOTION,
            isPositive = false,
            description = "The \"thing\" should evoke a negative emotion from your character.",
            prompt = "What negative emotion does this evoke?",
            examples = listOf(
                "A sword that is jagged, rusted, and bloodstained \u2192 Disgust",
                "A letter with a wax seal you recognize \u2192 Dread",
                "An empty room where someone should be \u2192 Worry"
            ),
            skewNote = "Use the Immersion table's \"and it causes\" column for emotion ideas."
        ),
        "Positive Emotion" to DetailModifierGuidance(
            result = "Positive Emotion",
            category = DetailModifierCategory.EMOTION,
            isPositive = true,
            description = "The \"thing\" should evoke a positive emotion from your character.",
            prompt = "What positive emotion does this evoke?",
            examples = listOf(
                "A familiar crest on a shield \u2192 Relief, recognition",
                "A hidden compartment with supplies \u2192 Joy, hope",
                "A note from an old friend \u2192 Warmth, nostalgia"
            ),
            skewNote = "Use the Immersion table's \"and it causes\" column for emotion ideas."
        ),
        "Disfavors PC" to DetailModifierGuidance(
            result = "Disfavors PC",
            category = DetailModifierCategory.PC,
            isPositive = false,
            description = "Whatever this \"thing\" is, it works against your character.",
            prompt = "How does this hurt or hinder you?",
            examples = listOf(
                "A mushroom with poisonous spores",
                "A door that's locked from the other side",
                "A witness who saw you at the scene"
            )
        ),
        "Favors PC" to DetailModifierGuidance(
            result = "Favors PC",
            category = DetailModifierCategory.PC,
            isPositive = true,
            description = "Whatever this \"thing\" is, it benefits your character.",
            prompt = "How does this help or benefit you?",
            examples = listOf(
                "A hidden passage that provides escape",
                "A document that proves your innocence",
                "A potion that's exactly what you needed"
            )
        ),
        "Disfavors Thread" to DetailModifierGuidance(
            result = "Disfavors Thread",
            category = DetailModifierCategory.THREAD,
            isPositive = false,
            description = "The \"thing\" has a detail that works against one of your active threads/quests.",
            prompt = "Which thread does this hinder? How?",
            followUpRoll = "Roll on your Thread list",
            examples = listOf(
                "A letter reveals the enemy has already fled",
                "The key you needed was destroyed in the fire",
                "Your contact has switched sides"
            )
        ),
        "Favors Thread" to DetailModifierGuidance(
            result = "Favors Thread",
            category = DetailModifierCategory.THREAD,
            isPositive = true,
            description = "The \"thing\" has a detail that advances one of your active threads/quests.",
            prompt = "Which thread does this advance? How?",
            followUpRoll = "Roll on your Thread list",
            examples = listOf(
                "A letter with a clue for finding the enemy",
                "A map showing the hidden entrance",
                "Evidence that confirms your suspicions"
            )
        ),
        "Disfavors NPC" to DetailModifierGuidance(
            result = "Disfavors NPC",
            category = DetailModifierCategory.NPC,
            isPositive = false,
            description = "The \"thing\" has a detail that works against an NPC on your Character list.",
            prompt = "Which NPC does this hurt? How?",
            followUpRoll = "Roll on your Character list",
            examples = listOf(
                "A complication that benefits your rival",
                "News that threatens an ally's position",
                "An object that reveals an NPC's secret"
            )
        ),
        "Favors NPC" to DetailModifierGuidance(
            result = "Favors NPC",
            category = DetailModifierCategory.NPC,
            isPositive = true,
            description = "The \"thing\" has a detail that benefits an NPC on your Character list.",
            prompt = "Which NPC does this help? How?",
            followUpRoll = "Roll on your Character list",
            examples = listOf(
                "Information that clears an ally's name",
                "Resources that strengthen a friend's position",
                "A development that aids someone's goals"
            )
        ),
        "History" to DetailModifierGuidance(
            result = "History",
            category = DetailModifierCategory.FOLLOW_UP,
            isPositive = true,
            description = "The \"thing\" connects to something from the past. Roll on the History table.",
            prompt = "What past event does this connect to?",
            followUpRoll = "Roll History (d10)",
            examples = listOf(
                "This symbol appeared in a previous scene",
                "You recognize this from your backstory",
                "This relates to an earlier thread"
            ),
            skewNote = "Advantage \u2192 More recent. Disadvantage \u2192 Further in the past."
        ),
        "Property" to DetailModifierGuidance(
            result = "Property",
            category = DetailModifierCategory.FOLLOW_UP,
            isPositive = true,
            description = "The \"thing\" has notable properties. Roll on the Property table.",
            prompt = "What properties does this have?",
            followUpRoll = "Roll Property (d10 + d6)",
            examples = listOf(
                "Examine the object's Age, Durability, Value...",
                "Determine the NPC's Style, Power, Rarity...",
                "Describe the location's Size, Quality, Familiarity..."
            )
        )
    )
}
