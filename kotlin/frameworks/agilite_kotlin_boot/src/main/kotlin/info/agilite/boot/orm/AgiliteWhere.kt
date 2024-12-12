package info.agilite.boot.orm


import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.security.UserContext

object AgiliteWhere {

  //TODO implementar where de empresa. (Opções: 1. Empresa logada, 2. Global sem empresa logada (retorna true), 3. Empresa configurada como centralizadora(tela a ser criada))
  fun defaultWhere(entityMetadata: EntityMetadata): String {
    val empresaId = UserContext.user?.empId

    if(entityMetadata.entityHasEmpresaField() && empresaId != null) {
      return " ${entityMetadata.getEmpresaField()!!.name} = $empresaId "
    }

    return " true "
  }



}