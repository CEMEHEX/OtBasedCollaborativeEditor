package ot.client.fsm

import ot.client.command.ApplyOperationLocally
import ot.client.command.NoCommand
import ot.client.command.OperationApplicationCommand
import ot.client.command.SendOperationToServer
import ot.service.Operation
import ot.service.OperationsManager
import ot.util.transformAgainstEach

class AwaitConfirmationState<T, O : Operation<T>>(
    override val clientFSM: ClientFSM<T, O>,
    private val operationsManager: OperationsManager<O>,
    private val pendingOperations: List<O>
) : State<T, O> {

    private fun addOperation(operation: O): AwaitConfirmationState<T, O> =
        AwaitConfirmationState(clientFSM, operationsManager, pendingOperations + operation)

    constructor(
        clientFSM: ClientFSM<T, O>,
        operationsManager: OperationsManager<O>,
        pendingOperation: O
    ) : this(clientFSM, operationsManager, listOf(pendingOperation))

    override fun processLocalOperation(operation: O): OperationApplicationCommand<O> {
        clientFSM.state = addOperation(operation)
        return NoCommand()
    }

    override fun processRemoteOperation(operation: O): OperationApplicationCommand<O> = when {
        operation.id == pendingOperations[0].id -> {
            val restOperations = pendingOperations.drop(1)
            when {
                restOperations.isEmpty() -> {
                    clientFSM.state = SynchronizedState(clientFSM, operationsManager, operation.revision)
                    NoCommand()
                }
                else -> {
                    clientFSM.state = AwaitConfirmationState(clientFSM, operationsManager, restOperations)
                    SendOperationToServer(restOperations[0])
                }
            }
        }
        else -> {
            val transformedPendingOperations = pendingOperations.map { pendingOperation ->
                operationsManager.transformAgainst(pendingOperation, operation)
            }
            clientFSM.state = AwaitConfirmationState(clientFSM, operationsManager, transformedPendingOperations)

            val transformedOperationFromServer = pendingOperations
                .transformAgainstEach(operation, operationsManager)
            ApplyOperationLocally(transformedOperationFromServer)
        }
    }
}