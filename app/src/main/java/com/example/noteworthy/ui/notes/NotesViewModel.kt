
package com.example.noteworthy.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.noteworthy.domain.model.Note
import com.example.noteworthy.domain.repository.NoteRepository

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    val notes: StateFlow<List<Note>> = repository.getNotes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.addNote(
                Note(title = title, content = content, timestamp = System.currentTimeMillis())
            )
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
