package com.example.recycler_view

class Person(
    var name: String?,
    var lastName: String?,
    var gender: GENDER?,
    var nationality: String?
) {

    enum class GENDER {
        MALE, FEMALE
    }
}
