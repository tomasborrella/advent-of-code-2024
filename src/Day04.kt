fun prepareDataDay04(input: List<String>): List<CharArray>{
    return input.map { line ->
        line.toCharArray()
    }
}

fun checkXMASFromX(data: List<CharArray>, xRowIndex: Int, xColIndex: Int, rowModifier: Int, colModifier: Int): Boolean {
    var newRowIndex = xRowIndex + rowModifier
    var newColIndex = xColIndex + colModifier
    if (validIndexes(data, newRowIndex, newColIndex) && data[newRowIndex][newColIndex] == 'M'){
        newRowIndex += rowModifier
        newColIndex += colModifier
        if (validIndexes(data, newRowIndex, newColIndex) && data[newRowIndex][newColIndex] == 'A'){
            newRowIndex += rowModifier
            newColIndex += colModifier
            if (validIndexes(data, newRowIndex, newColIndex) && data[newRowIndex][newColIndex] == 'S'){
                return true
            }
        }
    }
    return false
}

fun checkLeftMASFromA(data: List<CharArray>, aRowIndex: Int, aColIndex: Int): Boolean {
    return (((validIndexes(data,aRowIndex-1, aColIndex-1) && data[aRowIndex-1][aColIndex-1] == 'M') &&
            (validIndexes(data,aRowIndex+1, aColIndex+1) && data[aRowIndex+1][aColIndex+1] == 'S'))
            ||
            ((validIndexes(data,aRowIndex-1, aColIndex-1) && data[aRowIndex-1][aColIndex-1] == 'S') &&
                    (validIndexes(data,aRowIndex+1, aColIndex+1) && data[aRowIndex+1][aColIndex+1] == 'M')))
}

fun checkRightMASFromA(data: List<CharArray>, aRowIndex: Int, aColIndex: Int): Boolean {
    return (((validIndexes(data,aRowIndex-1, aColIndex+1) && data[aRowIndex-1][aColIndex+1] == 'M') &&
            (validIndexes(data,aRowIndex+1, aColIndex-1) && data[aRowIndex+1][aColIndex-1] == 'S'))
            ||
            ((validIndexes(data,aRowIndex-1, aColIndex+1) && data[aRowIndex-1][aColIndex+1] == 'S') &&
                    (validIndexes(data,aRowIndex+1, aColIndex-1) && data[aRowIndex+1][aColIndex-1] == 'M')))
}

fun main() {
    fun part1(input: List<String>): Int {
        val data = prepareDataDay04(input)
        var count = 0
        data.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, value ->
                if (value == 'X') {
                    if (checkXMASFromX(data, rowIndex, colIndex, -1, -1)){ count++ } // up-left
                    if (checkXMASFromX(data, rowIndex, colIndex, -1, 0)){ count++ } // up
                    if (checkXMASFromX(data, rowIndex, colIndex, -1, +1)){ count++ } // up-right
                    if (checkXMASFromX(data, rowIndex, colIndex, 0, -1)){ count++ } // left
                    if (checkXMASFromX(data, rowIndex, colIndex, 0, +1)){ count++ } // right
                    if (checkXMASFromX(data, rowIndex, colIndex, +1, -1)){ count++ } // down-left
                    if (checkXMASFromX(data, rowIndex, colIndex, +1, 0)){ count++ } // down
                    if (checkXMASFromX(data, rowIndex, colIndex, +1, +1)){ count++ } // down-right
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val data = prepareDataDay04(input)
        var count = 0
        data.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, value ->
                if (value == 'A') {
                    if(checkLeftMASFromA(data, rowIndex, colIndex) and checkRightMASFromA(data, rowIndex, colIndex)){
                        count++
                    }
                }
            }
        }
        return count
    }

    // Or read a large test input from the `src/Day04_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
