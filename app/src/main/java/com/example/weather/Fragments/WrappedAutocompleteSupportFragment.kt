package com.example.weather.Fragments

import android.content.Intent
import com.google.android.libraries.places.widget.AutocompleteSupportFragment

// Wrapped class in order to add desired functionality and styles.
class WrappedAutocompleteSupportFragment : AutocompleteSupportFragment() {

    // Wrap AutocompleteSupportFragment in order to clear text after a place is selected.
    override fun onActivityResult(p0: Int, p1: Int, p2: Intent?) {
        super.onActivityResult(p0, p1, p2)
        this.setText("")
    }
}
