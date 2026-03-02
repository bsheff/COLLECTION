# Watch & Clock Tracker

Android application for tracking antique watch and clock collections, inspired by WatcheePro for iPad.

## Features

- **Dual collection support** — track watches and clocks separately
- **Comprehensive item details** — brand, model, serial number, year, condition, movement type, valuation
- **Photo gallery** — multiple photos per item with swipeable gallery view
- **Brand/model dictionary** — pre-populated with 25 watch brands and 15 clock brands, plus autocomplete
- **Spell-check / fuzzy match** — Levenshtein-distance autocomplete catches misspellings
- **Feature chips** — select from 20+ complications/features per item
- **Service history** — log repairs and maintenance records
- **Export** — CSV and JSON export to Downloads folder
- **Dark mode** — automatic dark/light theme support
- **Material Design 3** — navy and gold antique-inspired color scheme

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 26+
- Kotlin 1.9+

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/bsheff/COLLECTION.git
   cd COLLECTION
   ```

2. Open the project in Android Studio.

3. Sync Gradle (`File → Sync Project with Gradle Files`).

4. Run on an emulator or physical device running Android 8.0 (API 26) or later.

## Project Structure

```
app/src/main/java/com/watchclock/tracker/
├── data/
│   ├── database/          # Room database, DAOs, and seed data
│   │   ├── dao/           # BrandDao, ItemDao, PhotoDao, ServiceHistoryDao, WatchModelDao
│   │   ├── AppDatabase.kt
│   │   └── DatabaseCallback.kt   # Pre-populates brands on first launch
│   ├── model/             # Data classes: CollectionItem, Brand, Photo, ServiceHistory, etc.
│   └── repository/        # BrandRepository, ItemRepository
├── ui/
│   ├── components/        # Reusable Compose components (AutocompleteTextField, PhotoGallery, …)
│   ├── navigation/        # NavGraph and Screen routes
│   ├── screens/           # Home, List, Detail, AddEdit, Brands, Settings
│   ├── theme/             # Material 3 color scheme and typography
│   └── viewmodel/         # MVVM ViewModels for each screen
└── util/
    ├── AutocompleteHelper.kt  # Fuzzy matching via Levenshtein distance
    ├── CurrencyHelper.kt
    ├── ExportHelper.kt        # CSV and JSON export
    ├── ImageHelper.kt         # Photo compression and EXIF rotation
    └── SpellCheckHelper.kt
```

## Architecture

- **MVVM** with Jetpack Compose UI
- **Room** database for local persistence
- **Repository pattern** for data access
- **StateFlow** for reactive UI updates
- **Coil** for image loading

## Pre-populated Brands

**Watches (25):** Rolex, Omega, Patek Philippe, Audemars Piguet, Vacheron Constantin, Jaeger-LeCoultre, IWC, Cartier, Breguet, Blancpain, A. Lange & Söhne, Girard-Perregaux, Zenith, TAG Heuer, Breitling, Seiko, Citizen, Hamilton, Longines, Tissot, Bulova, Timex, Elgin, Waltham, Illinois

**Clocks (15):** Seth Thomas, Ansonia, Waterbury, New Haven, Gilbert, Howard Miller, Ridgeway, Hermle, Kieninger, Urgos, Comtoise, Morbier, Gustav Becker, Junghans, Kienzle

## Build

```bash
./gradlew assembleDebug
```
