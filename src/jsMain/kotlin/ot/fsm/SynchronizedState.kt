package ot.fsm

import ot.command.ApplyOperationLocally
import ot.command.OperationApplicationCommand
import ot.command.SendOperationToServer
import ot.service.Operation
import ot.service.OperationsManager

class SynchronizedState<T, O : Operation<T>>(
    override val clientFSM: ClientFSM<T, O>,
    private val operationsManager: OperationsManager<O>
) : State<T, O> {

    override fun processLocalOperation(operation: O): OperationApplicationCommand<O> {
        console.log("Synchronized state: processing local operation $operation")
        clientFSM.state = AwaitConfirmationState(clientFSM, operationsManager, operation)
        return SendOperationToServer(operation)
    }

    override fun processRemoteOperation(operation: O): OperationApplicationCommand<O> {
        console.log("Synchronized state: processing remote operation $operation")
        clientFSM.state = SynchronizedState(clientFSM, operationsManager)
        ++clientFSM.revision
        return ApplyOperationLocally(operation)
    }
}