fun prepareDataDay07(input: List<String>): Pair<List<Long>, List<List<Int>>>{
    val (results, operators) = input.map { line ->
        line.split(": ").let { parts -> parts[0].toLong() to parts[1].split(" ").map { it.toInt() }}
    }.unzip()
    return Pair(results, operators)
}

fun calculateResults(results: List<Long>, operators: List<List<Int>>, part: Int = 1): Long {
    var sumOfResults: Long  = 0
    results.forEachIndexed { indexForResults, result ->
        var allOperatorsResult: MutableList<Long> = mutableListOf()
        operators[indexForResults].forEachIndexed {indexForOperators, currentNumberToCompute ->
            val tempResult: MutableList<Long> = mutableListOf()
            when (indexForOperators){
                0 -> allOperatorsResult.add(currentNumberToCompute.toLong())
                else -> {
                    allOperatorsResult.forEach { partialResult ->
                        tempResult.add(partialResult + currentNumberToCompute)
                        tempResult.add(partialResult * currentNumberToCompute)
                        if (part == 2){
                            tempResult.add((partialResult.toString() + currentNumberToCompute.toString()).toLong())
                        }
                    }
                    allOperatorsResult = tempResult
                }
            }
        }
        if (result in allOperatorsResult){
            sumOfResults += result
        }
    }
    return sumOfResults
}


fun main() {
    fun part1(input: List<String>): Long {
        val (results, operators) = prepareDataDay07(input)
        return calculateResults(results, operators, part=1)
    }

    fun part2(input: List<String>): Long {
        val (results, operators) = prepareDataDay07(input)
        return calculateResults(results, operators, part=2)
    }

    // Or read a large test input from the `src/Day07_test.txt` file:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == "3749".toLong())
    check(part2(testInput) == "11387".toLong())

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
