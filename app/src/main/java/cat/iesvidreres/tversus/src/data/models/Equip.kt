package cat.iesvidreres.tversus.src.data.models

import java.io.Serializable

data class Equip(val nom:String,
                 val integrantes: List<Any?> = List(5) { null }) : Serializable
