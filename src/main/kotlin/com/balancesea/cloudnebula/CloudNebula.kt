package com.balancesea.cloudnebula

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.platform.util.bukkitPlugin
import kotlin.math.roundToInt

/*
   █████████  ████                          █████
  ███░░░░░███░░███                         ░░███
 ███     ░░░  ░███   ██████  █████ ████  ███████
░███          ░███  ███░░███░░███ ░███  ███░░███
░███          ░███ ░███ ░███ ░███ ░███ ░███ ░███
░░███     ███ ░███ ░███ ░███ ░███ ░███ ░███ ░███
 ░░█████████  █████░░██████  ░░████████░░████████
  ░░░░░░░░░  ░░░░░  ░░░░░░    ░░░░░░░░  ░░░░░░░░



                               ██████   █████          █████                ████
                              ░░██████ ░░███          ░░███                ░░███
                               ░███░███ ░███   ██████  ░███████  █████ ████ ░███   ██████
                               ░███░░███░███  ███░░███ ░███░░███░░███ ░███  ░███  ░░░░░███
                               ░███ ░░██████ ░███████  ░███ ░███ ░███ ░███  ░███   ███████
                               ░███  ░░█████ ░███░░░   ░███ ░███ ░███ ░███  ░███  ███░░███
                               █████  ░░█████░░██████  ████████  ░░████████ █████░░████████
                              ░░░░░    ░░░░░  ░░░░░░  ░░░░░░░░    ░░░░░░░░ ░░░░░  ░░░░░░░░

 */
/*
艺术字渐变打印采用AI生成 不要在意喔
 */
object CloudNebula : Plugin() {

    override fun onEnable() {
        applyBlueGradient(BANNER).forEach { line ->
            console().sendMessage(line)
        }
    }
}

private val BANNER = """
    |   █████████  ████                          █████
    |  ███░░░░░███░░███                         ░░███
    | ███     ░░░  ░███   ██████  █████ ████  ███████
    |░███          ░███  ███░░███░░███ ░███  ███░░███
    |░███          ░███ ░███ ░███ ░███ ░███ ░███ ░███
    |░░███     ███ ░███ ░███ ░███ ░███ ░███ ░███ ░███
    | ░░█████████  █████░░██████  ░░████████░░████████
    |  ░░░░░░░░░  ░░░░░  ░░░░░░    ░░░░░░░░  ░░░░░░░░
    |
    |
    |
    |                               ██████   █████          █████                ████
    |                              ░░██████ ░░███          ░░███                ░░███
    |                               ░███░███ ░███   ██████  ░███████  █████ ████ ░███   ██████
    |                               ░███░░███░███  ███░░███ ░███░░███░░███ ░███  ░███  ░░░░░███
    |                               ░███ ░░██████ ░███████  ░███ ░███ ░███ ░███  ░███   ███████
    |                               ░███  ░░█████ ░███░░░   ░███ ░███ ░███ ░███  ░███  ███░░███
    |                               █████  ░░█████░░██████  ████████  ░░████████ █████░░████████
    |                              ░░░░░    ░░░░░  ░░░░░░  ░░░░░░░░    ░░░░░░░░ ░░░░░  ░░░░░░░░
    |                              CloudNebula | 物品库系统
    |                              作者: BalanceSea
    |                              交流群: 342097496
    |                              当前版本: ${bukkitPlugin.description.version}
""".trimMargin().lines()

/** 冰蓝 → 天空蓝 → 皇家蓝 → 靛蓝 → 深蓝 */
private val BLUE_STOPS = intArrayOf(0xA5F3FC, 0x38BDF8, 0x3B82F6, 0x1D4ED8, 0x1E3A8A)

private const val SECTION = '\u00A7'
private const val GRADIENT_STEPS = 32

private fun applyBlueGradient(lines: List<String>): List<String> {
    val width = lines.maxOf { it.length }.coerceAtLeast(1)
    val height = lines.size.coerceAtLeast(1)
    val xDenom = (width - 1).coerceAtLeast(1).toDouble()
    val yDenom = (height - 1).coerceAtLeast(1).toDouble()
    return lines.mapIndexed { row, line ->
        if (line.isBlank()) {
            return@mapIndexed line
        }
        buildString(line.length * 16) {
            var lastRgb = -1
            line.forEachIndexed { col, ch ->
                if (ch == ' ') {
                    append(' ')
                    return@forEachIndexed
                }
                val t = ((col / xDenom) * 0.7 + (row / yDenom) * 0.3)
                    .coerceIn(0.0, 1.0)
                    .let { ((it * GRADIENT_STEPS).roundToInt() / GRADIENT_STEPS.toDouble()) }
                var rgb = gradientRgb(t)
                if (ch == '░') {
                    rgb = darken(rgb, 0.62)
                }
                if (rgb != lastRgb) {
                    appendSectionHex(rgb)
                    lastRgb = rgb
                }
                append(ch)
            }
        }
    }
}

private fun gradientRgb(t: Double): Int {
    val last = BLUE_STOPS.lastIndex
    val scaled = t * last
    val index = scaled.toInt().coerceIn(0, last - 1)
    return lerpRgb(BLUE_STOPS[index], BLUE_STOPS[index + 1], scaled - index)
}

private fun lerpRgb(from: Int, to: Int, t: Double): Int {
    val r = lerpChannel(from shr 16, to shr 16, t)
    val g = lerpChannel(from shr 8, to shr 8, t)
    val b = lerpChannel(from, to, t)
    return (r shl 16) or (g shl 8) or b
}

private fun lerpChannel(from: Int, to: Int, t: Double): Int {
    val a = from and 0xFF
    val b = to and 0xFF
    return (a + (b - a) * t).roundToInt().coerceIn(0, 255)
}

private fun darken(rgb: Int, factor: Double): Int {
    val r = ((rgb shr 16 and 0xFF) * factor).roundToInt().coerceIn(0, 255)
    val g = ((rgb shr 8 and 0xFF) * factor).roundToInt().coerceIn(0, 255)
    val b = ((rgb and 0xFF) * factor).roundToInt().coerceIn(0, 255)
    return (r shl 16) or (g shl 8) or b
}

private fun StringBuilder.appendSectionHex(rgb: Int) {
    append(SECTION)
    append('x')
    String.format("%06X", rgb).forEach { digit ->
        append(SECTION)
        append(digit)
    }
}
