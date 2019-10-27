package ot.service

import ot.entity.Document

interface DocumentUpdater<T, D : Document<T>> {

    fun updateRevision(document: D, revision: Int): D

    fun updateContent(document: D, content: T): D

    fun updateContentAndRevision(document: D, content: T, revision: Int): D

}