package info.agilite.shared.extensions

fun Map<String, *>.nest (): MutableMap<String, Any?> {
    val result = mutableMapOf<String, Any?>()
    this.entries.forEach {
      if (!it.key.contains(".")) {
        result[it.key] = it.value
      } else {
        val keyList = it.key.split(".")
        var leaf = result.compute(keyList[0]) { _: String, value: Any? -> value ?: mutableMapOf<String, Any?>() } as MutableMap<String, Any?>
        for (i in 1 until keyList.size - 1) {
          leaf = leaf.compute(keyList[i]) { _: String, value: Any? -> value ?: mutableMapOf<String, Any?>() } as MutableMap<String, Any?>
        }
        leaf[keyList.last()] = it.value
      }
    }

    return result
}

fun Map<String, *>.nestedValue(key: String): Any? {
    if (!key.contains(".")) {
      return this[key]
    }

    val keyList = key.split(".")
    var leaf = this[keyList[0]] as Map<String, Any?>?
    for (i in 1 until keyList.size-1) {
      if(leaf == null) return null
      leaf = leaf[keyList[i]] as Map<String, Any?> ?
    }

    return leaf?.get(keyList.last())
}