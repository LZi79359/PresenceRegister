package com.example.presenceregisterv2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

// functions used by the entire program

class PresenceViewModel(application: Application) : AndroidViewModel(application) {

    // Initialisation of data
    private val dao = AppDatabase.getInstance(application).personDao()
    private val staffDao = AppDatabase.getInstance(application).staffDao()
    private val today = LocalDate.now().toString()
    private var correctPin = "1234"
    private var masterPin = "00000000"
    private val _people = MutableStateFlow<List<Person>>(emptyList())
    private val _staff = MutableStateFlow<List<Staff>>(emptyList())
    val people: StateFlow<List<Person>> = _people.asStateFlow()
    val staff: StateFlow<List<Staff>> = _staff.asStateFlow()
    private val _highContrast = MutableStateFlow(false)
    val highContrast: StateFlow<Boolean> = _highContrast.asStateFlow()

    private val _adminUnlocked = MutableStateFlow(false)
    val adminUnlocked: StateFlow<Boolean> = _adminUnlocked.asStateFlow()

    fun unlockAdmin() { _adminUnlocked.value = true }
    fun lockAdmin() { _adminUnlocked.value = false }

    fun toggleHighContrast() {
        _highContrast.value = !_highContrast.value
    }

    // ensures that only today's data is saved in the database
    init {
        // Delete old days' data on startup
        viewModelScope.launch {
            dao.deletePreviousDays(today)
        }

        // Observe today's people from DB
        viewModelScope.launch {
            dao.getPeopleForDate(today).collect { list ->
                _people.value = list
            }
        }

        viewModelScope.launch {
            staffDao.getAllStaff().collect { list ->
                _staff.value = list
            }
        }
    }

    // creates a new person object inside the database if the person isn't already inside
    fun registerPerson(
        name: String,
        surname: String,
        idCard: String,
        mobileNumber: String,
        isStaff: Boolean
    ): Boolean {
        val alreadyInside = _people.value.any { it.idCard == idCard && it.isInside }
        if (alreadyInside) return false

        viewModelScope.launch {
            dao.insert(Person(
                name = name.trim(),
                surname = surname.trim(),
                idCard = idCard.trim(),
                mobileNumber = mobileNumber.trim(),
                isStaff = isStaff,
                date = today
            ))
        }
        return true
    }

    // changes the given person's status as OUT
    fun markAsOut(idCard: String) {
        viewModelScope.launch {
            dao.markAsOut(idCard, today)
        }
    }

    // checks if the pin matches
    fun checkPin(input: String): Boolean {
        return input == correctPin
    }

    fun checkPinForChange(input: String):Boolean {
        return input == correctPin || input == masterPin
    }

    fun changePin(input :String) {
        correctPin = input
    }

    fun signInStaff(pin: Int): Boolean {
        val alreadyInside = _people.value.any {it.idCard == pin.toString() && it.isInside }
        if (alreadyInside) return false

        val staffMember = _staff.value.find { it.pin == pin } ?: return false

        viewModelScope.launch {
            dao.insert(
                Person(
                    name = staffMember.name,
                    surname = staffMember.surname,
                    idCard = pin.toString(),
                    mobileNumber = staffMember.mobileNumber,
                    isStaff = true,
                    date = today
                )
            )
        }
        return true
    }

    fun registerStaff(
        pin: Int,
        name: String,
        surname: String,
        mobileNumber: String,
    ): Boolean {
        val alreadyExists = _staff.value.any { it.pin == pin }
        if (alreadyExists) return false


        viewModelScope.launch {
            staffDao.insert(Staff(
                pin = pin,
                name = name.trim(),
                surname = surname.trim(),
                mobileNumber = mobileNumber.trim()
            ))
        }
        return true
    }

    fun removeStaff(staff: Staff) {
        viewModelScope.launch {
            staffDao.delete(staff)
        }
    }
}
