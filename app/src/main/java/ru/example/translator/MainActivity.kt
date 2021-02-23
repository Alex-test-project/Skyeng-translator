package ru.example.translator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.example.translator.databinding.ActivityMainBinding
import ru.example.translator.search.presentation.SearchFragment

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initBinding()
		openFragment(savedInstanceState)
	}

	private fun openFragment(savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction()
				.add(R.id.flFragmentContainer, SearchFragment())
				.commit()
		}
	}

	private fun initBinding() {
		binding = ActivityMainBinding.inflate(layoutInflater).apply {
			setContentView(root)
		}
	}
}