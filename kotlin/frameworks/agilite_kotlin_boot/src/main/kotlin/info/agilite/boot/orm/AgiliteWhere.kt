package info.agilite.boot.orm


import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.security.UserContext

object AgiliteWhere {

  fun defaultWhere(entityMetadata: EntityMetadata): String {
    val empresaId = UserContext.user?.empId

    if(entityMetadata.entityHasEmpresaField() && empresaId != null) {
      //TODO implementar where de empresa. (Opções: 1. Empresa logada(OK), 2. Global sem empresa logada (retorna true), 3. Empresa configurada como centralizadora(tela a ser criada))
      return " ${entityMetadata.getEmpresaField()!!.name} = $empresaId "
    }

    return " true "
  }



}