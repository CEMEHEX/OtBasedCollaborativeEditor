package client.fsm

import ot.Operation

interface ClientFSM<T, O : Operation<T>> {
    var state: State<T, O>
}