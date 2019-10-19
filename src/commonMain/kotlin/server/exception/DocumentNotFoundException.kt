package server.exception

class DocumentNotFoundException(id: Long) : Exception("No document found with id = $id")