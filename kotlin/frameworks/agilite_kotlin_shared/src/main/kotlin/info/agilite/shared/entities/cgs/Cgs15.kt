package info.agilite.shared.entities.cgs;

import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable

//GERADOR INI

@EntityCacheable
class Cgs15() : AbstractEntity(5) {
  constructor(cgs15id: Long) : this() {
    this.cgs15id = cgs15id
  }

  constructor(
    cgs15empresa: Long? = null,
    cgs15nome: String,
    cgs15modelo: String? = null,
    cgs15titulo: String? = null
  ) : this() {
    if(cgs15empresa != null) this.cgs15empresa = cgs15empresa
    this.cgs15nome = cgs15nome
    this.cgs15modelo = cgs15modelo
    this.cgs15titulo = cgs15titulo
  }


  //CUSTOM INI
  //CUSTOM END

  var cgs15id: Long = -1L
    get() {
      validateLoaded(0, "cgs15id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgs15empresa: Long = -1
    get() {
      validateLoaded(1, "cgs15empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgs15nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "cgs15nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cgs15modelo: String? = null
    get() {
      validateLoaded(3, "cgs15modelo", false)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cgs15titulo: String? = null
    get() {
      validateLoaded(4, "cgs15titulo", false)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cgs15id
    set(value) { if(value != null)this.cgs15id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgs15
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGS15_METADATA
}
const val N_CGS15EMPRESA = "cgs15empresa";
const val N_CGS15NOME = "cgs15nome";
const val N_CGS15MODELO = "cgs15modelo";
const val N_CGS15TITULO = "cgs15titulo";

val CGS15ID = FieldMetadata("cgs15id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGS15EMPRESA = FieldMetadata("cgs15empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val CGS15NOME = FieldMetadata("cgs15nome", 2, "Nome", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, true, true, true);
val CGS15MODELO = FieldMetadata("cgs15modelo", 3, "Modelo de e-mail", FieldTypeMetadata.string, 200.0, false, null, null, null, null, null, false, false, false);
val CGS15TITULO = FieldMetadata("cgs15titulo", 4, "Título do e-mail", FieldTypeMetadata.string, 100.0, false, null, null, null, null, null, false, false, false);
 
val CGS15_METADATA = EntityMetadata(
  name = "Cgs15",
  descr = "Modelo de e-mail",

  fields = listOf(
    CGS15ID,CGS15EMPRESA,CGS15NOME,CGS15MODELO,CGS15TITULO,
  ),

  keys = listOf(
    KeyMetadata("cgs15_uk", KeyMetadataType.uk, "cgs15empresa, cgs15nome"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
