package ot.entity

data class PlainTextDocument(
    override val id: Long,
    override val revision: Int,
    override val content: String) : Document<String>