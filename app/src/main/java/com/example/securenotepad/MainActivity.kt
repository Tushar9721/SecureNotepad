package com.example.securenotepad

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var realm: Realm
    private lateinit var notesList: ArrayList<Notes>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creating private initialize function
        init()

    }


    private fun init() {

        // performing clickListeners on buttons
        clickListeners()
        setAdapter()
    }

    private fun setAdapter() {
        rvNotesList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        rvNotesList.adapter = NotesAdapter(this)
        rvNotesList.adapter!!.notifyDataSetChanged()
       // notesList!!.clear()

       /* notesList = ArrayList()

        val result: RealmResults<Notes> = realm.where<Notes>(Notes::class.java).findAll()

        if(notesList!!.size <= 0){



        }
        else{
            rvNotesList.adapter = NotesAdapter(this, result)
            rvNotesList.adapter!!.notifyDataSetChanged()
        }*/


    }

    private fun clickListeners() {

        flAddNotes.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.flAddNotes -> {

                // calling another activity
                val intent = Intent(this, CrateNewNotes::class.java)
                startActivity(intent)


            }
        }

    }

}
