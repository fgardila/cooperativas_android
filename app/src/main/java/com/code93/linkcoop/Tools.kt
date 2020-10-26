package com.code93.linkcoop

import android.app.Dialog
import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.google.android.material.button.MaterialButton

object Tools {

    internal enum class NameFields {
        bitmap,
        message_code,
        transaction_code,
        reference,
        account_number,
        primary_account,
        secondary_account,
        transaction_amount,
        debit_amount,
        credit_amount,
        reversal_credit_amount,
        reversal_debit_amount,
        commision_amount,
        supercargo_amount,
        taxe_iva,
        taxe_isa,
        taxe_isd,
        taxe_rte,
        other_taxe,
        other_amount,
        ledger_balance,
        available_balance,
        business_date,
        switch_date_time,
        adquirer_date_time,
        device_date_time,
        issuer_date_time,
        limit_date,
        switch_sequence,
        adquirer_sequence,
        device_sequence,
        adquirer_region,
        adquirer_county,
        adquirer_city,
        issuer_institution_id,
        adquirer_institution_id,
        terminal_id,
        operator_id,
        supervisor_id,
        branch_id,
        terminal_location,
        channel_id,
        reversal_indicator,
        service_code,
        authorization_code,
        response_code,
        currency_code,
        issuer_institution_number,
        adquirer_institution_number,
        record_names, source_names,
        target_names, phone_number,
        card_info, original_data,
        buffer_data, security_data,
        token_data, product_id,
        email_address,
        acct_type,
        user_id,
        password,
        emv_data,
        validator_data
    }

    enum class Casos{
        prueba,
        prueba2,
    }

    fun checkEmailAdress(email: String): Boolean {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun showDialogError(context: Context?, messaje: String?) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_warning)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val content = dialog.findViewById<TextView>(R.id.content)
        content.text = messaje
        (dialog.findViewById<View>(R.id.bt_close) as MaterialButton).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }
}