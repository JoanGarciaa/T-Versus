package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasUser.DatosRonda

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.Match

class DetallMatchUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detall_match)

        val match = intent.getSerializableExtra("match") as? Match

        val equip1 = match?.equipo1
        val equip2 = match?.equipo2

        val stringBuilder1 = StringBuilder("<h1>${equip1?.nom}</h1>")

        for (integrant in equip1?.integrantes!!) {
            if (integrant is Map<*, *>) {
                val nom = integrant["username"] as? String
                stringBuilder1.append("<li>$nom</li>")
            }
        }

        stringBuilder1.append("</ul>")

        val stringBuilder2 = StringBuilder("<h1>${equip2?.nom}</h1>")
        for (integrant in equip2?.integrantes!!) {
            if (integrant is Map<*, *>) {
                val nom = integrant["username"] as? String
                stringBuilder2.append("<li>$nom</li>")
            }
        }

        stringBuilder2.append("</ul>")

        val htmlList1 = stringBuilder1.toString()
        val htmlList2 = stringBuilder2.toString()

        val textView1: TextView = findViewById(R.id.equip1TextView)
        textView1.text = HtmlCompat.fromHtml(htmlList1, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val textView2: TextView = findViewById(R.id.equip2TextView)
        textView2.text = HtmlCompat.fromHtml(htmlList2, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val equipo1Layout: LinearLayout = findViewById(R.id.equipo1)
        val equipo2Layout: LinearLayout = findViewById(R.id.equipo2)

        if (match != null) {
            if (match.equipo1?.nom.equals(match.ganador?.nom)) {
                equipo1Layout.setBackgroundColor(Color.parseColor("#00B894"));
            } else if (match.equipo2?.nom.equals(match.ganador?.nom)) {
                equipo2Layout.setBackgroundColor(Color.parseColor("#00B894"));
            }
        }

    }
}