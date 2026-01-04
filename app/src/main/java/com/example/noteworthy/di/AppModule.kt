package com.example.noteworthy.di

import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import androidx.room.Room
import com.example.noteworthy.data.local.NoteDatabase
import com.example.noteworthy.ui.notes.NotesViewModel
import com.example.noteworthy.domain.repository.NoteRepository
import com.example.noteworthy.data.repository.NoteRepositoryImpl

val appModule = module {
    single { Room.databaseBuilder(get(), NoteDatabase::class.java, "notes_db").build() }
    single { get<NoteDatabase>().noteDao() }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    viewModel { NotesViewModel(get()) }
}
