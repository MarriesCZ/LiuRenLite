package com.marries.liurenlite.ui.theme

import androidx.compose.ui.graphics.Color
import com.marries.liurenlite.liuren.GUA

data class ThemeColor(val background: Color, val textColor: Color)

enum class ColorTheme(private val themeColor: List<ThemeColor>) {
    KONG(
        arrayOf(
            ThemeColor(YaQing, black),
            ThemeColor(ZangQing, black),
            ThemeColor(Hui, black),
            ThemeColor(Mo, black)
        ).toList()
    ),
    AN(
        arrayOf(
            ThemeColor(YingBai, black),
            ThemeColor(LaoYin, black),
            ThemeColor(QinBai, black),
            ThemeColor(CangCui, black)
        ).toList()
    ),
    LIU(
        arrayOf(
            ThemeColor(QiuXiang, black),
            ThemeColor(ZhongHuang, black),
            ThemeColor(XingHuang, black),
            ThemeColor(JuHuang, black),
            ThemeColor(Jin, black),
            ThemeColor(EHuang, black),
            ThemeColor(YaHuang, black),
        ).toList()
    ),
    XI(
        arrayOf(
            ThemeColor(YingCao, black),
            ThemeColor(NenLv, black),
            ThemeColor(LiuLv, black),
            ThemeColor(CongLv, black),
            ThemeColor(YouLv, black),
            ThemeColor(LvShen, black)
        ).toList()
    ),
    CHI(
        arrayOf(
            ThemeColor(HuangLu, black),
            ThemeColor(Zhong, black),
            ThemeColor(HuPo, black),
            ThemeColor(ChiJin, black),
            ThemeColor(XingHong, black)
        ).toList()
    ),
    JI(
        arrayOf(
            ThemeColor(DingXiang, black),
            ThemeColor(OuHe, black),
            ThemeColor(ShuiHong, black)
        ).toList()
    );

    fun getThemeColor() = themeColor[(0..themeColor.lastIndex).random()]

    companion object {
        fun getColorTheme(gua: GUA) = when (gua) {
            GUA.KONG -> KONG
            GUA.AN -> AN
            GUA.LIU -> LIU
            GUA.XI -> XI
            GUA.CHI -> CHI
            GUA.JI -> JI
        }
    }
}