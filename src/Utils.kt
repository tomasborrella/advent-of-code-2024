import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

// TODO: validIndexesListCharArray should be replaced in all code by the more generic validIndexes
/**
 * Check if coordinates are inside the boundaries of a List<CharArray>
 */
fun validIndexesListCharArray(data: List<CharArray>, rowIndex: Int, colIndex: Int): Boolean {
    val maxRowIndex = data.size - 1
    val maxColIndex = data[0].size - 1
    return rowIndex >= 0 && colIndex >= 0 && rowIndex <= maxRowIndex && colIndex <= maxColIndex
}

/**
 * Check if coordinates are inside the boundaries of a generic List<List<T>>
 */
fun <T> validIndexes(data: List<List<T>>, rowIndex: Int, colIndex: Int): Boolean {
    val maxRowIndex = data.size - 1
    val maxColIndex = data[0].size - 1
    return rowIndex >= 0 && colIndex >= 0 && rowIndex <= maxRowIndex && colIndex <= maxColIndex
}

/**
 * Convert the input to a List<CharArray> (useful for maps)
 */
fun readMapInput(input: List<String>): List<CharArray> = input.map { line -> line.toCharArray() }

// 2-dimensional point
data class Point(val x: Int, val y: Int)
