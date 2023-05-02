package com.example.recycler_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recycler_view.Person.GENDER


class ListAdapterWithRecycleView(
    private val context: Context,
    private val personList: MutableList<Person> = mutableListOf()
) :
    RecyclerView.Adapter<ListAdapterWithRecycleView.PersonViewHolder>() {
    interface PersonModifier {
        fun onPersonSelected(position: Int)
        fun onPersonDeleted(position: Int)
    }

    private var personModifier: PersonModifier? = null

    fun setPersonModifier(personModifier: PersonModifier?) {
        this.personModifier = personModifier
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_person_row_item, parent, false)
        val personViewHolder = PersonViewHolder(view)
        view.setOnLongClickListener {
            val position = personViewHolder.adapterPosition
            Toast.makeText(context, "Item at position $position deleted", Toast.LENGTH_SHORT).show()
            personList.removeAt(position)
            notifyDataSetChanged()
            if (personModifier != null) {
                personModifier!!.onPersonDeleted(position)
            }
            true
        }
        view.setOnClickListener {
            if (personModifier != null) {
                personModifier!!.onPersonSelected(personViewHolder.adapterPosition)
            }
        }
        Log.i(TAG, "onCreateViewHolder invoked")
        return personViewHolder
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
        holder.textViewName.text = person.name
        holder.textViewLastName.text = person.lastName
        holder.textViewNationality.text = person.nationality
        holder.textViewGender.text = if (person.gender === GENDER.MALE) "Male" else "Female"
        holder.textViewGender.isEnabled = true
        holder.textViewGender.setOnClickListener {
            val gender = personList[holder.adapterPosition].gender
            if (gender === GENDER.MALE) {
                personList[holder.adapterPosition].gender=GENDER.FEMALE
            } else if (gender === GENDER.FEMALE) {
                personList[holder.adapterPosition].gender=GENDER.MALE
            }
            notifyItemChanged(holder.adapterPosition)
        }
        Log.i(
            TAG,
            "onBindViewHolder invoked: $position"
        )
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    inner class PersonViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var textViewName: TextView
        var textViewLastName: TextView
        var textViewGender: TextView
        var textViewNationality: TextView

        init {
            textViewName = view.findViewById<View>(R.id.textViewName) as TextView
            textViewLastName = view.findViewById<View>(R.id.textViewLastName) as TextView
            textViewGender = view.findViewById<View>(R.id.textViewGender) as TextView
            textViewNationality = view.findViewById<View>(R.id.textViewNationality) as TextView
        }
    }

    companion object {
        private val TAG = ListAdapterWithRecycleView::class.java.simpleName
    }

}


