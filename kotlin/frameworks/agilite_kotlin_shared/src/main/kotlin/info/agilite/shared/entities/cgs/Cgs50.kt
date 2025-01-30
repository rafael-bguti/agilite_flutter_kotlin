package info.agilite.shared.entities.cgs;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable
import java.math.BigDecimal
//GERADOR INI

@EntityCacheable
class Cgs50() : AbstractEntity(10) {
  constructor(cgs50id: Long) : this() {
    this.cgs50id = cgs50id
  }

  constructor(
    cgs50empresa: Long? = null,
    cgs50mps: Int,
    cgs50descr: String,
    cgs50codigo: String? = null,
    cgs50preco: BigDecimal? = null,
    cgs50unidade: String? = null,
    cgs50descrComp: String? = null,
    cgs50obs: String? = null,
    cgs50tipoServico: String? = null
  ) : this() {
    if(cgs50empresa != null) this.cgs50empresa = cgs50empresa
    this.cgs50mps = cgs50mps
    this.cgs50descr = cgs50descr
    this.cgs50codigo = cgs50codigo
    this.cgs50preco = cgs50preco
    this.cgs50unidade = cgs50unidade
    this.cgs50descrComp = cgs50descrComp
    this.cgs50obs = cgs50obs
    this.cgs50tipoServico = cgs50tipoServico
  }


  //CUSTOM INI
  //CUSTOM END

  var cgs50id: Long = -1L
    get() {
      validateLoaded(0, "cgs50id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgs50empresa: Long = -1
    get() {
      validateLoaded(1, "cgs50empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgs50mps: Int = -1
    get() {
      validateLoaded(2, "cgs50mps", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cgs50descr: String = "--defaultString--"
    get() {
      validateLoaded(3, "cgs50descr", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cgs50codigo: String? = null
    get() {
      validateLoaded(4, "cgs50codigo", false)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var cgs50preco: BigDecimal? = null
    get() {
      validateLoaded(5, "cgs50preco", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var cgs50unidade: String? = null
    get() {
      validateLoaded(6, "cgs50unidade", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var cgs50descrComp: String? = null
    get() {
      validateLoaded(7, "cgs50descrComp", false)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    
  var cgs50obs: String? = null
    get() {
      validateLoaded(8, "cgs50obs", false)
      return field
    }
    set(value){
      orm.changed(field, value, 8)
      field = value
    }
    
  var cgs50tipoServico: String? = null
    get() {
      validateLoaded(9, "cgs50tipoServico", false)
      return field
    }
    set(value){
      orm.changed(field, value, 9)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cgs50id
    set(value) { if(value != null)this.cgs50id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgs50
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGS50_METADATA
}
const val N_CGS50_EMPRESA = "cgs50empresa";
const val N_CGS50_MPS = "cgs50mps";
const val N_CGS50_DESCR = "cgs50descr";
const val N_CGS50_CODIGO = "cgs50codigo";
const val N_CGS50_PRECO = "cgs50preco";
const val N_CGS50_UNIDADE = "cgs50unidade";
const val N_CGS50_DESCR_COMP = "cgs50descrComp";
const val N_CGS50_OBS = "cgs50obs";
const val N_CGS50_TIPO_SERVICO = "cgs50tipoServico";

val CGS50ID = FieldMetadata("cgs50id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGS50EMPRESA = FieldMetadata("cgs50empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false, null);
val CGS50MPS = FieldMetadata("cgs50mps", 2, "MPS(0,1,2)", FieldTypeMetadata.int, 1.0, true, null, null, null, null, null, false, false, false, null);
val CGS50DESCR = FieldMetadata("cgs50descr", 3, "Descrição completa de serviços", FieldTypeMetadata.string, 0.0, true, null, null, null, null, null, true, true, true, null);
val CGS50CODIGO = FieldMetadata("cgs50codigo", 4, "Código", FieldTypeMetadata.string, 15.0, false, null, null, null, null, null, true, true, true, null);
val CGS50PRECO = FieldMetadata("cgs50preco", 5, "Preço", FieldTypeMetadata.decimal, 16.2, false, null, null, null, null, null, true, true, false, null);
val CGS50UNIDADE = FieldMetadata("cgs50unidade", 6, "Unidade", FieldTypeMetadata.string, 30.0, false, null, null, null, null, null, false, false, false, null);
val CGS50DESCRCOMP = FieldMetadata("cgs50descrComp", 7, "Descrição complementar", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, false, false, false, null);
val CGS50OBS = FieldMetadata("cgs50obs", 8, "Observações", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, false, false, false, null);
val CGS50TIPOSERVICO = FieldMetadata("cgs50tipoServico", 9, "Código do serviço conforme tabela de serviços", FieldTypeMetadata.string, 10.0, false, null, null, null, null, null, false, false, false, null);
 
val CGS50_METADATA = EntityMetadata(
  name = "Cgs50",
  descr = "Itens e Serviços",
  fields = listOf(
    CGS50ID,CGS50EMPRESA,CGS50MPS,CGS50DESCR,CGS50CODIGO,CGS50PRECO,CGS50UNIDADE,CGS50DESCRCOMP,CGS50OBS,CGS50TIPOSERVICO,
  ),
  keys = listOf(
    KeyMetadata("cgs50_uk", KeyMetadataType.uk, "cgs50empresa, cgs50codigo"),
  ),
  oneToMany = mapOf(
  )
)

//GERADOR END
