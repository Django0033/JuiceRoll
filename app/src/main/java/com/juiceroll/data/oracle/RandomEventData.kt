package com.juiceroll.data.oracle

/**
 * Static table data for Random Event generator.
 * Reference: random-event-challenge.md and random-tables.md
 */
object RandomEventData {
    /** Event focus types - d10 */
    val eventFocusTypes: List<String> = listOf(
        "Advance Time",    // 1
        "Close Thread",    // 2
        "Converge Thread", // 3
        "Diverge Thread",  // 4
        "Immersion",       // 5
        "Keyed Event",     // 6
        "New Character",   // 7
        "NPC Action",      // 8
        "Plot Armor",      // 9
        "Remote Event"     // 0/10
    )

    /** Descriptions for each event focus type. */
    val eventFocusDescriptions: Map<String, String> = mapOf(
        "Advance Time" to
            "Time in-game has advanced. Day turns to night, seasons change, " +
            "guards change patrol, rituals complete. Do bookkeeping: roll weather, " +
            "check torches, eat rations. If in a settlement, roll News.",
        "Close Thread" to
            "Roll on your thread list. That thread has ended without your intervention. " +
            "Determine WHY it ended and what it means for the story, then remove it.",
        "Converge Thread" to
            "Roll on your thread list. Something moves you closer to that thread, " +
            "intertwining with your current storyline. What connection was just revealed?",
        "Diverge Thread" to
            "Roll on your thread list. Something moves you away from that thread. " +
            "If current thread: perhaps it splits into two. What obstacle or distraction appeared?",
        "Immersion" to
            "Roll on the Immersion table and incorporate the sensory details " +
            "into what is currently happening. What do you see, hear, smell, or feel?",
        "Keyed Event" to
            "Something you WANT to happen, happens! Check your Keyed Event list. " +
            "No keyed events prepared? Roll a Plot Point instead.",
        "New Character" to
            "A new NPC is present in the scene. Roll on NPC and Name tables, " +
            "add to your character list. Could be person, creature, or important item.",
        "NPC Action" to
            "Roll on your Character list. That NPC performs an action. " +
            "If not present: flashback, scene change, or default to your companion.",
        "Plot Armor" to
            "\u2728 Whatever issue you are dealing with is SOLVED. \u2728 This is your lifeline " +
            "in an unforgiving world. No follow-up needed\u2014accept this gift!",
        "Remote Event" to
            "Something happens in a far away place that you don't yet know about. " +
            "Roll Locations list or Location Grid. Add to News for next Settlement visit."
    )

    /** Suggested follow-up actions for each event focus type. */
    val eventFocusActions: Map<String, List<EventFocusAction>> = mapOf(
        "Advance Time" to listOf(
            EventFocusAction("weather", "Roll Weather", "Update conditions"),
            EventFocusAction("news", "Roll News", "If in settlement")
        ),
        "Close Thread" to listOf(
            EventFocusAction("threadList", "Roll on Thread List", "Which thread ends"),
            EventFocusAction("discoverMeaning", "Discover Meaning", "Why did it end?")
        ),
        "Converge Thread" to listOf(
            EventFocusAction("threadList", "Roll on Thread List", "Which thread converges"),
            EventFocusAction("discoverMeaning", "Discover Meaning", "How are they connected?")
        ),
        "Diverge Thread" to listOf(
            EventFocusAction("threadList", "Roll on Thread List", "Which thread diverges"),
            EventFocusAction("discoverMeaning", "Discover Meaning", "What causes the split?")
        ),
        "Immersion" to listOf(
            EventFocusAction("immersion", "Roll Immersion", "Sensory detail")
        ),
        "Keyed Event" to listOf(
            EventFocusAction("keyedEventList", "Check Keyed Events", "Your prepared events"),
            EventFocusAction("plotPoint", "Roll Plot Point", "Fallback if no keyed events")
        ),
        "New Character" to listOf(
            EventFocusAction("npc", "Generate NPC", "Roll on NPC tables"),
            EventFocusAction("name", "Generate Name", "Roll 3d20")
        ),
        "NPC Action" to listOf(
            EventFocusAction("characterList", "Roll on Character List", "Which NPC acts"),
            EventFocusAction("npcAction", "NPC Action", "What they do"),
            EventFocusAction("companion", "Use Companion", "If NPC not present")
        ),
        "Plot Armor" to emptyList(),
        "Remote Event" to listOf(
            EventFocusAction("locationList", "Roll on Location List", "Where it happens"),
            EventFocusAction("locationGrid", "Location Grid", "Alternative if no list"),
            EventFocusAction("discoverMeaning", "Discover Meaning", "What happened there?")
        )
    )

    /** Represents a suggested follow-up action for an event focus. */
    data class EventFocusAction(
        val id: String,
        val label: String,
        val hint: String
    )

    /** Guidance for interpreting Modifier + Idea results. */
    val modifierIdeaGuidance: String =
        "Use this result to alter the current scene or determine what happens next. " +
        "Interpret the pairing creatively based on your current context."

    /** Category descriptions for Idea results. */
    val ideaCategoryDescriptions: Map<String, String> = mapOf(
        "Idea" to "Abstract concepts\u2014things to think about, motivations, or intangible elements.",
        "Event" to "Something that happens\u2014plot triggers, occurrences, or changes in the story.",
        "Person" to "An NPC archetype\u2014consider rolling on the NPC tables for more detail.",
        "Object" to "A symbolic item\u2014could be literal or represent something thematically."
    )

    /** Category probabilities for reference (d10 ranges). */
    val ideaCategoryRanges: Map<String, String> = mapOf(
        "Idea" to "1-3 (30%)",
        "Event" to "4-6 (30%)",
        "Person" to "7-8 (20%)",
        "Object" to "9-0 (20%)"
    )

    /** Example keyed events from the Juice instructions. */
    val keyedEventExamples: List<String> = listOf(
        "Random Zombie Attack",
        "The BBEG appears",
        "Earthquake!",
        "The Ritual is Complete"
    )

    /** Extended guidance for specific event focuses. */
    val eventFocusExtendedGuidance: Map<String, String> = mapOf(
        "Advance Time" to
            "This is a bookkeeping prompt\u2014don't just skip it! Time passing affects the world: " +
            "weather changes, NPCs complete tasks, villains progress their plans.",
        "Close Thread" to
            "New threads will naturally form through play. It's only natural for threads " +
            "to end without your intervention as time passes. This keeps your list manageable.",
        "Converge Thread" to
            "Sometimes, seemingly unrelated storylines become more connected than you first thought. " +
            "When this is revealed, it can produce an exciting and complex plot.",
        "Diverge Thread" to
            "Bad things happen. Sometimes your character gets distracted, outside forces intervene, " +
            "or you come to a fork in the road. If current thread: maybe it splits into two threads.",
        "Immersion" to
            "Roll for a sense (See, Hear, Smell, Feel), a location (behind, in front, etc.), " +
            "and optionally \"what it causes\" from the Emotion table. " +
            "The darker emotions often present more interesting situations to overcome.",
        "Keyed Event" to
            "Think of it like a timer\u2014things you want to happen eventually. " +
            "Examples: \"Random Zombie Attack\", \"The BBEG appears\", \"Earthquake!\"",
        "New Character" to
            "Characters don't need to be people\u2014could be a sentient sword, a dragon, " +
            "or an extremely important plot-based item. NPCs make the world come alive!",
        "NPC Action" to
            "The NPC should act on their own\u2014they aren't sitting around idle. " +
            "Consider their personality, needs, and motives from the NPC tables.",
        "Plot Armor" to
            "This is rare\u2014accept the gift! The oracle leans towards challenges and setbacks. " +
            "Plot Armor is your lifeline. No need to explain it\u2014just take the win.",
        "Remote Event" to
            "The rest of the world is still progressing forward. This event may become " +
            "known or relevant later\u2014track it for future News rolls."
    )

    /** Prompt questions for each event focus. */
    val eventFocusPrompts: Map<String, String> = mapOf(
        "Advance Time" to "What has changed while time passed?",
        "Close Thread" to "Why did this thread end? What does it mean for the story?",
        "Converge Thread" to "What connection between these threads was just revealed?",
        "Diverge Thread" to "What obstacle or distraction pulls you away?",
        "Immersion" to "What sensory detail draws your attention right now?",
        "Keyed Event" to "Which prepared event happens now?",
        "New Character" to "Who or what appears in the scene?",
        "NPC Action" to "What does this character do? Why now?",
        "Plot Armor" to "How is your current problem suddenly solved?",
        "Remote Event" to "What happens far away that you don't yet know about?"
    )

    /** Modifier words - d10 */
    val modifierWords: List<String> = listOf(
        "Change",     // 1
        "Continue",   // 2
        "Decrease",   // 3
        "Extra",      // 4
        "Increase",   // 5
        "Mundane",    // 6
        "Mysterious", // 7
        "Start",      // 8
        "Stop",       // 9
        "Strange"     // 0/10
    )

    /** Idea words (1-3) - d10 */
    val ideaWords: List<String> = listOf(
        "Attention",     // 1
        "Communication", // 2
        "Danger",        // 3
        "Element",       // 4
        "Food",          // 5
        "Home",          // 6
        "Resource",      // 7
        "Rumor",         // 8
        "Secret",        // 9
        "Vow"            // 0/10
    )

    /** Event words (4-6) - d10 */
    val eventWords: List<String> = listOf(
        "Ambush",    // 1
        "Anomaly",   // 2
        "Blessing",  // 3
        "Caravan",   // 4
        "Curse",     // 5
        "Discovery", // 6
        "Escape",    // 7
        "Journey",   // 8
        "Prophecy",  // 9
        "Ritual"     // 0/10
    )

    /** Person words (7-8) - d10 */
    val personWords: List<String> = listOf(
        "Criminal",    // 1
        "Entertainer", // 2
        "Expert",      // 3
        "Mage",        // 4
        "Mercenary",   // 5
        "Noble",       // 6
        "Priest",      // 7
        "Ranger",      // 8
        "Soldier",     // 9
        "Transporter"  // 0/10
    )

    /** Object words (9-0) - d10 */
    val objectWords: List<String> = listOf(
        "Arrow",     // 1
        "Candle",    // 2
        "Cauldron",  // 3
        "Chain",     // 4
        "Claw",      // 5
        "Hook",      // 6
        "Hourglass", // 7
        "Quill",     // 8
        "Rose",      // 9
        "Skull"      // 0/10
    )
}
