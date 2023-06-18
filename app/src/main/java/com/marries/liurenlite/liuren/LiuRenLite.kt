package com.marries.liurenlite.liuren

/**
 * 小六壬
 */
class LiuRenLite {

    private val timeYang: TimeYang = getCurrentTime()
    private val timeYin: TimeYin = timeYang.toTimeYin()
    private val month = timeYin.monthYin
    private val day = timeYin.dayYin
    private val hour = timeYin.hourYin

    fun getGua(): GUA {
        return GUA.values()[(month + day - 1 + hour - 1) % 6]
    }

    fun getGuaWithDayAndHour(): String {
        val dayIndex = (month + day - 1) % 6
        val gua = GUA.values()[dayIndex]
        val hourIndex = (month + day - 1 + hour - 1) % 6
        return gua.dayAndHour[hourIndex]
    }

    fun getMHD() =
        "${GUA.values()[month % 6].title} ${GUA.values()[(month + day - 1) % 6].title} ${GUA.values()[(month + day - 1 + hour - 1) % 6].title}"

    fun getTime() = timeYang.toString()

    fun getTimeYin() = timeYin.getMDH()
}