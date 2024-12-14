package info.agilite.core.orm

import com.fasterxml.jackson.annotation.JsonIgnore

interface Entity {
  var id: Long?
  var serializing: Boolean

  fun clearChanges(executeToCascade: Boolean = true, oldAttChanged: Set<Int>? = null)
  @JsonIgnore
  fun getAttChangedIndexes(): Set<Int>
}