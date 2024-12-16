package info.agilite.shared.entities.scf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.shared.entities.cgs.Cgs45
import java.math.BigDecimal
import java.time.LocalDate
//GERADOR INI


class Scf021() : AbstractEntity(5) {
  constructor(scf021id: Long) : this() {
    this.scf021id = scf021id
  }

  constructor(
    scf021doc: Long? = null,
    scf021remNumero: String,
    scf021conta: Cgs45,
    scf021dtBaixa: LocalDate? = null
  ) : this() {
    if(scf021doc != null) this.scf021doc = scf021doc
    this.scf021remNumero = scf021remNumero
    this.scf021conta = scf021conta
    this.scf021dtBaixa = scf021dtBaixa
  }


  //CUSTOM INI
  //CUSTOM END

  var scf021id: Long = -1L
    get() {
      validateLoaded(0, "scf021id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var scf021doc: Long = -1
    get() {
      validateLoaded(1, "scf021doc", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var scf021remNumero: String = "--defaultString--"
    get() {
      validateLoaded(2, "scf021remNumero", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var scf021conta: Cgs45 = Cgs45()
    get() {
      validateLoaded(3, "scf021conta", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var scf021dtBaixa: LocalDate? = null
    get() {
      validateLoaded(4, "scf021dtBaixa", false)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else scf021id
    set(value) { if(value != null)this.scf021id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Scf021
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = SCF021_METADATA
}
const val N_SCF021DOC = "scf021doc";
const val N_SCF021REMNUMERO = "scf021remNumero";
const val N_SCF021CONTA = "scf021conta";
const val N_SCF021DTBAIXA = "scf021dtBaixa";

val SCF021ID = FieldMetadata("scf021id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val SCF021DOC = FieldMetadata("scf021doc", 1, "Documento", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val SCF021REMNUMERO = FieldMetadata("scf021remNumero", 2, "Id da remessa", FieldTypeMetadata.string, 0.0, true, null, null, null, null, null, false, false, false);
val SCF021CONTA = FieldMetadata("scf021conta", 3, "Conta bancária", FieldTypeMetadata.fk, 10.0, true, "Cgs45", null, null, null, null, false, false, false);
val SCF021DTBAIXA = FieldMetadata("scf021dtBaixa", 4, "Data de pagamento", FieldTypeMetadata.date, 10.0, false, null, null, null, null, null, false, false, false);
 
val SCF021_METADATA = EntityMetadata(
  name = "Scf021",
  descr = "Integração Bancária",

  fields = listOf(
    SCF021ID,SCF021DOC,SCF021REMNUMERO,SCF021CONTA,SCF021DTBAIXA,
  ),

  keys = listOf(
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
