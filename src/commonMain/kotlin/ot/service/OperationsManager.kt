package ot.service

/**
 * Generic manager which can perform additional actions with operations
 *
 * @param O managed operations type
 */
interface OperationsManager<O : Operation<*>> {

    /**
     * Transforms one operation against other such that
     * op2'.applyTo(op1.applyTo(content)) === op1'.applyTo(op2.applyTo(content))
     * where op1' = transformAgainst(op1, op2)
     *       op2' = transformAgainst(op2, op1)
     *
     */
    fun transformAgainst(operation1: O, operation2: O): O

    /**
     * Inverts operation such that content = invert(op).applyTo(op.applyTo(content))
     *
     */
    fun invert(operation: O): O

    /**
     * Changes operation's revision number to specified one
     *
     */
    fun changeRevision(operation: O, newRevision: Int): O

}