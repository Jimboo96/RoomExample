package com.example.roomexample.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.roomexample.R
import com.example.roomexample.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view_notes.setHasFixedSize(true)
        recycler_view_notes.layoutManager = StaggeredGridLayoutManager(3,
            StaggeredGridLayoutManager.VERTICAL)

        launch {
            val notes = NoteDatabase(requireContext()).getNoteDao().getAllNotes()
            recycler_view_notes.adapter = NotesAdapter(notes)
        }

        add_button.setOnClickListener { addButton ->
            val action = HomeFragmentDirections.actionNewNote()
            Navigation.findNavController(addButton).navigate(action)
        }
    }

}