package ot.web.controller

import org.springframework.web.bind.annotation.*
import ot.entity.Document
import ot.entity.PlainTextDocument
import ot.repository.DocumentRepository

@RestController
@RequestMapping("/document")
class DocumentController(
    private val documentRepository: DocumentRepository<PlainTextDocument>
) {
    @GetMapping("{documentId}")
    fun getDocument(@PathVariable documentId: String): Document<String> =
        documentRepository.getDocumentByUUID(documentId)

    @GetMapping("all")
    fun getDocumentsList() : List<PlainTextDocument> = documentRepository.getDocumentsList()


    @PostMapping
    fun postDocument(@RequestBody document: PlainTextDocument) = documentRepository.save(document)
}