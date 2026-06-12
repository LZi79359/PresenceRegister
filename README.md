<<<<<<< HEAD
﻿# Presence Register
Android app for tracking who is currently inside a building

---
## Features
 - Register people entering with their name, surname, and ID card
 - Mark people as having left
 - View who is currently inside with a live occupancy count
 - View all of today's records with entry/exit status
 - PIN-protected data screen
 - Duplicate detection - blocks re-entry if a perosn is already inside
 - Automatic daily reset - previous days' data is clerad on app startup

---
## Screens
| Screen | Description |
| -------- | -------- |
| Main | Shows current occupancy count with OUT, and Today's Data buttons |
| Registration | Form to register a person entering the building |
| Exit | List of people currently inside, with a leave button and a confirmation pop up |
| Data | PIN-protected list of all records from today with status (Inside / Out) |

## Tech Stack
| Technology | Purpose |
| -------- | -------- |
| Kotlin | Primary language |
| Jetpack Compose | UI |
| Jetpack Navigation | Screen navigation |
| Room (v2.7.1) | Local database persistence |
| ViewModel + StateFLow | State management |
| KSP | Annotation processing for Room |

## Project Structure
```
app/src/main/java/com/example/presenceregister/
│
├── MainActivity.kt         # Entry point, hosts NavGraph
├── Person.kt               # Room entity / data model
├── PersonDao.kt            # Room DAO (queries)
├── AppDatabase.kt          # Room database instance
├── PresenceViewModel.kt    # Business logic and state
│
├── MainScreen.kt           # Occupancy overview, and buttons linking to other pages
├── RegistrationScreen.kt   # Entry form
├── ExitScreen.kt           # Exit list with confirmation
└── DataScreen.kt           # PIN-gated data view
```

## How it works
1. Person is a Room `@Entity` stored in a local SQLite database (`presence_db`)
2. `PersonDao` exposes queries scoped to today's date
3. `AppDatabase` initialises and proviceds a singleton database isntance
4. `PresenceViewModel` reads from the DAO as a `Flow`, exposing it to the UI via `StateFLow`
5. Each screen collects the `StateFlow` using `collectAsState()`, recomposing automatically when data changes

## Default PIN
The default PIN is 1234. To change it, update `correctPin` in `PresenceViewModel.kt`:
``` kotlin
private val correctPin = "1234"
```
The PIN can also be changed through the application by using the `Change Pin` button
If the old pin is forgotten the pin `00000000` (8 0s) can be used instead to change to a new pin
=======
﻿# Presence Register
Android app for tracking who is currently inside a building

---
## Features
 - Register people entering with their name, surname, and ID card
 - Mark people as having left
 - View who is currently inside with a live occupancy count
 - View all of today's records with entry/exit status
 - PIN-protected data screen
 - Duplicate detection - blocks re-entry if a perosn is already inside
 - Automatic daily reset - previous days' data is clerad on app startup

---
## Screens
| Screen | Description |
| -------- | -------- |
| Main | Shows current occupancy count with OUT, and Today's Data buttons |
| Registration | Form to register a person entering the building |
| Exit | List of people currently inside, with a leave button and a confirmation pop up |
| Data | PIN-protected list of all records from today with status (Inside / Out) |

## Tech Stack
| Technology | Purpose |
| -------- | -------- |
| Kotlin | Primary language |
| Jetpack Compose | UI |
| Jetpack Navigation | Screen navigation |
| Room (v2.7.1) | Local database persistence |
| ViewModel + StateFLow | State management |
| KSP | Annotation processing for Room |

## Project Structure
```
app/src/main/java/com/example/presenceregister/
│
├── MainActivity.kt         # Entry point, hosts NavGraph
├── Person.kt               # Room entity / data model
├── PersonDao.kt            # Room DAO (queries)
├── AppDatabase.kt          # Room database instance
├── PresenceViewModel.kt    # Business logic and state
│
├── MainScreen.kt           # Occupancy overview, and buttons linking to other pages
├── RegistrationScreen.kt   # Entry form
├── ExitScreen.kt           # Exit list with confirmation
└── DataScreen.kt           # PIN-gated data view
```

## How it works
1. Person is a Room `@Entity` stored in a local SQLite database (`presence_db`)
2. `PersonDao` exposes queries scoped to today's date
3. `AppDatabase` initialises and proviceds a singleton database isntance
4. `PresenceViewModel` reads from the DAO as a `Flow`, exposing it to the UI via `StateFLow`
5. Each screen collects the `StateFlow` using `collectAsState()`, recomposing automatically when data changes

## Default PIN
The default PIN is 1234. To change it, update `correctPin` in `PresenceViewModel.kt`:
``` kotlin
private val correctPin = "1234"
```
The PIN can also be changed through the application by using the `Change Pin` button
If the old pin is forgotten the pin `00000000` (8 0s) can be used instead to change to a new pin
>>>>>>> 7d65d27f9f4fe61c0e04f94fa7bdef4886c4ef38
