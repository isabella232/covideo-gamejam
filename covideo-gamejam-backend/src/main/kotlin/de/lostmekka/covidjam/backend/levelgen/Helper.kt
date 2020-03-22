package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Entity.Type.*
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import de.lostmekka.covidjam.backend.Tile.Type.*
import kotlin.random.Random

fun Point.toTile(type: Tile.Type) = Tile(this, type)

fun List<Tile>.addTilesToLevel(level: MutableLevel) {
    level.addTiles(this)
}

fun List<Entity>.addEntitiesToLevel(level: MutableLevel) {
    level.addEntities(this)
}

object TypeGroups {
    object wall {
        val all = listOf(Wall1,Wall2,Wall3,Wall4,Wall5,Wall6,Wall7,Wall8,Wall9)
    }

    object floor {
        val tiles = listOf(FloorTiles1,FloorTiles2, FloorTiles3)
        val stone = listOf(FloorStone1,FloorStone2)
        val all = tiles + stone
    }

    object shelve {
        val small = listOf(ShelveSmall1, ShelveSmall2, ShelveSmall3)
        val tall = listOf(ShelveTall1, ShelveTall2, ShelveTall3)
        val all = small + tall
    }

    object toiletPaper {
        val all = listOf(
            PaperRollScrappy,
            PaperRollBad,
            PaperRollGood,
            PaperRollPrint,
            PaperRollScented
        )
    }
}

fun Random.fromRange(start: Int, endInclusive: Int) = nextInt(start, endInclusive + 1)
