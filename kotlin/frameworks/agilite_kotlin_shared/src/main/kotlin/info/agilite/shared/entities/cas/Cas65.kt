package info.agilite.shared.entities.cas;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import java.math.BigDecimal
//GERADOR INI

class Cas65() : AbstractEntity(4) {
  constructor(cas65id: Long) : this() {
    this.cas65id = cas65id
  }

  constructor(
    cas65cnpj: String,
    cas65nome: String,
    cas65cnae: String
  ) : this() {
    this.cas65cnpj = cas65cnpj
    this.cas65nome = cas65nome
    this.cas65cnae = cas65cnae
  }


  var cas65id: Long = -1L
    get() {
      validateLoaded(0, "cas65id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cas65cnpj: String = "--defaultString--"
    get() {
      validateLoaded(1, "cas65cnpj", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cas65nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cas65nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cas65cnae: String? = null
    get() {
      validateLoaded(3, "cas65cnae", false)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cas65id
    set(value) { if(value != null)this.cas65id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cas65
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CAS65_METADATA
}
const val N_CAS65CNPJ = "cas65cnpj";
const val N_CAS65NOME = "cas65nome";
const val N_CAS65CNAE = "cas65cnae";

val CAS65ID = FieldMetadata("cas65id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CAS65CNPJ = FieldMetadata("cas65cnpj", 1, "Cnpj", FieldTypeMetadata.string, 14.0, true, null, null, null, null, null, false, false, false);
val CAS65NOME = FieldMetadata("cas65nome", 2, "Nome", FieldTypeMetadata.string, 100.0, true, null, null, null, null, null, false, false, false);
val CAS65CNAE = FieldMetadata("cas65cnae", 3, "Código CNAE", FieldTypeMetadata.string, 10.0, false, null, null, null, null, null, false, false, false);
 
val CAS65_METADATA = EntityMetadata(
  name = "Cas65",
  descr = "Empresa",

  fields = listOf(
    CAS65ID,CAS65CNPJ,CAS65NOME,CAS65CNAE,
  ),

  keys = listOf(
    KeyMetadata("cas65_uk", KeyMetadataType.uk, "cas65cnpj"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
