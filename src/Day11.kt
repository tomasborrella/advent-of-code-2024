fun prepareDataDay11(input: List<String>): List<Long> {
    // Only 1 line input
    return input.first().split(" ").map { it.toLong() }
}

fun blink(stones: List<Long>): List<Long> {
    return buildList() {
        stones.forEach { stone ->
            when (stone) {
                0L -> add(1L)
                else -> when (stone.toString().length % 2) {
                    0 -> {
                        val len = stone.toString().length
                        val firstPart = stone.toString().substring(0, len / 2).toLong()
                        val secondPart = stone.toString().substring(len / 2, len).toLong()
                        add(firstPart)
                        add(secondPart)
                    }

                    else -> add(stone * 2024)
                }
            }
        }
    }
}

fun blinkGrouped(stoneGroups: GroupedList): GroupedList {
    return buildGroupedList() {
        for ((number, count) in stoneGroups.stones) {
            when (number) {
                0L -> add(1L, count)
                else -> when (number.toString().length % 2) {
                    0 -> {
                        val len = number.toString().length
                        val firstPart = number.toString().substring(0, len / 2).toLong()
                        val secondPart = number.toString().substring(len / 2, len).toLong()
                        add(firstPart, count)
                        add(secondPart, count)
                    }

                    else -> add(number * 2024, count)
                }
            }
        }
    }
}

data class GroupedList(val stones: MutableMap<Long, Long> = mutableMapOf()) {
    fun add(number: Long, count: Long) {
        val currentCount = stones[number] ?: 0L
        stones[number] = currentCount + count
    }
}

fun buildGroupedList(builder: GroupedList.() -> Unit) : GroupedList {
    val list = GroupedList()
    list.builder()
    return list
}

fun main() {
    fun part1(input: List<String>): Long {
        val stones = prepareDataDay11(input)
        var modifiedStones = stones
        repeat(25){
            modifiedStones = blink(modifiedStones)
        }
        return modifiedStones.size.toLong()
    }

    fun part2(input: List<String>): Long {
        val stones = prepareDataDay11(input)
        val stoneCounts = stones.groupingBy {num -> num}.eachCount()
        var stoneList = buildGroupedList() {
            for ((k, v) in stoneCounts) {
                add(k, v.toLong())
            }
        }
        repeat(75){
            stoneList = blinkGrouped(stoneList)
        }
        return stoneList.stones.values.sumOf { count -> count }
    }

    // Or read a large test input from the `src/Day11_test.txt` file:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312L)

    // Read the input from the `src/Day11.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}