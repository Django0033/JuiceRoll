package com.juiceroll.data.oracle

/**
 * Static table data for Object and Treasure generator.
 * Reference: object-treasure.md tables
 */
object ObjectTreasureData {
    // === TRINKET (1) ===
    val trinketQualities: List<String> = listOf(
        "Broken",      // 1
        "Damaged",     // 2
        "Worn",        // 3
        "Simple",      // 4
        "Exceptional", // 5
        "Magic"        // 6
    )

    val trinketMaterials: List<String> = listOf(
        "Wood",    // 1
        "Bone",    // 2
        "Leather", // 3
        "Silver",  // 4
        "Gold",    // 5
        "Gem"      // 6
    )

    val trinketTypes: List<String> = listOf(
        "Toy/Game",   // 1
        "Bottle",     // 2
        "Instrument", // 3
        "Charm",      // 4
        "Tool",       // 5
        "Key"         // 6
    )

    // === TREASURE (2) ===
    val treasureQualities: List<String> = listOf(
        "Dusty",  // 1
        "Worn",   // 2
        "Sturdy", // 3
        "Fine",   // 4
        "New",    // 5
        "Ornate"  // 6
    )

    val treasureContainers: List<String> = listOf(
        "None",    // 1
        "Pouch",   // 2
        "Box",     // 3
        "Satchel", // 4
        "Crate",   // 5
        "Chest"    // 6
    )

    val treasureContents: List<String> = listOf(
        "Food",         // 1
        "Art",          // 2
        "Deed",         // 3
        "Silver Coins", // 4
        "Gold Coins",   // 5
        "Gems"          // 6
    )

    // === DOCUMENT (3) ===
    val documentTypes: List<String> = listOf(
        "Song",        // 1
        "Picture",     // 2
        "Letter/Note", // 3
        "Scroll",      // 4
        "Journal",     // 5
        "Book"         // 6
    )

    val documentContents: List<String> = listOf(
        "Lewd",      // 1
        "Common",    // 2
        "Map",       // 3
        "Prophecy",  // 4
        "Arcane",    // 5
        "Forbidden"  // 6
    )

    val documentSubjects: List<String> = listOf(
        "Religion",  // 1
        "Art",       // 2
        "Science",   // 3
        "Creatures", // 4
        "History",   // 5
        "Magic"      // 6
    )

    // === ACCESSORY (4) ===
    val accessoryQualities: List<String> = listOf(
        "Ruined",  // 1
        "Crude",   // 2
        "Simple",  // 3
        "Fine",    // 4
        "Crafted", // 5
        "Magic"    // 6
    )

    val accessoryMaterials: List<String> = listOf(
        "Wood",    // 1
        "Bone",    // 2
        "Leather", // 3
        "Silver",  // 4
        "Gold",    // 5
        "Gem"      // 6
    )

    val accessoryTypes: List<String> = listOf(
        "Headpiece", // 1
        "Emblem",    // 2
        "Earring",   // 3
        "Bracelet",  // 4
        "Necklace",  // 5
        "Ring"       // 6
    )

    // === WEAPON (5) ===
    val weaponQualities: List<String> = listOf(
        "Broken",     // 1
        "Improvised", // 2
        "Rough",      // 3
        "Simple",     // 4
        "Martial",    // 5
        "Masterwork"  // 6
    )

    val weaponMaterials: List<String> = listOf(
        "Wood",       // 1
        "Bone",       // 2
        "Steel",      // 3
        "Silver",     // 4
        "Mithral",    // 5
        "Adamantine"  // 6
    )

    val weaponTypes: List<String> = listOf(
        "Axe/Hammer",     // 1
        "Halberd/Spear",  // 2
        "Sword/Dagger",   // 3
        "Staff/Wand",     // 4
        "Bow",            // 5
        "Exotic"          // 6
    )

    // === ARMOR (6) ===
    val armorQualities: List<String> = listOf(
        "Broken",   // 1
        "Crude",    // 2
        "Rough",    // 3
        "Simple",   // 4
        "Martial",  // 5
        "Masterwork" // 6
    )

    val armorMaterials: List<String> = listOf(
        "Cloth",      // 1
        "Leather",    // 2
        "Bone",       // 3
        "Steel",      // 4
        "Mithral",    // 5
        "Adamantine"  // 6
    )

    val armorTypes: List<String> = listOf(
        "Helmet",     // 1
        "Torso",      // 2
        "Arms",       // 3
        "Legs",       // 4
        "Shield",     // 5
        "Full Suit"   // 6
    )

    /** Object types for d6 roll */
    val objectTypes: List<String> = listOf(
        "Trinket",   // 1
        "Treasure",  // 2
        "Document",  // 3
        "Accessory", // 4
        "Weapon",    // 5
        "Armor"      // 6
    )

    /** Treasure categories (d6) - alias for objectTypes */
    val treasureCategories: List<String> = listOf(
        "Trinket",   // 1
        "Treasure",  // 2
        "Document",  // 3
        "Accessory", // 4
        "Weapon",    // 5
        "Armor"      // 6
    )
}
