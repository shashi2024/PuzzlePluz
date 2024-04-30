package com.example.puzzelpluz

class Board (
    size:Int
) {
    private val size:Int
    private var numOfMoves:Int
    private val places:MutableList<Place>
    private val listners:MutableList<BoardChangeListener>

    fun rearrnge(){
        numOfMoves = 0
        for(i in 0 until size * size){
            swapTiles()
        }
        do{
            swapTiles()
        }while(!solvable() || solved())
    }
    /**swap two titles randomly**/
    private fun swapTiles(){
        val p1: Place? = at(rendoms.nextInt(size) + 1, rendoms.nextInt(size)+1)
        val p2: Place? = at(rendoms.nextInt(size) + 1, rendoms.nextInt(size)+1)

        if (p1 != p2){
            val t : Tile? = p1?.tile
            p1!!.tile = p2?.tile
            p2!!.tile = t
        }
    }

    /**Is the puzzele current arrangement of tile**/
    private fun solvable():Boolean{
        var inversion = 0
        for (p: Place in places){
            val pt: Tile? = p.tile
            for(q: Place in places){
                val qt :Tile? = q.tile
                if(p != q && pt != null && qt != null
                    && indexOf(p)< indexOf (q)
                    && pt.number()> qt.number()
                    ){
                    inversion++
                }
            }
        }

        val isEvenSize = size % 2 == 0
        val isEvenInversion = inversion %2 ==0
        var isBlankOnOddRow = blank()!!.y %2 ==1

        //from the bottom
        isBlankOnOddRow = if (isEvenSize) ! isBlankOnOddRow else isBlankOnOddRow

        return ! isEvenSize && isEvenInversion ||
                isEvenSize && isBlankOnOddRow == isEvenInversion
    }
    /**return the 1 -based index of the given place when all
    *the places are arranged in row - major order
    **/
    private fun indexOf(p:Place):Int{
        return (p.y -1) * size + p.x
    }
    /**Is this puzzle solved*/

    fun solved():Boolean{
        var result = true
        for (p:Place in places){
            result = result &&
                    (p.x == size && p.y == size
                            || p.tile != null &&
                            p.tile!!.number() == indexOf(p))
        }
        return  result
    }
    fun slide(tile: Tile?){
        for (p:Place in places){
            if(p.tile === tile){
                val to:Place? = blank()
                to!!.tile = tile
                p.tile = null
                numOfMoves++
                notifyTileSliding(p, to, numOfMoves)
                if (solved()){
                    notifyPuzzleSolved(numOfMoves)
                }
                return
            }
        }
    }

    /**Is the tile in the given place slidable? */
    fun slidable(place: Place):Boolean
    {
        val x:Int = place.x
        val y:Int = place.y
        return (isBlank(x-1,y)|| isBlank(x+1,y)
                ||isBlank(x,y-1) || isBlank(x,y+1))
    }
    private fun isBlank(x: Int, y:Int):Boolean
    {
        return ((x in 1..size)
                && (y in 1..size)
                && (at(x,y)!!. tile == null)
                )
    }
    private fun blank():Place?{
        for (p:Place in places){
            if(p.tile == null){
                return p
            }
        }
        //assert false : "Should never reach here!!"
        return null
    }
    fun places():Iterable<Place> = places

    fun at(x:Int,y:Int): Place?{

        for (p:Place in places){
            if(p.x == x && p.y == y){
                return p
            }
        }
        //assert false : "precondition violation !!"
        return null
    }
    fun size (): Int = size
    fun numOfMoves ():Int = numOfMoves

    fun addBoardChangeListener(listener:BoardChangeListener){
        if (!listners.contains(listener)){
            listners.add(listener)
        }
    }

    fun removeBoardChangeListener(listener:BoardChangeListener){
        listners.remove(listener)
    }

    private fun notifyPuzzleSolved(numOfMove:Int){
        for(listener:BoardChangeListener in listners){
            listener.solved(numOfMove)
        }
    }
    private fun notifyTileSliding(from :Place, to:Place, numOfMove: Int){
        for(listener:BoardChangeListener in listners){
            listener.tileSlid(from, to,numOfMove)
        }
    }

    companion object{
        private val rendoms: kotlin.random.Random = kotlin.random.Random
    }

    init{
        listners = ArrayList()
        this.size = size
        places = ArrayList(size * size)
        for (x in 1..size){
                for (y in 1..size){
                    places.add(
                        if(x == size && y == size){
                            Place(x,y,this)
                        }else {
                            Place(x, y, (y - 1) * size + x, this)
                        }
                    )
                }
            }
        numOfMoves = 0
    }
}