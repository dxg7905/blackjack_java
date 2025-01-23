import java.util.*;

public class Blackjack {
    
    // Function to create a shuffled deck of cards
    public static List<String> cardSelector() { // Return type and name of function
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        List<String> deck = new ArrayList<>(); // Creates new INSTANCE for the list of strings

        // Make the deck
        for(String suit : suits) { // Goes through the "suits" string array and temporarily stores the current suit in the string "suits"
            for(String rank : ranks) { // Does the same thing with the ranks
                deck.add(rank + " of " + suit); // Adds each rank to a suit until there are 13 ranks for each suit, for which there are 4, thus making 52 unique cards in the deck
            }
        }

        // Shuffle the deck
        Collections.shuffle(deck); // Shuffles all the string elements in the deck string array
        return deck;
    }

    // Function to calculate the hand value
    public static int calculateHandValue(List<String> hand) { // Passes through a string of the hand and returns an int value of that hand
        int value = 0; // Set an integer of value equal to 0
        int aces = 0; // Set an integer of aces need to the checked equal to 0

        for(String card : hand) { // For loop that iterates through all cards in the current hand
            String rank = card.split(" ")[0]; // Gets the rank of the card
            if (rank.equals("A")) { // First check if the rank equals A
                value += 11; // If the rank equals A then its value is 11
                aces++; // Since the rank is an ace, add 1 to the aces integer
            } else if (rank.equals("J") ||  rank.equals("Q") || rank.equals("K")) { // Check if the rank is a Jack, Queen, or King
                value += 10; // If the rank is a Jack, Queen, or a King, then its value is 10
            } else {
                value += Integer.parseInt(rank); // If a regular card is picked, then turn the selected rank into an integer, no need to change its value

            }
        }

        // Adjust for Aces if the value is over 21
        while(value > 21 && aces > 0) { // Checks if the decks value is under 21 and there are at least 1 ace in the current hand
            value -= 10; // If there are more than 0 aces in the hand and the value of the hand is under 21, the value of the aces are 1
            aces--; // Remove 1 from the amount of aces needed to be checked, otherwise while loop will continuously loop 
        }

        return value; // Returns the value of the hand
    }
    

    // Main game loop
    public static void main(String[] args) {
        Scanner bet = new Scanner(System.in);
        
        System.out.println("");
        System.out.println("---------------" + "Welcome to Blackjack" + "---------------");
        
        System.out.println("Amounts available to bet: 5, 10, 25, 50, 100");

        // Iniitialize player balance
        int balance = 100; // Sets the players balance to 100
        List<String> deck = cardSelector(); //  Creates a string array called deck

        while (balance > 0) {
            System.out.println("\nBalance: $" + balance);
            System.out.print("Select an amount to bet: ");
            int userBet = bet.nextInt();

            if (userBet > balance || userBet <= 0) {
                System.out.println("Invalid bet. Try again.");
                continue; // Goes back to the while loop and starts again
            }

            // Deal the first round of cards
            List<String> playerHand = new ArrayList<>(); // Creates a string array and creates an INSTANCE for that array so it can be dynamically changed
            List<String> dealerHand = new ArrayList<>(); // Same thing here

            playerHand.add(deck.remove(0)); // Removes a card chosen from the deck function and returns it
            dealerHand.add(deck.remove(0)); // same here
            playerHand.add(deck.remove(0)); // same here
            dealerHand.add(deck.remove(0)); // same here

            // Show initial hands
            System.out.println("\nDealer has: " + dealerHand.get(0) + " and [hidden]");
            System.out.println("You have: " + playerHand);

            if (calculateHandValue(playerHand) == 21) {
                System.out.println("You win!");
                balance += userBet * 1.5;
                continue;
            }

            boolean playerWon = false;
            
            // Player turn
            while (true) { // decides whether the player can continue playing
                System.out.println("\nYour total: " + calculateHandValue(playerHand));
                System.out.print("Hit or Stand (h/s): ");
                String choice = bet.next(); // stores user bet in an int

                int playerTotal = calculateHandValue(playerHand);

                if (choice.equalsIgnoreCase("h")) { // checks if the letter matches, ignoring whether its uppercase or lowercase
                    playerHand.add(deck.remove(0)); // takes a card from the deck and returns it, then adds it to the players hand
                    System.out.println("Your drew: " + playerHand.get(playerHand.size() - 1)); // gets the last card in the list of cards in the users hand
                    playerTotal = calculateHandValue(playerHand);

                    if (playerTotal == 21) {
                        System.out.println("You win!");
                        balance += userBet;
                        playerWon = true;
                        
                        System.out.print("\nPlay again? (y/n): ");
                        String playAgain = bet.next();
                        if (!playAgain.equalsIgnoreCase("y")) {
                                System.out.println("Thanks for playing! Final balance : $" + balance);
                                System.exit(0);
                            } else {
                                break;
                            }
                        }

                    if (playerTotal > 21) { // seeing if the user draws a card that makes their total over 21
                        System.out.println("Bust! Your total: " + calculateHandValue(playerHand)); 
                        balance -= userBet; // subtracts the users bet form their balance
                        break;
                    } 
                } else if (choice.equalsIgnoreCase("s")) { // if user selects s, then dont draw a card and go to dealer's turn
                    break;
                } else {
                    System.out.println("Invalid choice. Try again");
                }
            }

            // Dealer turn
            if (!playerWon && calculateHandValue(playerHand) <= 21) { // checks if the players hand is less than or equal to 21
                System.out.println("\nDealer's turn...");
                System.out.println("Dealer's hand: " + dealerHand);

                while (calculateHandValue(dealerHand) < 17) {
                    dealerHand.add(deck.remove(0));
                    System.out.println("Dealer drew: " + dealerHand.get(dealerHand.size() - 1));
            }

                System.out.println("Dealer's total: " + calculateHandValue(dealerHand));

                // Determine the winner
                int playerTotal = calculateHandValue(playerHand);
                int dealerTotal = calculateHandValue(dealerHand);

                if (dealerTotal > 21 || playerTotal > dealerTotal) {
                    System.out.println("You win!");
                    balance += userBet;
                } else if (playerTotal < dealerTotal) {
                    System.out.println("Dealer wins!");
                    balance -= userBet;
                } else {
                    System.out.println("It's a tie!");
                }
            }

            // Check if the player is out of money

            if (balance <= 0) {
                System.out.println("You are out of money. Game over!");
                break;
            }

            // Ask them to play again
            System.out.print("\nPlay again? (y/n): ");
            String playAgain = bet.next();
            if (!playAgain.equalsIgnoreCase("y")) {
                break;
            }
        }

        System.out.println("Thanks for playing! Final balance: $" + balance);
    }
}