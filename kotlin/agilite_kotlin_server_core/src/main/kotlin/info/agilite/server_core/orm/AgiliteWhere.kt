package info.agilite.server_core.orm


import info.agilite.server_core.metadata.models.EntityMetadata


//TODO implementar configuração por empresa.
//vamos adicionar 2 opções de configuração por tabela:
//1. Compartilhada (default): Todos os registros da tabela são compartilhados entre todas as empresas, pra isso vamos gravar o ID null na empresa.
//2. Por empresa: Cada empresa tem seus próprios registros, pra isso vamos gravar o ID da empresa no registro.

object AgiliteWhere { //TODO FIXME essa classe não poderá ser um Singleton, pois ela terá que ser injetada com a empresa logada. Ou a empresa logada deverá virar um ThreadLocal.
  //TODO implementar where de empresa. (Opções: 1. Empresa logada, 2. Global sem empresa logada)
  fun defaultWhere(entityMetadata: EntityMetadata, whereAndOr: String = "WHERE"): String {
    if(entityMetadata.entityHasEmpresaField()){
      return " $whereAndOr ${entityMetadata.getEmpresaField()!!.name} IS NULL "
    }

    return " $whereAndOr true "
  }


  //TODO implementar
  fun getEmpresaId(entityMetadata: EntityMetadata): Long? {
    return null
  }
}