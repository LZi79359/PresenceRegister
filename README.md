# Presence Register
Android app for tracking who is currently inside a building

---

## Features
- Register people entering with their name, surname, and ID card
- Mark people as having left
- View who is currently inside with a live occupancy count
- View all of today's records with entry/exit status
- PIN-protected data and admin screens
- Duplicate detection — blocks re-entry if a person is already inside
- Automatic daily reset — previous days' data is cleared on app startup
- Staff list that persists across sessions
- Staff sign-in/out via PIN — automatically creates a Person entry on sign-in
- Admin session lock — PIN required on each fresh visit to the admin screen

---

## Screens
| Screen | Description |
| -------- | -------- |
| Main | Shows current occupancy count with IN, OUT, and Today's Data buttons |
| Guest Registration | Form to register a guest entering the building |
| Exit | List of people currently inside, with a leave button and confirmation pop-up |
| Data | PIN-protected list of all records from today with status (Inside / Out) |
| Staff | PIN entry for staff sign-in and sign-out, and link to Admin |
| Admin | PIN-protected screen with links to Staff List and Today's Data |
| Staff List | View, add, and remove staff members |
| Staff Registration | Form to register a new staff member with name, surname, mobile, and PIN |

---

## Tech Stack
| Technology | Purpose |
| -------- | -------- |
| Kotlin | Primary language |
| Jetpack Compose | UI |
| Jetpack Navigation | Screen navigation |
| Room (v2.7.1) | Local database persistence |
| ViewModel + StateFlow | State management |
| KSP | Annotation processing for Room |

---

## Project Structure
```
app/src/main/java/com/example/presenceregisterv2/
│
├── MainActivity.kt               # Entry point, hosts NavGraph
├── Person.kt                     # Room entity for guests and staff visits
├── PersonDao.kt                  # Room DAO for people queries
├── Staff.kt                      # Room entity for persistent staff list
├── StaffDao.kt                   # Room DAO for staff queries
├── AppDatabase.kt                # Room database instance (people + staff)
├── PresenceViewModel.kt          # Business logic and state
│
├── MainScreen.kt                 # Occupancy overview and navigation buttons
├── GuestScreen.kt                # Guest registration entry form
├── ExitScreen.kt                 # Exit list with confirmation
├── DataScreen.kt                 # PIN-gated data view
├── StaffScreen.kt                # Staff PIN sign-in/out
├── AdminScreen.kt                # PIN-gated admin hub
├── StaffListScreen.kt            # View and remove staff members
└── StaffRegistrationScreen.kt    # Form to add a new staff member
```

---

## How it works
1. `Person` is a Room `@Entity` stored in a local SQLite database (`presence_db`), scoped to today's date
2. `Staff` is a separate Room `@Entity` that persists indefinitely, keyed by PIN
3. `PersonDao` exposes queries scoped to today's date; `StaffDao` exposes full CRUD for the staff list
4. `AppDatabase` initialises and provides a singleton instance exposing both DAOs
5. `PresenceViewModel` reads from both DAOs as `Flow`, exposing state to the UI via `StateFlow`
6. Each screen collects the relevant `StateFlow` using `collectAsState()`, recomposing automatically on data changes
7. When a staff member signs in, a `Person` entry is inserted with `isStaff = true` and their PIN as the `idCard` field for tracking purposes

---

## Default PIN
The default admin PIN is `1234`. It can be changed through the app using the Change PIN button on the admin login dialog.

If the PIN is forgotten, the master PIN `00000000` (eight zeros) can be used in its place to set a new one.

To change the default in code, update `correctPin` in `PresenceViewModel.kt`:
```kotlin
private var correctPin = "1234"
```

---

## Admin Session
The admin screen requires a PIN on each fresh visit. The session stays active while navigating to child screens (Staff List, Today's Data) and locks automatically when fully exiting the admin section.
