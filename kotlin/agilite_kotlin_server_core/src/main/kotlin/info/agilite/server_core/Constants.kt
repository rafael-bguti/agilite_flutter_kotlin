package info.agilite.server_core

import info.agilite.server_core.metadata.data.MetadataRepository
import info.agilite.server_core.orm.dialects.JdbcDialect

lateinit var defaultMetadataRepository: MetadataRepository
lateinit var jdbcDialect: JdbcDialect