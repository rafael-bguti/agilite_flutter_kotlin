package info.agilite.shared.entities.cas;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
//GERADOR INI

class Cas30() : AbstractEntity(6) {
  constructor(cas30id: Long) : this() {
    this.cas30id = cas30id
  }

  constructor(
    cas30autenticacao: Long,
    cas30nome: String,
    cas30empAtiva: Cas65,
    cas30credencial: Cas29,
    cas30interno: Boolean
  ) : this() {
    this.cas30autenticacao = cas30autenticacao
    this.cas30nome = cas30nome
    this.cas30empAtiva = cas30empAtiva
    this.cas30credencial = cas30credencial
    this.cas30interno = cas30interno
  }


  var cas30id: Long = -1L
    get() {
      validateLoaded(0, "cas30id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cas30autenticacao: Long = -1
    get() {
      validateLoaded(1, "cas30autenticacao", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cas30nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cas30nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cas30empAtiva: Cas65 = Cas65()
    get() {
      validateLoaded(3, "cas30empAtiva", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cas30credencial: Cas29 = Cas29()
    get() {
      validateLoaded(4, "cas30credencial", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var cas30interno: Boolean? = null
    get() {
      validateLoaded(5, "cas30interno", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cas30id
    set(value) { if(value != null)this.cas30id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cas30
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CAS30_METADATA
}
const val N_CAS30AUTENTICACAO = "cas30autenticacao";
const val N_CAS30NOME = "cas30nome";
const val N_CAS30EMPATIVA = "cas30empAtiva";
const val N_CAS30CREDENCIAL = "cas30credencial";
const val N_CAS30INTERNO = "cas30interno";

val CAS30ID = FieldMetadata("cas30id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CAS30AUTENTICACAO = FieldMetadata("cas30autenticacao", 1, "Autenticação (Rot10id)", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val CAS30NOME = FieldMetadata("cas30nome", 2, "Nome", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, false, false, false);
val CAS30EMPATIVA = FieldMetadata("cas30empAtiva", 3, "Empresa ativa", FieldTypeMetadata.fk, 10.0, true, "Cas65", null, null, null, null, false, false, false);
val CAS30CREDENCIAL = FieldMetadata("cas30credencial", 4, "Credencial", FieldTypeMetadata.fk, 10.0, true, "Cas29", null, null, null, null, false, false, false);
val CAS30INTERNO = FieldMetadata("cas30interno", 5, "Usuário interno do sistema, não é exibido para os clientes", FieldTypeMetadata.boolean, 1.0, false, null, null, null, null, null, false, false, false);
 
val CAS30_METADATA = EntityMetadata(
  name = "Cas30",
  descr = "Usuários",

  fields = listOf(
    CAS30ID,CAS30AUTENTICACAO,CAS30NOME,CAS30EMPATIVA,CAS30CREDENCIAL,CAS30INTERNO,
  ),

  keys = listOf(
    KeyMetadata("cas30_uk", KeyMetadataType.uk, "cas30autenticacao"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
