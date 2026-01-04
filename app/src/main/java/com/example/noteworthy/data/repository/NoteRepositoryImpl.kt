package com.example.noteworthy.data.repository

import com.example.noteworthy.domain.model.Note
import com.example.noteworthy.domain.repository.NoteRepository
import com.example.noteworthy.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> = dao.getAllNotes()
    override suspend fun addNote(note: Note) = dao.insert(note)
    override suspend fun deleteNote(note: Note) = dao.delete(note)
}
