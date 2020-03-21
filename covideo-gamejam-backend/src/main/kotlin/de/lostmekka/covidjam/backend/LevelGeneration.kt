package de.lostmekka.covidjam.backend

import de.lostmekka.covidjam.backend.levelgen.FloorGenerator
import de.lostmekka.covidjam.backend.levelgen.Rect
import de.lostmekka.covidjam.backend.levelgen.RoomGenerator

data class LevelGenerationInput(
    val levelSize: Dimension
)

data class LevelGenerationOutput(
    val tiles: List<Tile>
)

data class Dimension(val w: Int, val h: Int)
data class Point(val x: Int, val y: Int)

data class Tile(
    val pos: Point,
    val type: TileType,
    val isEntrance: Boolean
)

enum class TileType { Floor, Wall, Shelve, Door }

object LevelGeneration : BackendEndpoint<LevelGenerationInput, LevelGenerationOutput> {
    override val path = "level/generate"

    override fun handleRequest(input: LevelGenerationInput): LevelGenerationOutput {
        val generator = RoomGenerator(
            inner = FloorGenerator()
        )

        val rect = Rect(0, 0, input.levelSize.w, input.levelSize.h)
        val generated = generator.generate(rect)
        return LevelGenerationOutput(
            tiles = generated.tiles
        )
    }
}
