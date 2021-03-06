/*
 * The MIT License (MIT)
 * Copyright © 2018 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.yandex.money.android.sdk.impl.userAuth

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import ru.yandex.money.android.sdk.AuthorizedUser
import ru.yandex.money.android.sdk.userAuth.UserAuthCancelledOutputModel
import ru.yandex.money.android.sdk.userAuth.UserAuthNoWalletOutputModel
import ru.yandex.money.android.sdk.userAuth.UserAuthSuccessOutputModel

class UserAuthPresenterTest {

    private val presenter = UserAuthPresenter()

    @Test
    fun `should return UserAuthSuccessViewModel when UserAuthSuccessOutputModel`() {
        // prepare
        val user = AuthorizedUser("name")
        val outputModel = UserAuthSuccessOutputModel(user)

        // invoke
        val viewModel = presenter(outputModel)

        // assert
        assertThat(viewModel, equalTo(UserAuthSuccessViewModel(user) as UserAuthViewModel))
    }

    @Test
    fun `should return UserAuthCancelledViewModel when UserAuthCancelledOutputModel`() {
        // prepare
        val outputModel = UserAuthCancelledOutputModel()

        // invoke
        val viewModel = presenter(outputModel)

        // assert
        assertThat(viewModel, equalTo(UserAuthCancelledViewModel as UserAuthViewModel))
    }

    @Test
    fun `should return UserAuthNoWalletViewModel when UserAuthNoWalletOutputModel`() {
        // prepare
        val accountName = "name"
        val outputModel = UserAuthNoWalletOutputModel(accountName)

        // invoke
        val viewModel = presenter(outputModel)

        // assert
        assertThat(viewModel, equalTo(UserAuthNoWalletViewModel(accountName) as UserAuthViewModel))
    }
}
