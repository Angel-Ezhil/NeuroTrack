package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.data.local.NeuroTrackDatabase
import com.example.data.repository.NeuroTrackRepository
import com.example.ui.navigation.AppNavHost
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.NeuroTrackTheme
import com.example.ui.viewmodel.AssessmentViewModel
import com.example.ui.viewmodel.AthleteViewModel
import com.example.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    private val database by lazy {
        NeuroTrackDatabase.getDatabase(applicationContext, lifecycleScope)
    }

    private val repository by lazy {
        NeuroTrackRepository(database.athleteDao(), database.assessmentDao())
    }

    private val authViewModel: AuthViewModel by viewModels()

    private val athleteViewModel: AthleteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AthleteViewModel(repository) as T
            }
        }
    }

    private val assessmentViewModel: AssessmentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AssessmentViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroTrackTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NavyDarkest
                ) {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        authViewModel = authViewModel,
                        athleteViewModel = athleteViewModel,
                        assessmentViewModel = assessmentViewModel
                    )
                }
            }
        }
    }
}
