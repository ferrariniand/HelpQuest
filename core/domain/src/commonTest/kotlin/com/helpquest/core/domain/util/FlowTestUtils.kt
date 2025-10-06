package com.helpquest.core.domain.util

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.turbineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * Use when needing to test the state and side effect [Flow]s of a viewmodel at the same time.
 * This method will run [ReceiveTurbine] helpers on both in the same test coroutine scope by utilizing
 * [runTest] internally.
 *
 * When only one flow needs to be exercised in a test case use [test] or [testIn] directly.
 *
 * @param state the state of the viewmodel
 * @param effect the side effect of the viewmodel
 * @param block the lambda block to run the test code in, allows access to the [TestScope] from [runTest]
 */
fun <S, E> runViewModelTest(
    state: Flow<S>,
    effect: Flow<E>,
    block: suspend TestScope.(state: ReceiveTurbine<S>, effect: ReceiveTurbine<E>) -> Unit
) = runTest {
    turbineScope {
        val turbineState = state.testIn(this)
        val turbineEffect = effect.testIn(this)
        block(turbineState, turbineEffect)
        turbineState.cancel()
        turbineEffect.cancel()
    }
}