package ot.entity

data class PlainTextDocument(
    override val uuid: String,
    override val revision: Int,
    override val content: String) : Document<String>