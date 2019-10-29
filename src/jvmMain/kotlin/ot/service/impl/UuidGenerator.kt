package ot.service.impl

import ot.service.IdGenerator
import java.util.*

class UuidGenerator : IdGenerator<String>{
    override fun generateId(): String = UUID.randomUUID().toString()
}