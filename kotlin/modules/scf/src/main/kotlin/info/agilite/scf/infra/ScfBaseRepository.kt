package info.agilite.scf.infra

import info.agilite.boot.orm.repositories.RootRepository
import org.springframework.stereotype.Repository

@Repository
class ScfBaseRepository : RootRepository() {
}