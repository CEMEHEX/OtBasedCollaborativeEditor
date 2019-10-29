package ot.fsm

import ot.command.ApplyOperationLocally
import ot.command.NoCommand
import ot.command.OperationApplicationCommand
import ot.command.SendOperationToServer
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
        console.log("AwaitConfirmation state: processing local operation $operation")
        clientFSM.state = addOperation(operation)
        return NoCommand()
    }

    override fun processRemoteOperation(operation: O): OperationApplicationCommand<O> = when {
        operation.uuid == pendingOperations[0].uuid -> {
            console.log("AwaitConfirmation state: processing remote operation from this client $operation")
            val restOperations = pendingOperations.drop(1)
            when {
                restOperations.isEmpty() -> {
                    clientFSM.state = SynchronizedState(clientFSM, operationsManager)
                    NoCommand<O>()
                }
                else -> {
                    clientFSM.state = AwaitConfirmationState(clientFSM, operationsManager, restOperations)
                    SendOperationToServer(restOperations[0])
                }
            }
        }
        else -> {
            console.log("AwaitConfirmation state: processing remote operation from other client $operation")
            val transformedPendingOperations = pendingOperations.map { pendingOperation ->
                operationsManager.transformAgainst(pendingOperation, operation)
            }
            clientFSM.state = AwaitConfirmationState(clientFSM, operationsManager, transformedPendingOperations)

            val transformedOperationFromServer = pendingOperations
                .transformAgainstEach(operation, operationsManager)
            ApplyOperationLocally(transformedOperationFromServer)
        }
    }.also { ++clientFSM.revision }
}