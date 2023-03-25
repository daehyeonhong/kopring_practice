package com.practice.kopring.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)
