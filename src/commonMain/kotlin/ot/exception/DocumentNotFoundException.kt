package ot.exception

class DocumentNotFoundException(id: String) : Exception("No document found with id = $id")