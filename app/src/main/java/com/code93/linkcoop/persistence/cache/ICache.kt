package com.code93.linkcoop.persistence.cache

interface ICache {
    fun put(key: String?, value: Any?)
    operator fun get(key: String?): Any?
    fun remove(key: String?)
    operator fun contains(key: String?): Boolean
    fun clear()
}