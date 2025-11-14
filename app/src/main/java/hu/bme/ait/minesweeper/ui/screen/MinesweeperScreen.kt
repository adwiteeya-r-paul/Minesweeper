package hu.bme.ait.minesweeper.ui.screen

import android.media.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.ait.minesweeper.R

@Composable
fun MinesweeperScreen(
    modifier: Modifier = Modifier,
    minesweeperViewModel: MinesweeperViewModel = viewModel()
) {
    val context = LocalContext.current


    Column(
        modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(
            painter = painterResource(id = R.drawable.minisweeper),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(300.dp, 100.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                if (minesweeperViewModel.checked) "Flag mode on" else "Flag mode off"
            )
            Checkbox(
                checked = minesweeperViewModel.checked,
                onCheckedChange = {minesweeperViewModel.checked = it }
            )
        }

        MinesweeperBoard(
            minesweeperViewModel.clickX,
            minesweeperViewModel.clickY,
            minesweeperViewModel.lose,
            minesweeperViewModel.checked,
            minesweeperViewModel.board,) {
            minesweeperViewModel.onCellClicked(it)
        }

        LaunchedEffect(true) {minesweeperViewModel.setNewBoard(5)}


        Button(onClick = {
            minesweeperViewModel.setNewBoard(5)
        }
        ) {
            Text(context.getString(R.string.reset))
        }

        if (minesweeperViewModel.win == 0){
            Text(context.getString(R.string.win))
        }

        if (minesweeperViewModel.lose){
            Text(context.getString(R.string.lost))
        }
    }
}




@Composable
fun MinesweeperBoard(
    clickX : Int,
    clickY : Int,
    lose : Boolean,
    checked: Boolean,
    board: Array<Array<Container>>,
    onCellClicked: (BoardCell) -> Unit,

    ) {
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    val squareImage: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.squares)
    val safe: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.safe)
    val one: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.one)
    val two: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.two)
    val three: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.three)
    val bomb: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.bomb)
    val flag: ImageBitmap =
        ImageBitmap.imageResource(R.drawable.flag)





    Canvas(
        modifier = Modifier
            .fillMaxSize(0.5f)
            .aspectRatio(1.0f)
            .pointerInput(key1 = Unit) {
                detectTapGestures {
                    var cellX = (it.x / (canvasSize.width / 5)).toInt()
                    var cellY = (it.y / (canvasSize.height / 5)).toInt()

                    onCellClicked(
                        BoardCell(cellY, cellX)
                    )

                }
            }
    )


    {
        canvasSize = this.size
        val canvasWidth = size.width.toInt()
        val canvasHeight = size.height.toInt()

        // Draw the grid
        val gridSize = size.minDimension
        val fifthsize = gridSize / 5


        for (row in 0..4) {
            for (col in 0..4) {
                var cell = board[row][col]
                var dstOffset = IntOffset(col * fifthsize.toInt(), row * fifthsize.toInt())
                var dstSize = IntSize(fifthsize.toInt(), fifthsize.toInt())


                if (!board[row][col].clicked) {
                    drawImage(
                        squareImage,
                        srcOffset = IntOffset(0, 0),
                        srcSize = IntSize(squareImage.width, squareImage.height),
                        dstOffset = dstOffset,
                        dstSize = dstSize
                    )
                }

                if (cell.count == 3|| cell.mark == 3) {
                    drawImage(
                        three,
                        srcOffset = IntOffset(0, 0),
                        srcSize = IntSize(three.width, three.height),
                        dstOffset = dstOffset,
                        dstSize = dstSize,)
                }
                if (cell.count == 2|| cell.mark == 2) {
                    drawImage(
                        two,
                        srcOffset = IntOffset(0, 0),
                        srcSize = IntSize(two.width, two.height),
                        dstOffset = dstOffset,
                        dstSize = dstSize,)
                }
                if (cell.count == 1 || cell.mark == 1) {
                    drawImage(
                        one,
                        srcOffset = IntOffset(0, 0),
                        srcSize = IntSize(one.width, one.height),
                        dstOffset = dstOffset,
                        dstSize = dstSize,)
                }
                if (cell.nearsafe) {
                    drawImage(
                        safe,
                        srcOffset = IntOffset(0, 0),
                        srcSize = IntSize(safe.width, safe.height),
                        dstOffset = dstOffset,
                        dstSize = dstSize,)
                }
                if (lose && cell.mine) {
                    drawImage(
                        bomb,
                        srcOffset = IntOffset(0, 0),
                        srcSize = IntSize(bomb.width, bomb.height),
                        dstOffset = dstOffset,
                        dstSize = dstSize,
                        )

                }
                if (cell.flagged){
                    drawImage(
                        flag,
                        srcOffset = IntOffset(0, 0),
                        srcSize = IntSize(flag.width, flag.height),
                        dstOffset = dstOffset,
                        dstSize = dstSize,
                    )
                }

            }
        }
    }
}
















