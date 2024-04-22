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
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gasstation.R
import com.gasstation.domain.model.GasStation
import com.gasstation.domain.model.GasStationType
import com.gasstation.domain.model.OilType
import com.gasstation.extensions.distanceFormat
import com.gasstation.extensions.numberFormat
import com.gasstation.ui.theme.ColorBlack
import com.gasstation.ui.theme.ColorGray
import com.gasstation.ui.theme.ColorWhite

@Composable
fun GasStationItem(gasStations: GasStation, oilType: String) {
    Card(shape = RoundedCornerShape(4.dp),
        colors = CardColors(
            contentColor = ColorWhite,
            containerColor = ColorBlack,
            disabledContainerColor = ColorBlack,
            disabledContentColor = ColorWhite
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        onClick = { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(ColorWhite),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 8.dp),
                painter = painterResource(id = GasStationType.getGasStationImg(gasStations.POLL_DIV_CD)),
                contentDescription = "",
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier.background(ColorWhite),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = CardColors(
                            contentColor = ColorWhite,
                            containerColor = ColorGray,
                            disabledContainerColor = ColorGray,
                            disabledContentColor = ColorWhite
                        ),
                    ) {
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text = oilType,
                            style = typography.labelSmall,
                            color = ColorBlack
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = gasStations.OS_NM,
                        style = typography.labelSmall,
                        color = ColorGray
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = gasStations.PRICE.toString().numberFormat(),
                            style = typography.labelLarge,
                            color = ColorBlack
                        )
                        Text(
                            modifier = Modifier.padding(start = 1.dp),
                            text = stringResource(id = R.string.won),
                            style = typography.labelMedium,
                            color = ColorBlack
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = gasStations.DISTANCE.distanceFormat(),
                            style = typography.labelLarge,
                            color = ColorBlack
                        )
                        Text(
                            modifier = Modifier.padding(start = 1.dp),
                            text = stringResource(id = R.string.km),
                            style = typography.labelMedium,
                            color = ColorBlack
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
            UNI_ID = "A0000427",
        ), OilType.B027.oil
    )
}