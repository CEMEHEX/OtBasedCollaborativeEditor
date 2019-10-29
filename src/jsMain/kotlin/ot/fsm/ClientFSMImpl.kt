package ot.fsm

import ot.service.Operation

class ClientFSMImpl<T, O : Operation<T>>(
    override var state: State<T, O>,
    override var revision: Int
) : ClientFSM<T, O>