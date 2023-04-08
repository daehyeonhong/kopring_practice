package common

import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.Test

class CommonTests {
    companion object : Logging

    @Test
    fun logTest() {
        try {
            throw RuntimeException()
        } catch (exception: RuntimeException) {
            logger.error("qwe${exception.stackTrace}", exception)
        }
    }
}
