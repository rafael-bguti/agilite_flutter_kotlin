package info.agilite.boot.orm.operations

abstract class DBSelectColumnsStrategy {
  abstract fun getColumns(): String
}