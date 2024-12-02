package info.agilite.server_core.autocode.autocomplete

import info.agilite.server_core.autocode.autocomplete.models.AutoCompleteSearchDto
import info.agilite.server_core.autocode.autocomplete.models.KeyLabel

import info.agilite.shared.extensions.splitToList
import info.agilite.server_core.defaultMetadataRepository
import info.agilite.shared.model.LowerCaseMap
import info.agilite.server_core.orm.AgiliteWhere
import info.agilite.server_core.orm.repositories.DefaultRepository
import org.springframework.stereotype.Service

@Service
class AutoCompleteService(
  private val repository: DefaultRepository,
) {
  fun find(dto: AutoCompleteSearchDto): List<KeyLabel> {
    val autocompleteConfig = defaultMetadataRepository.loadAutocompleteFieldMetadata(dto.autocompleteFieldName)
    val columnsToSelect: String = autocompleteConfig.columnsToSelect

    val params = mutableMapOf<String, Any?>()
    var select = " SELECT ${autocompleteConfig.table.lowercase()}id, $columnsToSelect FROM ${autocompleteConfig.table} WHERE true "
    if(dto.defaultWhere != null){
      select += " AND ${dto.defaultWhere.filter} "
      params.putAll(dto.defaultWhere.params ?: emptyMap())
    }
    if(autocompleteConfig.defaultWhere != null){
      select += " AND ${autocompleteConfig.defaultWhere.filter} "
      params.putAll(autocompleteConfig.defaultWhere.params ?: emptyMap())
    }

    val columnsToSearch = autocompleteConfig.columnsToView ?: columnsToSelect
    if(!dto.ids.isNullOrEmpty()){
      select += " AND ${autocompleteConfig.table.lowercase()}id IN (:ids) "
      params["ids"] = dto.ids
    }else {
      if (!dto.query.isNullOrBlank()) {
        select += " AND ( "
        select += columnsToSearch.split(",").joinToString(" OR ") { "$it ILIKE :search" }
        select += " ) "
        params["search"] = "%${dto.query}%"
      }
      val entityMetadata = defaultMetadataRepository.loadEntityMetadata(autocompleteConfig.table)
      select += AgiliteWhere.defaultWhere(entityMetadata,  "AND")
    }

    select += " ORDER BY $columnsToSearch "
    select += " LIMIT 50"
    val data = repository.listMap(select, params)

    val columnsToLabel = (autocompleteConfig.columnsToView ?: columnsToSelect).splitToList(",")
    return data.map { map ->
      val lowerCaseMap = LowerCaseMap.of(map)
      KeyLabel(
          lowerCaseMap["${autocompleteConfig.table.lowercase()}id"]!!,
          columnsToLabel.map { lowerCaseMap[it] }.filterNotNull().joinToString("-"),
          lowerCaseMap
        )
    }
  }
}