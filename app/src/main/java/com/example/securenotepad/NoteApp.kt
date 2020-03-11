package com.example.securenotepad

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class NoteApp : Application(){


    override fun onCreate() {
        super.onCreate()


        //initialize realm database.

        Realm.init(this)
        val configurations = RealmConfiguration.Builder().name("Notes.db").deleteRealmIfMigrationNeeded().schemaVersion(0)
            .build()

        Realm.setDefaultConfiguration(configurations)



    }
}