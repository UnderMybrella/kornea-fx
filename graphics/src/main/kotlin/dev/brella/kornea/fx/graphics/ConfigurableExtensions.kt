package dev.brella.kornea.fx.graphics

import dev.brella.kornea.fx.base.Configurable
import javafx.scene.Node
import javafx.scene.layout.BorderPane

public inline fun <T : Node> BorderPane.center(configurable: Configurable<T>, block: T.() -> Unit) {
    center = configurable(block)
}