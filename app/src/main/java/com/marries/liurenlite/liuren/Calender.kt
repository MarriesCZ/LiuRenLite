package com.marries.liurenlite.liuren

import com.nlf.calendar.Lunar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class Calender {

    class Yang {
        val year: Int
        val month: Int
        val day: Int
        val hour: Int

        override fun toString() = "${year}年${month}月${day}日${hour}时"

        init {
            SimpleDateFormat("yyyy-MM-dd-HH", Locale.CHINA).format(Date()).split("-").also {
                year = Integer.valueOf(it[0])
                month = Integer.valueOf(it[1])
                day = Integer.valueOf(it[2])
                hour = Integer.valueOf(it[3])
            }
        }
    }

    class Yin {

        private val lunar = Lunar(Date())
        val month: Int
        val day: Int
        val hour: Int

        override fun toString(): String = lunar.toString()

        fun getMDH(): String {
            lunar.let {
                return "${it.monthInChinese}月${it.dayInChinese}${hour.toLunarHour()}"
            }
        }

        init {
            lunar.let {
                month = it.month
                day = it.day
                // 公历小时要转换成农历时辰
                hour = when (it.hour) {
                    in 1..2 -> 2
                    in 3..4 -> 3
                    in 5..6 -> 4
                    in 7..8 -> 5
                    in 9..10 -> 6
                    in 11..12 -> 7
                    in 13..14 -> 8
                    in 15..16 -> 9
                    in 17..18 -> 10
                    in 19..20 -> 11
                    in 21..22 -> 12
                    else -> 1
                }
            }
        }
    }
}

/**
 * 子时 23.00－1.00, 丑时 1.00－3.00, 寅时 3.00－5.00, 卯时 5.00－7.00,
 * 辰时 7.00－9.00, 巳时 9.00－11.00, 午时 11.00－13.00, 未时 13.00－15.00
 * 申时 15.00－17.00, 酉时 17.00－19.00, 戌时 19.00－21.00, 亥时 21.00－23.00
 */
private fun Int.toLunarHour(): String {
    val ZHI = arrayOf("", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
    return if (this in 1..12) {
        "${ZHI[this]}时"
    } else {
        "不存在 $this 时"
    }
}