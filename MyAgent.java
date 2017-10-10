import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     *
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     *
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     *
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     *
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     *
     */
    public void move()
    {
        int iCanWin = iCanWin();
        int theyCanWin = theyCanWin();

        if (iCanWin != -1){
            moveOnColumn(iCanWin);
        }
        else if (theyCanWin != -1){
            moveOnColumn(theyCanWin);
        }
        else
        {
            int randomColumn = randomMove();
            moveOnColumn(randomColumn);
        }
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     *
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
        // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     *
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     *
     * Random moves are focused on the central columns
     *
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount() / 2) + (myGame.getColumnCount() / 3);
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }

    /**
     * Returns the column that would allow the agent to win.
     *
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    public int iCanWin()
    {
        return someoneCanWin(super.iAmRed);
    }

    /**
     * Returns the column that would allow the opponent to win.
     *
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin()
    {
            return someoneCanWin(!super.iAmRed);
    }

    /**
     * Returns the column that would allow someone to win.
     *
     *
     * @param iAmRed player is Red or not
     * @return the column that would allow someone to win.
     */
    public int someoneCanWin(boolean iAmRed)
    {
        char[][] board = myGame.getBoardMatrix();
        int winColumn = -1;
        char agentsColour;

        if (iAmRed){
            agentsColour = 'R';
        }
        else {
            agentsColour = 'Y';
        }

        for (int i = 0; i < myGame.getColumnCount(); i++) {
                for (int j = 0; j < myGame.getRowCount(); j++) {
                    if (board[j][i] == agentsColour && j > 0) {
                        //vertical win
                        if (j + 2 < myGame.getRowCount() && board[j-1][i] == 'B') {
                            if (board[j][i] == board[j + 1][i] && board[j][i] == board[j + 2][i]) {
                                winColumn = i; }
                        }
                        //horizontal win
                        if (i + 3 < myGame.getColumnCount()) {
                            if (board[j][i] == board[j][i + 1] && board[j][i] == board[j][i + 2] && 'B' == board[j][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+3)) == j) {
                                winColumn = i+3;
                            }
                            if (board[j][i] == board[j][i + 1] && board[j][i] == board[j][i + 3] && 'B' == board[j][i + 2] && getLowestEmptyIndex(myGame.getColumn(i+2)) == j) {
                                winColumn = i+2;
                            }
                            if (board[j][i] == board[j][i + 2] && board[j][i] == board[j][i + 3] && 'B' == board[j][i + 1] && getLowestEmptyIndex(myGame.getColumn(i+1)) == j) {
                                winColumn = i+1;
                            }
                        }
                        if (i + 2 < myGame.getColumnCount() && i > 0) {
                            if (board[j][i] == board[j][i + 1] && board[j][i] == board[j][i + 2] && 'B' == board[j][i - 1] && getLowestEmptyIndex(myGame.getColumn(i-1)) == j) {
                                winColumn = i-1;
                            }
                        }
                        //left diagonal win
                        if (i + 3 < myGame.getColumnCount() && j + 3 < myGame.getRowCount()) {
                            if (board[j][i] == board[j + 1][i + 1] && board[j][i] == board[j + 2][i + 2] && 'B' == board[j + 3][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+3)) == j+3) {
                                winColumn = i+3;
                            }
                            if (board[j][i] == board[j + 1][i + 1] && board[j][i] == board[j + 3][i + 3] && 'B' == board[j + 2][i + 2] && getLowestEmptyIndex(myGame.getColumn(i+2)) == j+2) {
                                winColumn = i+2;
                            }
                            if (board[j][i] == board[j + 2][i + 2] && board[j][i] == board[j + 3][i + 3] && 'B' == board[j + 1][i + 1] && getLowestEmptyIndex(myGame.getColumn(i+1)) == j+1) {
                                winColumn = i+1;
                            }
                        }
                        if (i + 2 < myGame.getColumnCount() && j + 2 < myGame.getRowCount() && i > 0 && j >0) {
                            if (board[j][i] == board[j + 1][i + 1] && board[j][i] == board[j + 2][i + 2] && 'B' == board[j - 1][i - 1] && getLowestEmptyIndex(myGame.getColumn(i - 1)) == j - 1) {
                                winColumn = i-1;
                            }
                        }
                        //right diagonal win
                        if (i + 3 < myGame.getColumnCount() && j < myGame.getRowCount() && j -3 >= 0) {
                            if (board[j][i] == board[j - 1][i + 1] && board[j][i] == board[j - 2][i + 2] && 'B' == board[j - 3][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+3)) == j-3) {
                                winColumn = i+3;
                            }
                            if (board[j][i] == board[j - 1][i + 1] && board[j][i] == board[j - 3][i + 3] && 'B' == board[j - 2][i + 2] && getLowestEmptyIndex(myGame.getColumn(i+2)) == j-2) {
                                winColumn = i+2;
                            }
                            if (board[j][i] == board[j - 2][i + 2] && board[j][i] == board[j - 3][i + 3] && 'B' == board[j - 1][i + 1] && getLowestEmptyIndex(myGame.getColumn(i+1)) == j-1) {
                                winColumn = i+1;
                            }
                        }
                        if (i + 2 < myGame.getColumnCount() && j + 1 < myGame.getRowCount() && j > 2 && i > 0) {
                            if (board[j][i] == board[j - 1][i + 1] && board[j][i] == board[j - 2][i + 2] && 'B' == board[j + 1][i - 1] && getLowestEmptyIndex(myGame.getColumn(i - 1)) == j + 1) {
                                winColumn = i-1;
                            }
                        }
                    }
                }
            }
            return winColumn;
    }

    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }

}
