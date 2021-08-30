package BlackJack;

/** Player class sets player type, name and totalAmount.
 * @author Yehyun Kim
 */

public class Player {
    /** player name **/
    private String name;
    /** betting amount per round **/
    private int bettingAmount;
    /** total amount that player has **/
    private int totalAmount;
    /** player type [0]=dealer, [1]=player **/
    private int type;
    /** sum of cards **/
    private int cardsSum;

    /**
     * Constructor. Creates player.
     * @param type [0]dealer or [1]player
     * @param name
     * @param totalAmount
     */
    public Player(int type, String name, int totalAmount) {
        this.type = type;
        this.name = name;
        this.totalAmount = totalAmount;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return betting amount per round
     */
    public int getBettingAmount() {
        return bettingAmount;
    }

    /**
     * Check if it is valid betting amount. Lower than total amount.
     * @param totalAmount
     * @param bettingAmount
     * @return boolean
     */
    public boolean isBettingAmountValid(int totalAmount, int bettingAmount) {
        if (bettingAmount > totalAmount) {
            return false;
        }
        return true;
    }

    /**
     * set betting amount
     * @param bettingAmount
     */
    public void setBettingAmount(int bettingAmount) {
        this.bettingAmount = bettingAmount;
    }

    /**
     * set total amount
     * @param totalAmount
     */
    public void setTotalAmount(int totalAmount) {

        this.totalAmount = totalAmount;
    }

    /**
     * @return total amount
     */
    public int getTotalAmount() {
        return totalAmount;
    }

    /**
     * resets card sum
     */
    public void resetCardSum(){
        this.cardsSum = 0;
    }

    /**
     * If win, user will earn as much as the betting amount.
     */
    public void setWinTotalAmount() {
        this.totalAmount = this.totalAmount + this.bettingAmount; //이기면 두배

    }

    /**
     * If lost, user will loose betting money
     */
    public void setLostTotalAmount() {
        this.totalAmount = this.totalAmount - this.bettingAmount; //이기면 두배

    }

    /**
     * set sum of cards
     * @param currentCard
     */
    public void setCardsSum(int currentCard) {
        this.cardsSum += currentCard;
    }

    /**
     * @return sum of cards
     */
    public int getCardsSum() {
        return cardsSum;
    }
}
