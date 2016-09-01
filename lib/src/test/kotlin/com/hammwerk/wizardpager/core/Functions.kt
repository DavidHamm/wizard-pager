package com.hammwerk.wizardpager.core

import org.mockito.Mockito.mock

inline fun <reified T : Any> mock(): T = mock(T::class.java)