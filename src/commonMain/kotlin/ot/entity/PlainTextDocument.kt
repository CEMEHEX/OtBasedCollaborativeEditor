package ot.entity

data class PlainTextDocument(
    override val uuid: String,
    override val revision: Int,
    override val content: String,
    val title: String
) : Document<String>