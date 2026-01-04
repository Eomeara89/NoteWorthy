package com.example.noteworthy.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.noteworthy.domain.model.Note

@Dao
interface NoteDao {
    @Insert suspend fun insert(note: Note)
    @Delete suspend fun delete(note: Note)
    @Query("SELECT * FROM notes ORDER BY timestamp DESC") fun getAllNotes(): Flow<List<Note>>
}
