package com.example.securenotepad

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_crate_new_notes.*

class CrateNewNotes : AppCompatActivity(), View.OnClickListener {

    private var check: Boolean = false
    private var fingerPrint: Boolean = false
    private var passLock: Boolean = false
    private lateinit var realm: Realm
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
                passwordSet(p0)
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
            val nextId: Long = realm.where(Notes::class.java).count() + 1
            val notes = realm.createObject(Notes::class.java, nextId)
            notes.date = System.currentTimeMillis().toString()
            notes.description = edNotesDescription.text.toString().trim()
            notes.title = edNotesTitle.text.toString().trim()
            notes.lock = passLock
            notes.pin = check
            notes.fingerPrint = fingerPrint
            notes.password = password

            realm.copyToRealmOrUpdate(notes)
            realm.commitTransaction()
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
            finish()

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.e("Error", e.message!!)
        }
    }


    private fun whenClicked(v: View, msg: String) {
        val snack = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
        snack.show()
    }


    private fun passwordSet(v: View) {

        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.password_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)
        val cancelDialog = dialog.findViewById<ImageView>(R.id.cancelDialog)
        val donePassword = dialog.findViewById<TextView>(R.id.donePassword)
        val edPassword = dialog.findViewById<EditText>(R.id.edPassword)
        val checkBox = dialog.findViewById<CheckBox>(R.id.checkBox)


        donePassword.setOnClickListener {
            passLock = false
            fingerPrint = checkBox.isChecked

            if (edPassword.text.toString().trim().isNotEmpty()) {
                password = edPassword.text.toString().trim()
                dialog.dismiss()
                passLock = true
                whenClicked(v, "Password saved!!")
            } else {
                Toast.makeText(this,"Password field is empty!!",Toast.LENGTH_SHORT).show()
            }
        }




        dialog.show()


        cancelDialog.setOnClickListener {
            dialog.dismiss()
        }


    }

}
