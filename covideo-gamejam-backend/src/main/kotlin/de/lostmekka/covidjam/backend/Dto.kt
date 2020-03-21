package de.lostmekka.covidjam.backend

data class Tile(
    val x: Int,
    val y: Int,
    val type: TileType
)

enum class TileType { Floor, Wall, Shelve, Door, Entrance }
