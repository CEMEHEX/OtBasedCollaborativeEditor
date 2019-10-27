package ot.web.controller

import org.springframework.web.bind.annotation.*
import ot.entity.Document
import ot.entity.PlainTextDocument
import ot.service.DocumentStorageService

@RestController
@RequestMapping("/document")
class DocumentController(
    private val documentStorageService: DocumentStorageService<PlainTextDocument>
) {
    @GetMapping("{documentId}")
    fun getDocument(@PathVariable documentId: Long): Document<String> =
        documentStorageService.getDocumentById(documentId)


    @PostMapping
    fun postDocument(@RequestBody document: PlainTextDocument) = documentStorageService.save(document)
}