package com.guottviana.firestore_test.ui.screens.cupcake

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Comment
import com.guottviana.firestore_test.domain.model.Cupcake
import com.guottviana.firestore_test.ui.screens.auth.AuthViewModel


@Composable
fun CupcakeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: CupcakeViewModel = viewModel(),
    cupcake: Cupcake,
    paddingValues: PaddingValues
){

    var comment by remember {
        mutableStateOf("")
    }

    var showPopup by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val user by authViewModel.user.collectAsStateWithLifecycle()
    val comments by viewModel.getComments(cupcake).collectAsStateWithLifecycle()
    val filteredComments = comments.sortedByDescending { it.date }


    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .padding(8.dp)
                    .clickable { navController.popBackStack() }
            )

            if (user != null) {
                if (user?.typeId!! > 1) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(alpha = 0.4f))
                            .padding(8.dp)
                            .clickable {
                                viewModel.deleteCupcake(cupcake)
                                navController.popBackStack()
                            }

                    )
                }
            }

        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            AsyncImage(
                model = cupcake.image,
                contentDescription = "",
                modifier = Modifier.size(240.dp)
            )



            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = cupcake.flavor,
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))


            if (cupcake.description != "") {
                Text(
                    modifier = Modifier.background(Color.LightGray),
                    text = cupcake.description
                )
            }else{
                Text(
                    modifier = Modifier
                        .background(Color.LightGray),
                    text = stringResource(id = R.string.no_description),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { viewModel.addToCart(cupcake, context) }) {
                Text(text = stringResource(id = R.string.add_to_cart_button))
            }

            Spacer(modifier = Modifier.height(48.dp))

            Box() {
                Column(horizontalAlignment = Alignment.End) {
                    OutlinedTextField(
                        value = comment,
                        minLines = 3,
                        onValueChange = {
                            comment = it.trimEnd()
                        })
                    if (comment != ""){
                        Button(
                            onClick = {
                                viewModel.addComment(
                                    text = comment,
                                    flavor = cupcake.flavor,
                                    context,
                                    user?.userName.toString()
                                )
                                comment = ""
                            }
                        ) {
                            Text(text = stringResource(id = R.string.sendButton))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))

            Button(onClick = {
                showPopup = true
            }) {
                Text(text = stringResource(id = R.string.show_comments))
            }
        }

        CommentsPopup(showPopup = showPopup, comments = filteredComments, onDismiss = { showPopup = false})

    }
}

@Composable
fun CommentsPopup(showPopup: Boolean, comments:  List<Comment>, onDismiss: () -> Unit){

    if (showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .zIndex(10F),
            contentAlignment = Alignment.Center
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                ),
                // to dismiss on click outside
                onDismissRequest = { onDismiss() }
            ) {
                Box(
                    Modifier
                        .width(300F.dp)
                        .height(500F.dp)
                        .background(Color.White)
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn {
                        comments.forEach {
                            item {
                                Column {
                                    Text(text = it.userName)
                                    Text(
                                        modifier = Modifier.background(Color.LightGray),
                                        text = it.text
                                    )
                                    Text(text = it.date?.toDate().toString())
                                    Spacer(modifier = Modifier.height(16.dp))
                                }

                            }
                        }
                    }

                }
            }
        }
    }
}