package ot.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import ot.exception.DocumentNotFoundException

@ControllerAdvice
class ControllerAdviseExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DocumentNotFoundException::class)
    fun handle(documentNotFoundException: DocumentNotFoundException) {
        // Return 404 then document not found
    }
}