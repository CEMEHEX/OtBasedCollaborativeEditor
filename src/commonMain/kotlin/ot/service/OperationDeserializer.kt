package ot.service

interface OperationDeserializer<O : Operation<*>, R> {
    fun deserialize(rawData: R): O
}