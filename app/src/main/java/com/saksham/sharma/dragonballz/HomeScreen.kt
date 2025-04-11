package com.saksham.sharma.dragonballz

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.saksham.sharma.dragonballz.model.Character
import com.saksham.sharma.dragonballz.model.DummyCharacterResponseData
import com.saksham.sharma.dragonballz.ui.theme.BrightOrange
import com.saksham.sharma.dragonballz.ui.theme.DarkNavyBlue
import com.saksham.sharma.dragonballz.ui.theme.LightGrayWhite
import com.saksham.sharma.dragonballz.ui.theme.ultraInstinctColors


val roundedCornerShapeValue = 22.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    var selectedCharacter by remember { mutableStateOf<Character?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBlue)

    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.planet_earth),
                color = BrightOrange,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.coming_up),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.world_martial_arts_tournament),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = roundedCornerShapeValue,
                        topEnd = roundedCornerShapeValue
                    )
                )
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.participants),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
                )

                SharedTransitionLayout {
                    LazyVerticalGrid(
                        state = rememberLazyGridState(),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(DummyCharacterResponseData.characters.size) {
                            val data = DummyCharacterResponseData.characters[it]
                            AnimatedVisibility(
                                visible = data != selectedCharacter,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut(),
                                modifier = Modifier.animateItem()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Color.White,
                                            RoundedCornerShape(roundedCornerShapeValue)
                                        )
                                        .clip(RoundedCornerShape(roundedCornerShapeValue))
                                        .sharedBounds(
                                            rememberSharedContentState(key = "${data.id}-sharedBound"),
                                            animatedVisibilityScope = this@AnimatedVisibility,
                                            clipInOverlayDuringTransition = OverlayClip(
                                                RoundedCornerShape(roundedCornerShapeValue)
                                            ),
                                        )
                                ) {
                                    CharacterCardWithPopOutImage(
                                        modifier = Modifier.sharedElement(
                                            rememberSharedContentState(key = "${data.id}"),
                                            animatedVisibilityScope = this@AnimatedVisibility,
                                        ), characterData = data
                                    ) {
                                        selectedCharacter = data
                                    }
                                }
                            }
                        }
                    }

                    DetailCard(
                        characterData = selectedCharacter, onDismiss = { selectedCharacter = null })
                }
            }
        }
    }
}


@Composable
fun CharacterCardWithPopOutImage(
    modifier: Modifier = Modifier, characterData: Character, onClickListener: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(roundedCornerShapeValue))
                .background(LightGrayWhite)
                .clickable { onClickListener() },
        ) {
            AsyncImage(
                model = getImageRequest(characterData),
                contentDescription = null,
                contentScale = ContentScale.Crop, // or use TopCrop as shown below
                alignment = Alignment.TopCenter, // ensures top part is prioritized
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 6.dp, start = 6.dp, end = 6.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(characterData.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(
            characterData.description,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            maxLines = 2,
        )

    }


}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailCard(
    characterData: Character?, modifier: Modifier = Modifier, onDismiss: () -> Unit
) {


    // Animate a float that shifts gradient position
    val infiniteTransition = rememberInfiniteTransition(label = "ultraInstinctAnim")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradientShift"
    )

    val animatedBrush = Brush.linearGradient(
        colors = ultraInstinctColors,
        start = Offset(animatedOffset, 0f),
        end = Offset(animatedOffset + 500f, 500f),
        tileMode = TileMode.Mirror
    )


    AnimatedContent(
        modifier = modifier,
        targetState = characterData,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
    ) { data ->
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            if (data != null) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .border(4.dp, animatedBrush, RoundedCornerShape(roundedCornerShapeValue))
                        .background(Color.White, RoundedCornerShape(roundedCornerShapeValue))
                        .clickable {
                            false
                        }
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(roundedCornerShapeValue))
                            .clip(RoundedCornerShape(roundedCornerShapeValue))
                            .sharedBounds(
                                rememberSharedContentState(key = "${data.id}-sharedBound"),
                                animatedVisibilityScope = this@AnimatedContent,
                                clipInOverlayDuringTransition = OverlayClip(
                                    RoundedCornerShape(
                                        roundedCornerShapeValue
                                    )
                                )
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                        ) {
                            // Image
                            AsyncImage(
                                model = getImageRequest(data),
                                contentDescription = null,
                                contentScale = ContentScale.Fit, // or use TopCrop as shown below
                                alignment = Alignment.TopCenter, // ensures the top part is prioritized
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f)
                                    .sharedElement(
                                        rememberSharedContentState(key = "${data.id}"),
                                        animatedVisibilityScope = this@AnimatedContent
                                    ),
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                2.dp,
                                color = Color.DarkGray
                            )


                            // Title
                            Text(
                                text = data.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xff1d1f2e)
                            )


                            // Description
                            Text(
                                text = data.description,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp,
                                maxLines = 3
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                stringResource(R.string.base_ki_max_ki, data.ki, data.maxKi),
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )

                            Text(
                                stringResource(R.string.affiliation, data.affiliation),
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )


                        }

                        OutlinedButton(
                            modifier = Modifier.padding(end = 20.dp, bottom = 20.dp),
                            onClick = { onDismiss() }) {
                            Text(
                                stringResource(R.string.close),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = BrightOrange
                            )
                        }


                    }
                }
            }

        }

    }


}

@Composable
fun getImageRequest(data: Character): ImageRequest {
    return ImageRequest.Builder(LocalContext.current).data(data.image).crossfade(true)
        .diskCacheKey(data.image).networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.DISABLED).memoryCachePolicy(CachePolicy.ENABLED).listener(
            onStart = { Log.d("Coil", "Image loading started") },
            onSuccess = { _, _ -> Log.d("Coil", "Image loaded successfully") },
            onError = { _, throwable ->
                Log.e(
                    "Coil", "Image loading failed", throwable.throwable
                )
            }).build()
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        modifier = Modifier
    )
}