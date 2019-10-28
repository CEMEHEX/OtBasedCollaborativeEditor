package ot.service

/**
 * Generator of ids with specific type
 * @param ID id type
 */
interface IdGenerator<ID> {
    /**
     * Generate next id
     */
    fun generateId(): ID
}