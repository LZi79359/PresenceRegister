package com.example.presenceregister

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class PresenceViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).personDao()
    private val today = LocalDate.now().toString()
    private val correctPin = "1234"
    private val _people = MutableStateFlow<List<Person>>(emptyList())
    val people: StateFlow<List<Person>> = _people.asStateFlow()

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

    fun markAsOut(idCard: String) {
        viewModelScope.launch {
            dao.markAsOut(idCard, today)
        }
    }

    fun checkPin(input: String): Boolean {
        return input == correctPin
    }
}