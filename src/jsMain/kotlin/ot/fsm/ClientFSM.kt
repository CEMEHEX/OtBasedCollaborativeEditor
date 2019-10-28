package ot.fsm

import ot.service.Operation

interface ClientFSM<T, O : Operation<T>> {
    var state: State<T, O>
}