package info.agilite.shared.entities.cas;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import java.math.BigDecimal
//GERADOR INI


class Cas29() : AbstractEntity(2) {
  constructor(cas29id: Long) : this() {
    this.cas29id = cas29id
  }

  constructor(
    cas29nome: String
  ) : this() {
    this.cas29nome = cas29nome
  }


  //CUSTOM INI
  //CUSTOM END

  var cas29id: Long = -1L
    get() {
      validateLoaded(0, "cas29id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cas29nome: String = "--defaultString--"
    get() {
      validateLoaded(1, "cas29nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cas29id
    set(value) { if(value != null)this.cas29id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cas29
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CAS29_METADATA
}
const val N_CAS29_NOME = "cas29nome";

val CAS29ID = FieldMetadata("cas29id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CAS29NOME = FieldMetadata("cas29nome", 1, "Nome", FieldTypeMetadata.string, 50.0, true, null, null, null, null, null, false, false, false);
 
val CAS29_METADATA = EntityMetadata(
  name = "Cas29",
  descr = "Credencial",

  fields = listOf(
    CAS29ID,CAS29NOME,
  ),

  keys = listOf(
    KeyMetadata("cas29_uk", KeyMetadataType.uk, "cas29nome"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
