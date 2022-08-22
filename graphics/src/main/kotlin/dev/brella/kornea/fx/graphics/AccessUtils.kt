package dev.brella.kornea.fx.graphics

import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableValue
import kotlin.reflect.KProperty

public inline operator fun <T> ObservableValue<T>.getValue(thisRef: Any?, property: KProperty<*>): T =
    value

//public inline operator fun <T> WritableValue<T>.getValue(thisRef: Any?, property: KProperty<*>): T =
//    value

public inline operator fun <T> WritableValue<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    Platform.runLater { setValue(value) }
}