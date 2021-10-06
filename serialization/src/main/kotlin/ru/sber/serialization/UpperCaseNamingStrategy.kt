package ru.sber.serialization

import com.fasterxml.jackson.databind.PropertyNamingStrategies

class UpperCaseNamingStrategy : PropertyNamingStrategies.NamingBase() {
    override fun translate(input: String?): String {
        return input!!.uppercase()
    }
}