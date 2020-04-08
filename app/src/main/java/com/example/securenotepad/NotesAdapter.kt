package com.example.securenotepad

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.item_notes_view.view.*

class NotesAdapter(val context: Context, val result: RealmResults<Notes>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private lateinit var realm: Realm


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return (ViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.item_notes_view,
                parent,
                false
            )
        ))
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

                itemView.relativeNotes.setOnClickListener{
                    val intent = Intent(context,CrateNewNotes::class.java)
                    intent.putExtra("title",result[adapterPosition]!!.title)
                    intent.putExtra("description",result[adapterPosition]!!.description)
                    intent.putExtra("data","Yes")
                    context.startActivity(intent)


                }

            itemView.relativeNotes.setOnLongClickListener {



                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setMessage("Do you want to delete note: "+result[adapterPosition]!!.title)
                    .setIcon(R.drawable.ic_delete)
                    .setCancelable(false)
                    .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                            dialog, id ->
                        Realm.init(context)
                        realm = Realm.getDefaultInstance()

                        val notes = realm.where(Notes::class.java).equalTo("title", result[adapterPosition]!!.title).findAll()

                        notes.forEach { note ->

                            realm.beginTransaction()
                            note.deleteFromRealm()
                            realm.commitTransaction()
                        }

                    })

                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })

                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Delete")
                // show alert dialog
                alert.show()

                true
            }

        }

        fun bind() = with(itemView) {
            tvSetTitle.text = result[adapterPosition]!!.title
            tvSetContent.text = result[adapterPosition]!!.description

            /** setting the pinned note */
            if(result[adapterPosition]!!.pin == true){
                ivPinNoteView.visibility = View.VISIBLE
            }

            /** setting the locked note */
            if(result[adapterPosition]!!.lock == true){
                tvSetTitle.visibility = View.VISIBLE
                ivLockNoteView.visibility = View.VISIBLE
                tvSetContent.text = "This note is locked!!"
                itemView.relativeNotes.setOnClickListener {
                    val intent = Intent(context,FingerprintAuth::class.java)
                    intent.putExtra("title",result[adapterPosition]!!.title)
                    intent.putExtra("description",result[adapterPosition]!!.description)
                    intent.putExtra("password",result[adapterPosition]!!.password)
                    intent.putExtra("email",result[adapterPosition]!!.email)
                    if(result[adapterPosition]!!.fingerPrint == true){
                        intent.putExtra("finger","true")
                    }
                    else{
                        intent.putExtra("finger","false")
                    }
                    context.startActivity(intent)

                }
            }
        }

    }

}