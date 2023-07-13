package com.example.pizzaordering.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun ToppingItem(
    image: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isSelected,
        enter = scaleIn(
            initialScale = 10f,
        ) + fadeIn(),
        exit = fadeOut(),
    ) {
        Image(
            modifier = modifier,
            painter = painterResource(id = image),
            contentDescription = "Bread"
        )
    }
}

