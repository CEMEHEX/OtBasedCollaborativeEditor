package ot.service

import io.mockk.every
import io.mockk.mockk
import ot.service.impl.DeleteOperation
import ot.service.impl.IdentityOperation
import ot.service.impl.InsertOperation
import ot.service.impl.PlainTextOperationsManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlainTextOperationsManagerTest {

    private val idGenerator = mockk<IdGenerator<String>>()

    init {
        every { idGenerator.generateId() } returns "uuid doesn't matter in this test"
    }

    private val operationsManager = PlainTextOperationsManager(idGenerator)

    @Test
    fun transformAgainst_InsertAgainstInsertFstPositionLtSnd_Unchanged() {
        val op1 = InsertOperation("", 0, 'x')
        val op2 = InsertOperation("", 2, 'y')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(op1, op1Transformed)
    }

    @Test
    fun transformAgainst_InsertAgainstInsertFstPositionGtThenSnd_MovedRight() {
        val op1 = InsertOperation("", 2, 'x')
        val op2 = InsertOperation("", 0, 'y')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(InsertOperation("", 3, 'x'), op1Transformed)
    }

    @Test
    fun transformAgainst_InsertAgainstInsertFstPositionEqThenSnd_MovedRight() {
        val op1 = InsertOperation("", 2, 'x')
        val op2 = InsertOperation("", 2, 'y')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(InsertOperation("", 3, 'x'), op1Transformed)
    }

    @Test
    fun transformAgainst_InsertAgainstDeleteFstPositionLtSnd_Unchanged() {
        val op1 = InsertOperation("", 0, 'x')
        val op2 = DeleteOperation("", 2, 'c')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(op1, op1Transformed)
    }

    @Test
    fun transformAgainst_InsertAgainstDeleteFstPositionGtThenSnd_MovedLeft() {
        val op1 = InsertOperation("", 2, 'x')
        val op2 = DeleteOperation("", 0, 'a')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(InsertOperation("", 1, 'x'), op1Transformed)
    }

    @Test
    fun transformAgainst_InsertAgainstDeleteFstPositionEqThenSnd_Unchanged() {
        val op1 = InsertOperation("", 2, 'x')
        val op2 = DeleteOperation("", 2, 'c')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(op1, op1Transformed)
    }

    @Test
    fun transformAgainst_DeleteAgainstInsertFstPositionLtSnd_Unchanged() {
        val op1 = DeleteOperation("", 0, 'a')
        val op2 = InsertOperation("", 2, 'x')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(op1, op1Transformed)
    }

    @Test
    fun transformAgainst_DeleteAgainstInsertFstPositionGtThenSnd_MovedRight() {
        val op1 = DeleteOperation("", 2, 'c')
        val op2 = InsertOperation("", 0, 'x')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(DeleteOperation("", 3, 'c'), op1Transformed)
    }

    @Test
    fun transformAgainst_DeleteAgainstInsertFstPositionEqThenSnd_MovedRight() {
        val op1 = DeleteOperation("", 2, 'c')
        val op2 = InsertOperation("", 2, 'y')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(DeleteOperation("", 3, 'c'), op1Transformed)
    }

    @Test
    fun transformAgainst_DeleteAgainstDeleteFstPositionLtSnd_Unchanged() {
        val op1 = DeleteOperation("", 0, 'a')
        val op2 = DeleteOperation("", 2, 'c')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(op1, op1Transformed)
    }

    @Test
    fun transformAgainst_DeleteAgainstDeleteFstPositionGtThenSnd_MovedLeft() {
        val op1 = DeleteOperation("", 2, 'c')
        val op2 = DeleteOperation("", 0, 'a')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertEquals(DeleteOperation("", 1, 'c'), op1Transformed)
    }

    @Test
    fun transformAgainst_DeleteAgainstDeleteFstPositionEqThenSnd_Identity() {
        val op1 = DeleteOperation("", 2, 'c')
        val op2 = DeleteOperation("", 2, 'c')

        val op1Transformed = operationsManager.transformAgainst(op1, op2)

        assertTrue { op1Transformed is IdentityOperation }
    }

}
