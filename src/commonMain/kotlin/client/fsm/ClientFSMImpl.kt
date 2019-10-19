package client.fsm

import ot.Operation
import ot.OperationsManager
import server.entity.Document

class ClientFSMImpl<T, O : Operation<T>>(
    override var state: State<T, O>
) : ClientFSM<T, O>