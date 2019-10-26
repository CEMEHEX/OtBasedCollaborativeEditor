package ot.web.controller

import org.springframework.web.bind.annotation.*
import ot.impl.DeleteOperation
import ot.impl.IdentityOperation
import ot.impl.InsertOperation
import ot.impl.PlainTextSingleCharacterOperation

@RestController
@RequestMapping("/")
class SampleController {
    @GetMapping
    fun sample(): List<PlainTextSingleCharacterOperation> = listOf(
        InsertOperation(
            42,
            1,
            3,
            'x'
        ),
        DeleteOperation(
            228,
            30,
            100500,
            'y'
        ),
        IdentityOperation(
            1488,
            777
        )
    )

    @PostMapping("parse")
    fun parseTest(@RequestBody opList: List<PlainTextSingleCharacterOperation>) {
        println("**********************************")
        System.err.println(opList)
    }
}