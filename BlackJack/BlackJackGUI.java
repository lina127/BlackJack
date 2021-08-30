package BlackJack;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


/**
 * BlackJack GUI main.
 * Game rules:
 * 1. If player card sum is 21(BlackJack!), player wins automatically.
 * 2. Ace card can be 11(initial value) or 1. If card sum is higher than 21, it automatically counts as 1.
 * 3. If dealer card sum is under 16, he has to get cards until the sum is over 16.
 * 4. If dealer card sum is over 21, player wins.
 * 5. If dealer and player card sum are both under 21, higher sum wins.
 * 6. Player cannot see dealer's first card until the player press [Stay] button
 * @author Yehyun Kim
 */
public class BlackJackGUI extends Application {

    // TODO: Instance Variables for View Components and Model
    /** Call black Jack game class **/
    private Game blackJack;
    /** Call and create player and dealer **/
    private Player player;
    private Player dealer;
    private Pane root;

    // TODO: Private Event Handlers and Helper Methods
    private Label dealerLabel, nameLabel;
    private TextField nameTextField;
    private Button hitButton, stayButton;
    private Label permanentTotalAmountLabel;
    private Label permanentBettingAmountLabel;
    private Label dealerCardSum, playerCardSum;
    private Button setNameButton;
    private ImageView logoImageView;
    private TextField bettingAmountTextField;
    private Label temporaryBettingAmountLabel;
    private TextField totalAmountTextField;
    private Label temporaryTotalAmountLabel;
    private Button amountButton;
    private Button startButton;
    private Label resultLabel;
    private Button restartButton;
    private Label[] playerCardsLabel, dealerCardsLabel;
    private int playerCardsIndex, dealerCardsIndex;
    private Label cardBackSideLabel;

    // cards css
    final String cardCss = "-fx-font-family:\"Arial\";-fx-font-size:50pt;-fx-text-fill:black;-fx-background-color:white;-fx-border-color:black;-fx-padding:15;-fx-background-radius:10;-fx-border-radius:10";

    /**
     * Gives random cards & draw it on the GUI
     * @param root
     * @param player can be player or dealer
     * @param blackJack
     */
    private void drawCard(Pane root, Player player, Game blackJack) {

        blackJack.drawRandomCard(); //give random cards

        player.setCardsSum(blackJack.getSelectedCardToInt()); //set card sum
        blackJack.countAce(player); //if Ace, counter goes up by 1

        //If player is "dealer", it stores data and draws it on the GUI.
        if(player.getName().equalsIgnoreCase("dealer")){
            dealerCardsLabel[dealerCardsIndex] = new Label();
            dealerCardsLabel[dealerCardsIndex].setText(blackJack.getSelectedCard());
            dealerCardsLabel[dealerCardsIndex].setStyle(cardCss);
            dealerCardsLabel[dealerCardsIndex].resizeRelocate((120 * dealerCardsIndex + 120), 115, 80, 100);

            //Add the current card to String array
            root.getChildren().add(dealerCardsLabel[dealerCardsIndex++]);
            //create empty card to hide dealer's first card
            if(dealerCardsIndex == 1){
                cardBackSideLabel = new Label();
                cardBackSideLabel.setText("");
                cardBackSideLabel.setStyle(cardCss);
                cardBackSideLabel.resizeRelocate(120, 115, 80, 100);
                cardBackSideLabel.setMinWidth(80);
                root.getChildren().add(cardBackSideLabel); // to add it at the very front of the GUI
                cardBackSideLabel.setVisible(true);
            }
        }
        //If player is "player", it stores data and draws on the GUI.
        else{
            playerCardsLabel[playerCardsIndex] = new Label();
            playerCardsLabel[playerCardsIndex].setText(blackJack.getSelectedCard());
            playerCardsLabel[playerCardsIndex].resizeRelocate((120 * playerCardsIndex + 120), 275, 80, 100);
            playerCardsLabel[playerCardsIndex].setStyle(cardCss);
            root.getChildren().add(playerCardsLabel[playerCardsIndex++]);
        }
        blackJack.aceConverter(player); // If sum is over 21 and have Ace card, the value changes 11 to 1.
        updateSumText(player);
    }

    /**
     * updates sum text
     * @param player player or dealer
     */
    private void updateSumText(Player player){
        //if player is dealer, updates dealer sum
        if(player.getName().equalsIgnoreCase("dealer")){
            String dealerSumToString = Integer.toString(player.getCardsSum());
            dealerCardSum.setText("Sum: " + dealerSumToString);
        }
        //if player is player, updates player sum
        else{
            String playerSumToString = Integer.toString(player.getCardsSum());
            playerCardSum.setText("Sum: " + playerSumToString);
        }
    }

    /**
     * updates total amount and betting amount label
     * @param player
     */
    private void updateAmountText(Player player){
        permanentTotalAmountLabel.setText(String.format("Total Budget: $%d ", player.getTotalAmount()));
        permanentBettingAmountLabel.setText(String.format("Betting Amount: $%d ", player.getBettingAmount()));
    }

    /**
     * display win message if player wins
     */
    private void displayPlayerWin(){

        //disable hit button and stay button.
        hitButton.setDisable(true);
        stayButton.setDisable(true);
        //show result message
        resultLabel.setVisible(true);
        player.setWinTotalAmount(); //calculates player's total amount
        resultLabel.setText(String.format("Congratulation %s!\nEarned Amount: $%d\nTotal Amount: $%d", player.getName(), player.getBettingAmount(), player.getTotalAmount()));
        player.setBettingAmount(0); //reset betting amount
        updateAmountText(player); //update to current total amount and betting amount
        root.getChildren().add(restartButton); //add restartButton to root to display front
    }

    /**
     * display lost message if player lost
     */
    private void displayPlayerLost(){
        //disable hit&stay buttons
        hitButton.setDisable(true);
        stayButton.setDisable(true);
        //show result message
        resultLabel.setVisible(true);
        player.setLostTotalAmount(); //calculates current total amount
        resultLabel.setText(String.format("You Lost %s!\nTotal Amount: $%d", player.getName(), player.getTotalAmount()));
        player.setBettingAmount(0); //reset betting amount
        updateAmountText(player); //update to current total amount and betting amount
        root.getChildren().add(restartButton); //add restartButton to root to display front
    }


    /**
     * This is where you create your components and the model and add event
     * handlers.
     **/

    /**
     * If user(player) clicks stay button, it calculates player cards sum, shows dealer cards sum and show result
     * @param e
     */
    private void stayButtonHandler(ActionEvent e) {
        //show dealer cards sum
        dealerCardSum.setVisible(true);
        //disable hit&stay buttons
        hitButton.setDisable(true);
        stayButton.setDisable(true);

        root.getChildren().remove(cardBackSideLabel); //remove card backside label to display dealer's first card
        //while dealer's cards sum is under 16, he have to get cards until over 16.
        while(dealer.getCardsSum() < 17){
            drawCard(root, dealer, blackJack);
        }
        //if dealer cards sum is over 21, player win.
        if (dealer.getCardsSum() > 21) {
            displayPlayerWin();
        }
        //if dealer is blackJack (sum=21), dealer win
        else if (dealer.getCardsSum() == 21){
            displayPlayerLost();
        }
        //Dealer cards sum is under 21
        else {
            //player's card sum is higher: player win
            if (player.getCardsSum() > dealer.getCardsSum()){
                displayPlayerWin();
            }
            //dealer's card sum is higher: dealer win
            else if (player.getCardsSum() < dealer.getCardsSum()){
                displayPlayerLost();
            }
            //tie
            else {
                hitButton.setDisable(true);
                stayButton.setDisable(true);
                resultLabel.setVisible(true);
                resultLabel.setText(String.format("It's tie %s!\nTotal Amount: $%d", player.getName(), player.getTotalAmount()));
                player.setBettingAmount(0);
                updateAmountText(player);
                root.getChildren().add(restartButton);
            }
        }
    }


    /**
     * hit button: gives a card to user, and display result
     * @param e
     */
    private void hitButtonHandler(ActionEvent e){
        drawCard(root, player, blackJack); //give one random card

        //If player's sum is 21, it's blackJack. Player win
        if(player.getCardsSum()==21){
            displayPlayerWin();
        }

        //If over 21, player lost
        else if (player.getCardsSum() > 21){
            displayPlayerLost();
        }
    }

    /**
     * Display name
     * @param e
     */
    private void nameButtonHandler(ActionEvent e){
        //1.get Data from the view
        String name = nameTextField.getText();
        //3. Update the Model
        player.setName(name);
        //4. Update the view based on changes to the model
        if(name != ""){
            //Hide nameTextfield, setNameButton and relocate name label to the center bottom
            nameTextField.setVisible(false);
            setNameButton.setVisible(false);
            nameLabel.setText("Player: " + name);
            nameLabel.relocate(440,500);

            //Total Amount - visible to user
            totalAmountTextField.setVisible(true);
            temporaryTotalAmountLabel.setVisible(true);
            temporaryBettingAmountLabel.setVisible(true);
            bettingAmountTextField.setVisible(true);
            amountButton.setVisible(true);

        }

    }

    /**
     * Display current total amount and betting amount
     * @param e
     */
    private void amountButtonHandler(ActionEvent e){
        //1.get Data from the view & convert data
        int totalAmount = Integer.parseInt((totalAmountTextField.getText()));
        int bettingAmount = Integer.parseInt((bettingAmountTextField.getText()));
        // If player lost all money, they can reset the total amount
        if(player.getTotalAmount() == 0){
            player.setTotalAmount(totalAmount);
        }
        // If player has remaining money, it will be total amount for next round
        else{
            totalAmount = player.getTotalAmount();
        }

        //2. Update the Model
        //If betting amount is smaller than total amount, then process
        if(player.isBettingAmountValid(totalAmount,bettingAmount) && totalAmount != 0 && bettingAmount != 0){

            //if valid amount, set betting&total amount
            player.setBettingAmount(bettingAmount);
            player.setTotalAmount(totalAmount);

            //3. Update the view based on changes to the model
            permanentTotalAmountLabel.setText(String.format("Total Budget: $%d ", player.getTotalAmount()));
            permanentBettingAmountLabel.setText(String.format("Betting Amount: $%d ", player.getBettingAmount()));

            //Amount Setting - hide to user
            totalAmountTextField.setVisible(false);
            temporaryTotalAmountLabel.setVisible(false);
            bettingAmountTextField.setVisible(false);
            amountButton.setVisible(false);
            temporaryBettingAmountLabel.setVisible(false);

            //Start button - visible
            startButton.setVisible(true);
        }

    }

    /**
     * Game starts by giving 2 cards to player and dealer
     * @param e
     */
    private void startButtonHandler(ActionEvent e){

        //give 2 cards to player
        drawCard(root, player, blackJack);
        drawCard(root, player, blackJack);

        //give 2 cards to dealer
        drawCard(root, dealer, blackJack);
        drawCard(root, dealer, blackJack);



        //Update the Model, convert data type from int to string
        String playerSumToString = Integer.toString(player.getCardsSum());
        playerCardSum.setText("Sum: " + playerSumToString);
        String dealerSumToString = Integer.toString(dealer.getCardsSum());
        dealerCardSum.setText("Sum: " + dealerSumToString);

        //Update the view based on changes to the model
        //Hide unnecessary properties
        cardBackSideLabel.setVisible(true);
        hitButton.setVisible(true);
        stayButton.setVisible(true);
        playerCardSum.setVisible(true);
        //Display start button and make hit&stay buttons available for player
        startButton.setVisible(false);
        hitButton.setDisable(false);
        stayButton.setDisable(false);

        //If blackJack (sum=21), player win
        if(player.getCardsSum() == 21){
            hitButton.setDisable(true);
            stayButton.setDisable(true);
            displayPlayerWin();
        }
    }

    /**
     * Set new betting amount. Also set total amount if needed.
     * @param e
     */
    private void restartButtonHandler (ActionEvent e){
        root.getChildren().remove(restartButton); //remove restart button from root. It will be added when game is over (to place it very front of the screen).
        //betting amount text field and set button are visible to player
        temporaryBettingAmountLabel.setVisible(true);
        bettingAmountTextField.setVisible(true);
        amountButton.setVisible(true);

        //Hide unnecessary properties
        resultLabel.setVisible(false);
        dealerCardSum.setVisible(false);
        playerCardSum.setVisible(false);
        cardBackSideLabel.setVisible(false);

        //If player don't have remaining money, they can reset it
        if(player.getTotalAmount() == 0){
            totalAmountTextField.setVisible(true);
            temporaryTotalAmountLabel.setVisible(true);
        }

        //Reset player and dealer cards array
        while(playerCardsIndex >= 0){
            root.getChildren().remove(playerCardsLabel[playerCardsIndex--]);
        }
        while(dealerCardsIndex >= 0){
            root.getChildren().remove(dealerCardsLabel[dealerCardsIndex--]);
        }

        //Update display
        dealerCardSum.setText(String.format("Sum: ", dealer.getCardsSum()));
        playerCardSum.setText(String.format("Sum: ", player.getCardsSum()));


        // reset indexes to 0
        playerCardsIndex = 0;
        dealerCardsIndex = 0;

        //reset ace counter
        blackJack.setPlayerAceCounter(0);
        blackJack.setDealerAceCounter(0);

        //reset card sum
        player.resetCardSum();
        dealer.resetCardSum();
    }


    /**
     * @param stage The main stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        root = new Pane();
        Scene scene = new Scene(root, 1000, 550); // set the size here
        stage.setTitle("Blackjack"); // set the window title here
        stage.setScene(scene);


        // TODO: Add your GUI-building code here

        // 1. Create the model
        dealer = new Player(0,"Dealer", Integer.MAX_VALUE); //create dealer
        player = new Player(1,"",0 ); //create player
        blackJack = new Game (dealer, player); // create blackJack Game

        resetCards(); //creates new player and dealer cards index

        // 2. Create the GUI components
        dealerLabel = new Label(dealer.getName());
        nameLabel = new Label("Enter your name:");
        nameTextField = new TextField("");
        setNameButton = new Button("Set");
        hitButton = new Button("Hit");
        stayButton = new Button("Stay");

        //show total and betting amount to player
        permanentTotalAmountLabel = new Label((String.format("Total Budget: $%d", player.getTotalAmount())));
        permanentBettingAmountLabel = new Label((String.format("Betting Amount: $%d ", player.getBettingAmount())));
        dealerCardSum = new Label((String.format("Sum: ", dealer.getCardsSum())));
        playerCardSum = new Label((String.format("Sum: ", player.getCardsSum())));

        resultLabel = new Label("");
        restartButton = new Button("Restart");
        //Add blackJack card image
        Image blackJackImage = new Image("/BlackJack/aces.png",200,70, true, true);
        logoImageView = new ImageView(blackJackImage);

        //Create to set total amount
        totalAmountTextField = new TextField("0");
        temporaryTotalAmountLabel = new Label("Enter your budget");

        //Create to set betting amount
        bettingAmountTextField = new TextField("0");
        temporaryBettingAmountLabel = new Label("Enter betting amount");

        amountButton = new Button("Set");//amount set button
        startButton = new Button("Start");

        // 3. Add components to the root

        root.getChildren().addAll(permanentBettingAmountLabel, permanentTotalAmountLabel, dealerLabel, nameLabel, nameTextField, hitButton, stayButton, dealerCardSum, playerCardSum, setNameButton, logoImageView, temporaryTotalAmountLabel, totalAmountTextField, amountButton, temporaryBettingAmountLabel, bettingAmountTextField, startButton, resultLabel);


        // 4. Configure the components (colors, fonts, size, location)
        //css strings
        String textCss = "-fx-font-size:16pt;-fx-text-fill:white;-fx-font-family:Arial;";
        String setButtonCss = "-fx-font-family:Arial;-fx-font-size:12pt;-fx-text-fill:white;-fx-background-color:black;-fx-effect:dropshadow(three-pass-box, rgba(0,0,0,1), 10, 0, 0, 0);";
        String stayButtonCss= "-fx-font-size:17pt;-fx-text-fill:#785658;-fx-font-family:Arial;-fx-background-color:#F8BBB1;-fx-background-radius:10; -fx-effect:dropshadow(three-pass-box, rgba(0,0,0,1), 10, 0, 0, 0);";
        String hitButtonCss= "-fx-font-size:17pt;-fx-text-fill:#47584F;-fx-font-family:Arial;-fx-background-color:#C1D5A5;-fx-background-radius:10; -fx-effect:dropshadow(three-pass-box, rgba(0,0,0,1), 10, 0, 0, 0);";
        String sumCss = "-fx-font-family:\"Courier New\";-fx-font-size:14pt;-fx-text-fill:#687654;-fx-background-color:#E0DBEF;-fx-border-color:#9985B2;-fx-padding:5;-fx-background-radius:5;-fx-border-radius:5;";
        String textFieldCss = "-fx-font-family:\"Courier New\";-fx-font-size:12pt;-fx-text-fill:#402134;-fx-border-color:#E0DBEF;-fx-padding:5;-fx-background-radius:3;-fx-border-radius:3;";
        String restartButtonCss = "-fx-font-family:Arial;-fx-font-size:30pt;-fx-text-fill:#F7F0D2;-fx-background-color:#E97879;-fx-border-color:#F7F0D2;-fx-padding:20;-fx-background-radius:30;-fx-border-radius:30;-fx-effect:dropshadow(three-pass-box, rgba(0,0,0,1), 10, 0, 0, 0);";

        root.setStyle("-fx-background-color:green;");//background color for GUI
        dealerLabel.setStyle(textCss);
        dealerLabel.relocate(470,10);
        nameLabel.setStyle(textCss);
        nameLabel.relocate(250,445);
        nameTextField.relocate(435, 445);
        nameTextField.setStyle(textFieldCss);
        setNameButton.setStyle(setButtonCss);
        setNameButton.relocate (600, 445);
        //hitButton style
        hitButton.setStyle(hitButtonCss);
        hitButton.setPrefWidth(90);
        hitButton.relocate(230, 460);
        hitButton.setVisible(false);
        //stayButton style
        stayButton.setStyle(stayButtonCss);
        stayButton.setPrefWidth(90);
        stayButton.relocate(720, 460);
        stayButton.setVisible(false);
        //total amount & betting amount (permanent at the right top) style
        permanentTotalAmountLabel.setStyle("-fx-font-family:\"Courier New\";-fx-font-size:14pt;-fx-text-fill:#6E686A;-fx-background-color:#F5EEED;-fx-border-color:#3A3532;-fx-padding:5;-fx-background-radius:5;-fx-border-radius:5");
        permanentTotalAmountLabel.relocate(700, 10);
        permanentTotalAmountLabel.setPrefWidth(290);
        permanentBettingAmountLabel.setStyle("-fx-font-family:\"Courier New\";-fx-font-size:14pt;-fx-text-fill:#6E686A;-fx-background-color:#F5EEED;-fx-border-color:#3A3532;-fx-padding:5;-fx-background-radius:5;-fx-border-radius:5");
        permanentBettingAmountLabel.relocate(700, 50);
        permanentBettingAmountLabel.setPrefWidth(290);
        //card sum style
        dealerCardSum.setStyle(sumCss);
        dealerCardSum.relocate(450, 50);
        dealerCardSum.setPrefWidth(100);
        dealerCardSum.setVisible(false);
        playerCardSum.setStyle(sumCss);
        playerCardSum.relocate(450,450);
        playerCardSum.setPrefWidth(100);
        playerCardSum.setVisible(false);
        //blackJack image location
        logoImageView.relocate(10,10);
        //totalAmount label(temporary at the beginning of the game), text field, button design/position
        temporaryTotalAmountLabel.relocate(350, 200);
        temporaryTotalAmountLabel.setStyle(textCss);
        temporaryTotalAmountLabel.setVisible(false);
        totalAmountTextField.relocate(600,200);
        totalAmountTextField.setPrefWidth(70);
        totalAmountTextField.setVisible(false);
        totalAmountTextField.setStyle(textFieldCss);

        //bettingAmount label(temporary at the beginning of each round), text field, button design/position
        temporaryBettingAmountLabel.relocate(350, 250);
        temporaryBettingAmountLabel.setStyle(textCss);
        temporaryBettingAmountLabel.setVisible(false);
        bettingAmountTextField.relocate(600,250);
        bettingAmountTextField.setPrefWidth(70);
        bettingAmountTextField.setVisible(false);
        bettingAmountTextField.setStyle(textFieldCss);

        //set total&betting amount button style
        amountButton.relocate(480, 300);
        amountButton.setStyle(setButtonCss);
        amountButton.setVisible(false);
        //start button style
        startButton.relocate(420, 220);
        startButton.setStyle(restartButtonCss);
        startButton.setVisible(false);
        //result label style
        resultLabel.relocate(700, 10);
        resultLabel.setPrefWidth(290);
        resultLabel.setStyle("-fx-font-family:Arial;-fx-font-size:12pt;-fx-background-color:pink; -fx-background-radius:5;-fx-padding:20");
        resultLabel.setVisible(false);

        //restart button style
        restartButton.relocate(400, 230);
        restartButton.setStyle(restartButtonCss);


        // 5. Add Event Handlers and do final setup
        setNameButton.setOnAction(this::nameButtonHandler);
        amountButton.setOnAction(this::amountButtonHandler);
        startButton.setOnAction(this::startButtonHandler);
        hitButton.setOnAction(this::hitButtonHandler);
        stayButton.setOnAction(this::stayButtonHandler);
        restartButton.setOnAction(this::restartButtonHandler);

        // 6. Show the stage
        stage.show();
    }

    /**
     * Make no changes here.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * creates new card array and set index to 0
     */
    private void resetCards(){
        playerCardsLabel = new Label[20];
        dealerCardsLabel = new Label[20];
        playerCardsIndex = 0;
        dealerCardsIndex = 0;
    }


}

