package de.lostmekka.covidjam.backend

// TODO: actually generate stuff
fun generateLevel(): List<Tile> = listOf(
    Tile(0, 0, TileType.Floor),
    Tile(0, 1, TileType.Floor),
    Tile(1, 0, TileType.Floor),
    Tile(1, 1, TileType.Floor)
)
