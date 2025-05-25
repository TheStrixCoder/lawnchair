package app.lawnchair.smartspace

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import app.lawnchair.views.CustomTextView
import com.android.launcher3.icons.GraphicsUtils.setColorAlphaBound
import com.android.launcher3.views.ShadowInfo


open class DoubleShadowTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : CustomTextView(context, attrs) {

    private val shadowInfo = ShadowInfo.Companion.fromContext(context, attrs, 0)

    init {
        setShadowLayer(shadowInfo.ambientShadowBlur, 0f, 0f, shadowInfo.ambientShadowColor)
    }

    override fun onDraw(canvas: Canvas) {
        // If text is transparent or shadow alpha is 0, don't draw any shadow
        if (skipDoubleShadow()) {
            super.onDraw(canvas)
            return
        }

        // We enhance the shadow by drawing the shadow twice
        paint.setShadowLayer(shadowInfo.ambientShadowBlur, 0f, 0f, shadowInfo.ambientShadowColor)

        super.onDraw(canvas)
        canvas.save()
        canvas.clipRect(
            scrollX,
            scrollY + extendedPaddingTop,
            scrollX + width,
            scrollY + height,
        )

        paint.setShadowLayer(
            shadowInfo.keyShadowBlur,
            shadowInfo.keyShadowOffsetX,
            shadowInfo.keyShadowOffsetY,
            shadowInfo.keyShadowColor,
        )
        super.onDraw(canvas)
        canvas.restore()
    }

    private fun skipDoubleShadow(): Boolean {
        val textAlpha: Int = Color.alpha(currentTextColor)
        val keyShadowAlpha: Int = Color.alpha(shadowInfo.keyShadowColor)
        val ambientShadowAlpha: Int = Color.alpha(shadowInfo.ambientShadowColor)
        if (textAlpha == 0 || (keyShadowAlpha == 0 && ambientShadowAlpha == 0)) {
            paint.clearShadowLayer()
            return true
        } else if (ambientShadowAlpha > 0 && keyShadowAlpha == 0) {
            paint.setShadowLayer(
                shadowInfo.ambientShadowBlur, 0F, 0F,
                getTextShadowColor(shadowInfo.ambientShadowColor, textAlpha),
            )
            return true
        } else if (keyShadowAlpha > 0 && ambientShadowAlpha == 0) {
            paint.setShadowLayer(
                shadowInfo.keyShadowBlur,
                shadowInfo.keyShadowOffsetX,
                shadowInfo.keyShadowOffsetY,
                getTextShadowColor(shadowInfo.keyShadowColor, textAlpha),
            )
            return true
        } else {
            return false
        }
    }

    private fun getTextShadowColor(shadowColor: Int, textAlpha: Int): Int {
        return setColorAlphaBound(
            shadowColor,
            Math.round(Color.alpha(shadowColor) * textAlpha / 255f),
        )
    }

}
