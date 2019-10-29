package ot.entity

/**
 * Abstract document
 *
 * @param T content type
 */
interface Document<T> {
    val uuid: String
    val revision: Int
    val content: T
}