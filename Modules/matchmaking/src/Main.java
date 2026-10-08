import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

// Matchmaking program.
// Every turn, 1 to 7 players join the queue. When 5 or more players are waiting,
// a game starts and the first 5 players are dequeued.
// The program ends when 10 games have been made.
public class Main
{
    private static final int MIN_JOIN = 1;
    private static final int MAX_JOIN = 7;
    private static final int PLAYERS_PER_GAME = 5;
    private static final int GAMES_TO_MAKE = 10;

    // the names that players can randomly get (duplicates are possible)
    private static final String[] NAMES = {
            "Kely", "Risse", "Julian", "Sciezcka", "Taylor Swift",
            "Lany", "Lauv", "Franz", "Boolean", "Julie Ann",
            "Clarisse", "Anne", "Ellie", "Cara", "Iana",
            "Yani", "Ed Sheeran", "Abet", "Craig", "Joy"
    };

    private static final int WIDTH = 60;
    private static final String HEAVY_LINE = "=".repeat(WIDTH);
    private static final String LIGHT_LINE = "-".repeat(WIDTH);

    public static void main(String[] args)
    {
        ArrayQueue queue = new ArrayQueue(10);
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        int turn = 0;
        int gamesMade = 0;

        System.out.println(HEAVY_LINE);
        System.out.println(centered("MATCHMAKING"));
        System.out.println(centered("Ends after " + GAMES_TO_MAKE + " games"));
        System.out.println(HEAVY_LINE);

        while (gamesMade < GAMES_TO_MAKE)
        {
            turn++;
            System.out.println();
            System.out.println(HEAVY_LINE);
            System.out.println(centered("TURN " + turn));
            System.out.println(HEAVY_LINE);

            // x players join the queue (x is random from 1 to 7)
            int joining = random.nextInt(MAX_JOIN - MIN_JOIN + 1) + MIN_JOIN;
            ArrayList<Player> joined = new ArrayList<>();
            for (int i = 0; i < joining; i++)
            {
                // each player gets a random name from the list
                Player player = new Player(NAMES[random.nextInt(NAMES.length)]);
                queue.enqueue(player);
                joined.add(player);
            }
            System.out.println(" JOINED QUEUE (" + joining + "): " + joinNames(joined));

            // start a game if there are enough players
            if (queue.size() >= PLAYERS_PER_GAME)
            {
                ArrayList<Player> matched = new ArrayList<>();
                for (int i = 0; i < PLAYERS_PER_GAME; i++)
                {
                    matched.add(queue.dequeue());
                }
                gamesMade++;
                System.out.println(" GAME STARTED!");
                System.out.println(" MATCHED PLAYERS : " + joinNames(matched));
            }
            else
            {
                System.out.println(" Not enough players to start a game ("
                        + queue.size() + "/" + PLAYERS_PER_GAME + ")");
            }

            // show the current state
            System.out.println(LIGHT_LINE);
            System.out.println(" PLAYERS IN QUEUE : " + queue.size());
            System.out.println(" QUEUE (front first) : " + joinNames(queue.toList()));
            System.out.println(" GAMES MADE       : " + gamesMade + "/" + GAMES_TO_MAKE);
            System.out.println(LIGHT_LINE);

            // pressing Enter ends the turn
            if (gamesMade < GAMES_TO_MAKE)
            {
                System.out.print(" Press Enter to end the turn...");
                scanner.nextLine();
            }
        }

        System.out.println();
        System.out.println(HEAVY_LINE);
        System.out.println(centered("DONE"));
        System.out.println(centered(GAMES_TO_MAKE + " games made in " + turn + " turns"));
        System.out.println(HEAVY_LINE);
        scanner.close();
    }

    // turns a list of players into "Kely, Risse, Lauv"
    private static String joinNames(ArrayList<Player> players)
    {
        if (players.isEmpty())
        {
            return "(empty)";
        }

        String names = "";
        for (int i = 0; i < players.size(); i++)
        {
            names += players.get(i);
            if (i < players.size() - 1)
            {
                names += ", ";
            }
        }
        return names;
    }

    private static String centered(String text)
    {
        int padding = (WIDTH - text.length()) / 2;
        return " ".repeat(padding) + text;
    }
}

// A player waiting for a match
class Player
{
    private final String name;

    public Player(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}

// Array-backed queue (FIFO): the first player enqueued is the first player dequeued.
// It is circular, so the space freed by dequeue gets reused.
class ArrayQueue
{
    private Player[] queue;
    private int front; // index of the first player
    private int back;  // index of the next free slot
    private int size;  // number of players in the queue

    public ArrayQueue(int capacity)
    {
        queue = new Player[capacity];
    }

    // adds a player at the back of the queue
    public void enqueue(Player player)
    {
        // resize the array if it is full
        if (size == queue.length)
        {
            Player[] newQueue = new Player[queue.length * 2];
            // copy the players in order, starting from the front
            for (int i = 0; i < size; i++)
            {
                newQueue[i] = queue[(front + i) % queue.length];
            }
            queue = newQueue;
            front = 0;
            back = size;
        }

        queue[back] = player;
        back = (back + 1) % queue.length;
        size++;
    }

    // removes and returns the player at the front
    public Player dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty");
        }

        Player player = queue[front];
        queue[front] = null;
        front = (front + 1) % queue.length;
        size--;
        return player;
    }

    // returns the front player without removing them
    public Player peek()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty");
        }
        return queue[front];
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public int size()
    {
        return size;
    }

    // returns a copy of the players, front first (the queue itself is not changed)
    public ArrayList<Player> toList()
    {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            players.add(queue[(front + i) % queue.length]);
        }
        return players;
    }
}