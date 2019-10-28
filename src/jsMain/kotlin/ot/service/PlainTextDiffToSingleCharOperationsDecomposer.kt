package ot.service

import ot.external.diff_match_patch
import ot.service.impl.DeleteOperation
import ot.service.impl.InsertOperation
import ot.service.impl.PlainTextSingleCharacterOperation

class PlainTextDiffToSingleCharOperationsDecomposer(
    private val diffMatchPatch: diff_match_patch,
    private val idGenerator: IdGenerator<Long>
) : DiffToOperationsDecomposer<String, PlainTextSingleCharacterOperation> {

    override fun diffToOperationList(
        initialContent: String,
        changedContent: String
    ): List<PlainTextSingleCharacterOperation> = diffMatchPatch
        .diff_main(initialContent, changedContent)
        .map(::toDiff)
        .fold(emptyList<PlainTextSingleCharacterOperation>() to 0) { (result, cursor), diff ->
            val (nextResult, nextCursor) = diffEntryToOperationList(diff, cursor)
            (result + nextResult) to nextCursor
        }.first


    /**
     * Helper method for processing diff list
     *
     * @param diff difference entry
     * @param cursor cursor position
     * @return pair of operations list corresponding to this diff and new cursor position
     */
    private fun diffEntryToOperationList(
        diff: Diff,
        cursor: Int
    ): Pair<List<PlainTextSingleCharacterOperation>, Int> = when (diff.diffType) {
        DiffType.DELETION -> diff.content.map {
            DeleteOperation(
                idGenerator.generateId(),
                -1,
                cursor,
                it
            )
        } to cursor
        DiffType.EQUALITY -> emptyList<PlainTextSingleCharacterOperation>() to cursor + diff.content.length
        DiffType.INSERTION -> diff.content.withIndex().map {
            InsertOperation(
                idGenerator.generateId(),
                -1,
                cursor + it.index,
                it.value
            )
        } to cursor + diff.content.length
    }

    private fun toDiff(rawDiff: Array<dynamic>): Diff {
        check(rawDiff.size == 2) { "Invalid diff array size: ${rawDiff.size}, must be 2" }
        val (rawDiffType, rawContent) = rawDiff
        check(rawContent is String) { "Invalid content type: ${rawDiffType::class.simpleName}, must be String" }
        val diffType = when (rawDiffType) {
            -1 -> DiffType.DELETION
            0 -> DiffType.EQUALITY
            1 -> DiffType.INSERTION
            else -> throw IllegalArgumentException("Invalid diffType $rawDiffType, must be -1, 0 or 1")
        }
        return Diff(diffType, rawContent as String)
    }

}

private data class Diff(
    val diffType: DiffType,
    val content: String
)

private enum class DiffType {
    DELETION,
    EQUALITY,
    INSERTION
}