package info.agilite.shared.entities.cas;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable
//GERADOR INI

@EntityCacheable
class Cas65() : AbstractEntity(12) {
  constructor(cas65id: Long) : this() {
    this.cas65id = cas65id
  }

  constructor(
    cas65cnpj: String,
    cas65im: String? = null,
    cas65nome: String,
    cas65cnae: String? = null,
    cas65cep: String? = null,
    cas65endereco: String? = null,
    cas65bairro: String? = null,
    cas65numero: String? = null,
    cas65complemento: String? = null,
    cas65uf: String? = null,
    cas65municipio: String? = null
  ) : this() {
    this.cas65cnpj = cas65cnpj
    this.cas65im = cas65im
    this.cas65nome = cas65nome
    this.cas65cnae = cas65cnae
    this.cas65cep = cas65cep
    this.cas65endereco = cas65endereco
    this.cas65bairro = cas65bairro
    this.cas65numero = cas65numero
    this.cas65complemento = cas65complemento
    this.cas65uf = cas65uf
    this.cas65municipio = cas65municipio
  }


  //CUSTOM INI
  //CUSTOM END

  var cas65id: Long = -1L
    get() {
      validateLoaded(0, "cas65id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cas65cnpj: String = "--defaultString--"
    get() {
      validateLoaded(1, "cas65cnpj", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cas65im: String? = null
    get() {
      validateLoaded(2, "cas65im", false)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cas65nome: String = "--defaultString--"
    get() {
      validateLoaded(3, "cas65nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cas65cnae: String? = null
    get() {
      validateLoaded(4, "cas65cnae", false)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var cas65cep: String? = null
    get() {
      validateLoaded(5, "cas65cep", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var cas65endereco: String? = null
    get() {
      validateLoaded(6, "cas65endereco", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var cas65bairro: String? = null
    get() {
      validateLoaded(7, "cas65bairro", false)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    
  var cas65numero: String? = null
    get() {
      validateLoaded(8, "cas65numero", false)
      return field
    }
    set(value){
      orm.changed(field, value, 8)
      field = value
    }
    
  var cas65complemento: String? = null
    get() {
      validateLoaded(9, "cas65complemento", false)
      return field
    }
    set(value){
      orm.changed(field, value, 9)
      field = value
    }
    
  var cas65uf: String? = null
    get() {
      validateLoaded(10, "cas65uf", false)
      return field
    }
    set(value){
      orm.changed(field, value, 10)
      field = value
    }
    
  var cas65municipio: String? = null
    get() {
      validateLoaded(11, "cas65municipio", false)
      return field
    }
    set(value){
      orm.changed(field, value, 11)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cas65id
    set(value) { if(value != null)this.cas65id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cas65
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CAS65_METADATA
}
const val N_CAS65CNPJ = "cas65cnpj";
const val N_CAS65IM = "cas65im";
const val N_CAS65NOME = "cas65nome";
const val N_CAS65CNAE = "cas65cnae";
const val N_CAS65CEP = "cas65cep";
const val N_CAS65ENDERECO = "cas65endereco";
const val N_CAS65BAIRRO = "cas65bairro";
const val N_CAS65NUMERO = "cas65numero";
const val N_CAS65COMPLEMENTO = "cas65complemento";
const val N_CAS65UF = "cas65uf";
const val N_CAS65MUNICIPIO = "cas65municipio";

val CAS65ID = FieldMetadata("cas65id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CAS65CNPJ = FieldMetadata("cas65cnpj", 1, "Cnpj", FieldTypeMetadata.string, 14.0, true, null, null, null, null, null, false, false, false);
val CAS65IM = FieldMetadata("cas65im", 2, "Incrição Municipal", FieldTypeMetadata.string, 20.0, false, null, null, null, null, null, false, false, false);
val CAS65NOME = FieldMetadata("cas65nome", 3, "Nome", FieldTypeMetadata.string, 100.0, true, null, null, null, null, null, false, false, false);
val CAS65CNAE = FieldMetadata("cas65cnae", 4, "Código CNAE", FieldTypeMetadata.string, 10.0, false, null, null, null, null, null, false, false, false);
val CAS65CEP = FieldMetadata("cas65cep", 5, "CEP", FieldTypeMetadata.string, 8.0, false, null, null, null, null, null, false, false, false);
val CAS65ENDERECO = FieldMetadata("cas65endereco", 6, "Endereço", FieldTypeMetadata.string, 200.0, false, null, null, null, null, null, false, false, false);
val CAS65BAIRRO = FieldMetadata("cas65bairro", 7, "Bairro", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val CAS65NUMERO = FieldMetadata("cas65numero", 8, "Número", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val CAS65COMPLEMENTO = FieldMetadata("cas65complemento", 9, "Complemento", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val CAS65UF = FieldMetadata("cas65uf", 10, "UF", FieldTypeMetadata.string, 2.0, false, null, null, null, null, null, false, false, false);
val CAS65MUNICIPIO = FieldMetadata("cas65municipio", 11, "Município", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, true, true, false);
 
val CAS65_METADATA = EntityMetadata(
  name = "Cas65",
  descr = "Empresa",

  fields = listOf(
    CAS65ID,CAS65CNPJ,CAS65IM,CAS65NOME,CAS65CNAE,CAS65CEP,CAS65ENDERECO,CAS65BAIRRO,CAS65NUMERO,CAS65COMPLEMENTO,CAS65UF,CAS65MUNICIPIO,
  ),

  keys = listOf(
    KeyMetadata("cas65_uk", KeyMetadataType.uk, "cas65cnpj"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
