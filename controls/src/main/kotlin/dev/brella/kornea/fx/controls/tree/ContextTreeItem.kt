package dev.brella.kornea.fx.controls.tree

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.ContextMenu
import javafx.scene.control.TreeItem

import dev.brella.kornea.fx.graphics.getValue
import dev.brella.kornea.fx.graphics.setValue

public open class ContextTreeItem<T>(value: T): TreeItem<T>(value) {
    val contextMenuProperty: ObjectProperty<ContextMenu?> = SimpleObjectProperty(null)
    public var contextMenu: ContextMenu? by contextMenuProperty
}