package com.alvarodazacruces.proyectoalvarofirebase.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val name: String,
    val types: List<PokemonType>,
    val abilities: List<PokemonAbility>,
    val sprites: PokemonSprites
)

data class PokemonSprites(
    @SerializedName("other") val other: OtherSprites
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String // URL de la imagen
)
