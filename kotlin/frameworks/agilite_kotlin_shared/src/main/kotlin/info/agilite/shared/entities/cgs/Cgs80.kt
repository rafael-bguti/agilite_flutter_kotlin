package info.agilite.shared.entities.cgs;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable
//GERADOR INI

const val CGS80TIPO_PESSOA_FISICA = 0
const val CGS80TIPO_PESSOA_JURIDICA = 1
const val CGS80TIPO_ESTRANGEIRO = 2
const val CGS80CONTRIBUINTE_CONTRIBUINTE_ICMS = 1
const val CGS80CONTRIBUINTE_ISENTO_DE_ICMS = 2
const val CGS80CONTRIBUINTE_NAO_CONTRIBUINTE = 9
@EntityCacheable
class Cgs80() : AbstractEntity(24) {
  constructor(cgs80id: Long) : this() {
    this.cgs80id = cgs80id
  }

  constructor(
    cgs80empresa: Long? = null,
    cgs80nome: String,
    cgs80fantasia: String? = null,
    cgs80codigo: String? = null,
    cgs80tipo: Int? = null,
    cgs80ni: String? = null,
    cgs80contribuinte: Int? = null,
    cgs80ie: String? = null,
    cgs80im: String? = null,
    cgs80cep: String? = null,
    cgs80endereco: String? = null,
    cgs80bairro: String? = null,
    cgs80numero: String? = null,
    cgs80complemento: String? = null,
    cgs80uf: String? = null,
    cgs80municipio: String? = null,
    cgs80telefone: String? = null,
    cgs80celular: String? = null,
    cgs80email: String? = null,
    cgs80obs: String? = null,
    cgs80cliente: Boolean? = null,
    cgs80fornecedor: Boolean? = null,
    cgs80transportadora: Boolean? = null
  ) : this() {
    if(cgs80empresa != null) this.cgs80empresa = cgs80empresa
    this.cgs80nome = cgs80nome
    this.cgs80fantasia = cgs80fantasia
    this.cgs80codigo = cgs80codigo
    this.cgs80tipo = cgs80tipo
    this.cgs80ni = cgs80ni
    this.cgs80contribuinte = cgs80contribuinte
    this.cgs80ie = cgs80ie
    this.cgs80im = cgs80im
    this.cgs80cep = cgs80cep
    this.cgs80endereco = cgs80endereco
    this.cgs80bairro = cgs80bairro
    this.cgs80numero = cgs80numero
    this.cgs80complemento = cgs80complemento
    this.cgs80uf = cgs80uf
    this.cgs80municipio = cgs80municipio
    this.cgs80telefone = cgs80telefone
    this.cgs80celular = cgs80celular
    this.cgs80email = cgs80email
    this.cgs80obs = cgs80obs
    this.cgs80cliente = cgs80cliente
    this.cgs80fornecedor = cgs80fornecedor
    this.cgs80transportadora = cgs80transportadora
  }


  //CUSTOM INI
  //CUSTOM END

  var cgs80id: Long = -1L
    get() {
      validateLoaded(0, "cgs80id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgs80empresa: Long = -1
    get() {
      validateLoaded(1, "cgs80empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgs80nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cgs80nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cgs80fantasia: String? = null
    get() {
      validateLoaded(3, "cgs80fantasia", false)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cgs80codigo: String? = null
    get() {
      validateLoaded(4, "cgs80codigo", false)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var cgs80tipo: Int? = null
    get() {
      validateLoaded(5, "cgs80tipo", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var cgs80ni: String? = null
    get() {
      validateLoaded(6, "cgs80ni", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var cgs80contribuinte: Int? = null
    get() {
      validateLoaded(7, "cgs80contribuinte", false)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    
  var cgs80ie: String? = null
    get() {
      validateLoaded(8, "cgs80ie", false)
      return field
    }
    set(value){
      orm.changed(field, value, 8)
      field = value
    }
    
  var cgs80im: String? = null
    get() {
      validateLoaded(9, "cgs80im", false)
      return field
    }
    set(value){
      orm.changed(field, value, 9)
      field = value
    }
    
  var cgs80cep: String? = null
    get() {
      validateLoaded(10, "cgs80cep", false)
      return field
    }
    set(value){
      orm.changed(field, value, 10)
      field = value
    }
    
  var cgs80endereco: String? = null
    get() {
      validateLoaded(11, "cgs80endereco", false)
      return field
    }
    set(value){
      orm.changed(field, value, 11)
      field = value
    }
    
  var cgs80bairro: String? = null
    get() {
      validateLoaded(12, "cgs80bairro", false)
      return field
    }
    set(value){
      orm.changed(field, value, 12)
      field = value
    }
    
  var cgs80numero: String? = null
    get() {
      validateLoaded(13, "cgs80numero", false)
      return field
    }
    set(value){
      orm.changed(field, value, 13)
      field = value
    }
    
  var cgs80complemento: String? = null
    get() {
      validateLoaded(14, "cgs80complemento", false)
      return field
    }
    set(value){
      orm.changed(field, value, 14)
      field = value
    }
    
  var cgs80uf: String? = null
    get() {
      validateLoaded(15, "cgs80uf", false)
      return field
    }
    set(value){
      orm.changed(field, value, 15)
      field = value
    }
    
  var cgs80municipio: String? = null
    get() {
      validateLoaded(16, "cgs80municipio", false)
      return field
    }
    set(value){
      orm.changed(field, value, 16)
      field = value
    }
    
  var cgs80telefone: String? = null
    get() {
      validateLoaded(17, "cgs80telefone", false)
      return field
    }
    set(value){
      orm.changed(field, value, 17)
      field = value
    }
    
  var cgs80celular: String? = null
    get() {
      validateLoaded(18, "cgs80celular", false)
      return field
    }
    set(value){
      orm.changed(field, value, 18)
      field = value
    }
    
  var cgs80email: String? = null
    get() {
      validateLoaded(19, "cgs80email", false)
      return field
    }
    set(value){
      orm.changed(field, value, 19)
      field = value
    }
    
  var cgs80obs: String? = null
    get() {
      validateLoaded(20, "cgs80obs", false)
      return field
    }
    set(value){
      orm.changed(field, value, 20)
      field = value
    }
    
  var cgs80cliente: Boolean? = null
    get() {
      validateLoaded(21, "cgs80cliente", false)
      return field
    }
    set(value){
      orm.changed(field, value, 21)
      field = value
    }
    
  var cgs80fornecedor: Boolean? = null
    get() {
      validateLoaded(22, "cgs80fornecedor", false)
      return field
    }
    set(value){
      orm.changed(field, value, 22)
      field = value
    }
    
  var cgs80transportadora: Boolean? = null
    get() {
      validateLoaded(23, "cgs80transportadora", false)
      return field
    }
    set(value){
      orm.changed(field, value, 23)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cgs80id
    set(value) { if(value != null)this.cgs80id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgs80
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGS80_METADATA
}
const val N_CGS80_EMPRESA = "cgs80empresa";
const val N_CGS80_NOME = "cgs80nome";
const val N_CGS80_FANTASIA = "cgs80fantasia";
const val N_CGS80_CODIGO = "cgs80codigo";
const val N_CGS80_TIPO = "cgs80tipo";
const val N_CGS80_NI = "cgs80ni";
const val N_CGS80_CONTRIBUINTE = "cgs80contribuinte";
const val N_CGS80_IE = "cgs80ie";
const val N_CGS80_IM = "cgs80im";
const val N_CGS80_CEP = "cgs80cep";
const val N_CGS80_ENDERECO = "cgs80endereco";
const val N_CGS80_BAIRRO = "cgs80bairro";
const val N_CGS80_NUMERO = "cgs80numero";
const val N_CGS80_COMPLEMENTO = "cgs80complemento";
const val N_CGS80_UF = "cgs80uf";
const val N_CGS80_MUNICIPIO = "cgs80municipio";
const val N_CGS80_TELEFONE = "cgs80telefone";
const val N_CGS80_CELULAR = "cgs80celular";
const val N_CGS80_EMAIL = "cgs80email";
const val N_CGS80_OBS = "cgs80obs";
const val N_CGS80_CLIENTE = "cgs80cliente";
const val N_CGS80_FORNECEDOR = "cgs80fornecedor";
const val N_CGS80_TRANSPORTADORA = "cgs80transportadora";

val CGS80ID = FieldMetadata("cgs80id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGS80EMPRESA = FieldMetadata("cgs80empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val CGS80NOME = FieldMetadata("cgs80nome", 2, "Nome ou Razão Social", FieldTypeMetadata.string, 250.0, true, null, null, null, null, null, true, true, true);
val CGS80FANTASIA = FieldMetadata("cgs80fantasia", 3, "Nome Fantasia", FieldTypeMetadata.string, 100.0, false, null, null, null, null, null, false, false, false);
val CGS80CODIGO = FieldMetadata("cgs80codigo", 4, "Código", FieldTypeMetadata.string, 15.0, false, null, null, null, null, null, true, true, false);
val CGS80TIPO = FieldMetadata("cgs80tipo", 5, "Tipo de contato", FieldTypeMetadata.int, 1.0, false, null, listOf(FieldOptionMetadata(0, "Pessoa Física"),FieldOptionMetadata(1, "Pessoa Juridica"),FieldOptionMetadata(2, "Estrangeiro")), null, null, null, false, false, false);
val CGS80NI = FieldMetadata("cgs80ni", 6, "CPF/CNPJ", FieldTypeMetadata.string, 20.0, false, null, null, null, null, null, true, true, true);
val CGS80CONTRIBUINTE = FieldMetadata("cgs80contribuinte", 7, "Código contribuinte", FieldTypeMetadata.int, 1.0, false, null, listOf(FieldOptionMetadata(1, "Contribuinte ICMS"),FieldOptionMetadata(2, "Isento de ICMS"),FieldOptionMetadata(9, "Não contribuinte")), null, null, null, false, false, false);
val CGS80IE = FieldMetadata("cgs80ie", 8, "Inscrição estadual", FieldTypeMetadata.string, 30.0, false, null, null, null, null, null, false, false, false);
val CGS80IM = FieldMetadata("cgs80im", 9, "Inscrição municipal", FieldTypeMetadata.string, 30.0, false, null, null, null, null, null, false, false, false);
val CGS80CEP = FieldMetadata("cgs80cep", 10, "CEP", FieldTypeMetadata.string, 8.0, false, null, null, null, null, null, false, false, false);
val CGS80ENDERECO = FieldMetadata("cgs80endereco", 11, "Endereço", FieldTypeMetadata.string, 200.0, false, null, null, null, null, null, false, false, false);
val CGS80BAIRRO = FieldMetadata("cgs80bairro", 12, "Bairro", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val CGS80NUMERO = FieldMetadata("cgs80numero", 13, "Número", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val CGS80COMPLEMENTO = FieldMetadata("cgs80complemento", 14, "Complemento", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val CGS80UF = FieldMetadata("cgs80uf", 15, "UF", FieldTypeMetadata.string, 2.0, false, null, null, null, null, null, false, false, false);
val CGS80MUNICIPIO = FieldMetadata("cgs80municipio", 16, "Município", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, true, true, false);
val CGS80TELEFONE = FieldMetadata("cgs80telefone", 17, "Telefone", FieldTypeMetadata.string, 20.0, false, null, null, null, null, null, true, true, false);
val CGS80CELULAR = FieldMetadata("cgs80celular", 18, "Celular", FieldTypeMetadata.string, 20.0, false, null, null, null, null, null, true, true, false);
val CGS80EMAIL = FieldMetadata("cgs80email", 19, "E-Mail", FieldTypeMetadata.string, 200.0, false, null, null, null, null, "email,min:6", true, true, false);
val CGS80OBS = FieldMetadata("cgs80obs", 20, "Observações", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, false, false, false);
val CGS80CLIENTE = FieldMetadata("cgs80cliente", 21, "Cliente", FieldTypeMetadata.boolean, 1.0, false, null, null, null, null, null, false, false, false);
val CGS80FORNECEDOR = FieldMetadata("cgs80fornecedor", 22, "Fornecedor", FieldTypeMetadata.boolean, 1.0, false, null, null, null, null, null, false, false, false);
val CGS80TRANSPORTADORA = FieldMetadata("cgs80transportadora", 23, "Transportadora", FieldTypeMetadata.boolean, 1.0, false, null, null, null, null, null, false, false, false);
 
val CGS80_METADATA = EntityMetadata(
  name = "Cgs80",
  descr = "Entidade",

  fields = listOf(
    CGS80ID,CGS80EMPRESA,CGS80NOME,CGS80FANTASIA,CGS80CODIGO,CGS80TIPO,CGS80NI,CGS80CONTRIBUINTE,CGS80IE,CGS80IM,CGS80CEP,CGS80ENDERECO,CGS80BAIRRO,CGS80NUMERO,CGS80COMPLEMENTO,CGS80UF,CGS80MUNICIPIO,CGS80TELEFONE,CGS80CELULAR,CGS80EMAIL,CGS80OBS,CGS80CLIENTE,CGS80FORNECEDOR,CGS80TRANSPORTADORA,
  ),

  keys = listOf(
    KeyMetadata("cgs80_uk", KeyMetadataType.uk, "cgs80empresa, cgs80codigo"),
    KeyMetadata("cgs80_uk1", KeyMetadataType.uk, "cgs80empresa, cgs80ni"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
