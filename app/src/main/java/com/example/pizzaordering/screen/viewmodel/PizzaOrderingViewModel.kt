package com.example.pizzaordering.screen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pizzaordering.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PizzaOrderingViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(PizzaOrderingUiState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                breads = getBread(),
            )
        }
    }

    fun updateBreadSize(size: Size, breadIndex: Int) {
        _state.update {
            val breads = it.breads.toMutableList()
            val bread = breads[breadIndex]

            breads[breadIndex] = bread.copy(
                size = size,
                totalPrice = bread.defaultPrice + size.price +
                        bread.toppings.filter { topping -> topping.isSelected }
                            .sumOf { topping -> topping.price },
            )

            it.copy(
                breads = breads,
            )
        }
    }

    fun updateToppingSelection(toppingIndex: Int, breadIndex: Int) {
        _state.update {
            val breads = it.breads.toMutableList()
            val bread = breads[breadIndex]
            val toppings = bread.toppings.toMutableList()
            val topping = toppings[toppingIndex]

            toppings[toppingIndex] = topping.copy(
                isSelected = !topping.isSelected,
            )

            breads[breadIndex] = bread.copy(
                toppings = toppings,
                totalPrice = bread.totalPrice +
                        if (toppings[toppingIndex].isSelected) topping.price else -topping.price,
            )

            it.copy(
                breads = breads,
            )
        }
    }


    private fun getBread(): List<Bread> {
        return listOf(
            Bread(1, R.drawable.bread_1, 50, 50, Size.SMALL, getToppings()),
            Bread(2, R.drawable.bread_2, 55, 55, Size.SMALL, getToppings()),
            Bread(3, R.drawable.bread_3, 60, 60, Size.SMALL, getToppings()),
            Bread(4, R.drawable.bread_4, 65, 65, Size.SMALL, getToppings()),
            Bread(5, R.drawable.bread_5, 70, 70, Size.SMALL, getToppings()),
        )
    }

    private fun getToppings(): List<Topping> {
        return listOf(
            Topping(
                1, "Basil", R.drawable.basil_3, 2, R.drawable.basil_group,
            ),
            Topping(
                2, "Onion", R.drawable.onion_3, 4, R.drawable.onion_group,
            ),
            Topping(
                3, "Broccoli", R.drawable.broccoli_3, 6, R.drawable.brocoli_group,
            ),
            Topping(
                4, "Mushroom", R.drawable.mushroom_3, 8, R.drawable.mushroom_group,
            ),
            Topping(
                5, "Sausage", R.drawable.sausage_3, 10,
                R.drawable.susage_group,
            ),
        )
    }
}