package ot.service

/**
 * @param O operation type
 * @param R type to serialize in
 */
interface OperationSerializer<O : Operation<*>, R> {
    fun serialize(operation: O): R
}