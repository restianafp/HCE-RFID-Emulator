package com.example.hce_rfid_cardemulatorsample

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHostApduService : HostApduService() {

    private var n = 1

    // The `processCommandApdu` method will be called every time a card
    // reader sends an APDU command that is filtered by our manifest filter.
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        Log.d("layon.f", "MyHostApduService processCommandApdu(commandApdu = ${Utils.toHexString(commandApdu)}, extras = $extras)")
        var msg : String

        //LiveDataManager.updateMessage("HostApduService processCommandApdu() start...")
        msg = "${n++} - HostApduService processCommandApdu(commandApdu = ${Utils.toHexString(commandApdu)}, extras = $extras) start...\n"

        var response : ByteArray

        if (commandApdu == null) {
            msg += "\"${n++} - HostApduService processCommandApdu() commandApdu == null\n"
            response = Utils.hexStringToByteArray(STATUS_FAILED)
        }

        val hexCommandApdu = Utils.toHex(commandApdu)
        response = if (hexCommandApdu.substring(10, 24) == AID[0] || hexCommandApdu.substring(10,24) == AID[1])  {
            when (hexCommandApdu.substring(0,8)) {
                HEADER[0] -> {
                    Utils.APDURESPONSE
                }
                HEADER[1] -> {
                    Utils.toBinaryCodedDecimal(Utils.INPUT_DATA) + Utils.QRIS_DATA
                }
                else -> {
                    Utils.ERROR
                }
            }
        } else {
            Utils.hexStringToByteArray(STATUS_FAILED)
        }

        msg += "${n++} - HostApduService processCommandApdu() return : ${Utils.toHexString(response)}\n"

        msg += "${n++} - HostApduService processCommandApdu() ending...\n"


        LiveDataManager.updateMessage(msg)

        return response
    }

    // The `onDeactiveted` method will be called when the a different
    // AID has been selected or the NFC connection has been lost
    override fun onDeactivated(reason: Int) {
        val reason = "HostApduService onDeactivated(reason = ${
            when (reason) {
                0 -> "DEACTIVATION_LINK_LOSS"
                1 -> "DEACTIVATION_DESELECTED"
                else -> "UNKNOWN"
            }
        }"
        Log.d(TAG, reason)
        LiveDataManager.updateMessage("${n++} - $reason\n")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MyHostApduService onCreate()")
        LiveDataManager.updateMessage("${n++} - HostApduService onCreate()\n")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "MyHostApduService onStartCommand intent: $intent, flags: $flags, startId: $startId")
        LiveDataManager.updateMessage("${n++} - HostApduService onStartCommand intent: $intent, flags: $flags, startId: $startId\n")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MyHostApduService onDestroy()")
        LiveDataManager.updateMessage("${n++} - HostApduService onDestroy()\n\n")
    }

    companion object {
        val TAG = "layon.f"
        val STATUS_FAILED = "Fail"
        val AID = arrayListOf("F0010203040506", "A0000006022020")
        val HEADER = arrayListOf("00A40400", "00B20100")
    }

}