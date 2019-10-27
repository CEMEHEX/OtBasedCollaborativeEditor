package ot.service.impl

import ot.entity.PlainTextDocument
import ot.service.DocumentUpdater

class PlainTextDocumentUpdater : DocumentUpdater<String, PlainTextDocument> {
    override fun updateContent(
        document: PlainTextDocument,
        content: String
    ): PlainTextDocument = document.copy(content = content)

    override fun updateRevision(
        document: PlainTextDocument,
        revision: Int
    ): PlainTextDocument = document.copy(revision = revision)

    override fun updateContentAndRevision(
        document: PlainTextDocument,
        content: String,
        revision: Int
    ): PlainTextDocument = document.copy(content = content, revision = revision)
}