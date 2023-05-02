package com.example.recycler_view

import android.annotation.SuppressLint
import android.content.Context


class AppUtility private constructor(context: Context) {
    private val names: Array<String> = context.resources.getStringArray(R.array.names)
    private val lastnames: Array<String> = context.resources.getStringArray(R.array.lastnames)
    private val genders: Array<String> = context.resources.getStringArray(R.array.gender)
    private val nationalities: Array<String> = context.resources.getStringArray(R.array.nationality)
    lateinit var uniqueNationalitiesArray: Array<String>
        private set
    private val people: MutableList<Person>

    init {
        people = ArrayList()
        for (i in names.indices) {
            val person = Person(
                names[i], lastnames[i],
                if (genders[i].equals(
                        "male",
                        ignoreCase = true
                    )
                ) Person.GENDER.MALE else Person.GENDER.FEMALE,
                nationalities[i]
            )
            people.add(person)
        }
        setUniqueNationalities()
    }

    fun getPeople(): List<Person> {
        return people
    }

    private fun setUniqueNationalities() {
        val uniqueNationalities: MutableList<String> = ArrayList()
        for (nationality in nationalities) {
            if (!uniqueNationalities.contains(nationality)) {
                uniqueNationalities.add(nationality)
            }
        }
        uniqueNationalitiesArray = uniqueNationalities.toTypedArray()
    }

    fun getNationalityForSelectedIndex(nationality: String): Int {
        var nationalityIndex = 0
        val nationalities = uniqueNationalitiesArray
        do {
            if (nationalities[nationalityIndex] == nationality) {
                break
            }
            nationalityIndex++
        } while (nationalityIndex < nationalities.size)
        return nationalityIndex
    }

    companion object {
        private const val EMPTY_STRING = ""
        @SuppressLint("StaticFieldLeak")
        private var appUtility: AppUtility? = null
        fun getAppUtility(context: Context): AppUtility? {
            if (appUtility == null) {
                appUtility = AppUtility(context)
            }
            return appUtility
        }

        fun isStringEmpty(string: String?): Boolean {
            var isStringEmpty = false
            if (string == null) {
                isStringEmpty = true
            } else {
                if (string.isEmpty() || string == EMPTY_STRING) {
                    isStringEmpty = true
                }
            }
            return isStringEmpty
        }
    }
}