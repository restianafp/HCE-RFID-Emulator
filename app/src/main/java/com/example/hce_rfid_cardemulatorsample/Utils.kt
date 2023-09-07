package com.example.hce_rfid_cardemulatorsample

import android.util.Log

class Utils {

    companion object {


        private val HEX_CHARS = "0123456789ABCDEF"
        var APDURESPONSE = byteArrayOfInts(0x90, 0x00)
        var QRIS_DATA = byteArrayOfInts(0x00, 0x01, 0x90, 0x00)
        var ERROR = byteArrayOfInts(0x00, 0x01)
        var INPUT_DATA = "a1n0.1nd0n351a.2023.qr15.t4p"

        fun hexStringToByteArray(data: String) : ByteArray {

            val result = ByteArray(data.length / 2)

            for (i in data.indices step 2) {
                val firstIndex = HEX_CHARS.indexOf(data[i]);
                val secondIndex = HEX_CHARS.indexOf(data[i + 1]);

                val octet = firstIndex.shl(4).or(secondIndex)
                result.set(i.shr(1), octet.toByte())
            }

            return result
        }

        private val HEX_CHARS_ARRAY = "0123456789ABCDEF".toCharArray()
        fun toHex(byteArray: ByteArray) : String {
            val result = StringBuffer()

            byteArray.forEach {
                val octet = it.toInt()
                val firstIndex = (octet and 0xF0).ushr(4)
                val secondIndex = octet and 0x0F
                result.append(HEX_CHARS_ARRAY[firstIndex])
                result.append(HEX_CHARS_ARRAY[secondIndex])
            }

            return result.toString()
        }

        fun toBinaryCodedDecimal(string: String): ByteArray {
            return string.toByteArray()
        }

        fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

        fun toHexString(byteArray: ByteArray?): String {
            return byteArray?.joinToString("") { "%02X".format(it) }.toString()
        }

    }

}