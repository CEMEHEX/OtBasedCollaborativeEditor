package ot.service.impl

import ot.service.IdGenerator

class LongSequentialIdGenerator(private var initialId: Long = 0) : IdGenerator<Long> {
    override fun generateId() : Long = ++initialId
}