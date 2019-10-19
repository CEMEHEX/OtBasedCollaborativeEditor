package client.command

import ot.Operation

sealed class OperationApplicationCommand<O : Operation<*>>

data class ApplyOperationLocally<O : Operation<*>>(
    val operation: O
) : OperationApplicationCommand<O>()

data class SendOperationToServer<O : Operation<*>>(
    val operation: O
) : OperationApplicationCommand<O>()

class NoCommand<O : Operation<*>> : OperationApplicationCommand<O>()