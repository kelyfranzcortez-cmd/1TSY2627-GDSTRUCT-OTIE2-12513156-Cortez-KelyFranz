class Main
{
  public static void main(String[] args)
  {
    PlayerLinkedList players = new PlayerLinkedList();

    players.addToFront(new Player(1, "Taylor Swift", 5));
    players.addToFront(new Player(2, "Drake", 10));
    players.addToFront(new Player(3, "Lany", 80));
    players.addToFront(new Player(50, "Sir Abet", 999));

    System.out.println("Current List size: " + players.getSize());
    players.print();

    System.out.println("\n");

    // Remove first element
    players.removeFirst();

    System.out.println("List Size after Removal: " + players.getSize());
    players.print();

    System.out.println("\n");

    // Contains
    System.out.println("Contains 'Lany': " + players.contains("Lany"));

    // IndexOf
    System.out.println("Index of 'Drake': " + players.indexOf("Drake"));
  }
}


class Player
{
  int ID;
  String name;
  int level;

  public Player(int pID, String pName, int pLevel)
  {
    ID = pID;
    name = pName;
    level = pLevel;
  }

  public String toString()
  {
    return String.valueOf(ID) + " : " + name + ", lvl " + String.valueOf(level);
  }
}


class PlayerNode
{
  Player player;
  PlayerNode nextPlayer;

  public PlayerNode(Player pPlayer)
  {
    player = pPlayer;
  }

  public Player getPlayer()
  {
    return player;
  }

  public void setNextPlayer(PlayerNode pNextPlayer)
  {
    nextPlayer = pNextPlayer;
  }

  public PlayerNode getNextPlayer()
  {
    return nextPlayer;
  }

  public String toString()
  {
    return player.toString();
  }
}


class PlayerLinkedList
{
  PlayerNode head;
  private int size;

  public PlayerLinkedList()
  {
    this.head = null;
    this.size = 0;
  }

  // Add a player to the front
  public void addToFront(Player players)
  {
    PlayerNode playerNode = new PlayerNode(players);
    playerNode.setNextPlayer(head);
    head = playerNode;

    size++;
  }

  // Remove the first element
  public PlayerNode removeFirst()
  {
    if (head == null)
    {
      return null;
    }

    PlayerNode removedNode = head;
    head = head.getNextPlayer();
    removedNode.setNextPlayer(null);

    size--;

    return removedNode;
  }

  // Get the number of elements
  public int getSize()
  {
    return size;
  }

  // Print the linked list
  public void print()
  {
    PlayerNode current = head;

    System.out.print("Head->");

    while (current != null)
    {
      System.out.print(current);
      System.out.print("->");

      current = current.getNextPlayer();
    }

    System.out.print("null");
  }

  // Check if a player exists
  public boolean contains(String name)
  {
    PlayerNode current = head;

    while (current != null)
    {
      if (current.getPlayer().name.equals(name))
      {
        return true;
      }

      current = current.getNextPlayer();
    }

    return false;
  }

  // Find the index of a player
  public int indexOf(String name)
  {
    PlayerNode current = head;
    int index = 0;

    while (current != null)
    {
      if (current.getPlayer().name.equals(name))
      {
        return index;
      }

      current = current.getNextPlayer();
      index++;
    }

    return -1;
  }
}