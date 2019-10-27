package ot.service.impl

import ot.service.Operation

/**
 * Simple operation on plain text content. Modifies one character per operation.
 *
 */
sealed class PlainTextSingleCharacterOperation : Operation<String>

/**
 * Inserts one character
 *
 * @property position position to insert character in
 * @property symbol symbol to insert
 */
data class InsertOperation(
    override val id: Long,
    override val revision: Int,
    val position: Int,
    val symbol: Char
) : PlainTextSingleCharacterOperation() {

    override fun applyTo(content: String): String =
        if (position < 0 || position > content.length)
            content
        else
            "${content.substring(0, position)}$symbol${content.substring(position)}"
}

/**
 * Deletes one character
 *
 * @property position position of symbol to delete
 * @property symbol symbol to delete
 */
data class DeleteOperation(
    override val id: Long,
    override val revision: Int,
    val position: Int,
    val symbol: Char
) : PlainTextSingleCharacterOperation() {

    override fun applyTo(content: String): String = when {
        (position < 0 || position >= content.length) -> content
        content[position] != symbol -> throw IllegalStateException(
            "Delete operation failed: expected symbol $symbol on position $position, found ${content[position]}"
        )
        else -> content.removeRange(position, position + 1)
    }

}

/**
 * Identity operation, do nothing with content
 *
 */
data class IdentityOperation(
    override val id: Long,
    override val revision: Int
) : PlainTextSingleCharacterOperation() {
    override fun applyTo(content: String): String = content
}