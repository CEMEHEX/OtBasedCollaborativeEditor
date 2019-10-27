package ot.client.fsm

import ot.service.Operation

class ClientFSMImpl<T, O : Operation<T>>(
    override var state: State<T, O>
) : ClientFSM<T, O>