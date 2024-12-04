package info.agilite.boot.autocode.crud

import info.agilite.boot.autocode.crud.models.CrudConfigResponse
import info.agilite.boot.autocode.crud.models.CrudFilters
import info.agilite.boot.autocode.crud.models.CrudListColumn
import info.agilite.boot.autocode.crud.models.CrudListData
import info.agilite.core.model.LowerCaseMap
import info.agilite.boot.spring.RestMapping
import org.springframework.context.ApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestMapping("/crud")
class CrudController(
  private val appContext: ApplicationContext,
) {

  @GetMapping("/config/{taskName}")
  fun getMetadata(@PathVariable("taskName") taskName: String): CrudConfigResponse {
    return CrudConfigResponse(serviceResolver(taskName).getCrudMetadata(taskName))
  }

  @GetMapping("/list/columns/{taskName}")
  fun getListColumns(@PathVariable("taskName") taskName: String): List<CrudListColumn> {
    return serviceResolver(taskName).getCrudListColumns(taskName)
  }

  @PostMapping("/list/find/{taskName}")
  fun getListData(@PathVariable("taskName") taskName: String, @RequestBody filters: CrudFilters): CrudListData {
    return serviceResolver(taskName).findListData(taskName, filters)
  }

  @PostMapping("/onnew/{taskName}")
  fun onNew(@PathVariable("taskName") taskName: String): Map<String, Any?>? {
    return serviceResolver(taskName).createNewRecord(taskName)
  }

  @GetMapping("/{taskName}/{id}")
  fun onEdit(@PathVariable("taskName") taskName: String, @PathVariable("id") id: Long): ResponseEntity< Map<String, Any?>> {
    val data = serviceResolver(taskName).editRecord(taskName, id)
    return if (data != null) {
      ResponseEntity.ok(data)
    } else {
      ResponseEntity.notFound().build()
    }
  }

  @PostMapping("/{taskName}")
  @Transactional
  fun insert(@PathVariable("taskName") taskName: String, @RequestBody data: LowerCaseMap) {
    serviceResolver(taskName).let {
      it.insert(it.convertEntity(taskName, data, null))
    }
  }

  @PutMapping("/{taskName}/{id}")
  @Transactional
  fun update(@PathVariable("taskName") taskName: String, @PathVariable("id") id: Long, @RequestBody data: LowerCaseMap) {
    serviceResolver(taskName).let {
      it.update(it.convertEntity(taskName, data, id))
    }
  }

  @PostMapping("/delete/{taskName}")
  @Transactional
  fun delete(@PathVariable("taskName") taskName: String, @RequestBody ids: List<Long>) {
    serviceResolver(taskName).delete(taskName, ids)
  }

  private fun serviceResolver(taskName: String): CrudService<Any> {
    return CrudServiceResolver.createService(taskName, appContext) as CrudService<Any>
  }
}