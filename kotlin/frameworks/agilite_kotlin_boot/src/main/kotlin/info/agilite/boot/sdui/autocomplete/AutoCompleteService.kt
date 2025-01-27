package info.agilite.boot.sdui.autocomplete

import info.agilite.core.extensions.splitToList
import info.agilite.boot.defaultMetadataRepository
import info.agilite.core.model.LowerCaseMap
import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.repositories.DefaultRepository
import org.springframework.stereotype.Service

@Service
class AutoCompleteService(
  private val repository: DefaultRepository,
) {
  fun find(dto: AutoCompleteSearchDto): List<Option> {
    val autocompleteConfig = defaultMetadataRepository.loadAutocompleteFieldMetadata(dto.autocompleteFieldName)
    val columnsToSelect: String = autocompleteConfig.columnsToSelect

    val params = mutableMapOf<String, Any?>()
    
    var select = buildString {
      append(" SELECT ${autocompleteConfig.table.lowercase()}id, $columnsToSelect FROM ${autocompleteConfig.table} ")

      if(autocompleteConfig.simpleJoin != null){
        append(autocompleteConfig.simpleJoin)
      }  

      append(" WHERE TRUE ")

      if(dto.defaultWhere != null){
        append(" AND ${dto.defaultWhere.filter} ")
        params.putAll(dto.defaultWhere.params ?: emptyMap())
      }

      if(autocompleteConfig.defaultWhere != null){
        append(autocompleteConfig.defaultWhere.where("AND"))
        params.putAll(autocompleteConfig.defaultWhere.params ?: emptyMap())
      }

      val columnsToSearch = autocompleteConfig.columnsToView ?: columnsToSelect
      if(!dto.ids.isNullOrEmpty()){
        append(" AND ${autocompleteConfig.table.lowercase()}id IN (:ids) ")
        params["ids"] = dto.ids
      }else {
        if (!dto.query.isNullOrBlank()) {
          append(" AND ( ")
          append(columnsToSearch.split(",").joinToString(" OR ") { "$it ILIKE :search" })
          append(" ) ")
          params["search"] = "%${dto.query}%"
        }
        val entityMetadata = defaultMetadataRepository.loadEntityMetadata(autocompleteConfig.table)
        append(" AND ${AgiliteWhere.defaultWhere(entityMetadata)}")
      }

      append(" ORDER BY $columnsToSearch ")
      append(" LIMIT 50")
    }

    val data = repository.listMap(select, params)

    val columnsToLabel = (autocompleteConfig.columnsToView ?: columnsToSelect).splitToList(",")
    return data.map { map ->
      val lowerCaseMap = LowerCaseMap.of(map)
      Option(
          lowerCaseMap["${autocompleteConfig.table.lowercase()}id"]!!,
          columnsToLabel.map { lowerCaseMap[it] }.filterNotNull().joinToString("-"),
          lowerCaseMap
        )
    }
  }
}