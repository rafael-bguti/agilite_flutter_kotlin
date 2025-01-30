package info.agilite.shared.entities.cgs;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable
import java.math.BigDecimal
import java.time.LocalDate
//GERADOR INI

@EntityCacheable
class Cgs45() : AbstractEntity(4) {
  constructor(cgs45id: Long) : this() {
    this.cgs45id = cgs45id
  }

  constructor(
    cgs45empresa: Long? = null,
    cgs45nome: String,
    cgs45dtUltProcRetorno: LocalDate? = null
  ) : this() {
    if(cgs45empresa != null) this.cgs45empresa = cgs45empresa
    this.cgs45nome = cgs45nome
    this.cgs45dtUltProcRetorno = cgs45dtUltProcRetorno
  }


  //CUSTOM INI
  //CUSTOM END

  var cgs45id: Long = -1L
    get() {
      validateLoaded(0, "cgs45id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgs45empresa: Long = -1
    get() {
      validateLoaded(1, "cgs45empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgs45nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cgs45nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cgs45dtUltProcRetorno: LocalDate? = null
    get() {
      validateLoaded(3, "cgs45dtUltProcRetorno", false)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cgs45id
    set(value) { if(value != null)this.cgs45id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgs45
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGS45_METADATA
}
const val N_CGS45_EMPRESA = "cgs45empresa";
const val N_CGS45_NOME = "cgs45nome";
const val N_CGS45_DT_ULT_PROC_RETORNO = "cgs45dtUltProcRetorno";

val CGS45ID = FieldMetadata("cgs45id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGS45EMPRESA = FieldMetadata("cgs45empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false, null);
val CGS45NOME = FieldMetadata("cgs45nome", 2, "Descrição", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, true, true, true, null);
val CGS45DTULTPROCRETORNO = FieldMetadata("cgs45dtUltProcRetorno", 3, "Data do ultimo processamento de retorno do banco", FieldTypeMetadata.date, 10.0, false, null, null, null, null, null, false, false, false, null);
 
val CGS45_METADATA = EntityMetadata(
  name = "Cgs45",
  descr = "Contas de bancos",
  fields = listOf(
    CGS45ID,CGS45EMPRESA,CGS45NOME,CGS45DTULTPROCRETORNO,
  ),
  keys = listOf(
    KeyMetadata("cgs45_uk", KeyMetadataType.uk, "cgs45empresa, cgs45nome"),
  ),
  oneToMany = mapOf(
  )
)

//GERADOR END
