package cat.iesvidreres.tversus.src.core.ex

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

fun DialogFragment.show(launcher: DialogFragmentLauncher, activity: FragmentActivity) {
    launcher.show(this, activity)
}
