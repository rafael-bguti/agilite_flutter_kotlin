package info.agilite.cgl.domain.entities;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
//GERADOR INI

class Cgl30() : AbstractEntity(5) {
  constructor(cgl30id: Long) : this() {
    this.cgl30id = cgl30id
  }

  constructor(
    cgl30autenticacao: Long,
    cgl30nome: String,
    cgl30empAtiva: Cgl65,
    cgl30credencial: Cgl29
  ) : this() {
    this.cgl30autenticacao = cgl30autenticacao
    this.cgl30nome = cgl30nome
    this.cgl30empAtiva = cgl30empAtiva
    this.cgl30credencial = cgl30credencial
  }


  var cgl30id: Long = -1L
    get() {
      validateLoaded(0, "cgl30id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgl30autenticacao: Long = -1
    get() {
      validateLoaded(1, "cgl30autenticacao", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgl30nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cgl30nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cgl30empAtiva: Cgl65 = Cgl65()
    get() {
      validateLoaded(3, "cgl30empAtiva", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cgl30credencial: Cgl29 = Cgl29()
    get() {
      validateLoaded(4, "cgl30credencial", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(cgl30id == -1L) null else cgl30id
    set(value) { if(value != null)this.cgl30id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgl30
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGL30_METADATA
}
const val N_CGL30AUTENTICACAO = "cgl30autenticacao";
const val N_CGL30NOME = "cgl30nome";
const val N_CGL30EMPATIVA = "cgl30empAtiva";
const val N_CGL30CREDENCIAL = "cgl30credencial";

val CGL30ID = FieldMetadata("cgl30id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGL30AUTENTICACAO = FieldMetadata("cgl30autenticacao", 1, "Autenticação (Rot10id)", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val CGL30NOME = FieldMetadata("cgl30nome", 2, "Nome", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, false, false, false);
val CGL30EMPATIVA = FieldMetadata("cgl30empAtiva", 3, "Empresa ativa", FieldTypeMetadata.fk, 10.0, true, "Cgl65", null, null, null, null, false, false, false);
val CGL30CREDENCIAL = FieldMetadata("cgl30credencial", 4, "Credencial", FieldTypeMetadata.fk, 10.0, true, "Cgl29", null, null, null, null, false, false, false);
 
val CGL30_METADATA = EntityMetadata(
  name = "Cgl30",
  descr = "Usuários",

  fields = listOf(
    CGL30ID,CGL30AUTENTICACAO,CGL30NOME,CGL30EMPATIVA,CGL30CREDENCIAL,
  ),

  keys = listOf(
    KeyMetadata("cgl30_uk", KeyMetadataType.uk, "cgl30autenticacao"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
