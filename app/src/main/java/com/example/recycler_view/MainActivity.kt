package com.example.recycler_view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity(), ListAdapterWithRecycleView.PersonModifier {
    private var recyclerView: RecyclerView? = null
    private var appUtility: AppUtility? = null
    private var listAdapterWithRecycleView: ListAdapterWithRecycleView? = null
    private var editTextFirstName: EditText? = null
    private var editTextLastName: EditText? = null
    private var spinnerNationality: Spinner? = null
    private var radioGroup: RadioGroup? = null
    private var buttonAdd: Button? = null
    private var people: MutableList<Person> = mutableListOf()
    private var modificationIndex = -1
    private var firstName: String? = null
    private var lastName: String? = null
    var nationality: String? = null
    private var gender: Person.GENDER? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appUtility = AppUtility.getAppUtility(applicationContext)
        initPersonInputForm()
        recyclerView = findViewById<View>(R.id.recycleListView) as RecyclerView
        people = appUtility?.getPeople() as MutableList<Person>
        listAdapterWithRecycleView = ListAdapterWithRecycleView(this, people)
        listAdapterWithRecycleView?.setPersonModifier(this)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
        LinearLayoutManager(this)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = listAdapterWithRecycleView
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun initPersonInputForm() {
        editTextFirstName = findViewById<View>(R.id.editTextFirstName) as EditText
        editTextLastName = findViewById<View>(R.id.editTextLastName) as EditText
        radioGroup = findViewById<View>(R.id.radioGroupGender) as RadioGroup
        spinnerNationality = findViewById<View>(R.id.spinnerNationality) as Spinner
        spinnerNationality!!.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                appUtility!!.uniqueNationalitiesArray
            )
        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
        buttonAdd!!.tag = "Add"
        radioGroup!!.setOnCheckedChangeListener { _, i ->
            gender = when (i) {
                R.id.radioButtonMale -> Person.GENDER.MALE
                R.id.radioButtonFemale -> Person.GENDER.FEMALE
                else -> null
            }
        }
        spinnerNationality!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                nationality = resources.getStringArray(R.array.nationalities)[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        buttonAdd!!.setOnClickListener {
            firstName = editTextFirstName!!.text.toString()
            lastName = editTextLastName!!.text.toString()
            var person: Person? = null
            if (isInputDataValid) {
                person = Person(firstName, lastName, gender, nationality)
            } else {
                Toast.makeText(this@MainActivity, "Input Invalid", Toast.LENGTH_LONG).show()
            }
            val behaviour = buttonAdd!!.tag as String
            if (behaviour.equals("Add", ignoreCase = true)) {
                if (person != null) {
                    people.add(person)
                    listAdapterWithRecycleView?.notifyDataSetChanged()
                    recyclerView!!.scrollToPosition(people.size - 1)
                    clearInputForm()
                }
            } else if (behaviour.equals("modify", ignoreCase = true)) {
                if (person != null) {
                    try {
                        people[modificationIndex].name =person.name
                        people[modificationIndex].lastName=person.lastName
                        people[modificationIndex].gender=person.gender
                        people[modificationIndex].nationality=spinnerNationality!!.selectedItem as String
                        listAdapterWithRecycleView?.notifyItemChanged(modificationIndex)
                        clearInputForm()
                        buttonAdd!!.tag = "Add"
                        buttonAdd!!.text = "Add"
                    } catch (exception: IndexOutOfBoundsException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Can't modify, item moved",
                            Toast.LENGTH_LONG
                        ).show()
                        listAdapterWithRecycleView?.notifyDataSetChanged()
                        clearInputForm()
                        buttonAdd!!.tag = "Add"
                        buttonAdd!!.text = "Add"
                    }
                }
            }
        }
    }

    private val isInputDataValid: Boolean
        get() = !(AppUtility.isStringEmpty(firstName) || AppUtility.isStringEmpty(lastName) || AppUtility.isStringEmpty(
            nationality
        ) || gender == null)

    private fun clearInputForm() {
        editTextFirstName!!.setText("")
        editTextLastName!!.setText("")
        radioGroup!!.clearCheck()
        spinnerNationality!!.setSelection(0)
    }

    @SuppressLint("SetTextI18n")
    override fun onPersonSelected(position: Int) {
        modificationIndex = position
        val person: Person = people[position]
        buttonAdd?.tag = "Modify"
        buttonAdd?.text = "Modify"
        editTextFirstName?.setText(person.name)
        editTextLastName?.setText(person.lastName)
        if (person.gender === Person.GENDER.MALE) {
            (findViewById<View>(R.id.radioButtonMale) as RadioButton).performClick()
        } else if (person.gender === Person.GENDER.FEMALE) {
            (findViewById<View>(R.id.radioButtonFemale) as RadioButton).performClick()
        }
        spinnerNationality!!.setSelection(appUtility!!.getNationalityForSelectedIndex(person.nationality!!))
    }

    @SuppressLint("SetTextI18n")
    override fun onPersonDeleted(position: Int) {
        buttonAdd?.tag = "Add"
        buttonAdd?.text = "Add"
        clearInputForm()
    }

}





