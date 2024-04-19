package com.gasstation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gasstation.R
import com.gasstation.domain.model.GasStation
import com.gasstation.domain.model.GasStationType
import com.gasstation.extensions.distanceFormat
import com.gasstation.extensions.numberFormat

@Composable
fun GasStationItem(gasStations: GasStation) {
    Card(shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .background(Color.White),
        onClick = { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(45.dp),
                painter = painterResource(id = GasStationType.getGasStationImg(gasStations.POLL_DIV_CD)),
                contentDescription = "",
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Row() {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.background(Color.Gray)
                    ) {
                        Text(
                            text = "휘발유",
                            style = typography.labelMedium,
                            color = Color.Black
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = gasStations.OS_NM,
                        style = typography.labelMedium,
                        color = Color.Gray
                    )
                }
                Row() {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = gasStations.PRICE.toString().numberFormat(),
                            style = typography.labelLarge,
                            color = Color.Black
                        )
                        Text(
                            text = stringResource(id = R.string.won)
                        )
                    }
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = gasStations.DISTANCE.distanceFormat(),
                            style = typography.labelLarge,
                            color = Color.Black
                        )
                        Text(
                            text = stringResource(id = R.string.km)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewGasStationItem() {
    GasStationItem(
        GasStation(
            DISTANCE = 2981.1,
            PRICE = 1679,
            POLL_DIV_CD = "HDO",
            GIS_X_COOR = "302896.03050",
            GIS_Y_COOR = "545023.68630",
            OS_NM = "영등포제일셀프주유소",
            UNI_ID = "A0000427"
        )
    )
}