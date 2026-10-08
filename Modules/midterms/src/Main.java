import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Scanner;

// Runs the game. Each turn the program gives a random command to the player.
// There are 3 stacks: the player deck, the player hand and the discard pile.
// The game ends when the player deck is empty.
public class Main
{
    // the three possible commands
    private static final int DRAW = 0;
    private static final int DISCARD = 1;
    private static final int GET_FROM_DISCARD = 2;

    private static final int DECK_SIZE = 30;
    private static final int MAX_CARDS_PER_COMMAND = 5;
    private static final String[] CARD_NAMES = {
            "Bathala", "Mayari", "Apolaki", "Tala", "Lakapati",
            "Bakunawa", "Tikbalang", "Manananggal", "Sarimanok", "Diwata"
    };

    // used for the console display
    private static final int WIDTH = 60;
    private static final String HEAVY_LINE = "=".repeat(WIDTH);
    private static final String LIGHT_LINE = "-".repeat(WIDTH);
    private static final String CARD_INDENT = "        "; // same length as " TOP -> "

    public static void main(String[] args)
    {
        // the 3 stacks
        CardStack deck = new CardStack(DECK_SIZE);
        CardStack hand = new CardStack(DECK_SIZE);
        CardStack discardPile = new CardStack(DECK_SIZE);
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        buildDeck(deck, random);
        printStart(hand, deck, discardPile);

        // keep playing turns until the deck is empty
        int round = 0;
        while (!deck.isEmpty())
        {
            // the player must press Enter to start each turn
            System.out.print("\n Press Enter to proceed to the next turn...");
            scanner.nextLine();

            round++;
            playTurn(round, deck, hand, discardPile, random);
        }

        printGameOver(round);
        scanner.close();
    }

    // creates 30 cards (each name appears 3 times), shuffles them, then pushes them to the deck
    private static void buildDeck(CardStack deck, Random random)
    {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < DECK_SIZE; i++)
        {
            cards.add(new Card(CARD_NAMES[i % CARD_NAMES.length]));
        }

        Collections.shuffle(cards, random);
        for (Card card : cards)
        {
            deck.push(card);
        }
    }

    // gives one random command, carries it out, then shows the result
    private static void playTurn(int round, CardStack deck, CardStack hand,
                                 CardStack discardPile, Random random)
    {
        int command = pickRandomCommand(hand, discardPile, random);

        String commandName;
        String route;
        CardStack source;
        CardStack target;

        if (command == DRAW)
        {
            commandName = "DRAW";
            route = "Deck -> Hand";
            source = deck;
            target = hand;
        }
        else if (command == DISCARD)
        {
            commandName = "DISCARD";
            route = "Hand -> Discard Pile";
            source = hand;
            target = discardPile;
        }
        else
        {
            commandName = "GET FROM DISCARD PILE";
            route = "Discard Pile -> Hand";
            source = discardPile;
            target = hand;
        }

        // x is random from 1 to 5, but never more than the cards available,
        // so the command always matches what actually happens
        int maxAmount = Math.min(MAX_CARDS_PER_COMMAND, source.size());
        int amount = random.nextInt(maxAmount) + 1;

        ArrayList<Card> movedCards = moveCards(source, target, amount);

        String commandText = commandName + " " + movedCards.size() + " card(s)";
        printRound(round, commandText, route, movedCards, hand, deck, discardPile);
    }

    // picks a random command, but only from the commands that can be done right now
    private static int pickRandomCommand(CardStack hand, CardStack discardPile, Random random)
    {
        ArrayList<Integer> availableCommands = new ArrayList<>();
        availableCommands.add(DRAW); // the deck is never empty while the game is running

        if (!hand.isEmpty())
        {
            availableCommands.add(DISCARD);
        }
        if (!discardPile.isEmpty())
        {
            availableCommands.add(GET_FROM_DISCARD);
        }

        return availableCommands.get(random.nextInt(availableCommands.size()));
    }

    // moves up to 'amount' cards from the top of one stack to the top of another
    // returns the cards that were actually moved
    private static ArrayList<Card> moveCards(CardStack from, CardStack to, int amount)
    {
        ArrayList<Card> movedCards = new ArrayList<>();

        int count = Math.min(amount, from.size());
        for (int i = 0; i < count; i++)
        {
            Card card = from.pop();
            to.push(card);
            movedCards.add(card);
        }
        return movedCards;
    }

    // ---------------------------- Display ----------------------------

    private static void printStart(CardStack hand, CardStack deck, CardStack discardPile)
    {
        System.out.println(HEAVY_LINE);
        System.out.println(centered("MYTHICAL CARD GAME"));
        System.out.println(centered("The game ends when the deck runs out"));
        System.out.println(HEAVY_LINE);
        System.out.println(" STARTING STATE");
        printState(hand, deck, discardPile);
    }

    // shows what happened this round, then the state of all three stacks
    private static void printRound(int round, String command, String route,
                                   ArrayList<Card> movedCards, CardStack hand,
                                   CardStack deck, CardStack discardPile)
    {
        System.out.println();
        System.out.println(HEAVY_LINE);
        System.out.println(centered("ROUND " + round));
        System.out.println(HEAVY_LINE);
        System.out.println(" COMMAND : " + command);
        System.out.println(" MOVED   : " + route);
        System.out.println(" CARDS   : " + joinNames(movedCards));
        printState(hand, deck, discardPile);
    }

    private static void printGameOver(int rounds)
    {
        System.out.println();
        System.out.println(HEAVY_LINE);
        System.out.println(centered("GAME OVER"));
        System.out.println(centered("The deck is empty after " + rounds + " rounds"));
        System.out.println(HEAVY_LINE);
    }

    // prints the hand, the number of cards left in the deck, and the number in the discard pile
    private static void printState(CardStack hand, CardStack deck, CardStack discardPile)
    {
        System.out.println(LIGHT_LINE);
        printCardList("YOUR HAND", hand);
        System.out.println(LIGHT_LINE);
        System.out.println(" DECK (draw pile)  : " + deck.size() + " card(s) remaining");
        System.out.println(" DISCARD PILE      : " + discardPile.size() + " card(s)");
        System.out.println(LIGHT_LINE);
    }

    // prints the cards of a stack one per line, top card first, so it reads like a stack
    private static void printCardList(String title, CardStack stack)
    {
        System.out.println(" " + title + " - " + stack.size() + " card(s)");
        if (stack.isEmpty())
        {
            System.out.println("    (empty)");
            return;
        }

        ArrayList<Card> cards = stack.toList();
        for (int i = 0; i < cards.size(); i++)
        {
            // only the first card (the top of the stack) gets the marker
            String marker = CARD_INDENT;
            if (i == 0)
            {
                marker = " TOP -> ";
            }
            System.out.println(marker + cards.get(i));
        }
    }

    // turns a list of cards into "Bathala, Mayari, Tala"
    private static String joinNames(ArrayList<Card> cards)
    {
        if (cards.isEmpty())
        {
            return "(none)";
        }

        String names = "";
        for (int i = 0; i < cards.size(); i++)
        {
            names += cards.get(i);
            if (i < cards.size() - 1)
            {
                names += ", ";
            }
        }
        return names;
    }

    // adds spaces in front of the text so it sits in the middle of the line
    private static String centered(String text)
    {
        int padding = (WIDTH - text.length()) / 2;
        return " ".repeat(padding) + text;
    }
}

// A single card. Only the name matters, and names can be duplicated
class Card
{
    private final String name;

    public Card(String name)
    {
        this.name = name;
    }

    // used when we print the card
    @Override
    public String toString()
    {
        return name;
    }
}

// Array-backed stack of cards (LIFO): the last card pushed is the first card popped
class CardStack
{
    private Card[] stack;
    private int top; // index of the next free slot, also the number of cards

    public CardStack(int capacity)
    {
        stack = new Card[capacity];
    }

    // adds a card on the top of the stack
    public void push(Card card)
    {
        // resize the array if it is full
        if (top == stack.length)
        {
            Card[] newStack = new Card[stack.length * 2];
            // copy the old stack to the new one
            System.arraycopy(stack, 0, newStack, 0, stack.length);
            stack = newStack;
        }
        stack[top++] = card;
    }

    // removes and returns the top card
    public Card pop()
    {
        // check if stack is empty
        if (isEmpty())
        {
            throw new EmptyStackException();
        }
        Card poppedCard = stack[--top];
        stack[top] = null; // delete the value from the array
        return poppedCard;
    }

    // returns the top card without removing it
    public Card peek()
    {
        if (isEmpty())
        {
            throw new EmptyStackException();
        }
        return stack[top - 1];
    }

    public boolean isEmpty()
    {
        // if top is at 0, there is nothing in the stack
        return top == 0;
    }

    public int size()
    {
        return top;
    }

    // returns a copy of the cards, top card first (the stack itself is not changed)
    public ArrayList<Card> toList()
    {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = top - 1; i >= 0; i--) // we want the top first
        {
            cards.add(stack[i]);
        }
        return cards;
    }
}