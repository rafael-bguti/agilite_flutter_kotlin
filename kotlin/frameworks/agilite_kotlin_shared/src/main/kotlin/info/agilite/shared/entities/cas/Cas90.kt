package info.agilite.shared.entities.cas;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import java.math.BigDecimal
//GERADOR INI

const val CAS90GRUPO_CODIGOS_DE_SERVICO = 1
const val CAS90GRUPO_CNAE = 2

class Cas90() : AbstractEntity(4) {
  constructor(cas90id: Long) : this() {
    this.cas90id = cas90id
  }

  constructor(
    cas90codigo: String,
    cas90descr: String? = null,
    cas90grupo: Int
  ) : this() {
    this.cas90codigo = cas90codigo
    this.cas90descr = cas90descr
    this.cas90grupo = cas90grupo
  }


  //CUSTOM INI
  //CUSTOM END

  var cas90id: Long = -1L
    get() {
      validateLoaded(0, "cas90id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cas90codigo: String = "--defaultString--"
    get() {
      validateLoaded(1, "cas90codigo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cas90descr: String? = null
    get() {
      validateLoaded(2, "cas90descr", false)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cas90grupo: Int = -1
    get() {
      validateLoaded(3, "cas90grupo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cas90id
    set(value) { if(value != null)this.cas90id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cas90
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CAS90_METADATA
}
const val N_CAS90_CODIGO = "cas90codigo";
const val N_CAS90_DESCR = "cas90descr";
const val N_CAS90_GRUPO = "cas90grupo";

val CAS90ID = FieldMetadata("cas90id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CAS90CODIGO = FieldMetadata("cas90codigo", 1, "Código", FieldTypeMetadata.string, 100.0, true, null, null, null, null, null, true, true, true);
val CAS90DESCR = FieldMetadata("cas90descr", 2, "Descrição do campo", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, true, true, true);
val CAS90GRUPO = FieldMetadata("cas90grupo", 3, "Grupo", FieldTypeMetadata.int, 2.0, true, null, listOf(FieldOptionMetadata(1, "Códigos de serviço"),FieldOptionMetadata(2, "CNAE")), null, null, null, false, false, false);
 
val CAS90_METADATA = EntityMetadata(
  name = "Cas90",
  descr = "Repositório de dados",

  fields = listOf(
    CAS90ID,CAS90CODIGO,CAS90DESCR,CAS90GRUPO,
  ),

  keys = listOf(
    KeyMetadata("cas90_uk", KeyMetadataType.uk, "cas90codigo, cas90grupo"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
