package info.agilite.boot
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.FieldMetadata
import info.agilite.boot.metadata.models.FieldTypeMetadata
import info.agilite.boot.orm.AbstractEntity
import info.agilite.core.json.JsonUtils

//GERADOR INI

class Kakaka() : AbstractEntity(6) {
  constructor(rot10id: Long) : this() {
    this.rot10id = rot10id
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

  var rot10email: String = "--defaultString--"
    get() {
      validateLoaded(1, "rot10email", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }

  var rot10senha: String = "--defaultString--"
    get() {
      validateLoaded(2, "rot10senha", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }

  var rot10token: String? = null
    get() {
      validateLoaded(3, "rot10token", false)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }

  var rot10refreshToken: String? = null
    get() {
      validateLoaded(4, "rot10refreshToken", false)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }

  var rot10roles: String? = null
    get() {
      validateLoaded(5, "rot10roles", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }


  override var id: Long?
    @JsonIgnore get() = if(rot10id == -1L) null else rot10id
    set(value) { if(value != null)this.rot10id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Kakaka
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = ROT10_METADATA
}
const val N_ROT10EMAIL = "rot10email";
const val N_ROT10SENHA = "rot10senha";
const val N_ROT10TOKEN = "rot10token";
const val N_ROT10REFRESHTOKEN = "rot10refreshToken";
const val N_ROT10ROLES = "rot10roles";

val ROT10ID = FieldMetadata("rot10id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val ROT10EMAIL = FieldMetadata("rot10email", 1, "E-Mail", FieldTypeMetadata.string, 100.0, true, null, null, null, null, "email,min:6", false, false, false);
val ROT10SENHA = FieldMetadata("rot10senha", 2, "Senha", FieldTypeMetadata.string, 250.0, true, null, null, null, null, null, false, false, false);
val ROT10TOKEN = FieldMetadata("rot10token", 3, "Token", FieldTypeMetadata.string, 300.0, false, null, null, null, null, null, false, false, false);
val ROT10REFRESHTOKEN = FieldMetadata("rot10refreshToken", 4, "Refresh Token", FieldTypeMetadata.string, 300.0, false, null, null, null, null, null, false, false, false);
val ROT10ROLES = FieldMetadata("rot10roles", 5, "Roles", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, false, false, false);

val ROT10_METADATA = EntityMetadata(
  name = "Rot10",
  descr = "Autenticação",

  fields = listOf(
    ROT10ID,ROT10EMAIL,ROT10SENHA,ROT10TOKEN,ROT10REFRESHTOKEN,ROT10ROLES,
  ),

  keys = listOf(
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END


fun main() {
  val kakaka = Kakaka(125)
  kakaka.rot10email = "nana"


  println(kakaka.rot10email)
  println(kakaka.rot10id)
  println(JsonUtils.toJson(kakaka))
}