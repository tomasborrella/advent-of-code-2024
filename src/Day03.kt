

fun main() {
    fun part1(input: List<String>): Int {
        val regex = """mul\((\d*),(\d*)\)""".toRegex()
        val matches = regex.findAll(input.toString())
        return matches.sumOf { match ->
            match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val selectedFragment = input.toString().split("don't()").mapIndexed { index, part ->
            if (index == 0) {
                part
            } else {
                part.split("do()").let { if (it.size > 1) it.drop(1).joinToString("") else "" }
            }
        }.joinToString("")
        return part1(listOf(selectedFragment))
    }

    // Or read a large test input from the `src/Day03_test_X.txt` files:
    val testInput1 = readInput("Day03_test_1")
    check(part1(testInput1) == 161)
    val testInput2 = readInput("Day03_test_2")
    check(part2(testInput2) == 48)

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
