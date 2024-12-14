package info.agilite.boot.orm

class ORMController(
  private val propertiesCount: Int
) {
  internal val attLoaded = mutableSetOf<Int>()
  internal val attChanged = mutableSetOf<Int>()
  internal var serializing = false

  private var dbSaved = false

  fun isPartLoaded(): Boolean {
    if(dbSaved)return false
    if(!attLoaded.contains(0))return false //Id não definido

    return attLoaded.size < propertiesCount
  }

  fun onIdChange(id: Long?){
    if(id != null){
      attLoaded.add(0)
    }else{
      attLoaded.remove(0)
    }
  }

  fun changed(value: Any?, newValue: Any?, attIndex: Int) {
    attLoaded.add(attIndex)
    if (value != newValue) {
      attChanged.add(attIndex)
    }
  }

  internal fun setDbSaved() {
    attChanged.clear()
    dbSaved = true
  }

  internal fun isAttLoaded(attIndex: Int): Boolean {
    return attLoaded.contains(attIndex)
  }
}