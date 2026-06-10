package com.example.presenceregister

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
    private val today = LocalDate.now().toString()
    private var correctPin = "1234"
    private val _people = MutableStateFlow<List<Person>>(emptyList())
    val people: StateFlow<List<Person>> = _people.asStateFlow()

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
    }

    // creates a new person object inside the database if the person isn't already inside
    fun registerPerson(name: String, surname: String, idCard: String): Boolean {
        val alreadyInside = _people.value.any { it.idCard == idCard && it.isInside }
        if (alreadyInside) return false

        viewModelScope.launch {
            dao.insert(Person(
                name = name.trim(),
                surname = surname.trim(),
                idCard = idCard.trim(),
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

    fun changePin(input :String) {
        correctPin = input
    }
}