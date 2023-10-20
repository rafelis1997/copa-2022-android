package me.dio.copa.catar.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale

fun <T> Flow<T>.observe(owner: LifecycleOwner, observe: (T) -> Unit) {
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observe.collect(observe)
        }
    }
}

val Arrangement.Reverse: Arrangement.Horizontal
    get() = ReverseArrangement

object ReverseArrangement : Arrangement.Horizontal {

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        layoutDirection: LayoutDirection,
        outPositions: IntArray
    ) = if (layoutDirection == LayoutDirection.Ltr) {
        placeRightOrBottom(totalSize, sizes, outPositions, reverseInput = true)
    } else {
        placeLeftOrTop(sizes, outPositions, reverseInput = false)
    }

    // Had to copy function from sources because it is marked internal
    private fun placeRightOrBottom(
        totalSize: Int,
        size: IntArray,
        outPosition: IntArray,
        reverseInput: Boolean
    ) {
        val consumedSize = size.fold(0) { a, b -> a + b }
        var current = totalSize - consumedSize
        size.forEachIndexed(reverseInput) { index, it ->
            outPosition[index] = current
            current += it
        }
    }

    // Had to copy function from sources because it is marked internal
    private fun placeLeftOrTop(size: IntArray, outPosition: IntArray, reverseInput: Boolean) {
        var current = 0
        size.forEachIndexed(reverseInput) { index, it ->
            outPosition[index] = current
            current += it
        }
    }

    // Had to copy function from sources because it is marked private
    private inline fun IntArray.forEachIndexed(reversed: Boolean, action: (Int, Int) -> Unit) {
        if (!reversed) {
            forEachIndexed(action)
        } else {
            for (i in (size - 1) downTo 0) {
                action(i, get(i))
            }
        }
    }

}

fun String.capitalized(): String {
    return this.lowercase().replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}