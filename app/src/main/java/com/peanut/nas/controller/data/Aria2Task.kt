package com.peanut.nas.controller.data

import androidx.annotation.Keep

@Keep
data class Aria2Task(
    val bitfield: String,
    val completedLength: String,
    val connections: String,
    val dir: String,
    val downloadSpeed: String,
    val errorCode: String? = null,
    val errorMessage: String? = null,
    val files: List<File>,
    val gid: String,
    val numPieces: String,
    val pieceLength: String,
    val status: String,
    val totalLength: String,
    val uploadLength: String,
    val uploadSpeed: String
) {
    val downloadSpeedDesc: String get() = getFileLengthDesc(this.downloadSpeed.toLong()) + "/s"
    val downloadSize: String get() = getFileLengthDesc(this.completedLength.toLong())
    val totalSize: String get() = getFileLengthDesc(this.totalLength.toLong())
    val downloadPercent: Float get() {
        if (totalLength.toLong() == 0L)
            return 0f
        return completedLength.toLong() / totalLength.toLong().toFloat()
    }
        val state: Aria2DownloadState get() = Aria2DownloadState.parse(this.status)
        val errorCodeMessage: String get() {
        if (errorCodeMap.containsKey(errorCode?.toInt()?:-1)){
            return errorCodeMap[errorCode!!.toInt()]?:"?"
        }
        return this.errorMessage?:""
    }
        val remainTimeDesc: String
        get() {
            if (downloadSpeed.toLong() == 0L) return ""
            val remainSize: Long = totalLength.toLong() - completedLength.toLong()
            val remainSeconds: Double = remainSize / (downloadSpeed.toLong().toDouble())
            return remainSeconds.second2TimeDesc()
        }

        override fun hashCode(): Int {
            return gid.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Aria2Task
            if (gid != other.gid) return false
            return true
        }

        companion object {
        var errorCodeMap = HashMap<Int, String>(32).apply {
            this[0] = ""
            this[1] = "unknown error occurred."
            this[2] = "time out occurred."
            this[3] = "resource was not found."
            this[4] = "resource was not found. See --max-file-not-found option."
            this[5] = "resource was not found. See --lowest-speed-limit option."
            this[6] = "network problem occurred."
            this[7] = "unfinished download."
            this[8] = "remote server did not support resume when resume was required to complete download."
            this[9] = "there was not enough disk space available."
            this[10] = "piece length was different from one in .aria2 control file. See --allow-piece-length-change option."
            this[11] = "aria2 was downloading same file at that moment."
            this[12] = "aria2 was downloading same info hash torrent at that moment."
            this[13] = "file already existed. See --allow-overwrite option."
            this[14] = "renaming file failed. See --auto-file-renaming option."
            this[15] = "aria2 could not open existing file."
            this[16] = "aria2 could not create new file or truncate existing file."
            this[17] = "I/O error occurred."
            this[18] = "aria2 could not create directory."
            this[19] = "name resolution failed."
            this[20] = "could not parse Metalink document."
            this[21] = "FTP command failed."
            this[22] = "HTTP response header was bad or unexpected."
            this[23] = "too many redirections occurred."
            this[24] = "HTTP authorization failed."
            this[25] = "aria2 could not parse bencoded file(usually .torrent file)."
            this[26] = ".torrent file was corrupted or missing information that aria2 needed."
            this[27] = "Magnet URI was bad."
            this[28] = "bad/unrecognized option was given or unexpected option argument was given."
            this[29] = "the remote server was unable to handle the request due to a temporary overloading or maintenance."
            this[30] = "aria2 could not parse JSON-RPC request."
        }
    }
}
@Keep
data class File(
    val completedLength: String,
    val index: String,
    val length: String,
    val path: String,
    val selected: String,
    val uris: List<Uri>
) {
    fun getFileName(): String {
        return java.io.File(this.path).name
    }
}
@Keep
data class Uri(
    val status: String,
    val uri: String
)

fun getFileLengthDesc(length: Long): String {
    return when {
        length.shr(40) >= 1.0 -> String.format("%.2f ", length / 1_099_511_627_776) + "TB"
        length.shr(30) >= 1.0 -> String.format("%.2f ", length / 1_073_741_824.0) + "GB"
        length.shr(20) >= 1.0 -> String.format("%.2f ", length / 1_048_576.0) + "MB"
        length.shr(10) >= 1.0 -> String.format("%.2f ", length / 1024.0) + "KB"
        else -> String.format("%.2f ", length / 1.0) + "B"
    }
}

fun Double.second2TimeDesc(): String {
    val temp = this.toInt()
    val hh = temp / 3600
    val mm = temp % 3600 / 60
    val ss = temp % 3600 % 60
    return when {
        hh > 0 -> "${hh} 小时 ${"0".repeat(if (mm < 10) 1 else 0)}${mm} 分 ${"0".repeat(if (ss < 10) 1 else 0)}${ss} 秒"
        mm > 0 -> "${mm} 分 ${"0".repeat(if (ss < 10) 1 else 0)}${ss} 秒"
        else -> "${ss} 秒"
    }
}

val EXAMPLE_ACTIVE = Aria2Task(
    bitfield = "fffffffffffffffffffffffffff8000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ffffffffffffffffffffffffffffffffffffffffffffffffffffffc00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001ffff00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000fffffffffffffffffffffffffffffffffffffffffffffffffffff80000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    completedLength = "585547776",
    connections = "2",
    dir = "/home/pi/MediaServerGo/_disk_manager_dir/NAS600/Arrow S05",
    downloadSpeed = "301016",
    files = listOf(
        File(
            completedLength = "584056832",
            index = "1",
            length = "5768434140",
            path = "/home/pi/MediaServerGo/_disk_manager_dir/NAS600/Arrow S05/Arrow.S05E10.Who.Are.You.1080p.BluRay.REMUX.AVC.DTS-HD.MA.5.1-NOGRP.mkv",
            selected = "true",
            uris = listOf(
                Uri(
                    status = "used",
                    uri = "https://vod1492-aliyun06-vip-lixian.xunlei.com/download/?fid=VKIcyNGiLsA5KY*90HPi9mE2U-bcUdNXAQAAADSp5rnIXpcqEPlyUVZCKXNmxc3r&mid=666&threshold=251&tid=5068E2996301AAA4B4B92AC4572002E2&srcid=0&verno=2&pk=xdrive&e=1669043135&g=34A9E6B9C85E972A10F972515642297366C5CDEB&i=54A21CC8D1A22EC039298FBDD073E2F6613653F6&ui=571708815&t=0&hy=0&ms=153600&th=153600&pt=1&f=5768434140&alt=0&pks=652&rts=&spr=flow&clientid=Xp6vsxz_7IYVw2BB&fileid=VNHMvAn20qbrs93XA7QIlzwBA1&userid=571708815&cliplugver=&projectid=2rvk4e3gkdnl7u1kl0k&vip=FREE&clientver=7.55.0.8286&fext=mkv&at=C758683F8FD24F7D266BEBF71296FF48"
                )
            )
        )
    ),
    gid = "6a7725352d39e126",
    numPieces = "5502",
    pieceLength = "1048576",
    status = "active",
    totalLength = "5768434140",
    uploadLength = "0",
    uploadSpeed = "0"
)