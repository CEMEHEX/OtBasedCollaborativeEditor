package ot.fsm

import ot.service.Operation

class ClientFSMImpl<T, O : Operation<T>>(
    override var revision: Int
) : ClientFSM<T, O> {

    private var _state: State<T, O>? = null

    constructor(revision: Int, state: State<T, O>) : this(revision) {
        _state = state
    }

    override var state: State<T, O>
        get() = _state ?: throw IllegalStateException("State not initialized")
        set(value) {_state = value}
}