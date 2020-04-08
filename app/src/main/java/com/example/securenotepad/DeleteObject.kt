package com.example.securenotepad

import android.content.Context
import io.realm.Realm

class DeleteObject {

    private lateinit var realm: Realm

     fun deleteNote(context: Context, title: String):Boolean {

        Realm.init(context)
        realm = Realm.getDefaultInstance()

        val notes = realm.where(Notes::class.java).equalTo("title", title).findAll()

        notes.forEach { note ->

            realm.beginTransaction()
            note.deleteFromRealm()
            realm.commitTransaction()
        }

         return true
    }

}