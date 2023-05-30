package cat.iesvidreres.tversus.src.data.models

import java.io.Serializable

data class Match (val equipo1: Equip,
                 val equipo2: Equip,
                  var ganador: Equip?,
                  var ganadorSeleccionado: Boolean = false): Serializable {

}