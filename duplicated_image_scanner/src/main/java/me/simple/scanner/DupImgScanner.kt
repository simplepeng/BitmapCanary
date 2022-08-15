package me.simple.scanner

import sun.security.provider.MD5
import java.io.File
import java.io.FileInputStream
import java.security.DigestInputStream
import java.security.MessageDigest

val HEX_DIGITS = charArrayOf(
    '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
)
val md = MessageDigest.getInstance("MD5")

fun main(args: Array<String>) {
    val dirPath = mutableListOf<String>()
    val hashMap = hashMapOf<String, String>()

    dirPath.add("/Users/simple/Desktop/workspace/android/sleepsignin/app/src/main/res/drawable-xxhdpi")

    if (args.isEmpty()) {
        dirPath.add(File("").absolutePath)
    } else {
        dirPath.addAll(args)
    }

    println(dirPath)

    dirPath.forEach { path ->
        val dir = File(path)
        if (dir.isFile) return

        dir.listFiles().orEmpty().forEach { file ->

        }
    }
}

fun bytes2HexString(bytes: ByteArray?): String {
    if (bytes == null) return ""
    val len = bytes.size
    if (len <= 0) return ""
    val ret = CharArray(len shl 1)
    var i = 0
    var j = 0
    while (i < len) {
        ret[j++] = HEX_DIGITS[bytes[i].toInt() shr 4 and 0x0f]
        ret[j++] = HEX_DIGITS[bytes[i].toInt() and 0x0f]
        i++
    }
    return String(ret)
}

fun encryptMD5File(file: File): ByteArray? {
    var fis: FileInputStream? = null
    val digestInputStream: DigestInputStream

    try {
        fis = FileInputStream(file)
        var md = MessageDigest.getInstance("MD5")
        digestInputStream = DigestInputStream(fis, md)
        val buffer = ByteArray(256 * 1024)
        while (true) {
            if (digestInputStream.read(buffer) <= 0) break
        }
        md = digestInputStream.messageDigest
        return md.digest()
    } catch (e:Throwable){
        e.printStackTrace()
    }finally {
        fis?.close()
    }

    return null
}
