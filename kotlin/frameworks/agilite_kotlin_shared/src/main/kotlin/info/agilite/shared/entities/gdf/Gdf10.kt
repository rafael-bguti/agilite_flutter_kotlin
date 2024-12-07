package info.agilite.shared.entities.gdf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.core.model.LowerCaseMap
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
//GERADOR INI

const val GDF10SISTEMA_NFSE = 1
const val GDF10TIPODOC_DOCUMENTO = 1
const val GDF10TIPODOC_CANCELAMENTO = 2
const val GDF10TIPODOC_CARTA_DE_CORRECAO = 3
const val GDF10TIPODOC_INUTILIZACAO = 4
const val GDF10TIPODOC_EVENTO = 5
const val GDF10STATUSPROC_AGUARDANDO_PROC_ASSINCRONO = 0
const val GDF10STATUSPROC_FALHA = 10
const val GDF10STATUSPROC_ERRO = 20
const val GDF10STATUSPROC_REJEITADO = 30
const val GDF10STATUSPROC_APROVADO = 100
class Gdf10() : AbstractEntity(13) {
  constructor(gdf10id: Long) : this() {
    this.gdf10id = gdf10id
  }

  constructor(
    gdf10dtEmiss: LocalDate,
    gdf10hrEmiss: LocalTime,
    gdf10sistema: Int,
    gdf10tipoDoc: Int,
    gdf10documento: String,
    gdf10statusProc: Int,
    gdf10protocolo: String,
    gdf10cStat: String,
    gdf10xMotivo: String,
    gdf10uidTrack: String,
    gdf10dados: LowerCaseMap,
    gdf10regOrigem: LowerCaseMap
  ) : this() {
    this.gdf10dtEmiss = gdf10dtEmiss
    this.gdf10hrEmiss = gdf10hrEmiss
    this.gdf10sistema = gdf10sistema
    this.gdf10tipoDoc = gdf10tipoDoc
    this.gdf10documento = gdf10documento
    this.gdf10statusProc = gdf10statusProc
    this.gdf10protocolo = gdf10protocolo
    this.gdf10cStat = gdf10cStat
    this.gdf10xMotivo = gdf10xMotivo
    this.gdf10uidTrack = gdf10uidTrack
    this.gdf10dados = gdf10dados
    this.gdf10regOrigem = gdf10regOrigem
  }


  var gdf10id: Long = -1L
    get() {
      validateLoaded(0, "gdf10id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var gdf10dtEmiss: LocalDate = LocalDate.now()
    get() {
      validateLoaded(1, "gdf10dtEmiss", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var gdf10hrEmiss: LocalTime = LocalTime.now()
    get() {
      validateLoaded(2, "gdf10hrEmiss", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var gdf10sistema: Int = -1
    get() {
      validateLoaded(3, "gdf10sistema", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var gdf10tipoDoc: Int = -1
    get() {
      validateLoaded(4, "gdf10tipoDoc", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var gdf10documento: String? = null
    get() {
      validateLoaded(5, "gdf10documento", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var gdf10statusProc: Int = -1
    get() {
      validateLoaded(6, "gdf10statusProc", true)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var gdf10protocolo: String = "--defaultString--"
    get() {
      validateLoaded(7, "gdf10protocolo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    
  var gdf10cStat: String = "--defaultString--"
    get() {
      validateLoaded(8, "gdf10cStat", true)
      return field
    }
    set(value){
      orm.changed(field, value, 8)
      field = value
    }
    
  var gdf10xMotivo: String = "--defaultString--"
    get() {
      validateLoaded(9, "gdf10xMotivo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 9)
      field = value
    }
    
  var gdf10uidTrack: String = "--defaultString--"
    get() {
      validateLoaded(10, "gdf10uidTrack", true)
      return field
    }
    set(value){
      orm.changed(field, value, 10)
      field = value
    }
    
  var gdf10dados: LowerCaseMap = LowercaseMap()
    get() {
      validateLoaded(11, "gdf10dados", true)
      return field
    }
    set(value){
      orm.changed(field, value, 11)
      field = value
    }
    
  var gdf10regOrigem: LowerCaseMap = LowercaseMap()
    get() {
      validateLoaded(12, "gdf10regOrigem", true)
      return field
    }
    set(value){
      orm.changed(field, value, 12)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else gdf10id
    set(value) { if(value != null)this.gdf10id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Gdf10
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = GDF10_METADATA
}
const val N_GDF10DTEMISS = "gdf10dtEmiss";
const val N_GDF10HREMISS = "gdf10hrEmiss";
const val N_GDF10SISTEMA = "gdf10sistema";
const val N_GDF10TIPODOC = "gdf10tipoDoc";
const val N_GDF10DOCUMENTO = "gdf10documento";
const val N_GDF10STATUSPROC = "gdf10statusProc";
const val N_GDF10PROTOCOLO = "gdf10protocolo";
const val N_GDF10CSTAT = "gdf10cStat";
const val N_GDF10XMOTIVO = "gdf10xMotivo";
const val N_GDF10UIDTRACK = "gdf10uidTrack";
const val N_GDF10DADOS = "gdf10dados";
const val N_GDF10REGORIGEM = "gdf10regOrigem";

val GDF10ID = FieldMetadata("gdf10id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val GDF10DTEMISS = FieldMetadata("gdf10dtEmiss", 1, "Data de emissão", FieldTypeMetadata.date, 10.0, true, null, null, null, null, null, false, false, false);
val GDF10HREMISS = FieldMetadata("gdf10hrEmiss", 2, "Hora de emissão", FieldTypeMetadata.time, 6.0, true, null, null, null, null, null, false, false, false);
val GDF10SISTEMA = FieldMetadata("gdf10sistema", 3, "Sistema", FieldTypeMetadata.int, 2.0, true, null, listOf(FieldOptionMetadata(1, "NFSe")), null, null, null, false, false, false);
val GDF10TIPODOC = FieldMetadata("gdf10tipoDoc", 4, "Tipo de documento", FieldTypeMetadata.int, 2.0, true, null, listOf(FieldOptionMetadata(1, "Documento"),FieldOptionMetadata(2, "Cancelamento"),FieldOptionMetadata(3, "Carta de correção"),FieldOptionMetadata(4, "Inutilização"),FieldOptionMetadata(5, "Evento")), null, null, null, false, false, false);
val GDF10DOCUMENTO = FieldMetadata("gdf10documento", 5, "Documento armazenado", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, false, false, false);
val GDF10STATUSPROC = FieldMetadata("gdf10statusProc", 6, "Status do processamento", FieldTypeMetadata.int, 3.0, true, null, listOf(FieldOptionMetadata(0, "Aguardando proc assíncrono"),FieldOptionMetadata(10, "Falha"),FieldOptionMetadata(20, "Erro"),FieldOptionMetadata(30, "Rejeitado"),FieldOptionMetadata(100, "Aprovado")), null, null, null, false, false, false);
val GDF10PROTOCOLO = FieldMetadata("gdf10protocolo", 7, "Número protocolo", FieldTypeMetadata.string, 40.0, true, null, null, null, null, null, false, false, false);
val GDF10CSTAT = FieldMetadata("gdf10cStat", 8, "Código de retorno", FieldTypeMetadata.string, 20.0, true, null, null, null, null, null, false, false, false);
val GDF10XMOTIVO = FieldMetadata("gdf10xMotivo", 9, "Descrição do retorno", FieldTypeMetadata.string, 0.0, true, null, null, null, null, null, false, false, false);
val GDF10UIDTRACK = FieldMetadata("gdf10uidTrack", 10, "UID do Tracker armazenado externamente", FieldTypeMetadata.string, 100.0, true, null, null, null, null, null, false, false, false);
val GDF10DADOS = FieldMetadata("gdf10dados", 11, "Resumo dos dados do documento", FieldTypeMetadata.json, 0.0, true, null, null, null, null, null, false, false, false);
val GDF10REGORIGEM = FieldMetadata("gdf10regOrigem", 12, "Origem do registro", FieldTypeMetadata.json, 0.0, true, null, null, null, null, null, false, false, false);
 
val GDF10_METADATA = EntityMetadata(
  name = "Gdf10",
  descr = "Transmissões efetuadas",

  fields = listOf(
    GDF10ID,GDF10DTEMISS,GDF10HREMISS,GDF10SISTEMA,GDF10TIPODOC,GDF10DOCUMENTO,GDF10STATUSPROC,GDF10PROTOCOLO,GDF10CSTAT,GDF10XMOTIVO,GDF10UIDTRACK,GDF10DADOS,GDF10REGORIGEM,
  ),

  keys = listOf(
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
