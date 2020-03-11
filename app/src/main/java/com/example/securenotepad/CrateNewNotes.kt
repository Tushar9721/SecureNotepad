package com.example.securenotepad

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_crate_new_notes.*

class CrateNewNotes : AppCompatActivity(), View.OnClickListener {

    private var check: Boolean = false
    private var passLock: Boolean = false
    private lateinit var realm: Realm
    private var count: Int? = 0
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crate_new_notes)


        //creating private initialize function
        init()

    }

    private fun init() {

        // performing clickListeners on buttons
        clickListeners()
        NoteApp()
        realm = Realm.getDefaultInstance()


    }


    private fun clickListeners() {

        ivBackPage.setOnClickListener(this)
        ivSubmitNote.setOnClickListener(this)
        ivPinNote.setOnClickListener(this)
        ivLockNote.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.ivBackPage -> {
                // coming back to main activity
                finish()


            }

            R.id.ivSubmitNote -> {
                // submitting the notes to realm database
                if (edNotesDescription.text.toString().trim().isEmpty() || edNotesTitle.text.toString().trim().isEmpty()) {
                    whenClicked(p0, "Fields are empty!!")
                } else {
                    addNotesToRealm(p0)
                }

            }

            R.id.ivLockNote -> {
                // locking important notes
                if (!passLock) {
                    ivLockNote.setImageResource(R.drawable.ic_lock)
                    passLock = true
                    passwordSet(p0)

                } else {
                    ivLockNote.setImageResource(R.drawable.ic_un_lock)
                    passLock = false
                }
            }

            R.id.ivPinNote -> {
                // pinning important notes
                if (check) {
                    ivPinNote.setImageResource(R.drawable.ic_pushpin_white)
                    check = false
                    whenClicked(p0, "You have unpinned the note!!")
                } else {
                    ivPinNote.setImageResource(R.drawable.ic_pushpin)
                    check = true
                    whenClicked(p0, "You have pinned the note!!")
                }

            }

        }

    }


    private fun addNotesToRealm(p0: View) {


        try {
            realm.beginTransaction()
            val notes = Notes()
            notes.date = System.currentTimeMillis().toString()
            notes.description = edNotesDescription.text.toString().trim()
            notes.title = edNotesTitle.text.toString().trim()
            notes.id = count!! + 1
            notes.lock = passLock
            notes.pin = check
            notes.password = password

            realm.copyToRealmOrUpdate(notes)
            realm.commitTransaction()

            whenClicked(p0, "Note saved!!")
            finish()

        } catch (e: Exception) {
            Log.e("Error", e.message!!)
        }
    }


    private fun whenClicked(v: View, msg: String) {
        val snack = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
        snack.show()
    }


    private fun passwordSet(v: View) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.setContentView(R.layout.password_dialog)

        val cancelDialog = dialog.findViewById<TextView>(R.id.cancelDialog)
        val donePassword = dialog.findViewById<TextView>(R.id.donePassword)
        val edPassword = dialog.findViewById<EditText>(R.id.edPassword)

        cancelDialog.setOnClickListener {
            passLock = true
            dialog.dismiss()
        }

        donePassword.setOnClickListener {
            passLock = false
            if(!edPassword.text.toString().trim().isEmpty()) {
                password = edPassword.text.toString().trim()
                dialog.dismiss()
                whenClicked(v, "Password saved!!")
            }
            else{
                whenClicked(v, "Password field is empty!!")
            }
        }
        dialog.show()


    }

}
