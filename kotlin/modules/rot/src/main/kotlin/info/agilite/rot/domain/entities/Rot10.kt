package info.agilite.rot.domain.entities;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
//GERADOR INI

class Rot10() : AbstractEntity(7) {
  constructor(rot10id: Long) : this() {
    this.rot10id = rot10id
  }

  constructor(
    rot10contrato: Rot01,
    rot10email: String,
    rot10senha: String,
    rot10token: String,
    rot10refreshToken: String,
    rot10roles: String
  ) : this() {
    this.rot10contrato = rot10contrato
    this.rot10email = rot10email
    this.rot10senha = rot10senha
    this.rot10token = rot10token
    this.rot10refreshToken = rot10refreshToken
    this.rot10roles = rot10roles
  }


  var rot10id: Long = -1L
    get() {
      validateLoaded(0, "rot10id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var rot10contrato: Rot01 = Rot01()
    get() {
      validateLoaded(1, "rot10contrato", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var rot10email: String = "--defaultString--"
    get() {
      validateLoaded(2, "rot10email", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var rot10senha: String = "--defaultString--"
    get() {
      validateLoaded(3, "rot10senha", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var rot10token: String? = null
    get() {
      validateLoaded(4, "rot10token", false)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var rot10refreshToken: String? = null
    get() {
      validateLoaded(5, "rot10refreshToken", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var rot10roles: String? = null
    get() {
      validateLoaded(6, "rot10roles", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else rot10id
    set(value) { if(value != null)this.rot10id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Rot10
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = ROT10_METADATA
}
const val N_ROT10CONTRATO = "rot10contrato";
const val N_ROT10EMAIL = "rot10email";
const val N_ROT10SENHA = "rot10senha";
const val N_ROT10TOKEN = "rot10token";
const val N_ROT10REFRESHTOKEN = "rot10refreshToken";
const val N_ROT10ROLES = "rot10roles";

val ROT10ID = FieldMetadata("rot10id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val ROT10CONTRATO = FieldMetadata("rot10contrato", 1, "Contrato", FieldTypeMetadata.fk, 10.0, true, "Rot01", null, null, null, null, false, false, false);
val ROT10EMAIL = FieldMetadata("rot10email", 2, "E-Mail", FieldTypeMetadata.string, 100.0, true, null, null, null, null, "email,min:6", false, false, false);
val ROT10SENHA = FieldMetadata("rot10senha", 3, "Senha", FieldTypeMetadata.string, 250.0, true, null, null, null, null, null, false, false, false);
val ROT10TOKEN = FieldMetadata("rot10token", 4, "Token", FieldTypeMetadata.string, 300.0, false, null, null, null, null, null, false, false, false);
val ROT10REFRESHTOKEN = FieldMetadata("rot10refreshToken", 5, "Refresh Token", FieldTypeMetadata.string, 300.0, false, null, null, null, null, null, false, false, false);
val ROT10ROLES = FieldMetadata("rot10roles", 6, "Roles", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, false, false, false);
 
val ROT10_METADATA = EntityMetadata(
  name = "Rot10",
  descr = "Autenticação",

  fields = listOf(
    ROT10ID,ROT10CONTRATO,ROT10EMAIL,ROT10SENHA,ROT10TOKEN,ROT10REFRESHTOKEN,ROT10ROLES,
  ),

  keys = listOf(
    KeyMetadata("rot10_uk", KeyMetadataType.uk, "rot10email"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
