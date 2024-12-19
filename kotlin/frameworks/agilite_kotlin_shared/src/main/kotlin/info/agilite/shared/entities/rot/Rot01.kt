package info.agilite.shared.entities.rot;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import java.math.BigDecimal
//GERADOR INI


class Rot01() : AbstractEntity(4) {
  constructor(rot01id: Long) : this() {
    this.rot01id = rot01id
  }

  constructor(
    rot01cnpj: String,
    rot01rs: String,
    rot01tenant: String
  ) : this() {
    this.rot01cnpj = rot01cnpj
    this.rot01rs = rot01rs
    this.rot01tenant = rot01tenant
  }


  //CUSTOM INI
  //CUSTOM END

  var rot01id: Long = -1L
    get() {
      validateLoaded(0, "rot01id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var rot01cnpj: String = "--defaultString--"
    get() {
      validateLoaded(1, "rot01cnpj", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var rot01rs: String = "--defaultString--"
    get() {
      validateLoaded(2, "rot01rs", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var rot01tenant: String = "--defaultString--"
    get() {
      validateLoaded(3, "rot01tenant", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else rot01id
    set(value) { if(value != null)this.rot01id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Rot01
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = ROT01_METADATA
}
const val N_ROT01_CNPJ = "rot01cnpj";
const val N_ROT01_RS = "rot01rs";
const val N_ROT01_TENANT = "rot01tenant";

val ROT01ID = FieldMetadata("rot01id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val ROT01CNPJ = FieldMetadata("rot01cnpj", 1, "CNPJ", FieldTypeMetadata.string, 14.0, true, null, null, null, null, null, false, false, false);
val ROT01RS = FieldMetadata("rot01rs", 2, "Razão Social", FieldTypeMetadata.string, 100.0, true, null, null, null, null, null, false, false, false);
val ROT01TENANT = FieldMetadata("rot01tenant", 3, "Tenant name", FieldTypeMetadata.string, 20.0, true, null, null, null, null, null, false, false, false);
 
val ROT01_METADATA = EntityMetadata(
  name = "Rot01",
  descr = "Contrato",

  fields = listOf(
    ROT01ID,ROT01CNPJ,ROT01RS,ROT01TENANT,
  ),

  keys = listOf(
    KeyMetadata("rot01_uk", KeyMetadataType.uk, "rot01cnpj"),
    KeyMetadata("rot01_uk1", KeyMetadataType.uk, "rot01tenant"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
