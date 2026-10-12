package com.example

import com.example.horuscode.engine.interpreter.HorusInterpreter
import com.example.horuscode.engine.parser.HorusParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HorusEngineUnitTest {

    @Test
    fun testHieroglyphicStartAndPrint() {
        val hieroglyphicCode = """
            ☥ بدء
            𓏛 "مرحباً بكم في شفرة حورس"
            ☥ ختام
        """.trimIndent()

        val (blocks, errors) = HorusParser.parseTextToBlocks(hieroglyphicCode)
        assertTrue(errors.isEmpty())
        assertEquals(3, blocks.size)

        val result = HorusInterpreter().execute(blocks)
        assertTrue(result.isSuccess)
        assertEquals(listOf("مرحباً بكم في شفرة حورس"), result.outputs)
    }

    @Test
    fun testHieroglyphicVariablesAndCondition() {
        val code = """
            ☥ بدء
            𓋹 النقاط = 20
            𓂀 النقاط > 10
            𓏛 "اجتياز بتفوق"
            وإلا
            𓏛 "إعادة المحاولة"
            ختام_الشرط
            ☥ ختام
        """.trimIndent()

        val (blocks, errors) = HorusParser.parseTextToBlocks(code)
        assertTrue("Expected no errors: $errors", errors.isEmpty())

        val result = HorusInterpreter().execute(blocks)
        assertTrue(result.isSuccess)
        assertEquals(listOf("اجتياز بتفوق"), result.outputs)
    }

    @Test
    fun testHieroglyphicRepeatLoop() {
        val code = """
            ☥ بدء
            𓆣 3
            𓏛 "كيميت"
            ختام_التكرار
            ☥ ختام
        """.trimIndent()

        val (blocks, errors) = HorusParser.parseTextToBlocks(code)
        assertTrue(errors.isEmpty())

        val result = HorusInterpreter().execute(blocks)
        assertTrue(result.isSuccess)
        assertEquals(listOf("كيميت", "كيميت", "كيميت"), result.outputs)
    }
}
