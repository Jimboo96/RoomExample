package com.example.roomexample.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.Navigation

import com.example.roomexample.R
import com.example.roomexample.db.Note
import com.example.roomexample.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_new_note.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NewNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = NewNoteFragmentArgs.fromBundle(it).note
            title_edit_text.setText(note?.title)
            note_edit_text.setText(note?.note)
        }

        save_button.setOnClickListener { saveButton ->
            val noteTitle = title_edit_text.text.toString().trim()
            val noteBody = note_edit_text.text.toString().trim()

            if (noteTitle.isEmpty()) {
                title_edit_text.error = "Please insert a title!"
                title_edit_text.requestFocus()
                return@setOnClickListener
            }

            if (noteBody.isEmpty()) {
                note_edit_text.error = "Please fill out the note!"
                note_edit_text.requestFocus()
                return@setOnClickListener
            }

            launch {
                val newNote = Note(title = noteTitle, note = noteBody)

                if (note == null) {
                    NoteDatabase(requireContext()).getNoteDao().addNote(newNote)
                    requireContext().toast("Note saved!")
                } else {
                    newNote.id = note!!.id
                    NoteDatabase(requireContext()).getNoteDao().updateNote(newNote)
                    requireContext().toast("Note updated!")
                }

                val action = NewNoteFragmentDirections.actionSaveNote()
                Navigation.findNavController(saveButton).navigate(action)
            }
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure you want to delete the note?")
            setMessage("You can't undo this operation.")
            setPositiveButton("Yes") { _, _ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val action = NewNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> if (note != null) {
                deleteNote()
            } else {
                requireContext().toast("Cannot delete note!")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }
}