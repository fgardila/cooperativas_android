package com.code93.linkcoop.xmlParsers

import android.util.Xml
import com.code93.linkcoop.models.FieldsTrx
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

object XmlParser {

    @JvmStatic
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(response: String, replyTag: String): FieldsTrx {
        if (response.contains(replyTag)) {
            val input: InputStream = ByteArrayInputStream(response.toByteArray())
            input.use { inputStream ->
                val parser: XmlPullParser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(inputStream, null)
                parser.nextTag()
                return read(parser, replyTag)
            }
        } else {
            val input: InputStream = ByteArrayInputStream(response.toByteArray())
            input.use { inputStream ->
                val parser: XmlPullParser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(inputStream, null)
                parser.nextTag()
                return read(parser, "default_reply_error")
            }
        }

    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun read(parser: XmlPullParser, replyTag: String): FieldsTrx {
        val fieldsTrx = FieldsTrx()
        parser.require(XmlPullParser.START_TAG, null, replyTag)
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "bitmap" -> fieldsTrx.bitmap = readData(parser, "bitmap")
                "message_code" -> fieldsTrx.message_code = readData(parser, "message_code")
                "transaction_code" -> fieldsTrx.transaction_code = readData(parser, "transaction_code")
                "reference" -> fieldsTrx.reference = readData(parser, "reference")
                "account_number" -> fieldsTrx.account_number = readData(parser, "account_number")
                "primary_account" -> fieldsTrx.primary_account = readData(parser, "primary_account")
                "secondary_account" -> fieldsTrx.secondary_account = readData(parser, "secondary_account")
                "transaction_amount" -> fieldsTrx.transaction_amount = readData(parser, "transaction_amount")
                "debit_amount" -> fieldsTrx.debit_amount = readData(parser, "debit_amount")
                "credit_amount" -> fieldsTrx.credit_amount = readData(parser, "credit_amount")
                "reversal_credit_amount" -> fieldsTrx.reversal_credit_amount = readData(parser, "reversal_credit_amount")
                "reversal_debit_amount" -> fieldsTrx.reversal_debit_amount = readData(parser, "reversal_debit_amount")
                "commision_amount" -> fieldsTrx.commision_amount = readData(parser, "commision_amount")
                "supercargo_amount" -> fieldsTrx.supercargo_amount = readData(parser, "supercargo_amount")
                "taxe_iva" -> fieldsTrx.taxe_iva = readData(parser, "taxe_iva")
                "taxe_isa" -> fieldsTrx.taxe_isa = readData(parser, "taxe_isa")
                "taxe_isd" -> fieldsTrx.taxe_isd = readData(parser, "taxe_isd")
                "taxe_rte" -> fieldsTrx.taxe_rte = readData(parser, "taxe_rte")
                "other_taxe" -> fieldsTrx.other_taxe = readData(parser, "other_taxe")
                "other_amount" -> fieldsTrx.other_amount = readData(parser, "other_amount")
                "ledger_balance" -> fieldsTrx.ledger_balance = readData(parser, "ledger_balance")
                "available_balance" -> fieldsTrx.available_balance = readData(parser, "available_balance")
                "business_date" -> fieldsTrx.business_date = readData(parser, "business_date")
                "switch_date_time" -> fieldsTrx.switch_date_time = readData(parser, "switch_date_time")
                "adquirer_date_time" -> fieldsTrx.adquirer_date_time = readData(parser, "adquirer_date_time")
                "device_date_time" -> fieldsTrx.device_date_time = readData(parser, "device_date_time")
                "issuer_date_time" -> fieldsTrx.issuer_date_time = readData(parser, "issuer_date_time")
                "limit_date" -> fieldsTrx.limit_date = readData(parser, "limit_date")
                "switch_sequence" -> fieldsTrx.switch_sequence = readData(parser, "switch_sequence")
                "adquirer_sequence" -> fieldsTrx.adquirer_sequence = readData(parser, "adquirer_sequence")
                "device_sequence" -> fieldsTrx.device_sequence = readData(parser, "device_sequence")
                "adquirer_region" -> fieldsTrx.adquirer_region = readData(parser, "adquirer_region")
                "adquirer_county" -> fieldsTrx.adquirer_county = readData(parser, "adquirer_county")
                "adquirer_city" -> fieldsTrx.adquirer_city = readData(parser, "adquirer_city")
                "issuer_institution_id" -> fieldsTrx.issuer_institution_id = readData(parser, "issuer_institution_id")
                "adquirer_institution_id" -> fieldsTrx.adquirer_institution_id = readData(parser, "adquirer_institution_id")
                "terminal_id" -> fieldsTrx.terminal_id = readData(parser, "terminal_id")
                "operator_id" -> fieldsTrx.operator_id = readData(parser, "operator_id")
                "supervisor_id" -> fieldsTrx.supervisor_id = readData(parser, "supervisor_id")
                "branch_id" -> fieldsTrx.branch_id = readData(parser, "branch_id")
                "terminal_location" -> fieldsTrx.terminal_location = readData(parser, "terminal_location")
                "channel_id" -> fieldsTrx.channel_id = readData(parser, "channel_id")
                "reversal_indicator" -> fieldsTrx.reversal_indicator = readData(parser, "reversal_indicator")
                "service_code" -> fieldsTrx.service_code = readData(parser, "service_code")
                "authorization_code" -> fieldsTrx.authorization_code = readData(parser, "authorization_code")
                "response_code" -> fieldsTrx.response_code = readData(parser, "response_code")
                "currency_code" -> fieldsTrx.currency_code = readData(parser, "currency_code")
                "issuer_institution_number" -> fieldsTrx.issuer_institution_number = readData(parser, "issuer_institution_number")
                "adquirer_institution_number" -> fieldsTrx.adquirer_institution_number = readData(parser, "adquirer_institution_number")
                "record_names" -> fieldsTrx.record_names = readData(parser, "record_names")
                "source_names" -> fieldsTrx.source_names = readData(parser, "source_names")
                "target_names" -> fieldsTrx.target_names = readData(parser, "target_names")
                "phone_number" -> fieldsTrx.phone_number = readData(parser, "phone_number")
                "card_info" -> fieldsTrx.card_info = readData(parser, "card_info")
                "original_data" -> fieldsTrx.original_data = readData(parser, "original_data")
                "buffer_data" -> fieldsTrx.buffer_data = readData(parser, "buffer_data")
                "security_data" -> fieldsTrx.security_data = readData(parser, "security_data")
                "token_data" -> fieldsTrx.token_data = readData(parser, "token_data")
                "product_id" -> fieldsTrx.product_id = readData(parser, "product_id")
                "email_address" -> fieldsTrx.email_address = readData(parser, "email_address")
                "acct_type" -> fieldsTrx.acct_type = readData(parser, "acct_type")
                "user_id" -> fieldsTrx.user_id = readData(parser, "user_id")
                "password" -> fieldsTrx.password = readData(parser, "password")
                "emv_data" -> fieldsTrx.emv_data = readData(parser, "emv_data")
                "validator_data" -> fieldsTrx.validator_data = readData(parser, "validator_data")
                else -> skip(parser)
            }
        }
        return fieldsTrx
    }

    private fun readData(parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, null, tag)
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, tag)
        return title
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}