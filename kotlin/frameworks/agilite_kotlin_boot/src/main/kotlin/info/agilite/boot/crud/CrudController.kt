package info.agilite.boot.crud

import info.agilite.boot.spring.RestMapping
import info.agilite.core.model.LowerCaseMap
import org.springframework.context.ApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestMapping("/crud")
class CrudController(
  private val appContext: ApplicationContext,
) {
  @PostMapping("/list/find/{taskName}")
  fun getListData(@PathVariable("taskName") taskName: String, @RequestBody request: CrudListRequest): CrudListResponse {
    val service = CrudServiceResolver.createService(taskName, appContext)
    return service.findListData(taskName, request)
  }

  @GetMapping("/{taskName}/edit")
  fun onEdit(@PathVariable("taskName") taskName: String): ResponseEntity<CrudEditResponse> {
    return onEdit(taskName, null)
  }

  @GetMapping("/{taskName}/edit/{id}")
  fun onEdit(@PathVariable("taskName") taskName: String, @PathVariable("id", required = false) id: Long?): ResponseEntity<CrudEditResponse> {
    val service = CrudServiceResolver.createService(taskName, appContext)
    val response: CrudEditResponse;
    if(id == null) {
      response = service.createNewRecord(taskName)
    }else {
      response = service.editRecord(taskName, id)
    }
    return if (response.data != null) {
      ResponseEntity.ok(response)
    } else {
      ResponseEntity.notFound().build()
    }
  }

  @PostMapping("/{taskName}")
  @Transactional
  fun insert(@PathVariable("taskName") taskName: String, @RequestBody data: LowerCaseMap) {
    val service = CrudServiceResolver.createService(taskName, appContext)
    val entity = service.convertEntity(taskName, data, null)

    service.save(entity)
  }

  @PutMapping("/{taskName}/{id}")
  @Transactional
  fun update(@PathVariable("taskName") taskName: String, @PathVariable("id") id: Long, @RequestBody data: LowerCaseMap) {
    val service = CrudServiceResolver.createService(taskName, appContext)
    service.save(service.convertEntity(taskName, data, id))
  }

  @PostMapping("/delete/{taskName}")
  @Transactional
  fun delete(@PathVariable("taskName") taskName: String, @RequestBody ids: List<Long>) {
    CrudServiceResolver.createService(taskName, appContext).delete(taskName, ids)
  }
}