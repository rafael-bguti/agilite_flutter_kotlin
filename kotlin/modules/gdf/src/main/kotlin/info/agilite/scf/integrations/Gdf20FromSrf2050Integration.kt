package info.agilite.scf.integrations

import info.agilite.boot.security.UserContext
import info.agilite.gdf.adapter.infra.Gdf20Repository
import info.agilite.shared.entities.gdf.Gdf20
import info.agilite.shared.events.srf.Srf2050EventLoteGerado
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class Gdf20FromSrf2050Integration(
  private val gdf20repo: Gdf20Repository
) {

  @EventListener
  fun onSrf2050LoteGerado(event: Srf2050EventLoteGerado) {
    val gdf20 = Gdf20(
      gdf20empresa = UserContext.safeUser.empId,
      gdf20dtEnvio = LocalDate.now()
    )

    gdf20repo.save(gdf20)
    gdf20repo.updateSrf01AddLote(gdf20.gdf20id, event.srf01ids)
  }
}