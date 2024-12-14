data class Point(val x: Int, val y: Int)

fun getAntennas(map: List<CharArray>): MutableMap<Char, MutableList<Point>> {
    val antennas: MutableMap<Char, MutableList<Point>> = mutableMapOf()
    map.forEachIndexed { x, row ->
        row.forEachIndexed { y, value ->
            if (value != '.') {
                antennas.getOrPut(value) { mutableListOf() }.add(Point(x,y))
            }
        }
    }
    return antennas
}

fun antennaCombinations(antennasOfOneType: List<Point>): MutableList<Pair<Point, Point>>{
    val antennaPairsList: MutableList<Pair<Point, Point>> = mutableListOf()
    antennasOfOneType.forEachIndexed{index, element ->
        for (i in index+1..<antennasOfOneType.size){
            antennaPairsList.add(Pair(element, antennasOfOneType[i]))
        }
    }
    return antennaPairsList
}

fun getAntinodes(map: List<CharArray>, antennas: MutableMap<Char, MutableList<Point>>, part: Int = 1):
        Set<Point> {
        val xMax = map.size - 1
        val yMax = map[0].size - 1
        val antinodes = when (part) {
            1 -> antinodesCalculusPart1(antennas, xMax, yMax)
            2 -> antinodesCalculusPart2(antennas, xMax, yMax)
            else -> mutableMapOf()
        }
        return antinodes.values.flatten().toSet()
}

fun antinodesCalculusPart1(antennas: MutableMap<Char, MutableList<Point>>, xMax: Int, yMax: Int):
        MutableMap<Char, MutableList<Point>> {
        val antinodes: MutableMap<Char, MutableList<Point>> = mutableMapOf()
        antennas.forEach { antenna ->
            val pairs = antennaCombinations(antenna.value)
            pairs.forEach { (first, second) ->
                val antinodesCandidates : MutableList<Point> = mutableListOf()
                antinodesCandidates.add(Point(first.x + (first.x - second.x), first.y + (first.y - second.y)))
                antinodesCandidates.add(Point(second.x + (second.x - first.x), second.y + (second.y - first.y)))
                antinodesCandidates.forEach { antinode ->
                    if (antinode.x >= 0 && antinode.y >= 0 && antinode.x <= xMax && antinode.y <= yMax) {
                        antinodes.getOrPut(antenna.key) { mutableListOf() }.add(antinode)
                    }
                }
            }
        }
        return antinodes
}

fun antinodesCalculusPart2(antennas: MutableMap<Char, MutableList<Point>>, xMax: Int, yMax: Int):
        MutableMap<Char, MutableList<Point>> {
        val antinodes: MutableMap<Char, MutableList<Point>> = mutableMapOf()
        antennas.forEach { antenna ->
            val pairs = antennaCombinations(antenna.value)
            pairs.forEach { (first, second) ->
                antinodes.getOrPut(antenna.key) { mutableListOf() }.add(Point(first.x, first.y))
                antinodes.getOrPut(antenna.key) { mutableListOf() }.add(Point(second.x, second.y))
                val xDecrement = first.x - second.x
                val yDecrement = first.y - second.y
                var xOrig = first.x
                var yOrig = first.y
                while(true) // For decrements
                {
                    val xNew = xOrig + xDecrement
                    val yNew = yOrig + yDecrement
                    if (xNew >= 0 && yNew >= 0 && xNew <= xMax && yNew <= yMax) {
                        antinodes.getOrPut(antenna.key) { mutableListOf() }.add(Point(xNew, yNew))
                    }
                    else {
                        break
                    }
                    xOrig = xNew
                    yOrig = yNew
                }
                while(true) // For increments
                {
                    val xNew = xOrig - xDecrement
                    val yNew = yOrig - yDecrement
                    if (xNew >= 0 && yNew >= 0 && xNew <= xMax && yNew <= yMax) {
                        antinodes.getOrPut(antenna.key) { mutableListOf() }.add(Point(xNew, yNew))
                    }
                    else {
                        break
                    }
                    xOrig = xNew
                    yOrig = yNew
                }
            }
        }
        return antinodes
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = readMapInput(input)
        val antennas = getAntennas(map)

        val antinodes = getAntinodes(map, antennas, part=1)
        return antinodes.size
    }

    fun part2(input: List<String>): Int {
        val map = readMapInput(input)
        val antennas = getAntennas(map)

        val antinodes = getAntinodes(map, antennas, part=2)
        return antinodes.size
    }

    // Or read a large test input from the `src/Day08_test.txt` file:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
