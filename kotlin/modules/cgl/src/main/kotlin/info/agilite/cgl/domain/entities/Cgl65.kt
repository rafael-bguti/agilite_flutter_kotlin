package info.agilite.cgl.domain.entities;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
//GERADOR INI

class Cgl65() : AbstractEntity(3) {
  constructor(cgl65id: Long) : this() {
    this.cgl65id = cgl65id
  }

  constructor(
    cgl65cnpj: String,
    cgl65nome: String
  ) : this() {
    this.cgl65cnpj = cgl65cnpj
    this.cgl65nome = cgl65nome
  }


  var cgl65id: Long = -1L
    get() {
      validateLoaded(0, "cgl65id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgl65cnpj: String = "--defaultString--"
    get() {
      validateLoaded(1, "cgl65cnpj", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgl65nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cgl65nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(cgl65id == -1L) null else cgl65id
    set(value) { if(value != null)this.cgl65id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgl65
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGL65_METADATA
}
const val N_CGL65CNPJ = "cgl65cnpj";
const val N_CGL65NOME = "cgl65nome";

val CGL65ID = FieldMetadata("cgl65id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGL65CNPJ = FieldMetadata("cgl65cnpj", 1, "Cnpj", FieldTypeMetadata.string, 14.0, true, null, null, null, null, null, false, false, false);
val CGL65NOME = FieldMetadata("cgl65nome", 2, "Nome", FieldTypeMetadata.string, 100.0, true, null, null, null, null, null, false, false, false);
 
val CGL65_METADATA = EntityMetadata(
  name = "Cgl65",
  descr = "Empresa",

  fields = listOf(
    CGL65ID,CGL65CNPJ,CGL65NOME,
  ),

  keys = listOf(
    KeyMetadata("cgl65_uk", KeyMetadataType.uk, "cgl65cnpj"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
