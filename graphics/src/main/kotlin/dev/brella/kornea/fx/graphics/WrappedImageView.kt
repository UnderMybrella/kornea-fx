package dev.brella.kornea.fx.graphics

import javafx.scene.image.ImageView

class WrappedImageView: ImageView() {
    override fun minWidth(height: Double): Double = 40.0
    override fun minHeight(width: Double): Double = 40.0

    override fun maxWidth(height: Double): Double = 16384.0
    override fun maxHeight(width: Double): Double = 16384.0

    override fun prefWidth(height: Double): Double = image?.width ?: minWidth(height)
    override fun prefHeight(width: Double): Double = image?.height ?: minHeight(width)

    override fun isResizable(): Boolean = true
    override fun resize(width: Double, height: Double) {
        fitWidth = width
        fitHeight = height
    }

    init {
        isPreserveRatio = true
    }
}