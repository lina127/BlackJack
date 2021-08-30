package BlackJack;

/** Game(black jack) class gives random cards, counts Ace that player got to
 * convert it from 11 to 1 if needed.
 * @author Yehyun Kim
 */

public class Game {
    /** cards array**/
    private final String[] cards = {"A", "K", "Q", "J", "2", "3", "4", "5", "6", "7", "8", "9"};
    /** cards array to integer **/
    private final int[] cardsToInt = {11,10,10,10,2,3,4,5,6,7,8,9}; //Ace counts as 11 and can be changed to 1 when sum is over 21.

    /** selected card **/
    private String selectedCard;
    /** selected card to integer **/
    private int selectedCardToInt;
    /** call Player class **/
    private Player dealer;
    private Player player;
    /**Ace counter to change its value 11 to 1 if needed **/
    private int dealerAceCounter;
    private int playerAceCounter;

    /**
     * Game constructor
     * @param dealer
     * @param player
     */
    public Game (Player dealer, Player player){
        this.dealer = dealer;
        this.player = player;
    }

    /**
     * gives random cards
     */
    public void drawRandomCard (){
        int selectedArray = (int) (Math.random() * 12); //min index 0, max index: 12
        String selectedCard = cards[selectedArray];
        this.selectedCard = selectedCard;
        this.selectedCardToInt = cardsToInt[selectedArray];
    }

    /**
     * @return selected card
     */
    public String getSelectedCard() {
        return selectedCard;
    }

    /**
     * @return selected card to int
     */
    public int getSelectedCardToInt() {
        return selectedCardToInt;
    }

    /**
     * @return number of Ace which dealer has
     */
    public int getDealerAceCounter() {
        return dealerAceCounter;
    }

    /**
     * set number of Ace that dealer has
     * @param dealerAceCounter
     */
    public void setDealerAceCounter(int dealerAceCounter) {
        this.dealerAceCounter = dealerAceCounter;
    }

    /**
     * @return number of Ace that player has
     */
    public int getPlayerAceCounter() {
        return playerAceCounter;
    }

    /**
     * set number of Ace that player has
     * @param playerAceCounter
     */
    public void setPlayerAceCounter(int playerAceCounter) {
        this.playerAceCounter = playerAceCounter;
    }

    /**
     * counts number of Ace that player and dealer have
     * @param player
     */
    public void countAce(Player player){
        if (player.getName().equalsIgnoreCase("dealer")){
            if(this.selectedCard.equals("A")) {
                this.dealerAceCounter += 1;
            }
        }
        else {
            if(this.selectedCard.equals("A")) {
                this.playerAceCounter += 1;
            }
        }
    }


    /**
     * Ace can be 11 or 1. Initial value is 11.
     * If sum goes over 21, change the value to 1.
     * @param player
     */
    public void aceConverter(Player player){

        //converts dealer ace
        if(player.getName().equalsIgnoreCase("dealer")){
            if(dealer.getCardsSum() > 21 && getDealerAceCounter() > 0) {
                dealer.setCardsSum(-10);
                this.dealerAceCounter -= 1;
            }
        }
        //converts player ace
        else{
            if (player.getCardsSum() > 21 && getPlayerAceCounter() > 0){
                player.setCardsSum(-10);
                this.playerAceCounter -= 1;
            }
        }
    }
}


