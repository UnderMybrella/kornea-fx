package dev.brella.kornea.fx.controls.tree

import dev.brella.kornea.fx.base.config
import javafx.beans.WeakInvalidationListener
import javafx.scene.Node
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.layout.HBox
import javafx.util.StringConverter
import java.lang.ref.WeakReference

abstract class BaseTreeCell<T>(protected val transformer: (T) -> String = DEFAULT_TRANSFORMER) : TreeCell<T>() {
    companion object {
        public val DEFAULT_TRANSFORMER = Any?::toString
    }

    protected var hbox = config { HBox(3.0) }

    private var treeItemRef: WeakReference<TreeItem<T>>? = null

    private val weakTreeItemGraphicListener = WeakInvalidationListener { updateDisplay(item, isEmpty) }
    private val weakTreeItemListener = WeakInvalidationListener {
        val oldTreeItem = treeItemRef?.get()
        oldTreeItem?.graphicProperty()?.removeListener(weakTreeItemGraphicListener)
        val newTreeItem = treeItem
        if (newTreeItem != null) {
            newTreeItem.graphicProperty().addListener(weakTreeItemGraphicListener)
            treeItemRef = WeakReference<TreeItem<T>>(newTreeItem)
        }

        treeItemInvalidated(oldTreeItem, newTreeItem)
    }

    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        updateDisplay(item, empty)
    }


    open fun treeItemInvalidated(oldTreeItem: TreeItem<T>?, newTreeItem: TreeItem<T>?) {}

    /** Default impl is pulled from [javafx.scene.control.cell.DefaultTreeCell] */
    open fun updateDisplay(item: T?, empty: Boolean = this.isEmpty, editing: Boolean = this.isEditing) {
        if (item == null || empty) {
            hbox.clear()

            text = null
            setGraphic(null)
        } else {
            // update the graphic if one is set in the TreeItem
            val treeItem = treeItem
            if (treeItem != null && treeItem.graphic != null) {
                if (item is Node) {
                    text = null

                    // the item is a Node, and the graphic exists, so
                    // we must insert both into an HBox and present that
                    // to the user (see RT-15910)
                    graphic = hbox {
                        children.setAll(treeItem.graphic, item)
                    }
                } else {
                    hbox.clear()

                    text = transformer(item)
                    graphic = treeItem.graphic
                }
            } else {
                hbox.clear()

                if (item is Node) {
                    text = null
                    graphic = item
                } else {
                    text = transformer(item)
                    graphic = null
                }
            }
        }
    }

    init {
        treeItemProperty().addListener(weakTreeItemListener)
        if (treeItem != null) {
            treeItem.graphicProperty().addListener(weakTreeItemGraphicListener)
        }
    }
}