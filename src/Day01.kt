import kotlin.math.abs

data class LocationIds(val left: List<Int>, val right: List<Int>)

fun prepareDataDay01(input: List<String>): LocationIds {
    val (left, right) = input.map { line ->
        line.split("   ").let { parts -> parts[0].toInt() to parts[1].toInt() } }
        .unzip()
    return LocationIds(left, right)
}


fun main() {
    fun part1(input: List<String>): Int {
        val locationIds: LocationIds = prepareDataDay01(input)

        var solution = 0
        locationIds.left.sorted().zip(locationIds.right.sorted()).forEach {pair ->
            solution += abs(pair.component1() - pair.component2())
        }
        return solution
    }

    fun part2(input: List<String>): Int {
        val locationIds: LocationIds = prepareDataDay01(input)

        val rightCountMap = locationIds.right.groupingBy { it }.eachCount()
        var solution = 0
        locationIds.left.forEach { element ->
            val multiplier = rightCountMap.getOrDefault(element, 0)
            solution += element * multiplier
        }
        return solution
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
