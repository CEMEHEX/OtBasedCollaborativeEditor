package ot

/**
 * Action that can be applied to transform some content
 *
 * @param T content type, which can be transformed by this operation
 */
interface Operation<T> {

    /**
     * Unique identifier, can be used for matching operations
     */
    val id: Long

    // TODO separate operation and revision
    /**
     * Revision from which this operation created
     */
    val revision: Int

    /**
     * Applies operation to content
     *
     * @param content some content
     *
     * @return result of operation application
     */
    fun applyTo(content: T): T

}