package info.agilite.boot

import info.agilite.boot.metadata.data.MetadataRepository
import info.agilite.boot.orm.dialects.JdbcDialect

lateinit var defaultMetadataRepository: MetadataRepository
lateinit var jdbcDialect: JdbcDialect

var bool: Boolean = false