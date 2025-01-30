package info.agilite.shared.entities.cgs;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable
//GERADOR INI

@EntityCacheable
class Cgs15() : AbstractEntity(8) {
  constructor(cgs15id: Long) : this() {
    this.cgs15id = cgs15id
  }

  constructor(
    cgs15empresa: Long? = null,
    cgs15nome: String,
    cgs15template: String,
    cgs15titulo: String? = null,
    cgs15fromName: String? = null,
    cgs15replayTo: String? = null,
    cgs15replayToName: String? = null
  ) : this() {
    if(cgs15empresa != null) this.cgs15empresa = cgs15empresa
    this.cgs15nome = cgs15nome
    this.cgs15template = cgs15template
    this.cgs15titulo = cgs15titulo
    this.cgs15fromName = cgs15fromName
    this.cgs15replayTo = cgs15replayTo
    this.cgs15replayToName = cgs15replayToName
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
    
  var cgs15template: String = "--defaultString--"
    get() {
      validateLoaded(3, "cgs15template", true)
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
    
  var cgs15fromName: String? = null
    get() {
      validateLoaded(5, "cgs15fromName", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var cgs15replayTo: String? = null
    get() {
      validateLoaded(6, "cgs15replayTo", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var cgs15replayToName: String? = null
    get() {
      validateLoaded(7, "cgs15replayToName", false)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
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
const val N_CGS15_EMPRESA = "cgs15empresa";
const val N_CGS15_NOME = "cgs15nome";
const val N_CGS15_TEMPLATE = "cgs15template";
const val N_CGS15_TITULO = "cgs15titulo";
const val N_CGS15_FROM_NAME = "cgs15fromName";
const val N_CGS15_REPLAY_TO = "cgs15replayTo";
const val N_CGS15_REPLAY_TO_NAME = "cgs15replayToName";

val CGS15ID = FieldMetadata("cgs15id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGS15EMPRESA = FieldMetadata("cgs15empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false, null);
val CGS15NOME = FieldMetadata("cgs15nome", 2, "Nome", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, true, true, true, null);
val CGS15TEMPLATE = FieldMetadata("cgs15template", 3, "Template de e-mail", FieldTypeMetadata.string, 0.0, true, null, null, null, null, null, false, false, false, null);
val CGS15TITULO = FieldMetadata("cgs15titulo", 4, "Título do e-mail", FieldTypeMetadata.string, 100.0, false, null, null, null, null, null, false, false, false, null);
val CGS15FROMNAME = FieldMetadata("cgs15fromName", 5, "Remetente do e-mail", FieldTypeMetadata.string, 100.0, false, null, null, null, null, null, false, false, false, null);
val CGS15REPLAYTO = FieldMetadata("cgs15replayTo", 6, "Responder para", FieldTypeMetadata.string, 100.0, false, null, null, null, null, null, false, false, false, null);
val CGS15REPLAYTONAME = FieldMetadata("cgs15replayToName", 7, "Responder para", FieldTypeMetadata.string, 100.0, false, null, null, null, null, null, false, false, false, null);
 
val CGS15_METADATA = EntityMetadata(
  name = "Cgs15",
  descr = "Modelo de e-mail",
  fields = listOf(
    CGS15ID,CGS15EMPRESA,CGS15NOME,CGS15TEMPLATE,CGS15TITULO,CGS15FROMNAME,CGS15REPLAYTO,CGS15REPLAYTONAME,
  ),
  keys = listOf(
    KeyMetadata("cgs15_uk", KeyMetadataType.uk, "cgs15empresa, cgs15nome"),
  ),
  oneToMany = mapOf(
  )
)

//GERADOR END
