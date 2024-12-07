package info.agilite.core.xml

import javax.xml.XMLConstants
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory


private val SCHEMAS: MutableMap<String, Schema> = mutableMapOf()

object SchemaBuilder {
    fun buildSchema(schemaPathLocation: String): Schema {
        return SCHEMAS.getOrPut(schemaPathLocation) {
            val schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            return schemaFactory.newSchema(SchemaValidator::class.java.getResource(schemaPathLocation))
        }
    }
}