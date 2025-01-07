package info.agilite.boot.orm

import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.core.utils.MapUtils

@DslMarker
annotation class WhereDsl

@WhereDsl
class WhereBuilder {
    private var wheres = mutableListOf<Where>()

    fun eq(vararg values: Any) {
        wheres += WhereEquals(*values)
    }

    fun default(entityMetadata: EntityMetadata) {
        wheres += WhereDefault(entityMetadata)
    }

    fun simple(filter: String, vararg params: Any?) {
        wheres += WhereSimple(filter, MapUtils.newStringMap(*params))
    }

    fun isNotNull(field: String) {
        wheres += WhereNotNull(field)
    }

    fun isNull(field: String) {
        wheres += WhereNull(field)
    }

    fun and(block: WhereBuilder.() -> Unit) {
        val andConditions = WhereBuilder().apply(block).build()
        wheres += WhereAnd(*andConditions.toTypedArray())
    }

    fun or(block: WhereBuilder.() -> Unit) {
        val orConditions = WhereBuilder().apply(block).build()
        wheres += WhereOr(*orConditions.toTypedArray())
    }

    fun build(): List<Where> = wheres
}

fun where(builder: WhereBuilder.() -> Unit): Where {
    val conditions = WhereBuilder().apply(builder).build()
    return if (conditions.size == 1) conditions.first() else WhereAnd(*conditions.toTypedArray())
}