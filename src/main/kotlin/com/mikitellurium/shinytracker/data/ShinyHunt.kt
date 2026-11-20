package com.mikitellurium.shinytracker.data

import javafx.beans.property.SimpleIntegerProperty
import kotlin.math.pow

class ShinyHunt(
    val pokemon: Pokemon,
    var rolls: Int,
    val baseOdds: Int,
    attempts: Int = 0
) {
    val counterProperty = SimpleIntegerProperty(attempts)
    var attempts
        get() = counterProperty.get()
        set(value) = counterProperty.set(value)

    fun increaseAttempts() = attempts++
    fun chance(): Int = baseOdds / rolls
    fun geometricDistribution(): Double {
        val chance = chance()
        return (1 - ((chance - 1) / chance.toDouble()).pow(attempts + 1)) * 100
    }
    fun toSavedData(): SavedHuntData = SavedHuntData(pokemon, rolls, baseOdds, attempts)

    override fun toString(): String = "ShinyHunt (pokemon=${pokemon.displayName()}, rolls=$rolls, odds=$baseOdds, attempts=$attempts)"
}