package client

import client.command.OperationApplicationCommand
import client.fsm.ClientFSM
import ot.Operation

class ClientDocumentManagerImpl<T, O : Operation<T>>(
        private val clientFSM: ClientFSM<T, O>
) : ClientDocumentManager<T, O> {


    override fun processLocalOperation(operation: O): OperationApplicationCommand<O> =
            clientFSM.state.processLocalOperation(operation)

    override fun processRemoteOperation(operation: O): OperationApplicationCommand<O> =
            clientFSM.state.processRemoteOperation(operation)


}