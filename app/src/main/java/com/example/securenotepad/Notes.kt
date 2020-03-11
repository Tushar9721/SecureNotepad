package com.example.securenotepad

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Notes(

    @PrimaryKey
    var id:Int? = null,
    var title:String? = null,
    var description:String? = null,
    var lock:Boolean? = null,
    var email:String? = null,
    var password:String? = null,
    var date:String? = null,
    var pin:Boolean? = null

) : RealmObject()