fun prepareDataDay05(input: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
    val rules = input.filter { it.contains("|") }.map {line ->
        line.split("|").map { it.toInt() }
    }
    val updates = input.filter { it.contains(",") }.map {line ->
        line.split(",").map { it.toInt() }
    }
    return Pair(rules, updates)
}

fun checkRules(update: List<Int>, rules: List<List<Int>>): Boolean =
    rules.all { (firstPage, secondPage) ->
        !(update.contains(firstPage) && update.contains(secondPage)
                && update.indexOf(firstPage) > update.indexOf(secondPage))
    }

fun getAffectingRules(update: List<Int>, rules: List<List<Int>>): List<List<Int>> {
    return rules.filter {  (firstPage, secondPage) ->
        update.contains(firstPage) && update.contains(secondPage)
    }
}

fun fixUpdate(update: List<Int>, rules: List<List<Int>>): Int {
    val fixedUpdate: MutableList<Int> = mutableListOf()
    val updateWorkingCopy: MutableList<Int> = update.toMutableList()
    var affectingRules = getAffectingRules(update, rules)
    while (fixedUpdate.size < update.size) {
        val nextNumber = updateWorkingCopy.first { page ->
            affectingRules.none { (_, secondPage) -> secondPage == page }
        }
        fixedUpdate.add(nextNumber)
        updateWorkingCopy.remove(nextNumber)
        affectingRules = getAffectingRules(updateWorkingCopy, rules)
    }
    return fixedUpdate[fixedUpdate.size/2]
}

fun main() {
    fun part1(input: List<String>): Int {
        val (rules, updates) = prepareDataDay05(input)
        return updates.filter { update -> checkRules(update, rules) }.sumOf { update -> update[update.size/2] }
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = prepareDataDay05(input)
        val failedUpdates = updates.filter { update -> !checkRules(update, rules) }
        return failedUpdates.sumOf { update -> fixUpdate(update, rules) }
    }

    // Or read a large test input from the `src/Day05_test.txt` file:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
