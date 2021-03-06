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

@file:JvmName("PaymentOptionExtensions")

package ru.yandex.money.android.sdk.impl.extensions

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import ru.yandex.money.android.sdk.AbstractWallet
import ru.yandex.money.android.sdk.GooglePay
import ru.yandex.money.android.sdk.LinkedCard
import ru.yandex.money.android.sdk.NewCard
import ru.yandex.money.android.sdk.PaymentOption
import ru.yandex.money.android.sdk.R
import ru.yandex.money.android.sdk.SbolSmsInvoicing
import ru.yandex.money.android.sdk.Wallet
import ru.yandex.money.android.sdk.impl.metrics.TokenizeSchemeBankCard
import ru.yandex.money.android.sdk.impl.metrics.TokenizeSchemeGooglePay
import ru.yandex.money.android.sdk.impl.metrics.TokenizeSchemeLinkedCard
import ru.yandex.money.android.sdk.impl.metrics.TokenizeSchemeSbolSms
import ru.yandex.money.android.sdk.impl.metrics.TokenizeSchemeWallet

internal fun PaymentOption.getIcon(context: Context) = checkNotNull(
    AppCompatResources.getDrawable(
        context, when (this) {
            is NewCard -> R.drawable.ym_ic_add_card
            is Wallet, is AbstractWallet -> R.drawable.ym_ic_yamoney
            is LinkedCard -> brand.getIconResId()
            is SbolSmsInvoicing -> R.drawable.ym_ic_sberbank
            is GooglePay -> R.drawable.ym_ic_google_pay
        }
    )?.apply { setBounds(0, 0, intrinsicWidth, intrinsicHeight) }
) { "icon not found for $this" }

internal fun PaymentOption.getTitle(context: Context): CharSequence = when (this) {
    is NewCard -> context.getText(R.string.ym_payment_option_new_card)
    is Wallet -> userName
    is AbstractWallet -> context.getText(R.string.ym_payment_option_yamoney)
    is LinkedCard -> name.takeUnless(String?::isNullOrEmpty) ?: pan.chunked(4).joinToString(" ")
    is SbolSmsInvoicing -> context.getText(R.string.ym_sberbank)
    is GooglePay -> context.getText(R.string.ym_payment_option_google_pay)
}.let {
    it.takeIf { this is Wallet }?.let {
        val drawable = AppCompatResources.getDrawable(context, R.drawable.ym_ic_exit)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(context, R.color.ym_button_text_link))
        SpannableStringBuilder.valueOf(it).append("   ").apply {
            setSpan(imageSpan, lastIndex - 1, lastIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(colorSpan, 0, lastIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    } ?: it
}

internal fun PaymentOption.getAdditionalInfo() = (this as? Wallet)?.balance?.let {
    SpannableStringBuilder.valueOf(it.format()).apply {
        setSpan(StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

internal fun PaymentOption.toTokenizeScheme() = when (this) {
    is Wallet, is AbstractWallet -> TokenizeSchemeWallet()
    is LinkedCard -> TokenizeSchemeLinkedCard()
    is NewCard -> TokenizeSchemeBankCard()
    is SbolSmsInvoicing -> TokenizeSchemeSbolSms()
    is GooglePay -> TokenizeSchemeGooglePay()
}
