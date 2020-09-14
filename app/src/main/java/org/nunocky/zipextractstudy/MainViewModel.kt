package org.nunocky.zipextractstudy

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.security.MessageDigest
import java.util.*
import java.util.zip.ZipFile

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val _message1 = MutableLiveData("")
    val message1: LiveData<String> = _message1

    private val _message2 = MutableLiveData("")
    val message2: LiveData<String> = _message2

    private val _message3 = MutableLiveData("")
    val message3: LiveData<String> = _message3

    fun extraZip() {
        clearMessages()

        var datBytes: ByteArray? = null
        var md5Comp = ""

        val filename = "sample.zip"

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val file = File(downloadDir, filename)

        if (!file.exists()) {
            _message1.value = "sample.zip not found"
            return
        }

        val zipFile = ZipFile(file)

        for (entry in zipFile.entries()) {
            when (entry.name) {
                "sample.dat" -> {
                    val stream = zipFile.getInputStream(entry)
                    datBytes = stream.readBytes()
                }
                "sample.md5" -> {
                    val stream = zipFile.getInputStream(entry)
                    val bytes = stream.readBytes()
                    val line = String(bytes)
                    val ary = line.split(" ")

                    md5Comp = ary.first()
                }
            }
        }
        zipFile.close()

        when {
            datBytes == null -> {
                _message1.value = "sample.dat not found"
            }

            md5Comp.isEmpty() -> {
                _message1.value = "sample.md5 not found"
            }

            else -> {
                val md5sum = calcMD5Sum(datBytes)
                _message1.value = md5sum
                _message2.value = md5Comp
                _message3.value = if (md5sum == md5Comp) {
                    "match :-D"
                } else {
                    "not match :-O"
                }
            }
        }
    }

    fun clearMessages() {
        _message1.value = ""
        _message2.value = ""
        _message3.value = ""
    }
}


// Utils

private fun calcMD5Sum(bytes: ByteArray): String {
    val md = MessageDigest.getInstance("MD5")
    return md.digest(bytes).toHex().toLowerCase(Locale.US)
}

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

private fun ByteArray.toHex(): String {
    val result = StringBuffer()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS[firstIndex])
        result.append(HEX_CHARS[secondIndex])
    }

    return result.toString().toLowerCase(Locale.US)
}