package hu.bme.ait.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.ait.minesweeper.ui.screen.MinesweeperScreen
import hu.bme.ait.minesweeper.ui.screen.MinesweeperViewModel
import hu.bme.ait.minesweeper.ui.theme.MinesweeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinesweeperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MinesweeperScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

