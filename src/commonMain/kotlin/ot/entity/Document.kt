package ot.entity

/**
 * Abstract document
 *
 * @param T content type
 */
interface Document<T> {
    val id: Long
    val revision: Int
    val content: T
}