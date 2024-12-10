import kotlin.math.abs

fun prepareDataDay02(input: List<String>): List<List<Int>> {
   return input.map { line ->
        line.split(" ")
            .map { it.toInt() }
    }
}

fun checkSafety(report: List<Int>) : Boolean {
    val initialDirection = report[1] > report[0]

    return report.windowed(2).all { (previous, current) ->
        when {
            abs(current - previous) > 3 -> false
            initialDirection && current <= previous -> false
            !initialDirection && current >= previous -> false
            else -> true
        }
    }
}

fun checkSafetyWithOneErrorTolerance(report: List<Int>): Boolean {
    val initialResult: Boolean = checkSafety(report)

    if (!initialResult) {
        val reportVariations : MutableList<List<Int>> = mutableListOf()
        report.forEachIndexed{ index, _ ->
            reportVariations.add(report.filterIndexed { idx, _ -> idx != index })
        }
        val safetyVariationsCount = reportVariations.count { variation -> checkSafety(variation) }
        if (safetyVariationsCount > 0) return true
    }

    return initialResult
}

fun main() {
    fun part1(input: List<String>): Int {
        val reports: List<List<Int>> = prepareDataDay02(input)
        return reports.count { report -> checkSafety(report) }
    }

    fun part2(input: List<String>): Int {
        val reports: List<List<Int>> = prepareDataDay02(input)
        return reports.count { report -> checkSafetyWithOneErrorTolerance(report) }
    }

    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
