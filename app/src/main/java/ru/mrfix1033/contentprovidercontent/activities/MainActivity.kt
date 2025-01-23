package ru.mrfix1033.contentprovidercontent.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.mrfix1033.contentprovidercontent.R
import ru.mrfix1033.contentprovidercontent.databinding.ActivityMainBinding
import ru.mrfix1033.contentprovidercontent.fragments.MainFragment
import ru.mrfix1033.contentprovidercontent.utils.FragmentReplacer

class MainActivity : AppCompatActivity(), FragmentReplacer {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replace(MainFragment())
    }

    override fun replace(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
}