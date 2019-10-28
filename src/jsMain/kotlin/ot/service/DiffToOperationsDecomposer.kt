package ot.service

/**
 * Service for decomposing content change to operations list
 *
 * @param T content type
 * @param O operation type
 */
interface DiffToOperationsDecomposer<T, O : Operation<T>> {

    /**
     * @param initialContent initial content
     * @param changedContent changed content
     * @return list of operations that must be applied to initial content to get changed content
     */
    fun diffToOperationList(initialContent: T, changedContent: T): List<O>
}