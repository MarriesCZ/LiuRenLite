package com.marries.liurenlite.liuren

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

data class TimeYang(
    val year: Int, val month: Int, val day: Int, val hour: Int
) {
    val calendar: Calendar
        @SuppressLint("SimpleDateFormat") get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd-hh")
            val cal = Calendar.getInstance()
            cal.time = sdf.parse("$year-$month-$day-$hour")!!
            return cal
        }

    override fun toString() = "${year}年${month}月${day}日${hour}时"
}

data class TimeYin(val yearYin: Int, val monthYin: Int, val dayYin: Int, val hourYin: Int) {

    override fun toString(): String {
        // 农历年
        var year = ""
        var tempYear = yearYin
        year += NUM[tempYear / 1000]
        tempYear %= 1000
        year += NUM[tempYear / 100]
        tempYear %= 100
        year += NUM[tempYear / 10]
        tempYear %= 10
        year += NUM[tempYear]

        return "${year}年${getMDH()}"
    }

    fun getMDH(): String {
        return "${MONTH_NUM[monthYin]}月${MONTH_DAY_NUM[dayYin]}${ZHI[hourYin]}时"
    }
}

fun TimeYang.toTimeYin(): TimeYin {
    val DAY_TIME = 1000 * 60 * 60 * 24L

    val baseDate = SimpleDateFormat("yyyy-MM-dd").parse("1900-1-31")
    //求出和1900年1月31日相差的天数
    var gapDays = ((calendar.time.time - baseDate!!.time) / DAY_TIME).toInt()
    // 用gapDays减去每农历年的天数
    // 计算当天是农历第几天
    // _year 最终结果是农历的年份
    // daysOfYear 是当年的第几天
    var _year = 1900
    var daysOfYear = 0
    while (_year < 2050 && gapDays > 0) {
        daysOfYear = yearYinDays(_year)
        gapDays -= daysOfYear
        _year++
    }
    if (gapDays < 0) {
        gapDays += daysOfYear
        _year--
    }

    val leapMonth = monthRunNum(_year) // 闰哪个月,1-12
    var leap = false
    // 用当年的天数gapDays, 逐个减去每月（农历）的天数，求出当天是本月的第几天
    var _month = 1
    var daysOfMonth = 0
    while (_month < 13 && gapDays > 0) {
        //闰月
        if ((leapMonth > 0 && _month == (leapMonth + 1)) && !leap) {
            --_month
            leap = true
            daysOfMonth = monthRunDays(_year)
        } else {
            daysOfMonth = monthYinDays(_year, _month)
        }
        gapDays -= daysOfMonth
        //解除闰月
        if (leap && _month == leapMonth + 1) {
            leap = false
        }
        _month++
    }
    // gapDays为0时，并且刚才计算的月份是闰月，要校正
    if (gapDays == 0 && leapMonth > 0 && _month == leapMonth + 1) {
        if (leap) {
            leap = false
        } else {
            leap = true
            --_month
        }
    }
    // gapDays小于0时，也要校正
    if (gapDays < 0) {
        gapDays += daysOfMonth
        --_month
    }
    return TimeYin(_year, _month, gapDays + 1, getTimeYin(hour))
}

/**
 * 子时 23.00－1.00, 丑时 1.00－3.00, 寅时 3.00－5.00, 卯时 5.00－7.00,
 * 辰时 7.00－9.00, 巳时 9.00－11.00, 午时 11.00－13.00, 未时 13.00－15.00
 * 申时 15.00－17.00, 酉时 17.00－19.00, 戌时 19.00－21.00, 亥时 21.00－23.00
 */
private fun getTimeYin(hourYang: Int) = when (hourYang) {
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

@SuppressLint("SimpleDateFormat")
fun getCurrentTime(): TimeYang {
    val timeStr = SimpleDateFormat("yyyy-MM-dd-HH").format(Date())
    val times = timeStr.split("-")
    return TimeYang(
        Integer.valueOf(times[0]),
        Integer.valueOf(times[1]),
        Integer.valueOf(times[2]),
        Integer.valueOf(times[3])
    )
}