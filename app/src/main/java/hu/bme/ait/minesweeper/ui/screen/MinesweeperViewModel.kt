package hu.bme.ait.minesweeper.ui.screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random


data class Container(
    var count : Int,
    var mine: Boolean,
    var clicked: Boolean,
    var nearsafe: Boolean,
    var flagged: Boolean,
    var mark: Int,
)
data class BoardCell(
    var row: Int, var col: Int
)


class MinesweeperViewModel : ViewModel() {


    var board by mutableStateOf(
        Array(5) { Array(5){Container(-1, false, false, false, false,-1)} })
        private set

    var lose by mutableStateOf(false)
    var checked by mutableStateOf(false)

    var win by mutableIntStateOf(3)


    var clickY by mutableStateOf(-1)
    var clickX by mutableStateOf(-1)




    fun setMines(){
        val rows = (0..4).shuffled()
        val cols = (0..4).shuffled()


        for (i in 0..2) {
            var row = rows[i]
            var col = cols[i]
            var cell = board[row][col]
            cell.mine = true
        }

        board = board.copyOf()
    }


    fun setNewBoard(size: Int) {
        lose = false
        clickX = -1
        clickY = -1
        win = 3
        board = Array(size) { Array(size) {Container(-1, false, false, false, false, -1)} }
        setMines()
    }

    fun countMines(cell: BoardCell) : Int{

        var count = 0
        var row = cell.row
        var col = cell.col
        var mine = board[row][col].mine


        if (!mine) {
            // left
            if (col > 0) {
                if (board[row][col - 1].mine) {
                    count = count + 1
                }
            }

            // right
            if (col < 4) {
                if (board[row][col + 1].mine) {
                    count = count + 1
                }
            }

            // above left diagonal
            if ((row > 0) && (col > 0)) {
                if (board[row - 1][col - 1].mine) {
                    count = count + 1
                }
            }

            //above
            if (row > 0) {
                if (board[row - 1][col].mine) {
                    count = count + 1
                }
            }

            // above right diagonal
            if ((row > 0) && (col < 4)) {
                if (board[row - 1][col + 1].mine) {
                    count = count + 1
                }
            }

            // below left diagonal
            if ((row < 4) && (col > 0)) {
                if (board[row + 1][col - 1].mine) {
                    count = count + 1
                }
            }

            //below
            if (row < 4) {
                if (board[row + 1][col].mine) {
                    count = count + 1
                }
            }

            //below right diagonal
            if ((row < 4) && (col < 4)) {
                if (board[row + 1][col + 1].mine) {
                    count = count + 1
                }
            }
        }
        return count

    }

    fun checkSafe(cell: BoardCell) : Int{
        var neighbor = cell
        var count = countMines(neighbor)
        if (count == 0) {board[neighbor.row][neighbor.col].nearsafe = true}
        if (count == 1){board[neighbor.row][neighbor.col].mark = 1}
        if (count == 2){board[neighbor.row][neighbor.col].mark = 2}
        if (count == 3){board[neighbor.row][neighbor.col].mark = 3}
        board[neighbor.row][neighbor.col].clicked = true

        return count

    }

    fun findSafe(cell: BoardCell) : Array<BoardCell>{

        var row = cell.row
        var col = cell.col
        var checkList = Array(8){BoardCell(-1,-1)}
        if (col > 0){checkList[0] = BoardCell(row, col-1)}
        if (col < 4){checkList[1] = BoardCell(row, col+1)}
        if (col > 0 && row > 0){checkList[2] = BoardCell(row-1, col-1)}
        if (row > 0){checkList[3] = BoardCell(row-1, col)}
        if (col < 4 && row > 0){checkList[4] = BoardCell(row-1, col+1)}
        if (col > 0 && row < 4){checkList[5] = BoardCell(row+1, col-1)}
        if (row < 4){checkList[6] = BoardCell(row+1, col)}
        if (col < 4 && row < 4){checkList[7] = BoardCell(row+1, col+1)}

        return checkList
    }



    fun bfs(cell:BoardCell) {
        val queue = ArrayDeque<BoardCell>()

        if (cell != null) queue.add(cell)

        while (queue.isNotEmpty()) {
                val node = queue.removeFirst()
                var cells = findSafe(node)
                for (cell in cells){
                    if (cell.row != -1 && cell.col != -1 && !board[cell.row][cell.col].mine && !board[cell.row][cell.col].clicked){
                    var count = checkSafe(cell)
                    if (count == 0) {
                        queue.add(cell)
                    }
                    }
                }
        }
    }

    fun checkLoser(cell: BoardCell){
        if (board[cell.row][cell.col].mine) {
            lose = true
        }
    }


    fun onCellClicked(cell: BoardCell){

        if ( board[cell.row][cell.col].flagged ||  board[cell.row][cell.col].clicked || lose || win == 0){return}

       else{

            if (checked){ if ( board[cell.row][cell.col].mine){ board[cell.row][cell.col].flagged = true; win = win-1} else{lose = true}}

            else {
                if (board[cell.row][cell.col].mine) {
                    lose = true
                } else {
                    board[cell.row][cell.col].clicked = true
                    var count = countMines(cell)
                    board[cell.row][cell.col].count = count
                    if (count == 0) {
                        board[cell.row][cell.col].nearsafe = true
                        bfs(cell)
                    }
                }
            }
       }
        board =  board.copyOf()

    }
}