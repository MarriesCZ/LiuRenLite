package com.marries.liurenlite.liuren

/**
 * 小六壬
 */
class LiuRenLite {
    private val lunar = Calender.Yin()

    fun getGua(): GUA {
        with(lunar) {
            return GUA.values()[(month + day - 1 + hour - 1) % 6]
        }
    }

    fun getGuaWithDayAndHour(): String {
        with(lunar) {
            val dayIndex = (month + day - 1) % 6
            val gua = GUA.values()[dayIndex]
            val hourIndex = (month + day - 1 + hour - 1) % 6
            return gua.dayAndHour[hourIndex]
        }
    }

    fun getMHD(): String {
        with(lunar) {
            return "${GUA.values()[month % 6].title} ${GUA.values()[(month + day - 1) % 6].title} ${GUA.values()[(month + day - 1 + hour - 1) % 6].title}"
        }
    }

    fun getTimeYin() = lunar.getMDH()
}