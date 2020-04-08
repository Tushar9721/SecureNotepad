package com.example.securenotepad

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kotlinx.android.synthetic.main.item_notes_view.view.*

class NotesAdapter(
    val context: Context,
    val result: RealmResults<Notes>
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

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