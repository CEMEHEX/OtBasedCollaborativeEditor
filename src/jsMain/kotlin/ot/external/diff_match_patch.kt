package ot.external

@Suppress("ClassName", "FunctionName")
external class diff_match_patch {
    fun diff_main(text1: String,
                  text2: String,
                  opt_checklines: dynamic = definedExternally,
                  opt_deadline: dynamic = definedExternally
    ): Array<Array<dynamic>>
}