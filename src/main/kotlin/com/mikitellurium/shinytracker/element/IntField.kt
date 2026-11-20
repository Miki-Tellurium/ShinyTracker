package com.mikitellurium.shinytracker.element

import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.converter.IntegerStringConverter

class IntField(val minValue: Int = 0, val maxValue: Int = Int.MAX_VALUE, val defaultValue: Int = minValue): TextField() {
    private val formatter: TextFormatter<Int>

    init {
        require(minValue <= maxValue) { "IntegerField minimum value $minValue is greater than maximum value $maxValue" }
        require(defaultValue in minValue..maxValue) { "IntegerField default value $defaultValue is outside $minValue..$maxValue" }
        formatter = TextFormatter(IntegerStringConverter(), defaultValue) { change ->
            if (!change.isContentChange) return@TextFormatter change

            val newText = change.controlNewText
            when {
                newText.isEmpty() -> change                              // allow clearing
                !newText.all { it.isDigit() } -> null                    // digits only
                newText.toLongOrNull()?.let { it <= maxValue } == true -> change
                else -> null
            }
        }
        textFormatter = formatter

        // Commit on lost focus
        focusedProperty().addListener { _, _, focused ->
            if (!focused) commit()
        }
        // Commit on enter
        setOnAction { commit() }
    }

    private fun commit() {
        val value = text.toIntOrNull()?.coerceIn(minValue, maxValue) ?: defaultValue
        formatter.value = value
        text = value.toString()
    }

    fun value(): Int = text.toIntOrNull()?.coerceIn(minValue, maxValue) ?: formatter.value
    fun setValue(value: Int) {
        formatter.value = value.coerceIn(minValue, maxValue)
        text = formatter.value.toString()
    }
}