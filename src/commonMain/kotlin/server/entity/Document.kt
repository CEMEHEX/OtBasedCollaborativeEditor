package server.entity

/**
 * Abstract document
 *
 * @param T content type
 */
data class Document<T>(
    val id: Long,
    val revision: Int,
    val content: T
)