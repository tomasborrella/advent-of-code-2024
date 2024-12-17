fun prepareDataDay10(input: List<String>): List<List<Int>> {
    return input.map { str ->
        str.map { char -> char.toString().toInt() }
    }
}

fun getTrailHeads(map: List<List<Int>>): List<Point> {
    val trailHeads = mutableListOf<Point>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, value ->
            if (value == 0) {
                trailHeads.add(Point(rowIndex, colIndex))
            }
        }
    }
    return trailHeads
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun getMovement(direction: Direction): Pair<Int, Int> {
   return when (direction) {
        Direction.LEFT -> Pair(0 ,-1)
        Direction.UP -> Pair(-1 ,0)
        Direction.RIGHT -> Pair(0 ,+1)
        Direction.DOWN -> Pair(1 ,0)
    }
}

data class Trail (val deep: Int, var points: MutableList<Point> )

fun calculateTrails(trailHead: Point, topographicMap: List<List<Int>>, part: Int = 1): Int {
    val trailsList: MutableList<Trail> = mutableListOf()
    trailsList.add(Trail(0, mutableListOf(trailHead)))
    val directions = listOf<Direction>(Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN)
    for (i in 1..9){
        val initialPoints = trailsList.first{it.deep == i-1}.points
        initialPoints.forEach { point ->
            for (direction in directions) {
                val (movX, movY) = getMovement(direction)
                val x = point.x + movX
                val y = point.y + movY
                if (validIndexes(topographicMap, x, y) && topographicMap[x][y] == i) {
                    if(trailsList.none{it.deep == i}) {
                        trailsList.add(Trail(i, mutableListOf(Point(x, y))))
                    }
                    else {
                        trailsList.first { it.deep == i }.points.add(Point(x, y))
                    }
                }
            }
        }
        // If no points for the current deep level
        if (trailsList.none { it.deep == i }){
            return 0
        }
        // deduplicate points for part 1
        if (part == 1) {
            trailsList.first { it.deep == i }.points = trailsList.first { it.deep == i }.points.toSet().toMutableList()
        }
    }
    return trailsList.first { it.deep == 9 }.points.size
}

fun main() {
    fun part1(input: List<String>): Int {
        val topographicMap = prepareDataDay10(input)
        val trailHeads = getTrailHeads(topographicMap)
        return trailHeads.sumOf { trailHead -> calculateTrails(trailHead, topographicMap, 1) }
    }

    fun part2(input: List<String>): Int {
        val topographicMap = prepareDataDay10(input)
        val trailHeads = getTrailHeads(topographicMap)
        return trailHeads.sumOf { trailHead -> calculateTrails(trailHead, topographicMap, 2) }
    }

    // Or read a large test input from the `src/Day10_test.txt` file:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}