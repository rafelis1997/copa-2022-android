package me.dio.copa.catar.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.dio.copa.catar.R
import me.dio.copa.catar.domain.extensions.getDate
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.extensions.Reverse
import me.dio.copa.catar.extensions.capitalized
import me.dio.copa.catar.ui.theme.Shapes

typealias NotificationOnClick = (match: MatchDomain) -> Unit

@Composable
fun MainScreen(matches: List<MatchDomain>, onNotificationOnClick: NotificationOnClick) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .width(IntrinsicSize.Max)
        ) {
            Text(
                text = "Copa do Mundo",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp, top = 24.dp)
            )
        }


        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(matches) { match ->
                    MainInfo(match, onNotificationOnClick)
                }
            }
        }
    }
}

@Composable
fun MainInfo(match: MatchDomain, onNotificationOnClick: NotificationOnClick) {
    Card(
        shape = Shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box {
            Icon(
                painter = painterResource(
                    id = me.dio.copa.catar.notification.scheduler.R.drawable.ic_soccer,
                ),
                tint = MaterialTheme.colorScheme.inversePrimary,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(180.dp)
                    .offset(y = 49.dp)
                    .alpha(0.3F)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(IntrinsicSize.Min)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth(0.6F)
                        .fillMaxHeight()
                ) {
                    Teams(match)
                    Info(match)
                }


                Box(
                    modifier = Modifier.fillMaxHeight().clip(Shapes.copy(medium = RoundedCornerShape(16.dp)).medium)
                ) {
                    AsyncImage(
                        model = match.stadium.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .drawWithCache {
                                val gradient = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black),
                                    startY = size.height / 3,
                                    endY = size.height
                                )
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(gradient, blendMode = BlendMode.Multiply)
                                }
                            }

                    )

                    Text(
                        text = match.stadium.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                    )

                    Notification(match, onNotificationOnClick)
                }
            }
        }
    }
}

@Composable
fun Notification(match: MatchDomain, onClick: NotificationOnClick) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val drawable = if (match.notificationEnabled) R.drawable.ic_notifications_active
        else R.drawable.ic_notifications

        Image(
            painter = painterResource(id = drawable),
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp)
                .clickable {
                    onClick(match)
                }
                .size(24.dp)
        )
    }
}

@Composable
fun Info(match: MatchDomain) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize(),
    ) {
        val dateList = match.date.getDate().split(" ")
        Text(
            text = "Data: ${dateList[0]}",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
            ),
        )
        Text(
            text = "Horário: ${dateList[1]}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
            ),
        )
        Text(
            text = "Fase: ${match.name.capitalized()}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
            ),
        )
    }
}

@Composable
fun Teams(match: MatchDomain) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        TeamItem(team = match.team1)
        Text(
            text = "x",
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
            )
        )
        TeamItem(team = match.team2, invert = true)
    }
}

@Composable
fun TeamItem(team: TeamDomain, invert: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (invert) Arrangement.Reverse else Arrangement.Start
    ) {
        Text(
            text = team.flag,
            modifier = Modifier.align((Alignment.CenterVertically)),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.White,
            )
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = team.displayName,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
            )
        )
    }
}