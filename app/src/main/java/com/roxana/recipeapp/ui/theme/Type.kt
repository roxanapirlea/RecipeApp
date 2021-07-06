package com.roxana.recipeapp.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roxana.recipeapp.R

private val Poppins = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold)
)

private val Raleway = FontFamily(
    Font(R.font.raleway_light, FontWeight.Light),
    Font(R.font.raleway_regular, FontWeight.Normal),
    Font(R.font.raleway_medium, FontWeight.Medium),
    Font(R.font.raleway_semibold, FontWeight.SemiBold)
)

val RecipesTypography = Typography(
    h1 = TextStyle(
        fontFamily = Raleway,
        fontSize = 98.sp,
        fontWeight = FontWeight.Light,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = Raleway,
        fontSize = 61.sp,
        fontWeight = FontWeight.Light,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = Raleway,
        fontSize = 49.sp,
        fontWeight = FontWeight.Normal
    ),
    h4 = TextStyle(
        fontFamily = Raleway,
        fontSize = 35.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontFamily = Raleway,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    h6 = TextStyle(
        fontFamily = Raleway,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Raleway,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Raleway,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontFamily = Poppins,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontFamily = Poppins,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.25.sp
    ),
    button = TextStyle(
        fontFamily = Poppins,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontFamily = Poppins,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        fontFamily = Poppins,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.5.sp
    )
)

@Preview("typography")
@Composable
fun TypographyPreview() {
    RecipeTheme {
        Surface {
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = "Headline 1",
                    style = MaterialTheme.typography.h1
                )
                Text(
                    text = "Headline 2",
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = "Headline 3",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Headline 4",
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = "Headline 5",
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = "Headline 6",
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = "Subtitle 1",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Subtitle 2",
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = "Body 1",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Body 2",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "Caption",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Overline",
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}

@Preview("Typography Dark")
@Composable
fun TypographyDarkPreview() {
    RecipeTheme(darkTheme = true) {
        Surface {
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = "Headline 1",
                    style = MaterialTheme.typography.h1
                )
                Text(
                    text = "Headline 2",
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = "Headline 3",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Headline 4",
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = "Headline 5",
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = "Headline 6",
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = "Subtitle 1",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Subtitle 2",
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = "Body 1",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Body 2",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "Caption",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Overline",
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}
