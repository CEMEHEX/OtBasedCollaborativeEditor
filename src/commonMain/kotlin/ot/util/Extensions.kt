package ot.util

import ot.service.Operation
import ot.service.OperationsManager

fun <O : Operation<*>> Collection<O>.transformAgainstEach(
    operation: O,
    operationsManager: OperationsManager<O>
): O = this.fold(operation) { result, currentOperation ->
    operationsManager.transformAgainst(result, currentOperation)
}