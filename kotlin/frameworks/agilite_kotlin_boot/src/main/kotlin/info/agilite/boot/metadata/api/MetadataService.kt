package info.agilite.boot.metadata.api

import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.metadata.exceptions.EntityMetadataNotFoundException
import info.agilite.boot.metadata.exceptions.MetadataNotFoundException
import info.agilite.boot.metadata.models.*
import org.springframework.stereotype.Service

@Service
class MetadataService {
  fun loadMetadata(names: List<String>): List<FieldMetadataDto> {
    val result = mutableListOf<FieldMetadataDto>()

    names.forEach { name ->
      val entityMetadata = tryLoadEntityByName(name)
      if (entityMetadata != null) {
        entityMetadata.fields.forEach { field ->
          if(field.type == FieldTypeMetadata.fk){
            try {
              val autocompleteConfig = defaultMetadataRepository.loadAutocompleteFieldMetadata(field.name)
              result.add(
                buildFieldMetadataDto(
                  field = autocompleteConfig.field,
                  autocompleteColumnId = "${autocompleteConfig.table.lowercase()}id",
                  autocompleteColumnsView = autocompleteConfig.columnsToView ?: autocompleteConfig.columnsToSelect
                )
              )
            }catch (e: MetadataNotFoundException){
              result.add(
                buildFieldMetadataDto(field)
              )
            }
          }else{
            result.add(
              buildFieldMetadataDto(field)
            )
          }
        }
      }else{
        val autocompleteConfig = tryLoadAutocompleteByName(name)
        if (autocompleteConfig != null) {
          result.add(
            buildFieldMetadataDto(
              field = autocompleteConfig.field.copy(name = name),
              autocompleteColumnId = "${autocompleteConfig.table.lowercase()}id",
              autocompleteColumnsView = autocompleteConfig.columnsToView ?: autocompleteConfig.columnsToSelect
            )
          )
        }else{
          val field = tryLoadFieldByName(name)
          if (field != null) {
            result.add(
              buildFieldMetadataDto(field)
            )
          }else{
            throw EntityMetadataNotFoundException(name)
          }
        }
      }

    }

    return result
  }

  private fun buildFieldMetadataDto(field: FieldMetadata, autocompleteColumnId: String? = null, autocompleteColumnsView: String? = null) = FieldMetadataDto(
    name = field.name.lowercase(),
    type = if(autocompleteColumnId != null) "autocomplete" else field.type.frontEndType,
    label = field.label,
    req = field.required,
    size = field.size,
    options = field.options,
    autocompleteColumnId = autocompleteColumnId,
    autocompleteColumnsView = autocompleteColumnsView,
    validationQuery = field.validationQuery,
    mod = field.mod
  )

  private fun tryLoadEntityByName(name: String): EntityMetadata? {
    return try {
      defaultMetadataRepository.loadEntityMetadata(name)
    } catch (e: MetadataNotFoundException) {
      null
    }
  }

  private fun tryLoadAutocompleteByName(name: String): AutocompleteConfig? {
    return try {
      defaultMetadataRepository.loadAutocompleteFieldMetadata(name)
    } catch (e: MetadataNotFoundException) {
      null
    }
  }

  private fun tryLoadFieldByName(name: String): FieldMetadata? {
    return try {
      defaultMetadataRepository.loadFieldMetadata(name)
    } catch (e: MetadataNotFoundException) {
      null
    }
  }
}