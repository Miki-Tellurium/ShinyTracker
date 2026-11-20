package com.mikitellurium.shinytracker.data

import kotlinx.serialization.KSerializer
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
    val attempts: Int
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
            return PokemonData.LIST.find { it.name == array[0] && it.variant == array[1] } ?:
            throw RuntimeException("Pokemon not found: $str")
        }
    }
}