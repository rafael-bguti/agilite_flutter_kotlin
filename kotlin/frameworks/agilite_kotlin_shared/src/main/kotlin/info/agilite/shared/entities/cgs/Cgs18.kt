package info.agilite.shared.entities.cgs;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable
import java.math.BigDecimal
//GERADOR INI

const val CGS18TIPO_ORCAMENTO = 0
const val CGS18TIPO_PEDIDO = 1
const val CGS18TIPO_NOTA_FISCAL_PRODUTO = 2
const val CGS18TIPO_NOTA_FISCAL_SERVICO = 3
const val CGS18ES_ENTRADA = 0
const val CGS18ES_SAIDA = 1
const val CGS18SCF_NAO_GERAR = 0
const val CGS18SCF_AO_CRIAR_O_DOCUMETO = 1
const val CGS18SCF_NA_APROVACAO_FISCAL = 2
@EntityCacheable
class Cgs18() : AbstractEntity(10) {
  constructor(cgs18id: Long) : this() {
    this.cgs18id = cgs18id
  }

  constructor(
    cgs18empresa: Long? = null,
    cgs18nome: String,
    cgs18tipo: Int,
    cgs18es: Int,
    cgs18scf: Int,
    cgs18serie: Int? = null,
    cgs18emitirDoc: Boolean,
    cgs18alqIss: BigDecimal? = null,
    cgs18modeloEmail: Cgs15? = null
  ) : this() {
    if(cgs18empresa != null) this.cgs18empresa = cgs18empresa
    this.cgs18nome = cgs18nome
    this.cgs18tipo = cgs18tipo
    this.cgs18es = cgs18es
    this.cgs18scf = cgs18scf
    this.cgs18serie = cgs18serie
    this.cgs18emitirDoc = cgs18emitirDoc
    this.cgs18alqIss = cgs18alqIss
    this.cgs18modeloEmail = cgs18modeloEmail
  }


  //CUSTOM INI
  //CUSTOM END

  var cgs18id: Long = -1L
    get() {
      validateLoaded(0, "cgs18id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgs18empresa: Long = -1
    get() {
      validateLoaded(1, "cgs18empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgs18nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cgs18nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cgs18tipo: Int = -1
    get() {
      validateLoaded(3, "cgs18tipo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cgs18es: Int = -1
    get() {
      validateLoaded(4, "cgs18es", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var cgs18scf: Int = -1
    get() {
      validateLoaded(5, "cgs18scf", true)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var cgs18serie: Int? = null
    get() {
      validateLoaded(6, "cgs18serie", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var cgs18emitirDoc: Boolean = false
    get() {
      validateLoaded(7, "cgs18emitirDoc", true)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    
  var cgs18alqIss: BigDecimal? = null
    get() {
      validateLoaded(8, "cgs18alqIss", false)
      return field
    }
    set(value){
      orm.changed(field, value, 8)
      field = value
    }
    
  var cgs18modeloEmail: Cgs15? = null
    get() {
      validateLoaded(9, "cgs18modeloEmail", false)
      return field
    }
    set(value){
      orm.changed(field, value, 9)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cgs18id
    set(value) { if(value != null)this.cgs18id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgs18
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGS18_METADATA
}
const val N_CGS18EMPRESA = "cgs18empresa";
const val N_CGS18NOME = "cgs18nome";
const val N_CGS18TIPO = "cgs18tipo";
const val N_CGS18ES = "cgs18es";
const val N_CGS18SCF = "cgs18scf";
const val N_CGS18SERIE = "cgs18serie";
const val N_CGS18EMITIRDOC = "cgs18emitirDoc";
const val N_CGS18ALQISS = "cgs18alqIss";
const val N_CGS18MODELOEMAIL = "cgs18modeloEmail";

val CGS18ID = FieldMetadata("cgs18id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGS18EMPRESA = FieldMetadata("cgs18empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val CGS18NOME = FieldMetadata("cgs18nome", 2, "Nome", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, true, true, true);
val CGS18TIPO = FieldMetadata("cgs18tipo", 3, "Tipo de Operação", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Orçamento"),FieldOptionMetadata(1, "Pedido"),FieldOptionMetadata(2, "Nota Fiscal Produto"),FieldOptionMetadata(3, "Nota Fiscal Serviço")), null, null, null, false, false, false);
val CGS18ES = FieldMetadata("cgs18es", 4, "Entrada/Saida", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Entrada"),FieldOptionMetadata(1, "Saída")), null, null, null, false, false, false);
val CGS18SCF = FieldMetadata("cgs18scf", 5, "Quando gerar o SCF", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Não gerar"),FieldOptionMetadata(1, "Ao criar o documeto"),FieldOptionMetadata(2, "Na aprovacao fiscal")), null, null, null, false, false, false);
val CGS18SERIE = FieldMetadata("cgs18serie", 6, "Série", FieldTypeMetadata.int, 3.0, false, null, null, null, null, null, false, false, false);
val CGS18EMITIRDOC = FieldMetadata("cgs18emitirDoc", 7, "Emitir documento fiscal", FieldTypeMetadata.boolean, 1.0, true, null, null, null, null, null, false, false, false);
val CGS18ALQISS = FieldMetadata("cgs18alqIss", 8, "Aliquota ISS", FieldTypeMetadata.decimal, 4.2, false, null, null, null, null, null, false, false, false);
val CGS18MODELOEMAIL = FieldMetadata("cgs18modeloEmail", 9, "Modelo de e-mail", FieldTypeMetadata.fk, 10.0, false, "Cgs15", null, null, null, null, false, false, false);
 
val CGS18_METADATA = EntityMetadata(
  name = "Cgs18",
  descr = "Natureza da Operação",

  fields = listOf(
    CGS18ID,CGS18EMPRESA,CGS18NOME,CGS18TIPO,CGS18ES,CGS18SCF,CGS18SERIE,CGS18EMITIRDOC,CGS18ALQISS,CGS18MODELOEMAIL,
  ),

  keys = listOf(
    KeyMetadata("cgs18_uk", KeyMetadataType.uk, "cgs18empresa, cgs18nome"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
