package com.mikitellurium.shinytracker.data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class SavedHuntData(
    @Serializable(with = PokemonSerializer::class) val pokemon: Pokemon,
    val rolls: Int,
    val baseOdds: Int,
    val attempts: Int,
    @SerialName("saved_on")
    val savedOn: LocalDate
) {
    fun toHunt(): ShinyHunt = ShinyHunt(pokemon, rolls, baseOdds, attempts)

    class PokemonSerializer : KSerializer<Pokemon> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Pokemon", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Pokemon) {
            encoder.encodeString(value.spriteId())
        }

        override fun deserialize(decoder: Decoder): Pokemon {
            val str = decoder.decodeString()
            val array = str.split("_", limit = 2)
            return if (str == NONE.name) NONE
            else PokemonData.LIST.find {
                 it.name == array[0] && array.getOrNull(1)?.let { v -> it.variant == v } ?: true
            } ?: throw RuntimeException("Pokemon not found: $str")
        }
    }
}