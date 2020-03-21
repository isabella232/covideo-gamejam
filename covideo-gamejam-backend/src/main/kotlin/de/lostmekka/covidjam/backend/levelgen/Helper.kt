package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import de.lostmekka.covidjam.backend.TileType

fun Point.toTile(type: TileType) = Tile(this, type)
fun List<Tile>.toGeneratedArea() = GeneratedArea(this)
