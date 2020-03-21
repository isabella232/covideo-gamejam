package de.lostmekka.covidjam.backend

data class Dimension(val x: Int, val y: Int)

class LevelGenerationEvent(
    val levelSize: Dimension
)

data class Tile(
    val x: Int,
    val y: Int,
    val type: TileType
)

enum class TileType { Floor, Wall, Shelve, Door, Entrance }

object LevelGeneration : BackendEndpoint<LevelGenerationEvent, List<Tile>> {
    override val path = "level/generate"

    override fun handleRequest(input: LevelGenerationEvent): List<Tile> {
        // TODO: actually generate stuff
        return listOf(
            Tile(0, 0, TileType.Floor),
            Tile(0, 1, TileType.Floor),
            Tile(1, 0, TileType.Floor),
            Tile(1, 1, TileType.Floor)
        )
    }
}
