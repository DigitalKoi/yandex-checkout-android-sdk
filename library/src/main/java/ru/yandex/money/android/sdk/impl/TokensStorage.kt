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

package ru.yandex.money.android.sdk.impl

import android.content.SharedPreferences
import ru.yandex.money.android.sdk.impl.extensions.set
import ru.yandex.money.android.sdk.payment.CheckPaymentAuthRequiredGateway
import ru.yandex.money.android.sdk.paymentAuth.PaymentAuthTokenGateway
import ru.yandex.money.android.sdk.userAuth.UserAuthTokenGateway

private const val KEY_USER_AUTH_TOKEN = "userAuthToken"
private const val KEY_PAYMENT_AUTH_TOKEN = "paymentAuthToken"

internal class TokensStorage(
    private val preferences: SharedPreferences,
    private val encrypt: (String) -> String,
    private val decrypt: (String) -> String
) : CheckPaymentAuthRequiredGateway, UserAuthTokenGateway, PaymentAuthTokenGateway {

    private var _paymentAuthToken: String? = null

    override var userAuthToken: String?
        get() = preferences.getString(KEY_USER_AUTH_TOKEN, null)?.let(decrypt)
        set(value) {
            preferences[KEY_USER_AUTH_TOKEN] = value?.let(encrypt)
        }


    override var paymentAuthToken: String?
        get() = _paymentAuthToken ?: preferences.getString(KEY_PAYMENT_AUTH_TOKEN, null)?.let(decrypt)
        set(value) {
            _paymentAuthToken = value
            if (value == null) {
                preferences[KEY_PAYMENT_AUTH_TOKEN] = null
            }
        }

    override val isPaymentAuthPersisted: Boolean
        get() = KEY_PAYMENT_AUTH_TOKEN in preferences

    override fun persistPaymentAuth() {
        preferences[KEY_PAYMENT_AUTH_TOKEN] = paymentAuthToken?.let(encrypt)
    }

    override fun checkPaymentAuthRequired() =
        _paymentAuthToken == null && preferences.getString(KEY_PAYMENT_AUTH_TOKEN, null) == null

    fun reset() {
        _paymentAuthToken = null
    }
}
