package com.cory.streamline.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.foundation.DrawImage
import androidx.ui.layout.*
import androidx.ui.layout.LayoutSize.*
import androidx.ui.material.MaterialTheme
import androidx.ui.res.imageResource
import androidx.ui.tooling.preview.Preview
import com.cory.streamline.R

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsStory()
        }
    }

    @Composable
    fun NewsStory() {
        val image= +imageResource(R.drawable.header)
        Column(
            crossAxisSize = Expand,
            modifier=Spacing(16.dp)
        ) {
            Container(expanded = true,height = 180.dp) {
                DrawImage(image = image)
            }
            HeightSpacer(height = 16.dp)
            Text("A day in Shark Fin Cove")
            Text("Davenport, California")
            Text("December 2018")
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text (text = "Hello $name!")
    }

    @Preview
    @Composable
    fun PreviewGreeting() {
        MaterialTheme {
            Greeting("Android")
        }
    }
}
