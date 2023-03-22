package cat.iesvidreres.tversus.src.data.models

import com.google.common.math.IntMath

data class Tournament (
    val name : String,
    val id : String,
    val game : String,
    val organizer : String,
    val description : String,
    val price: Int,
    val image: Int
)