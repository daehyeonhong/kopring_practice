package com.practice.kopring.user.enumerate

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class StatusTests {
    companion object {
        @JvmStatic
        fun provideStringsForIsBlank(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(true, Status.SUCCESS), Arguments.of(false, Status.SIGNUP)
            )
        }
    }

    @ParameterizedTest
    @MethodSource(value = ["provideStringsForIsBlank"])
    fun statusTest(input: Boolean, status: Status): Unit {
        val actual: Status = Status.of(input)
        Assertions.assertEquals(status, actual)
        Assertions.assertEquals(status.description, actual.description)
    }

    @ParameterizedTest
    @EnumSource(
        value = Status::class,
        names = ["SUCCESS", "SIGNUP"]
    )
    fun statusTest1(status: Status): Unit {
        val actual: Status = Status.of(status == Status.SUCCESS)
        Assertions.assertEquals(status, actual)
        Assertions.assertEquals(status.description, actual.description)
    }

    @Test
    fun statusTest2(): Unit {
        val actual: Status = Status.of(true)
        Assertions.assertEquals(Status.SUCCESS, actual)
        Assertions.assertEquals(Status.SUCCESS.description, actual.description)
    }

    @Test
    fun statusTest3(): Unit {
        val actual: Status = Status.of(false)
        Assertions.assertEquals(Status.SIGNUP, actual)
        Assertions.assertEquals(Status.SIGNUP.description, actual.description)
    }
}
