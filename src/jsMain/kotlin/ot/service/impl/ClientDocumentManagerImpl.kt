package ot.service.impl

import ot.command.OperationApplicationCommand
import ot.fsm.ClientFSM
import ot.service.ClientDocumentManager
import ot.service.Operation

class ClientDocumentManagerImpl<T, O : Operation<T>>(
        private val clientFSM: ClientFSM<T, O>
) : ClientDocumentManager<T, O> {


    override fun processLocalOperation(operation: O): OperationApplicationCommand<O> =
            clientFSM.state.processLocalOperation(operation)

    override fun processRemoteOperation(operation: O): OperationApplicationCommand<O> =
            clientFSM.state.processRemoteOperation(operation)


}