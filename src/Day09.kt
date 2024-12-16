fun prepareDataDay09(input: List<String>): List<Int>{
    return input[0].map { it.digitToInt() }
}

enum class Type {
    CODE_BLOCK,
    FREE_SPACE
}

fun getNextEnumValue(current: Type): Type {
    val enumValues = Type.entries.toTypedArray()
    val currentIndex = enumValues.indexOf(current)

    return enumValues.getOrElse(currentIndex + 1) { enumValues.first() }
}

fun getNumber(type: Type, id: Int): Int {
    return when (type) {
        Type.CODE_BLOCK -> id
        Type.FREE_SPACE -> -1 // Free space represented as negative number
    }
}

fun getSparseDisk (diskMap: List<Int>): MutableList<Int>{
    var idNumber = 0
    var type = Type.CODE_BLOCK
    val diskSparse: MutableList<Int> = mutableListOf()
    diskMap.forEach { times ->
        repeat(times) {
            diskSparse.add(getNumber(type, idNumber))
        }
        if (type == Type.CODE_BLOCK) { idNumber += 1 }
        type = getNextEnumValue(type)
    }
    return diskSparse
}

fun checkAllNumbersGroupedTogetherAtBeginning(input: List<Int>): Boolean {
    val firstNumberIndex = input.indexOfFirst { it >= 0 }
    val lastNumberIndex = input.indexOfLast { it >= 0 }

    for (i in firstNumberIndex until lastNumberIndex) {
        if (input[i] < 0) {
            return false
        }
    }
    return true
}

fun swapFirstFreeSpaceWithLastNumber(input: MutableList<Int>) {
    val firstNegativeIndex = input.indexOfFirst { it < 0 }
    val lastPositiveNumberIndex = input.indexOfLast { it >= 0 }

    input[firstNegativeIndex] = input[lastPositiveNumberIndex].also { input[lastPositiveNumberIndex] = -1 }
}

fun calculateChecksum(input: List<Int>): Long {
    var sum: Long = 0
    input.forEachIndexed { index, value ->
        if (value > 0) sum += index * value
    }
    return sum
}

// New data structure for part 2

data class Data(val type: Type, val value: Int, val length: Int, var triedToMove: Boolean = false)

fun getSparseDiskNewDataStructure (diskMap: List<Int>): MutableList<Data>{
    val data: MutableList<Data> = mutableListOf()
    var idNumber = 0
    var type = Type.CODE_BLOCK
    diskMap.forEach { times ->
        data.add(Data(type, getNumber(type, idNumber), times))
        if (type == Type.CODE_BLOCK) { idNumber += 1 }
        type = getNextEnumValue(type)
    }
    return data
}

fun moveLastCodeBlockNewDataStructure(input: MutableList<Data>) {
    val indexedDataToMove = input.withIndex().last { (_, element) ->
        element.type == Type.CODE_BLOCK && !element.triedToMove
    }
    val indexedFreeSpace = input.withIndex().firstOrNull { (index, element) ->
        element.type == Type.FREE_SPACE && element.length >= indexedDataToMove.value.length &&
                index < indexedDataToMove.index
    }
    input[indexedDataToMove.index].triedToMove = true
    if (indexedFreeSpace != null) {
        val spaceDifference = indexedFreeSpace.value.length - indexedDataToMove.value.length
        input[indexedDataToMove.index] = Data(Type.FREE_SPACE, -1, indexedDataToMove.value.length)
        input[indexedFreeSpace.index] = indexedDataToMove.value
        if (spaceDifference > 0) {
            input.add(indexedFreeSpace.index + 1, Data(Type.FREE_SPACE, -1, spaceDifference))
        }
    }
}

fun calculateChecksumNewDataStructure(input: List<Data>): Long {
    var index = 0
    var sum: Long = 0
    input.forEach { data ->
        for (i in 0..<data.length) {
            if (data.type == Type.CODE_BLOCK) {
                sum += index * data.value
            }
            index++
        }
    }
    return sum
}

// Main function

fun main() {
    fun part1(input: List<String>): Long {
        val diskMap = prepareDataDay09(input)
        val diskSparse: MutableList<Int> = getSparseDisk(diskMap)
        while(!checkAllNumbersGroupedTogetherAtBeginning(diskSparse)){
            swapFirstFreeSpaceWithLastNumber(diskSparse)
        }
        return calculateChecksum(diskSparse)
    }

    fun part2(input: List<String>): Long {
        val diskMap = prepareDataDay09(input)
        val diskSparse: MutableList<Data> = getSparseDiskNewDataStructure(diskMap)
        while(diskSparse.any{it.type == Type.CODE_BLOCK && !it.triedToMove}) {
            moveLastCodeBlockNewDataStructure(diskSparse)
        }
        return calculateChecksumNewDataStructure(diskSparse)
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
