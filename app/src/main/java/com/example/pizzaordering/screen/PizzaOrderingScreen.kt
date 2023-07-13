@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package com.example.pizzaordering.screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.pizzaordering.R
import com.example.pizzaordering.screen.viewmodel.Bread
import com.example.pizzaordering.screen.viewmodel.PizzaOrderingUiState
import com.example.pizzaordering.screen.viewmodel.PizzaOrderingViewModel
import com.example.pizzaordering.screen.viewmodel.Size
import com.example.pizzaordering.screen.viewmodel.Topping
import com.example.pizzaordering.ui.theme.PizzaOrderingTheme

@Composable
fun PizzaOrderingScreen(
    viewModel: PizzaOrderingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    PizzaOrderingContent(
        state,
        onSizeClick = viewModel::updateBreadSize,
        onToppingClick = viewModel::updateToppingSelection
    )
}

@Composable
fun PizzaOrderingContent(
    state: PizzaOrderingUiState,
    onSizeClick: (Size, Int) -> Unit,
    onToppingClick: (Int, Int) -> Unit,
) {
    Scaffold(
        containerColor = Color.White,
        topBar = { PizzaOrderingTopBar() }
    ) { paddings ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddings.calculateTopPadding(),
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val pagerState = rememberPagerState(0)

            BreadPager(
                pagerState = pagerState,
                breads = state.breads,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .height(232.dp)
                    .fillMaxWidth()
                    .paint(
                        painter = painterResource(id = R.drawable.plate),
                        contentScale = ContentScale.Inside,
                    )
            )

            PizzaPrice(
                totalPrice = "$${state.breads[pagerState.currentPage].totalPrice}",
                modifier = Modifier.padding(top = 32.dp)
            )

            PizzaSizes(
                size = state.breads[pagerState.currentPage].size,
                modifier = Modifier.padding(top = 16.dp),
                onSizeClick = { size -> onSizeClick(size, pagerState.currentPage) },
            )

            PizzaToppingText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp),
            )

            PizzaToppings(
                toppings = state.breads[pagerState.currentPage].toppings,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = { toppingIndex -> onToppingClick(toppingIndex, pagerState.currentPage) },
            )

            Spacer(modifier = Modifier.weight(1f))

            AddToCartButton(
                modifier = Modifier.padding(bottom = 64.dp),
            )
        }
    }
}

@Composable
fun PizzaOrderingTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Pizza",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Menu",
                )
            }

        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                )
            }
        },
        modifier = Modifier.padding(4.dp),
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)

    )
}

@Composable
fun BreadPager(
    pagerState: PagerState,
    breads: List<Bread>,
    modifier: Modifier = Modifier
) {

    HorizontalPager(
        pageCount = breads.size,
        modifier = modifier,
        state = pagerState,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val size by animateDpAsState(
            when (breads[it].size) {
                Size.SMALL -> 32.dp
                Size.MEDIUM -> 24.dp
                Size.LARGE -> 16.dp
            }, label = "size Animation",
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        Box(
            modifier = Modifier
                .padding(size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(breads[it].image)
                    .scale(Scale.FILL)
                    .build(),
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = "Bread"
            )

            for (item in breads[it].toppings) {
                ToppingItem(
                    image = item.image,
                    isSelected = item.isSelected,
                    modifier = Modifier
                        .size(180.dp)
                        .padding(size)
                )
            }
        }
    }
}

@Composable
fun PizzaPrice(
    totalPrice: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = totalPrice,
        color = Color.Black,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun PizzaSizes(
    size: Size,
    modifier: Modifier = Modifier,
    onSizeClick: (Size) -> Unit,
) {
    val position by animateDpAsState(
        targetValue = when (size) {
            Size.SMALL -> 0.dp
            Size.MEDIUM -> 48.dp
            Size.LARGE -> 95.dp
        }, label = "size Animation",
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        )
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .matchParentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = Modifier
                    .size(48.dp)
                    .offset(x = position, y = 0.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
            ) {

            }
        }

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PizzaSize(
                text = "S",
                onClick = { onSizeClick(Size.SMALL) },
            )
            PizzaSize(
                text = "M",
                onClick = { onSizeClick(Size.MEDIUM) },
            )
            PizzaSize(
                text = "L",
                onClick = { onSizeClick(Size.LARGE) },
            )
        }
    }
}

@Composable
fun PizzaSize(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.size(48.dp).wrapContentHeight()
            .clickable { onClick() },
        text = text,
        textAlign = TextAlign.Center,
        color = Color.Black,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun PizzaToppingText(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = "Customize your pizza".uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
    )
}

@Composable
fun PizzaToppings(
    toppings: List<Topping>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        itemsIndexed(toppings, key = { _, item -> item.id }) { index, topping ->
            PizzaToppingItem(
                image = topping.mainImage,
                isSelected = topping.isSelected,
                onClick = { onClick(index) },
            )
        }
    }
}

@Composable
fun PizzaToppingItem(
    image: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier
            .size(64.dp)
            .background(
                if (isSelected) Color(0XFFDBF3E2)
                else Color.Transparent, CircleShape
            ),
        onClick = onClick,
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = image),
            contentDescription = "Topping",
        )
    }
}


@Composable
fun AddToCartButton(
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF372B29),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = "Add to cart",
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = "Add to cart",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}


@Preview
@Composable
fun PizzaOrderingScreenPreview() {
    PizzaOrderingTheme {
        PizzaOrderingScreen()
    }
}


