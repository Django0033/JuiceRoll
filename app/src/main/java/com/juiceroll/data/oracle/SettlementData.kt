package com.juiceroll.data.oracle

/**
 * Static table data for Settlement generator.
 * Reference: settlement.md tables
 */
object SettlementData {
    /** Name prefixes - d10 */
    val settlementNamePrefixes: List<String> = listOf(
        "Frost",  // 1
        "High",   // 2
        "Long",   // 3
        "Lost",   // 4
        "Raven",  // 5
        "Shield", // 6
        "Storm",  // 7
        "Sword",  // 8
        "Thorn",  // 9
        "Wolf"    // 0/10
    )

    /** Name suffixes - d10 */
    val settlementNameSuffixes: List<String> = listOf(
        "Barrow", // 1
        "Brook",  // 2
        "Fall",   // 3
        "Haven",  // 4
        "Ridge",  // 5
        "River",  // 6
        "Rock",   // 7
        "Stead",  // 8
        "Stone",  // 9
        "Wood"    // 0/10
    )

    /** Establishments - d10 (d6 for villages, d10 for cities) */
    val settlementEstablishments: List<String> = listOf(
        "Stable",        // 1
        "Tavern",        // 2
        "Inn",           // 3
        "Entertainment", // 4
        "General Store", // 5
        "Artisan",       // 6 (roll on artisan table)
        "Courier",       // 7
        "Temple",        // 8
        "Guild Hall",    // 9
        "Magic Shop"     // 0/10
    )

    /** Establishment descriptions for display. */
    val settlementEstablishmentDescriptions: Map<String, String> = mapOf(
        "Stable" to "Rent/buy horses, pay for transportation to another area.",
        "Tavern" to "Food, drink, stories, rumors. Great for NPC info and side quests.",
        "Inn" to "Spend the night and rest safely. Sometimes combined with Tavern.",
        "Entertainment" to "Market, bath house, casino, brothel, etc.",
        "General Store" to "Basics and common items. Stock up on rations and torches.",
        "Artisan" to "Specialist craftsperson. Better quality, repairs, custom orders.",
        "Courier" to "Send messages, money, packages. Receive news from other settlements.",
        "Temple" to "Pray, receive blessings, remove curses. Library access for history.",
        "Guild Hall" to "Quest distribution, guild services. May offer food and lodging.",
        "Magic Shop" to "Potions, arcane books, dark secrets, trinkets, artificers."
    )

    /** Artisans - d10 */
    val settlementArtisans: List<String> = listOf(
        "Artist",     // 1
        "Baker",      // 2
        "Tailor",     // 3
        "Tanner",     // 4
        "Archer",     // 5
        "Blacksmith", // 6
        "Carpenter",  // 7
        "Apothecary", // 8
        "Jeweler",    // 9
        "Scribe"      // 0/10
    )

    /** Artisan descriptions. */
    val settlementArtisanDescriptions: Map<String, String> = mapOf(
        "Artist" to "Painter, calligrapher, cartologist (maps), glassblower.",
        "Baker" to "Delicious meals, breads, rations.",
        "Tailor" to "Clothing, costumes, light armor.",
        "Tanner" to "Leather armor (medium), accessories, saddles.",
        "Archer" to "Bows, bowstrings, arrows, quivers.",
        "Blacksmith" to "Weapons, heavy armor, metal accessories.",
        "Carpenter" to "Wagons, structures, furniture, wood items.",
        "Apothecary" to "Medicine, herbs, pharmacy. Knowledge of flora.",
        "Jeweler" to "Gems, appraisal, cutting, magic infusion, engravings.",
        "Scribe" to "Formal letters, magical scrolls, legal documents, forgery."
    )

    /** News/Events - d10 */
    val settlementNews: List<String> = listOf(
        "War",              // 1
        "Sickness",         // 2
        "Natural Disaster", // 3
        "Crime",            // 4
        "Succession",       // 5
        "Remote Event",     // 6
        "Arrival",          // 7
        "Mail",             // 8
        "Sale",             // 9
        "Celebration"       // 0/10
    )

    /** News descriptions. */
    val settlementNewsDescriptions: Map<String, String> = mapOf(
        "War" to "Battle, civil war, trade war, gang rivalry, shop competition, debate.",
        "Sickness" to "Plague, celebrity illness, crop fungus, dying trees.",
        "Natural Disaster" to "Fire, earthquake, flood, tornado.",
        "Crime" to "Assassination, theft, racketeering, smuggling.",
        "Succession" to "Death, term ended, coming of age, election, retirement.",
        "Remote Event" to "News from far away. Update on a previous Remote Event.",
        "Arrival" to "Someone/something is coming. King? Army? Music group? Adventurers?",
        "Mail" to "You've got mail! Letter or package. Good or bad news?",
        "Sale" to "Shop or market sale today. Act quick for discount!",
        "Celebration" to "Festival or event. Holiday? Birthday? Anniversary?"
    )
}
