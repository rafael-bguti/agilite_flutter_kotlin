package info.agilite.shared.entities.gdf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import java.math.BigDecimal
import java.time.LocalDate
//GERADOR INI


class Gdf20() : AbstractEntity(4) {
  constructor(gdf20id: Long) : this() {
    this.gdf20id = gdf20id
  }

  constructor(
    gdf20empresa: Long? = null,
    gdf20numero: Long,
    gdf20dtEnvio: LocalDate
  ) : this() {
    if(gdf20empresa != null) this.gdf20empresa = gdf20empresa
    this.gdf20numero = gdf20numero
    this.gdf20dtEnvio = gdf20dtEnvio
  }


  //CUSTOM INI
  //CUSTOM END

  var gdf20id: Long = -1L
    get() {
      validateLoaded(0, "gdf20id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var gdf20empresa: Long = -1
    get() {
      validateLoaded(1, "gdf20empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var gdf20numero: Long = -1
    get() {
      validateLoaded(2, "gdf20numero", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var gdf20dtEnvio: LocalDate = LocalDate.now()
    get() {
      validateLoaded(3, "gdf20dtEnvio", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else gdf20id
    set(value) { if(value != null)this.gdf20id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Gdf20
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = GDF20_METADATA
}
const val N_GDF20EMPRESA = "gdf20empresa";
const val N_GDF20NUMERO = "gdf20numero";
const val N_GDF20DTENVIO = "gdf20dtEnvio";

val GDF20ID = FieldMetadata("gdf20id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val GDF20EMPRESA = FieldMetadata("gdf20empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val GDF20NUMERO = FieldMetadata("gdf20numero", 2, "Número do lote", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val GDF20DTENVIO = FieldMetadata("gdf20dtEnvio", 3, "Data de emissão", FieldTypeMetadata.date, 10.0, true, null, null, null, null, null, false, false, false);
 
val GDF20_METADATA = EntityMetadata(
  name = "Gdf20",
  descr = "Lote de envio de NFSe",

  fields = listOf(
    GDF20ID,GDF20EMPRESA,GDF20NUMERO,GDF20DTENVIO,
  ),

  keys = listOf(
    KeyMetadata("gdf20_uk", KeyMetadataType.uk, "gdf20empresa, gdf20numero"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
