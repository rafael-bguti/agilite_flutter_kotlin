package info.agilite.boot.crud

import info.agilite.boot.spring.RestMapping
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestMapping("/crud")
class CrudController(
  private val appContext: ApplicationContext,
) {

  @PostMapping("/list/find/{taskName}")
  fun getListData(@PathVariable("taskName") taskName: String, @RequestBody request: CrudListRequest): CrudListResponse {
    val service = CrudServiceResolver.createService(taskName, appContext)

    return service.findListData(taskName, request)
  }
//
//  @PostMapping("/onnew/{taskName}")
//  fun onNew(@PathVariable("taskName") taskName: String): Map<String, Any?>? {
//    return serviceResolver(taskName).createNewRecord(taskName)
//  }
//
//  @GetMapping("/{taskName}/{id}")
//  fun onEdit(@PathVariable("taskName") taskName: String, @PathVariable("id") id: Long): ResponseEntity< Map<String, Any?>> {
//    val data = serviceResolver(taskName).editRecord(taskName, id)
//    return if (data != null) {
//      ResponseEntity.ok(data)
//    } else {
//      ResponseEntity.notFound().build()
//    }
//  }
//
//  @PostMapping("/{taskName}")
//  @Transactional
//  fun insert(@PathVariable("taskName") taskName: String, @RequestBody data: LowerCaseMap) {
//    serviceResolver(taskName).let {
//      it.insert(it.convertEntity(taskName, data, null))
//    }
//  }
//
//  @PutMapping("/{taskName}/{id}")
//  @Transactional
//  fun update(@PathVariable("taskName") taskName: String, @PathVariable("id") id: Long, @RequestBody data: LowerCaseMap) {
//    serviceResolver(taskName).let {
//      it.update(it.convertEntity(taskName, data, id))
//    }
//  }
//
//  @PostMapping("/delete/{taskName}")
//  @Transactional
//  fun delete(@PathVariable("taskName") taskName: String, @RequestBody ids: List<Long>) {
//    serviceResolver(taskName).delete(taskName, ids)
//  }
//
}