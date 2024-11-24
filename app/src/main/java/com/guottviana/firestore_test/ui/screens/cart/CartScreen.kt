package com.guottviana.firestore_test.ui.screens.cart

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.CartItem
import com.guottviana.firestore_test.navigation.Routes

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = viewModel(),
    paddingValues: PaddingValues
){

    val cartItems by viewModel.cartItemList.collectAsStateWithLifecycle()

    val address by viewModel.address.collectAsStateWithLifecycle()

    val context: Context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(id = R.string.cart_title),
                style =  MaterialTheme.typography.titleMedium,
                fontSize = 32.sp)
            Spacer(modifier = Modifier.size(8.dp))
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn {
                    items(cartItems) { item ->
                        CartItem(
                            item = item,
                            onIncrement = { viewModel.incrementQuantity(item) },
                            onDecrement = { viewModel.decrementQuantity(item) },
                            onRemove = { viewModel.removeItem(item) }
                        )
                    }
                }
            }
            if (cartItems.isNotEmpty()) {
                Button(
                    onClick = {
                        if (address?.country == "" ||
                            address?.street == "" ||
                            address?.state == "" ||
                            address?.city == "" ||
                            address?.number == null ||
                            address?.number == 0
                        ) {
                            navController.navigate(Routes.addressScreen)
                            Toast.makeText(
                                context,
                                R.string.configure_address_warning,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            navController.navigate(Routes.confirmationScreen)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.checkout_button))
                }
            }

        }

    }
}

@Composable
fun CartItem(
    item: CartItem,
    onIncrement: (CartItem) -> Unit,
    onDecrement: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(alpha = 0.4f))
    ) {
        AsyncImage(
            model = item.image,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = item.flavor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "$${item.price}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End) {

            IconButton(onClick = { onRemove(item) }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete), contentDescription = null
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onIncrement(item) }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_add), contentDescription = null
                    )
                }
                Text(text = item.quantity.toString())
                IconButton(onClick = { onDecrement(item) }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_subtract),
                        contentDescription = null
                    )
                }

            }
        }
    }

}