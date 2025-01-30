package info.agilite.shared.entities.srf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.shared.entities.cgs.Cgs38
import info.agilite.shared.entities.scf.Scf02
import java.math.BigDecimal
import java.time.LocalDate
//GERADOR INI


class Srf012() : AbstractEntity(7) {
  constructor(srf012id: Long) : this() {
    this.srf012id = srf012id
  }

  constructor(
    srf012doc: Long? = null,
    srf012forma: Cgs38,
    srf012parcela: Int,
    srf012dtVenc: LocalDate,
    srf012valor: BigDecimal,
    srf012documento: Scf02? = null
  ) : this() {
    if(srf012doc != null) this.srf012doc = srf012doc
    this.srf012forma = srf012forma
    this.srf012parcela = srf012parcela
    this.srf012dtVenc = srf012dtVenc
    this.srf012valor = srf012valor
    this.srf012documento = srf012documento
  }


  //CUSTOM INI
  //CUSTOM END

  var srf012id: Long = -1L
    get() {
      validateLoaded(0, "srf012id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var srf012doc: Long = -1
    get() {
      validateLoaded(1, "srf012doc", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var srf012forma: Cgs38 = Cgs38()
    get() {
      validateLoaded(2, "srf012forma", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var srf012parcela: Int = -1
    get() {
      validateLoaded(3, "srf012parcela", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var srf012dtVenc: LocalDate = LocalDate.now()
    get() {
      validateLoaded(4, "srf012dtVenc", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var srf012valor: BigDecimal = BigDecimal("-1")
    get() {
      validateLoaded(5, "srf012valor", true)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var srf012documento: Scf02? = null
    get() {
      validateLoaded(6, "srf012documento", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else srf012id
    set(value) { if(value != null)this.srf012id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Srf012
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = SRF012_METADATA
}
const val N_SRF012_DOC = "srf012doc";
const val N_SRF012_FORMA = "srf012forma";
const val N_SRF012_PARCELA = "srf012parcela";
const val N_SRF012_DT_VENC = "srf012dtVenc";
const val N_SRF012_VALOR = "srf012valor";
const val N_SRF012_DOCUMENTO = "srf012documento";

val SRF012ID = FieldMetadata("srf012id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val SRF012DOC = FieldMetadata("srf012doc", 1, "Documento", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false, null);
val SRF012FORMA = FieldMetadata("srf012forma", 2, "Forma de Pagamento/Recebimento", FieldTypeMetadata.fk, 10.0, true, "Cgs38", null, null, null, null, false, false, false, null);
val SRF012PARCELA = FieldMetadata("srf012parcela", 3, "Parcela", FieldTypeMetadata.int, 10.0, true, null, null, null, null, null, false, false, false, null);
val SRF012DTVENC = FieldMetadata("srf012dtVenc", 4, "Data de vencimento", FieldTypeMetadata.date, 10.0, true, null, null, null, null, null, false, false, false, null);
val SRF012VALOR = FieldMetadata("srf012valor", 5, "Valor", FieldTypeMetadata.decimal, 16.2, true, null, null, null, null, null, false, false, false, null);
val SRF012DOCUMENTO = FieldMetadata("srf012documento", 6, "Contas Pagar/receber", FieldTypeMetadata.fk, 10.0, false, "Scf02", null, null, null, null, false, false, false, null);
 
val SRF012_METADATA = EntityMetadata(
  name = "Srf012",
  descr = "Parcelamento",
  fields = listOf(
    SRF012ID,SRF012DOC,SRF012FORMA,SRF012PARCELA,SRF012DTVENC,SRF012VALOR,SRF012DOCUMENTO,
  ),
  keys = listOf(
  ),
  oneToMany = mapOf(
  )
)

//GERADOR END
