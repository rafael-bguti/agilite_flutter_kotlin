package info.agilite.shared.orm

interface Entity {
  var id: Long?
  var serializing: Boolean

  fun clearChanges(executeToCascade: Boolean = true)
}