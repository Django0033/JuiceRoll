# JuiceRoll

**A digital companion for the [Juice Oracle](https://thunder9861.itch.io/juice-oracle), a solo RPG tool inspired by Mythic GME, Ironsworn, and classic tabletop systems.**

JuiceRoll brings 23 oracle generators, dice rolling, and session management to Android in a parchment-and-ink dark theme. Originally built as a [Flutter iOS/web app](example/), this native Android port uses Kotlin and Jetpack Compose with an MVVM architecture.

> Juice Oracle and all related content is licensed under [CC BY-NC-SA 4.0](LICENSE) by thunder9861.

## Features

### Core Roll Engine

| Dice Type                | Description                                                                              |
| ------------------------ | ---------------------------------------------------------------------------------------- |
| Standard (NdX)           | Roll any combination of d4, d6, d8, d10, d12, d20, d100                                  |
| Fate/Fudge               | Roll dF with +, -, and blank faces — symbolic display (+, −, ○)                          |
| Advantage / Disadvantage | Roll twice, keep higher or lower                                                         |
| Skewed d6                | Weighted dice favoring high or low results                                               |
| Ironsworn                | Action rolls (1d6 + 2d10), Progress rolls, Oracle d100, Yes/No with odds, Momentum burns |

### 23 Oracle Generators

| Category            | Generators                                                                                             |
| ------------------- | ------------------------------------------------------------------------------------------------------ |
| **Core Oracles**    | Fate Check, Expectation Check, Next Scene, Random Event, Discover Meaning, Interrupt Plot Point, Scale |
| **Character & NPC** | NPC Action, Dialog Generator, Name Generator, Extended NPC Conversation                                |
| **World Building**  | Settlement, Wilderness, Dungeon Generator, Location, Monster Encounter, Quest, Treasure                |
| **Challenge**       | Challenge (DC system), Pay the Price                                                                   |
| **Flavor**          | Details, Immersion, Abstract Icons                                                                     |

Each generator uses table lookups from 20 oracle data files and produces typed results with full serialization support.

### Session Management

- Multiple named sessions with persistent roll history
- Per-session settings (max roll limits, dice dialog preferences)
- Session import/export as JSON
- Track wilderness and dungeon state across sessions
- 50-session limit with LRU eviction, 500-roll cap per session

### User Interface

- Dark parchment-and-ink theme (Material3)
- 24 oracle buttons organized by category
- Scrollable roll history with rich result cards
- 23 modal dialogs for oracle configuration
- Splash screen with fade-in animation

## Tech Stack

| Component     | Technology                                                                                                      |
| ------------- | --------------------------------------------------------------------------------------------------------------- |
| Language      | [Kotlin](https://kotlinlang.org/) 2.2.10                                                                        |
| UI            | [Jetpack Compose](https://developer.android.com/jetpack/compose) + Material3                                    |
| Architecture  | MVVM with Repository Pattern                                                                                    |
| DI            | [Hilt](https://dagger.dev/hilt/) 2.59.2 (KSP)                                                                   |
| Persistence   | [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)                   |
| Serialization | [kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization) (polymorphic)                          |
| Navigation    | [Navigation Compose](https://developer.android.com/develop/ui/views/navigation/navigation-getting-started)      |
| Build         | [AGP](https://developer.android.com/build) 9.2.1 / Gradle 9.4.1                                                 |
| Code Quality  | [ktlint](https://github.com/pinterest/ktlint) + [Kover](https://github.com/Kotlin/kotlinx-kover) (80% coverage) |
| CI            | [GitHub Actions](.github/workflows/ci.yml)                                                                      |

## Project Structure

```
app/src/main/java/com/juiceroll/
├── JuiceRollApp.kt                    # @HiltAndroidApp entry point
├── MainActivity.kt                    # @AndroidEntryPoint with Compose setContent
│
├── di/
│   └── AppModule.kt                  # Hilt DI module
│
├── domain/
│   ├── engine/
│   │   ├── RollEngine.kt             # Core dice rolling (NdX, Fate, adv/disadv)
│   │   └── TableLookup.kt            # Generic range-based table lookup
│   └── model/
│       ├── RollResult.kt             # Sealed class hierarchy (69+ subtypes)
│       ├── ValueObjects.kt           # 27 serializable value objects
│       ├── SerializationConfig.kt    # Polymorphic JSON config
│       ├── OracleResults.kt
│       ├── RandomEventResults.kt
│       ├── NpcResults.kt
│       ├── StoryResults.kt
│       ├── WorldResults.kt
│       ├── BasicResults.kt
│       └── IronswornResults.kt
│
├── data/
│   ├── oracle/                       # 20 static oracle table files
│   │   ├── FateCheckIntensityExamples.kt
│   │   ├── MeaningData.kt
│   │   ├── RandomEventData.kt
│   │   ├── SettlementData.kt
│   │   └── ...
│   └── session/
│       ├── Session.kt               # Session data model
│       └── SessionRepository.kt      # CRUD + import/export via DataStore
│
├── generator/                        # 23 preset generators + registry
│   ├── PresetRegistry.kt            # Lazy-initialized registry
│   ├── oracle/                      # FateCheck, NextScene, RandomEvent, ...
│   ├── character/                   # NpcAction, Dialog, Name, ExtendedNpcConv
│   ├── world/                       # Settlement, Dungeon, Wilderness, ...
│   ├── challenge/                   # Challenge, PayThePrice, Details
│   └── flavor/                      # Immersion, Location, Monster, AbstractIcons
│
├── viewmodel/
│   ├── HomeViewModel.kt             # Main ViewModel orchestrating all generators
│   └── SessionViewModel.kt          # Session management
│
└── ui/
    ├── theme/                       # Material3 dark theme (Color, Type, Theme)
    ├── navigation/NavGraph.kt       # Routes + NavHost
    ├── screens/
    │   ├── splash/SplashScreen.kt   # Animated splash
    │   └── home/HomeScreen.kt       # Main screen with button grid + history
    ├── components/                   # Shared composables
    │   ├── OracleButtonGrid.kt      # 24-button categorized grid
    │   └── RollHistory.kt           # Scrollable history section
    ├── display/                      # Polymorphic result rendering
    │   ├── ResultDisplay.kt         # Exhaustive when entry point
    │   ├── OracleDisplays.kt
    │   ├── NpcDisplays.kt
    │   ├── StoryDisplays.kt
    │   ├── WorldDisplays.kt
    │   └── IronswornDisplays.kt
    └── dialogs/                      # 23 oracle dialog composables
        ├── DialogComponents.kt      # Shared dialog building blocks
        ├── DiceRollDialog.kt        # 3-mode dice roll dialog
        ├── SessionSelectorSheet.kt  # Bottom sheet for session switching
        ├── SessionDetailsDialog.kt
        ├── SessionSettingsDialog.kt
        ├── AboutDialog.kt
        └── oracle/                  # 17 oracle-specific dialogs
```

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) Ladybug or later
- JDK 17+
- Android SDK 36

### Clone and Build

```bash
git clone <repo-url>
cd JuiceRoll

# Run unit tests
./gradlew testDebugUnitTest

# Build debug APK
./gradlew assembleDebug

# Build release bundle (requires signing config)
./gradlew bundleRelease
```

### Run

Open the project in Android Studio and run on a device or emulator (API 26+).

## Architecture

The app follows a layered MVVM architecture with unidirectional data flow:

```
Compose UI  ──observes──>  ViewModel  ──calls──>  Generator / Repository
     ^                                                        |
     └────────────────── StateFlow ───────────────────────────┘
```

- **UI Layer**: Composable screens and dialogs observe `StateFlow` from ViewModels
- **ViewModel Layer**: `HomeViewModel` owns the `PresetRegistry` (23 generators) and orchestrates roll execution
- **Data Layer**: `SessionRepository` persists sessions via DataStore, generators perform table lookups against 20 oracle data files
- **Domain Layer**: Pure Kotlin — `RollEngine` and `TableLookup` have no Android dependencies

### Key Design Decisions

| Decision                                  | Rationale                                                                                                                                                          |
| ----------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Sealed class hierarchy** for RollResult | Kotlin's `when` exhaustiveness ensures all 69+ subtypes are handled in display logic — compiler-enforced completeness                                              |
| **Polymorphic kotlinx-serialization**     | Uses `classDiscriminator = "className"` to match the Flutter reference's JSON format, enabling cross-platform session portability                                  |
| **DataStore per-session keys**            | Avoids the 100KB per-key limit by storing each session independently; metadata-only list for the session selector                                                  |
| **No Google Fonts API**                   | Bundled Roboto Mono TTF + system Noto Serif — more reliable than the DownloadableFonts API, works offline                                                          |
| **Stateless generators**                  | All 23 generators accept `RollEngine` via constructor injection. The one stateful Flutter generator (DialogGenerator) was refactored to accept state as parameters |

## Tests

```bash
# Run all tests
./gradlew testDebugUnitTest

# Run with coverage
./gradlew koverHtmlReport

# Open coverage report
open app/build/reports/kover/html/index.html
```

The test suite covers:

- **Serialization round-trip**: 158 parametrized tests verifying all 69+ result subtypes encode/decode without data loss
- **Repository CRUD**: 22 tests covering session lifecycle, LRU eviction, roll caps, and import/export
- **Coverage threshold**: 80% minimum across branches, functions, lines, and statements

## Acknowledgements

- **[Juice Oracle](https://thunder9861.itch.io/juice-oracle)** by thunder9861 — the original oracle system
- **[Ironsworn](https://www.ironswornrpg.com/)** by Shawn Tomkin — solo RPG mechanics
- **[Mythic GME](https://www.mythic.wordpr.com/page14/page14.html)** by Tana Pigeon — game master emulator concepts

A port of johnkord's Juice Roll
