package com.example.noteworthy.domain.repository

import com.example.noteworthy.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun deleteNote(note: Note)
}
