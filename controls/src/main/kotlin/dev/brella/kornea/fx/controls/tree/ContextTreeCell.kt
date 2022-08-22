package dev.brella.kornea.fx.controls.tree

import javafx.scene.control.TreeItem

abstract class ContextTreeCell<T>(transformer: (T) -> String = DEFAULT_TRANSFORMER): BaseTreeCell<T>(transformer) {
    override fun treeItemInvalidated(oldTreeItem: TreeItem<T>?, newTreeItem: TreeItem<T>?) {
        super.treeItemInvalidated(oldTreeItem, newTreeItem)

        if (newTreeItem is ContextTreeItem<T>) {
            this.contextMenuProperty().bind(newTreeItem.contextMenuProperty)
        } else {
            this.contextMenuProperty().unbind()
        }
    }
}