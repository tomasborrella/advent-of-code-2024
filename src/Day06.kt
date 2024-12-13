data class Coordinates(val row: Int, val col: Int)

const val POSITIONS_TO_CHECK = 10

fun prepareDataDay06(input: List<String>): List<CharArray>{
    return input.map { line ->
        line.toCharArray()
    }
}

fun indexOfSubList(list: List<Coordinates>, subList: List<Coordinates>): Int {
    return (0..list.size - subList.size).firstOrNull { index ->
        list.subList(index, index + subList.size) == subList
    } ?: -1
}

fun inALoop(allPositions: List<Coordinates>, positionsToCheck: Int = POSITIONS_TO_CHECK): Boolean {
    val pattern = allPositions.takeLast(positionsToCheck)
    val pos = allPositions.dropLast(positionsToCheck)
    return indexOfSubList(pos, pattern) != -1
}

fun getGuardCoordinates(map: List<CharArray>): Coordinates {
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, value ->
            if (value == '^' || value == '<' || value == '>' || value == 'v') {
                return Coordinates(rowIndex, colIndex)
            }
            if (value == 'T'){
                return Coordinates(-2, -2) // In case guard is in a loop
            }
        }
    }
    return Coordinates(-1, -1) // In case guard is not in the map
}

fun getXCoordinates(map: List<CharArray>): List<Coordinates> {
    val positions: MutableList<Coordinates> = mutableListOf()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, value ->
            if (value == 'X') {
                positions.add(Coordinates(rowIndex, colIndex))
            }
        }
    }
    return positions
}

fun newDirection(direction: Char): Char {
    return when (direction){
        '^' -> '>'
        '>' -> 'v'
        'v' -> '<'
        '<' -> '^'
        else -> {
            println("ERROR: Unknown direction $direction")
            direction
        }
    }
}

fun moveGuard(direction: Char, currentRow: Int, currentCol: Int, newRow: Int, newCol: Int, map: List<CharArray>) {
    if (validIndexes(map, newRow, newCol)){
        if (map[newRow][newCol] != '#') {
            map[currentRow][currentCol] = 'X'
            map[newRow][newCol] = direction
        }
        else {
            map[currentRow][currentCol] = newDirection(direction)
        }
    }
    else {
        map[currentRow][currentCol] = 'X'
    }
}

// TODO: moveGuard and moveGuardPart2 should be combined in an only function
fun moveGuardPart2(direction: Char, currentRow: Int, currentCol: Int, newRow: Int, newCol: Int, map: List<CharArray>,
                   allPositions: MutableList<Coordinates>) {
    if (validIndexes(map, newRow, newCol)){
        if (map[newRow][newCol] != '#' && map[newRow][newCol] != 'O') {
            map[currentRow][currentCol] = 'X'
            map[newRow][newCol] = direction
            allPositions.add(Coordinates(currentRow, currentCol))
        }
        else {
            map[currentRow][currentCol] = newDirection(direction)
        }
    }
    else {
        map[currentRow][currentCol] = 'X'
    }
    if (allPositions.size > 0 && inALoop(allPositions)) {
        map[currentRow][currentCol] = 'T'
    }
}

fun getObstacleOptionsBasedOnPossiblePositions(map: List<CharArray>): List<Coordinates> {
    val internalMap = completeAMapWithoutAddedObstacles(map)
    return getXCoordinates(internalMap)
}

fun completeAMapWithoutAddedObstacles(originalMap: List<CharArray>): List<CharArray> {
    val map: MutableList<CharArray> = originalMap.toMutableList()
    var guardPosition = getGuardCoordinates(map)
    while (guardPosition.row >= 0 && guardPosition.col > 0) {
        when (map[guardPosition.row][guardPosition.col]){
            '^' -> {
                moveGuard('^', guardPosition.row, guardPosition.col,guardPosition.row - 1, guardPosition.col, map)
            }
            '>' -> {
                moveGuard('>', guardPosition.row, guardPosition.col, guardPosition.row, guardPosition.col + 1, map)
            }
            '<' -> {
                moveGuard('<', guardPosition.row, guardPosition.col, guardPosition.row, guardPosition.col - 1, map)
            }
            'v' -> {
                moveGuard('v', guardPosition.row, guardPosition.col, guardPosition.row + 1, guardPosition.col, map)
            }
            else -> println("ERROR")
        }
        guardPosition = getGuardCoordinates(map)
    }
    return map
}

fun main() {
    fun part1(input: List<String>): Int {
        val initialMap = prepareDataDay06(input)
        val map = completeAMapWithoutAddedObstacles(initialMap)
        return getXCoordinates(map).size
    }

    // TODO: Improve performance: it takes about 6 minutes to run in my M1 PRO computer
    fun part2(input: List<String>): Int {
        val initialMap = prepareDataDay06(input)
        var map: MutableList<CharArray> = initialMap.toMutableList()
        var loopCounter = 0
        val allObstacleOptions = getObstacleOptionsBasedOnPossiblePositions(map)
        println("allObstacleOptions: ${allObstacleOptions.size}")
        allObstacleOptions.forEachIndexed { index, option ->
            println("index: $index") // Just to see the progress of the execution
            map = prepareDataDay06(input).toMutableList()
            map[option.row][option.col] = 'O'
            var guardPosition = getGuardCoordinates(map)
            val allPositions: MutableList<Coordinates> = mutableListOf()
            allPositions.clear()
            while (guardPosition.row > 0 && guardPosition.col >= 0) {
                when (map[guardPosition.row][guardPosition.col]) {
                    '^' -> {
                        moveGuardPart2(
                            '^',
                            guardPosition.row,
                            guardPosition.col,
                            guardPosition.row - 1,
                            guardPosition.col,
                            map,
                            allPositions
                        )
                    }

                    '>' -> {
                        moveGuardPart2(
                            '>',
                            guardPosition.row,
                            guardPosition.col,
                            guardPosition.row,
                            guardPosition.col + 1,
                            map,
                            allPositions
                        )
                    }

                    '<' -> {
                        moveGuardPart2(
                            '<',
                            guardPosition.row,
                            guardPosition.col,
                            guardPosition.row,
                            guardPosition.col - 1,
                            map,
                            allPositions
                        )
                    }

                    'v' -> {
                        moveGuardPart2(
                            'v',
                            guardPosition.row,
                            guardPosition.col,
                            guardPosition.row + 1,
                            guardPosition.col,
                            map,
                            allPositions
                        )
                    }

                    else -> println("ERROR")
                }
                guardPosition = getGuardCoordinates(map)
            }
            if (guardPosition.row == -2 && guardPosition.col == -2) {
                loopCounter++
            }
        }
        return loopCounter
    }

    // Or read a large test input from the `src/Day06_test.txt` file:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day06.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
