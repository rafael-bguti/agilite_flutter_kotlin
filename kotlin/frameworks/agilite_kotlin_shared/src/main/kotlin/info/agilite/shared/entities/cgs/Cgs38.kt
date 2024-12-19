package info.agilite.shared.entities.cgs;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable
import info.agilite.shared.entities.scf.Scf10
import java.math.BigDecimal
//GERADOR INI

const val CGS38TIPO_RECEBIMENTO = 0
const val CGS38TIPO_PAGAMENTO = 1
const val CGS38GERAR_GERAR_EM_ABERTO = 1
const val CGS38GERAR_GERAR_QUITADO = 2
const val CGS38FORMA_DINHEIRO = 0
const val CGS38FORMA_BOLETO = 1
@EntityCacheable
class Cgs38() : AbstractEntity(7) {
  constructor(cgs38id: Long) : this() {
    this.cgs38id = cgs38id
  }

  constructor(
    cgs38empresa: Long? = null,
    cgs38nome: String,
    cgs38tipo: Int,
    cgs38gerar: Int,
    cgs38forma: Int,
    cgs38conta: Cgs45? = null
  ) : this() {
    if(cgs38empresa != null) this.cgs38empresa = cgs38empresa
    this.cgs38nome = cgs38nome
    this.cgs38tipo = cgs38tipo
    this.cgs38gerar = cgs38gerar
    this.cgs38forma = cgs38forma
    this.cgs38conta = cgs38conta
  }


  //CUSTOM INI
  //CUSTOM END

  var cgs38id: Long = -1L
    get() {
      validateLoaded(0, "cgs38id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgs38empresa: Long = -1
    get() {
      validateLoaded(1, "cgs38empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgs38nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cgs38nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cgs38tipo: Int = -1
    get() {
      validateLoaded(3, "cgs38tipo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cgs38gerar: Int = -1
    get() {
      validateLoaded(4, "cgs38gerar", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var cgs38forma: Int = -1
    get() {
      validateLoaded(5, "cgs38forma", true)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var cgs38conta: Cgs45? = null
    get() {
      validateLoaded(6, "cgs38conta", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cgs38id
    set(value) { if(value != null)this.cgs38id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgs38
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGS38_METADATA
}
const val N_CGS38_EMPRESA = "cgs38empresa";
const val N_CGS38_NOME = "cgs38nome";
const val N_CGS38_TIPO = "cgs38tipo";
const val N_CGS38_GERAR = "cgs38gerar";
const val N_CGS38_FORMA = "cgs38forma";
const val N_CGS38_CONTA = "cgs38conta";

val CGS38ID = FieldMetadata("cgs38id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGS38EMPRESA = FieldMetadata("cgs38empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val CGS38NOME = FieldMetadata("cgs38nome", 2, "Nome", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, true, true, true);
val CGS38TIPO = FieldMetadata("cgs38tipo", 3, "Tipo", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Recebimento"),FieldOptionMetadata(1, "Pagamento")), null, null, null, true, true, false);
val CGS38GERAR = FieldMetadata("cgs38gerar", 4, "Geração do documento financeiro", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(1, "Gerar em aberto"),FieldOptionMetadata(2, "Gerar quitado")), null, null, null, true, true, false);
val CGS38FORMA = FieldMetadata("cgs38forma", 5, "Forma", FieldTypeMetadata.int, 2.0, true, null, listOf(FieldOptionMetadata(0, "Dinheiro"),FieldOptionMetadata(1, "Boleto")), null, null, null, true, true, false);
val CGS38CONTA = FieldMetadata("cgs38conta", 6, "Conta corrente para o lançamento financeiro", FieldTypeMetadata.fk, 10.0, false, "Cgs45", null, null, null, null, false, false, false);
 
val CGS38_METADATA = EntityMetadata(
  name = "Cgs38",
  descr = "Forma de Pagamento/Recebimento",

  fields = listOf(
    CGS38ID,CGS38EMPRESA,CGS38NOME,CGS38TIPO,CGS38GERAR,CGS38FORMA,CGS38CONTA,
  ),

  keys = listOf(
    KeyMetadata("cgs38_uk", KeyMetadataType.uk, "cgs38empresa, cgs38nome"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
