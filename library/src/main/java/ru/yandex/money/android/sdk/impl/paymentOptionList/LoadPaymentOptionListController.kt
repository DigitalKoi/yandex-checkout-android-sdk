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

package ru.yandex.money.android.sdk.impl.paymentOptionList

import ru.yandex.money.android.sdk.Amount
import ru.yandex.money.android.sdk.BaseController
import ru.yandex.money.android.sdk.Presenter
import ru.yandex.money.android.sdk.UseCase
import ru.yandex.money.android.sdk.ViewModel
import ru.yandex.money.android.sdk.payment.loadOptionList.PaymentOptionListInputModel
import ru.yandex.money.android.sdk.payment.loadOptionList.PaymentOptionListOutputModel

internal class LoadPaymentOptionListController(
        private val paymentOptionListUseCase: UseCase<PaymentOptionListInputModel, PaymentOptionListOutputModel>,
        private val paymentOptionListPresenter: Presenter<PaymentOptionListOutputModel, PaymentOptionListViewModel>,
        private val progressPresenter: Presenter<Unit, PaymentOptionListProgressViewModel>,
        private val errorPresenter: Presenter<Exception, PaymentOptionListFailViewModel>,
        resultConsumer: (ViewModel) -> Unit,
        logger: (String, Exception?) -> Unit
) : BaseController<PaymentOptionListInputModel, PaymentOptionListOutputModel, PaymentOptionListViewModel>(
        resultConsumer = resultConsumer,
        logger = logger
) {

    override val progressViewModel: PaymentOptionListProgressViewModel
        get() = progressPresenter(Unit)

    override fun useCase(inputModel: Amount) = paymentOptionListUseCase(inputModel)

    override fun presenter(outputModel: PaymentOptionListOutputModel) = paymentOptionListPresenter(outputModel)

    override fun exceptionPresenter(e: Exception) = errorPresenter(e)
}
