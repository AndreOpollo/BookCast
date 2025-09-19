package com.opollo.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GenreCard(genre: Genre, onClick:()->Unit, modifier: Modifier = Modifier){
    Card(
        modifier = modifier.aspectRatio(1.2f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = genre.color)
    ){
        Box(
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        ){
            Text(text = genre.name,
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Image(
                painter = painterResource(id = genre.image),
                contentDescription = genre.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.align(Alignment.BottomEnd)
                    .offset(x=30.dp,y=20.dp)
                    .size(90.dp)
                    .graphicsLayer{
                        rotationZ = 25f
                    }
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}