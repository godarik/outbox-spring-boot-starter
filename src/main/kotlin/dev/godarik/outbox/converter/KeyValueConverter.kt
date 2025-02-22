package dev.godarik.outbox.converter

import java.io.Serializable

interface KeyValueConverter<K : Serializable, V> {

    fun convertKey(key: Any): K

    fun convertValue(value: Any): V
}