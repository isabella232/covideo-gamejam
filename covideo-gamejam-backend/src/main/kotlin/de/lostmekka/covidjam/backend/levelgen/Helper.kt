package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile

fun Point.toTile(type: Tile.Type) = Tile(this, type)

fun List<Tile>.addTilesToLevel(level: MutableLevel) {
    level.addTiles(this)
}

fun List<Entity>.addEntitiesToLevel(level: MutableLevel) {
    level.addEntities(this)
}

object TypeGroups {
    object Shelve {
        val smallShelves = listOf(Entity.Type.ShelveSmall1, Entity.Type.ShelveSmall2, Entity.Type.ShelveSmall3)
        val tall = listOf(Entity.Type.ShelveTall1, Entity.Type.ShelveTall2, Entity.Type.ShelveTall3)
    }
    object ToiletPaper {
        val all = listOf(
            Entity.Type.PaperRollScrappy,
            Entity.Type.PaperRollBad,
            Entity.Type.PaperRollGood,
            Entity.Type.PaperRollPrint,
            Entity.Type.PaperRollScented
        )
    }
}
