package info.agilite.server_core.orm.operations

abstract class DBSelectColumnsStrategy {
  abstract fun getColumns(): String
}