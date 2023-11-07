import java.util.*;

/**
 * This class is part of the "University escape" application. 
 * "University Escape" is a very simple, text based adventure game.
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game. It also evaluates and
 * executes the commands that the parser returns.
 * 
 * @author Michael Kölling and David J. Barnes
 * @editor Ethan Jones
 * @version 2016.02.29
 */

public class Game {
    private Parser parser;
    private Room currentRoom;
    private Room pastRoom;
    
    Item dungeonKey, closetKey1, closetKey2, gateKey, note1, note2, note3, note4, note5, allNotes;
    private boolean unlockedCloset1, unlockedCloset2, unlockedDungeon, unlockedGate, finished, anyInInventory;
    private int moves, shoutCount;
    public ArrayList<Room> rooms = new ArrayList<Room>();
    private String difficulty;
    Room front, physics, subway, lab, office, compsci, math, dungeon, closet1, closet2,
            gate, union1, union2, east1, east2, west1, west2, sC, library, teleport;
    private Map gameMap = new Map();

    /**
     * Allows for game to be downloaded and played without needing the source code.
     */
    public static void main(String[] args) 
    {
        Game game = new Game();
        game.play();
    }

    /**
     * Create the game and initialise its internal map.
     * 
     * Pre condition: none
     * 
     * Post condition: Rooms and items are initialized, and all booleans to lock doors are set to false.
     */
    public Game() 
    {
        createRooms();
        createItems();
        parser = new Parser();

        unlockedCloset1 = false;
        unlockedCloset2 = false;
        unlockedDungeon = false;
        unlockedGate = false;
        moves = 0;
        shoutCount = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     * 
     * Pre condition: Game constructor must be called.
     * 
     * Post condition: All rooms are created and connected in some way. 
     * The player starts in the front room, every room is given a description
     * which is printed when the player enters the room which the description
     * belongs to. Every room except the teleporter and gate will have exits.
     */
    private void createRooms() 
    {

        // create the rooms
        front = new Room("inside the front gate, right outside of the student union, you are locked in");
        physics = new Room("in my physics lecture hall");
        subway = new Room("in the campus Subway");
        union1 = new Room("in the top half of the Student Union");
        union2 = new Room("in the bottom half of the Student Union");
        lab = new Room("in the physics lab. There seems to be a teleporter in here");
        office = new Room("in the academic dean's office");
        math = new Room("in a Calculus classroom");
        compsci = new Room("in a computer science classroom");
        gate = new Room("Freedom.");
        dungeon = new Room("inside the dungeon under the school Subway. There is a cell and a cell door with a slot with a sign above it that states: \"deposit the notes here\"");
        closet1 = new Room("in the closet in the student union");
        closet2 = new Room("in the closet of the campus Subway");
        library = new Room("in the student library");
        west1 = new Room("in the top half of the west courtyard");
        west2 = new Room("in the bottom half of the west courtyard");
        east1 = new Room("in the top half of the east courtyard");
        east2 = new Room("in the bottom half of the east courtyard");
        sC = new Room("in the south courtyard");
        teleport = new Room("teleporter in the physics lab");

        rooms.add(0, front);
        rooms.add(1, union1);
        rooms.add(2, union2);
        rooms.add(3, lab);
        rooms.add(4, subway);
        rooms.add(5, office);
        rooms.add(6, west1);
        rooms.add(7, west2);
        rooms.add(8, east1);
        rooms.add(9, east2);
        rooms.add(10, sC);
        rooms.add(11, physics);
        rooms.add(12, front);
        rooms.add(13, math);
        rooms.add(14, library);

        // initialise room exits
        front.setExit("north", gate);
        front.setExit("south", union1);
        front.setExit("east", east1);
        front.setExit("west", west1);
        
        union1.setExit("north", front);
        union1.setExit("south", union2);
        union1.setExit("east", east1);
        union1.setExit("west", west1);

        east1.setExit("northwest", front);
        east1.setExit("southwest", union1);
        east1.setExit("south", east2);
        east1.setExit("east", subway);


        west1.setExit("northeast", front);
        west1.setExit("southeast", union1);
        west1.setExit("south", west2);
        west1.setExit("west", library);

        library.setExit("east", west1);

        east2.setExit("north", east1);
        east2.setExit("south", office);
        east2.setExit("east", compsci);
        east2.setExit("west", union2);
        
        west2.setExit("north", west1);
        west2.setExit("south", math);
        west2.setExit("east", union2);
        west2.setExit("west", physics);

        union2.setExit("north", union1);
        union2.setExit("south", sC);
        union2.setExit("east", east2);
        union2.setExit("west", west2);
        union2.setExit("forward", closet1);

        closet1.setExit("out", union2);

        sC.setExit("north", union2);
        sC.setExit("east", office);
        sC.setExit("west", math);

        math.setExit("north", west2);
        math.setExit("east", sC);

        physics.setExit("south", lab);
        physics.setExit("east", west2);
        
        lab.setExit("north", physics);
        lab.setExit("in", teleport);

        subway.setExit("west", east1);
        subway.setExit("forward", closet2);

        closet2.setExit("down", dungeon);
        closet2.setExit("out", subway);
        
        // there are no exits from the telepoerter since it automatically teleports the player
        teleport.setExit("none", teleport);

        compsci.setExit("west", east2);

        office.setExit("north", east2);
        office.setExit("west", sC);

        dungeon.setExit("up", closet2);
        
        // you have escaped when you reach the gate, thus there are no exits
        gate.setExit("none", gate);
        
        currentRoom = front; // start game outside
    }

    /**
     * Creates all items in the game.
     * 
     * Pre condition: Game constructor must be called.
     * 
     * Post condition: declares and initializes the 10 objects in the game. 
     */
    private void createItems() 
    {
        dungeonKey = new Item();
        gateKey = new Item();
        closetKey1 = new Item();
        closetKey2 = new Item();
        note1 = new Item();
        note2 = new Item();
        note3 = new Item();
        note4 = new Item();
        note5 = new Item();
        allNotes = new Item();
    }

    /**
     * Main play routine. Loops until end of play.
     * 
     * Pre condition: The Game constructor must be called
     * 
     * Post condition: The game willl continue to run until wantToQuit 
     * is equal to true. The game is finished one of two ways, either shouting
     * five times and losing in the only way possible or making 
     * it to the gate with the gate key to win.
     */
    public void play() 
    {
        printFirstWelcome();

        boolean howDifficult = false;
        while (!howDifficult) {
            howDifficult = difficulty();
        }
        printSecondWelcome();
        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.

        finished = false;
        while (!finished) 
        {
            Command command = parser.getCommand();
            boolean won = processCommand(command);
            if(!won && !finished)
            {
                finished = won;
            }
            else
            {
                break;
            }
        }
        System.out.println("Thank you for playing. Goodbye.");
    }

    /**
     * Print out the opening message for the player.
     * 
     * Pre condition: the method play() has been called.
     * 
     * Post condition: prints the first part of the introduction to the game.
     */
    private void printFirstWelcome() 
    {
        System.out.println();
        System.out.println("Welcome to University Escape");
        System.out.println("University Escape is a new adventure game that tests your searching skills");
        System.out.println("Before the game can begin, please follow the directions below: \n \n");
    }

    /**
     * Prints out the difficulty selection statement
     * 
     * Pre condition: the play() method must be called
     * 
     * Post condition: asigns the difficulty chosen to the difficulty string,
     * which determines how many notes are required for completion of the game.
     */
    private boolean difficulty() 
    {
        difficulty = "";

        Scanner scanner1 = new Scanner(System.in);
        System.out.println("The difficulty you choose determines how many notes you need to collect");
        System.out.println("You can choose: hard (5 notes), medium (4 notes), easy (3 notes)");
        System.out.println("Choose by typing: hard, medium, or easy.");
        System.out.println("These do not need to be typed exactly as stated, it can include different capitalization");
        System.out.println("Please choose your difficulty and type as presented above.");

        difficulty = scanner1.nextLine();

        while (!(difficulty.equalsIgnoreCase("hard") || difficulty.equalsIgnoreCase("medium") || difficulty.equalsIgnoreCase("easy"))) 
        {
            System.out.println("Make sure you have typed in a proper difficulty selection!");
            System.out.println("The difficulties are: easy, meduim, and hard.");
            System.out.println("Please select your difficulty.");

            difficulty = scanner1.nextLine();
        }

        if (difficulty.equalsIgnoreCase("hard"))
        {
            difficulty = "hard";
            return true;
        }

        else if (difficulty.equalsIgnoreCase("medium")) 
        {
            difficulty = "medium";
            return true;
        }

        else if (difficulty.equalsIgnoreCase("easy"))
        {
            difficulty = "easy";
            return true;
        } 
        else 
        {
            System.out.println("Please select a difficulty!");
            scanner1.nextLine();
            return false;
        }
    }

    /**
     * print out the message after difficulty selection.
     * 
     * Pre condition: the method play() has been called.
     * 
     * Post condition: prints the final part of the introduction to the game.
     */
    private void printSecondWelcome() 
    {
        System.out.println();
        System.out.println("You thought you had school at the university today.");
        System.out.println("Though, you thought it was a little weird you had to climb over the wall.");
        System.out.println("You realize today is Saturday, and you are not able to climb back over the wall.");
        System.out.println("You decide to wander around the university to find the gate key and escape.");
        System.out.println("You might run into some people who can help you.");
        System.out.println("Type \"help\" if you need help");
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * Pre Condition: the method play() has been called and the user
     * types in the command words with proper case and spelling.
     * If the command word isn't on the list, a message will be printed telling
     * the user that the command doesn't exist
     * 
     * Post Condition: The different commands are run if the user used them.
     * Some are carried out using methods inside the listed command,
     * others are carried out using helper methods.
     * 
     * @param command The command to be processed.
     * 
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if (command.isUnknown()) 
        {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) 
        {
            printHelp();
        } 
        else if (commandWord.equals("go"))
        {
            goRoom(command);
        } 
        else if (commandWord.equals("back")) 
        {
            // goes back one room from your current room
            if (pastRoom == null) 
            {
                    System.out.println("You just entered the university. You cannot go back");
                    System.out.println("To move type \"go\" and any directional word to move");
            }
            else 
            {
                Room temp = currentRoom;
                currentRoom =  pastRoom;
                pastRoom = temp;
                System.out.println(currentRoom.getLongDescription());
                moves += 1;
            }
        }
        else if (commandWord.equals("quit"))
        {
            wantToQuit = quit(command);
        } 
        else if (commandWord.equals("speak")) 
        {
            speak();
        } 
        else if (commandWord.equals("inventory")) 
        {
            inventory();
        } 
        else if (commandWord.equals("search")) 
        {
            if (command.hasSecondWord()) 
            {
                System.out.println("This command only needs one word. Type \"search\" to search the room.");
            }
            else
            {
                search();
            }
        } 
        else if (commandWord.equals("map")) 
        {
            gameMap.map();
        } 
        else if (commandWord.equals("shout")) 
        {
                //A gag command that not many people will find
                //The ONLY way to lose the game
                shoutCount++;
                if(shoutCount == 1)
                {
                    System.out.println("You shout as loud as you can but nobody can hear you");
                }
                if(shoutCount == 2)
                {
                    System.out.println("You shout again, and again nobody hears you");
                }
                if(shoutCount == 3)
                {
                    System.out.println("You shout yet again, and again NOBODY hears you. You are ALONE on campus");
                }
                if(shoutCount == 4)
                {
                    System.out.println("Again, you shout. I don't know why you keep using this command. One more time and you lose the game, you are literally alone on campus.");
                }
                if(shoutCount == 5)
                {
                    System.out.println("I warned you. You lose.");
                    System.out.println("You lost on difficulty " + difficulty + " in " + moves + " moves.");
                    System.out.println("You lost because you didn't heed the creator's warning.");
                    wantToQuit = true;
                }
        } 
        else if (commandWord.equals("use"))
        {
            use(command.getSecondWord());
        }
        else if (commandWord.equals("cheat")) // cheats the system
        {
            gateKey.setInInventory();
            note1.setInInventory();
            note2.setInInventory();
            note3.setInInventory();
            if(difficulty.equals("hard") || difficulty.equals("medium"))
            {
                note4.setInInventory();
            }
            if(difficulty.equals("hard"))
            {
                note5.setInInventory();
            }
            allNotes.setInInventory();
            dungeonKey.setInInventory();
            closetKey1.setInInventory();
            closetKey2.setInInventory();
            System.out.println("You have cheated to get all items, congrats on winning. You cheater.");
        }
        
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     * 
     * Pre condition: the play() method is called and the command word is typed in
     * properly in casing and spelling
     * 
     * Post condition: Prints a cryptic message and shows the command words
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * Pre condition: The play() method is called and the command word go is typed
     * followed by the direction that the player wishes to go in
     * 
     * Post condition: If there is no second word, a message is printed. There are checks for
     * locked doors so the player cannot access rooms they have not unlocked yet. There is a moves counter
     * that is shown at the end of the game that ticks up with each move the player makes.
     */
    private void goRoom(Command command) 
    {
        if (!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            System.out.println(currentRoom.getLongDescription());
        }

        String direction = command.getSecondWord();

        
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        if (moves == 1) 
        {
            System.out.println("You start to wonder what would happen if you searched the rooms");
            System.out.println("you have heard rumors about what happens when students stay at the university over the weekend and you want to escape");
            moves++;
        }
        if (nextRoom == null) 
        {
            System.out.println("There is no door!");
            System.out.println(currentRoom.getLongDescription());
        } 
        else if (!checkRoom(nextRoom)) 
        {
            System.out.println("This room is locked, get a key to unlock it!");
            System.out.println(currentRoom.getLongDescription());
        } 
        else if(nextRoom == teleport)
        {
            teleport();
        }
        else if(nextRoom == gate && unlockedGate)
        {
            youWin();
        }
        else 
        {
            pastRoom = currentRoom;
            currentRoom = nextRoom;
            moves++;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * Checks to see if the room is locked or unlocked
     * 
     * Pre Condition: the play() method is called and the command word is typed properly
     * 
     * Post condition: determines if the room the player is trying to enter is locked or unlocked.
     * This is done using if/else statements that return true or false based on the state of the rooms.
     * 
     * @return true or false depending on the status of the room(locked or unlocked)
     */
    private boolean checkRoom(Room room) 
    {
        ArrayList<Room> locked = new ArrayList<Room>();
        locked.add(dungeon);
        locked.add(closet1);
        locked.add(closet2);
        locked.add(gate);

        if (!locked.contains(room)) 
        {
            return true;
        } 
        else if (room == gate && unlockedGate) 
        {
            return true;
        } 
        else if (room == dungeon && unlockedDungeon) 
        {
            return true;
        } 
        else if (room == closet1 && unlockedCloset1) 
        {
            return true;
        } 
        else if (room == closet2 && unlockedCloset2)
        {
            return true;
        } 
        else 
        {
            return false;
        }
    }

    /**
     * Outputs items that are currently in the inventory
     * 
     * Pre condition: the play() method is called and the command word is typed 
     * with proper casing and spelling
     * 
     * Post condition: outputs the items that are currently in the inventory. 
     * Will not output the item "allNotes" because it is not needed.
     */
    private void inventory() 
    {
             if(!(note1.getInInventory() && note2.getInInventory() && note3.getInInventory() && dungeonKey.getInInventory() && closetKey1.getInInventory() && closetKey2.getInInventory() && allNotes.getInInventory() && gateKey.getInInventory()))
            {
                if(difficulty.equals("easy"))
                {
                    System.out.println("You have nothing in your inventory");
                }
                else if(difficulty.equals("medium"))
                {
                    if(!note4.getInInventory())
                    {
                        System.out.println("You have nothing in your inventory");
                    }
                }
                else if(difficulty.equals("hard"))
                {
                    if(!note5.getInInventory() || !note4.getInInventory())
                    {
                        System.out.println("You have nothing in your inventory");
                    }
                }
            }    
            if (closetKey1.getInInventory() == true) 
            {
                System.out.println("You have the Student Union Closet Key (closetKey1)");
            }
            if (closetKey2.getInInventory() == true) 
            {
                System.out.println("You have the Subway closet key (closetKey2)");
            }
            if (dungeonKey.getInInventory() == true) 
            {
                System.out.println("You have the the dungeon key");
            }
            if (gateKey.getInInventory() == true) 
            {
                System.out.println("You have the gate key");
            }
            if (note1.getInInventory() == true) 
            {
                System.out.println("You have the first note");
            }
            if (note2.getInInventory() == true) 
            {
                System.out.println("You have the second note");
            }
            if (note3.getInInventory() == true) 
            {
                System.out.println("You have the third note");
            }
            if (note4.getInInventory() == true && (difficulty.equalsIgnoreCase("medium") || difficulty.equalsIgnoreCase("hard"))) 
            {
                System.out.println("You have the fourth note");
            }
            if (note5.getInInventory() == true && (difficulty.equalsIgnoreCase("hard"))) 
            {
                System.out.println("You have the fifth note");
            }
            
    }
    

    /**
     * searches the room for any objects that are currently in the room
     * 
     * Pre Condition: The play() method must be caled and the command word is typed
     * with proper casing and spelling
     * 
     * Post condition: if in the rooms: math, union1, closet1, office, physics,
     *library,  or compsci, it will put the item in your inventory. If the item is already
     * in your inventory or has been used, it will return a message saying that
     * the room has already been searched. 
     */
    private void search() 
    {
            if (currentRoom == math)
            {
                if(closetKey1.getInInventory() || closetKey1.getRemoveFromInventory())
                {
                    System.out.println("This room has already been searched");
                }
                else if(!(closetKey1.getInInventory())) 
                {
                    closetKey1.setInInventory();
                    System.out.println("You now have the key to the student union closet. (closetKey1)");
                }
            } 
            
            else if (currentRoom == union1) 
            {
                if(note1.getInInventory() || note1.getRemoveFromInventory())
                {
                    System.out.println("This room has already been searched");
                }
                else if(!note1.getInInventory())
                {
                    note1.setInInventory();
                     if (getHasAllNotes()) 
                    {
                        System.out.println( "You have found the final note! (which, ironically, is the first note you were meant to find)");
                        allNotes.setInInventory();
                    } 
                    else if (note4.getInInventory() || note2.getInInventory() || note3.getInInventory()|| note5.getInInventory() && !getHasAllNotes()) 
                    {
                        System.out.println("You have found another note! (which, ironically, is the first note you were meant to find)");
                    } 
                    else if (!(note2.getInInventory() || note3.getInInventory() || note4.getInInventory() || note5.getInInventory())) 
                    {
                        System.out.println("You have found a note!");
                    }
                    
                    if (difficulty.equals("easy")) 
                    {
                        System.out.println("It says: \"This is the first note, you have two more notes to find so make sure you search rooms as often as possible!\"");
                        System.out.println("Written at the bottom of the note it says: \"There are many dark secrets about this university\"");
                    }
                    else if (difficulty.equals("medium")) 
                    {
                        System.out.println("It says: \"This is the first note, you have three more notes to find so make sure you search rooms as often as possible!\"");
                        System.out.println("Written at the bottom of the note it says: \"There are many dark secrets about this university\"");
                    }
                    else if (difficulty.equals("hard")) 
                    {
                        System.out.println("It says: \"This is the first note, you have four more notes to find so make sure you search rooms as often as possible!\"");
                        System.out.println("Written at the bottom of the note it says: \"There are many dark secrets about this university\"");
                    }
                }              
            } 
                else if (currentRoom == library) 
                {
                    if(note2.getInInventory() || note2.getRemoveFromInventory())
                    {
                        System.out.println("This room has already been searched.");
                    }
                    else if(!note2.getInInventory())
                    {
                        note2.setInInventory();
                        if (getHasAllNotes()) 
                        {
                            System.out.println("You found all the notes!");
                            allNotes.setInInventory();
                        } 
                        else if (note1.getInInventory() || note5.getInInventory() || note3.getInInventory() || note5.getInInventory() && !getHasAllNotes()) 
                        {
                            System.out.println("You found another note!");
                        } 
                        else if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory())) 
                        {
                            System.out.println("You found a note!");
                        }
                        System.out.println("This note says: \"If anyone finds this second note, you must escape!\"");
                        System.out.println("Scrawled at the bottom it says: \"You need to make it to the dungeon to do so.\"");
                }
            } 
            else if (currentRoom == closet1) 
            {
                if(note3.getInInventory() || note3.getRemoveFromInventory())
                {
                    System.out.println("This room has already been searched");
                }
                else if(!(note3.getInInventory() && closetKey2.getInInventory()) )
                {
                    note3.setInInventory();
                    closetKey2.setInInventory();
                    System.out.println("You have found the subway closet key! (closetKey2)");
                    if (difficulty.equals("easy") && !(dungeonKey.getInInventory())) 
                    {
                        if (getHasAllNotes())
                        {
                            System.out.println("You also have found the final note!");
                            System.out.println(
                                    "This note says \"This is my final note, you have proved you are good at searching. Go to the dungeon.\"");
                            System.out.println("You realize you still have to find the dungeon key!");
                            allNotes.setInInventory();
                        } 
                        else if (note1.getInInventory() || note2.getInInventory() || note5.getInInventory() || note5.getInInventory() && !getHasAllNotes()) 
                        {
                            System.out.println("You also found another note!");
                            System.out.println("This note says \"This is my final note, you have proved you are good at searching. Go to the dungeon.\"");
                            System.out.println("You realize you still have to find the other notes and the dungeon key!");
                        } 
                        else if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory())) 
                        {
                            System.out.println("You also found a note!");
                            System.out.println("This note says \"This is my final note, you have proved you are good at searching. Go to the dungeon.\"");
                            System.out.println("You realize you still have to find the other notes and the dungeon key!");
                        }
                    } 
                    else if (difficulty.equals("easy") && dungeonKey.getInInventory()) 
                    {
                        if (getHasAllNotes()) 
                        {
                            System.out.println("You also found the final note!");
                            System.out.println("This note says \"This is my final note, you have proved you are good at searching. Go to the dungeon.\"");
                            System.out.println("You realize you have the dungeon key, make your way to the dungeon!");
                            allNotes.setInInventory();
                        } 
                        else if (note1.getInInventory() || note2.getInInventory() || note5.getInInventory() || note5.getInInventory() && !getHasAllNotes()) 
                        {
                            System.out.println("You also found another note!");
                            System.out.println("This note says \"This is my final note, you have proved you are good at searching. Go to the dungeon.\"");
                            System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                        }
                         else if (!(note1.getInInventory() || note2.getInInventory() || note5.getInInventory() || note4.getInInventory())) 
                        {
                            System.out.println("You also found a note!");
                            System.out.println("This note says \"This is my final note, you have proved you are good at searching. Go to the dungeon.\"");
                            System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                        }
                    }
                    
                    else if (difficulty.equals("medium") || difficulty.equals("hard") && dungeonKey.getInInventory()) 
                    {
                        if (getHasAllNotes()) 
                        {
                            System.out.println("You found the final note!");
                            System.out.println("This note says: \"I see you are really invested in searching now. You have found three of my notes.\"");
                            System.out.println("You realize you have the dungeon key, make your way to the dungeon!");
                            allNotes.setInInventory();
                        } 
                        else if (note1.getInInventory() || note2.getInInventory() || note5.getInInventory() || note5.getInInventory() && !getHasAllNotes()) 
                        {
                            System.out.println("You found another note!");
                            System.out.println("This note says: \"I see you are really invested in searching now. You have found three of my notes.\"");
                            System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                        } 
                        else if (!(note1.getInInventory() || note2.getInInventory() || note5.getInInventory() || note4.getInInventory())) 
                        {
                            System.out.println("You found a note!");
                            System.out.println("This note says: \"I see you are really invested in searching now. You have found three of my notes.\"");
                            System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                        }
                    } 
                    else if (difficulty.equals("medium") || difficulty.equals("hard") && !(dungeonKey.getInInventory())) 
                    {
                        if (getHasAllNotes()) 
                        {
                            System.out.println("You found the final note!");
                            System.out.println("This note says: \"I see you are really invested in searching now. You have found three of my notes.\"");
                            System.out.println("You realize you don't have the dungeon key, find it then make your way to the dungeon!");
                            allNotes.setInInventory();
                        } 
                        else if (note1.getInInventory() || note2.getInInventory() || note5.getInInventory()|| note5.getInInventory() && !getHasAllNotes()) 
                            {
                            System.out.println("You found another note!");
                            System.out.println("This note says: \"I see you are really invested in searching now. You have found three of my notes.\"");
                            System.out.println("You realize you don't have the dungeon key or the other notes, find the notes and dungeon key then make your way to the dungeon!");
                        } 
                        else if (!(note1.getInInventory() || note2.getInInventory() || note5.getInInventory() || note4.getInInventory())) 
                        {
                            System.out.println("You found a note!");
                            System.out.println("This note says: \"I see you are really invested in searching now. You have found three of my notes.\"");
                            System.out.println("You realize you don't have the dungeon key or the other notes, find the notes and dungeon key then make your way to the dungeon!");
                        }
    
                        if (difficulty.equals("medium")) 
                        {
                            System.out.println("At the bottom it says: \"you must find one more!\"");
                        }
                        
                        if (difficulty.equals("hard"))
                        {
                            System.out.println("At the bottom it says: \"you must find two more!\"");
                        }
                    }
                }
            } 
            else if (currentRoom == physics) 
                {
                if (difficulty.equals("hard") || difficulty.equals("medium")) 
                {
                    if(note4.getInInventory() || note4.getRemoveFromInventory())
                    {
                        System.out.println("This room has already been searched.");
                    }
                    else if(!(note4.getInInventory()))
                    {
                       if(difficulty.equals("medium") || difficulty.equals("hard"))
                        {
                            note4.setInInventory();
                            if (difficulty.equals("medium")) 
                            {
                                if (dungeonKey.getInInventory()) 
                                {
                                    if (getHasAllNotes()) 
                                    {
                                        System.out.println("You found the final note!");
                                        System.out.println("This note says \"This is my final note, you have proved you are really good at searching. Go to the dungeon.\"");
                                        System.out.println("You realize you have the dungeon key, make your way to the dungeon!");
                                        allNotes.setInInventory();
                                    } 
                                    else if (note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note5.getInInventory() && !getHasAllNotes()) 
                                    {
                                        System.out.println("You found another note!");
                                        System.out.println("This note says \"This is my final note, you have proved you are really good at searching. Go to the dungeon.\"");
                                        System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                                    } 
                                    else if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory()|| note5.getInInventory()))
                                    {
                                        System.out.println("You found a note!");
                                        System.out.println("This note says \"This is my final note, you have proved you are really good at searching. Go to the dungeon.\"");
                                        System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                                    }
                                }
                                
                                if (!(dungeonKey.getInInventory())) 
                                {
                                    if (getHasAllNotes()) 
                                    {
                                        System.out.println("You have found the final note!");
                                        System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                        System.out.println("You realize you still have to find the dungeon key!");
                                        allNotes.setInInventory();
                                    }    
                                    else if (note1.getInInventory() || note2.getInInventory() || note3.getInInventory()|| note5.getInInventory() && !getHasAllNotes()) 
                                    {
                                        System.out.println("You found another note!");
                                        System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                        System.out.println("You realize you stil have to find the other notes and the dungeon key!");
                                    } 
                                    else if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note5.getInInventory())) 
                                    {
                                        System.out.println("You found a note!");
                                        System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                        System.out.println("You realize you still have to find the other notes and the dungeon key!");
                                    }
                                 }
                            }
                            
                            else if (difficulty.equals("hard")) 
                            {   
                                if (dungeonKey.getInInventory()) 
                                {
                                    if (getHasAllNotes()) 
                                    {
                                        System.out.println("You found the final note!");
                                        System.out.println("This note says \"You are nearing the end, keep searching for that last note!.\"");
                                        System.out.println("You realize you have all the notes and the dungeon key, make your way to the dungeon!");
                                        allNotes.setInInventory();
                                        } 
                                    else if (note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note5.getInInventory() && !getHasAllNotes()) 
                                    {
                                        System.out.println("You found another note!");
                                        System.out.println("This note says \"You are nearing the end, keep searching for that last note!.\"");
                                        System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                                    } 
                                    else if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note5.getInInventory())) {
                                        System.out.println("You found a note!");
                                        System.out.println("This note says \"You are nearing the end, keep searching for that last note!.\"");
                                        System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                                    }
                                }
                            
                                if (!(dungeonKey.getInInventory())) 
                                {
                                    if (getHasAllNotes())
                                    {
                                        System.out.println("You also have found the final note!");
                                        System.out.println("This note says \"You are nearing the end, keep searching for that last note!.\"");
                                        System.out.println("You realize you have all the notes but still have to find the dungeon key!");
                                        allNotes.setInInventory();
                                    }   
                                    else if (note1.getInInventory() || note2.getInInventory() || note3.getInInventory()|| note5.getInInventory() && !getHasAllNotes()) 
                                    {
                                        System.out.println("You also found another note!");
                                        System.out.println("This note says \"You are nearing the end, keep searching for that last note!.\"");
                                        System.out.println("You realize you still have to find the other notes and the dungeon key!");
                                    } 
                                    else if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note5.getInInventory())) 
                                    {
                                        System.out.println("You also found a note!");
                                        System.out.println("This note says \"You are nearing the end, keep searching for that last note!.\"");
                                        System.out.println("You realize you still have to find the other notes and the dungeon key!");
                                    }
                                }
                            }
                        }
                     }
                }
                    else 
                    {
                        System.out.println("You found nothing in this room.");
                    }
                } 
                else if (currentRoom == compsci)
                    {
                    if (difficulty.equals("hard")) 
                    {
                        if(note5.getInInventory() || note5.getRemoveFromInventory())
                        {
                            System.out.println("This room has already been searched.");
                        }
                        else if(!(note5.getInInventory()))
                        {
                            note5.setInInventory();
                            if (dungeonKey.getInInventory()) 
                            {
                                if (getHasAllNotes()) 
                                {
                                    System.out.println("You also found the final note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you have all the notes and the dungeon key, make your way to the dungeon!");
                                    allNotes.setInInventory();
                                }
                            
                                if (note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory() && !getHasAllNotes())
                                {
                                    System.out.println("You also found another note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                                }
                            
                                if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory())) 
                                {
                                    System.out.println("You also found a note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                                }
                            }
                        
                            else if (!(dungeonKey.getInInventory())) 
                            {
                                if (getHasAllNotes()) 
                                {
                                    System.out.println("You also have found the final note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you have all the notes but still have to find the dungeon key!");
                                    allNotes.setInInventory();
                                } 
                                else if (note1.getInInventory() || note2.getInInventory() || note3.getInInventory()|| note4.getInInventory() && !getHasAllNotes()) 
                                {
                                    System.out.println("You also found another note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you still have to find the other notes and the dungeon key!");
                                } 
                                else if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory())) 
                                {
                                    System.out.println("You also found a note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you still have to find the other notes and the dungeon key!");
                                }
                            }
                        }
                    }
                    else 
                    {
                        System.out.println("You found nothing in this room.");
                    }
                }
                    else if (currentRoom == office) 
                    {
                        if(dungeonKey.getInInventory() || dungeonKey.getRemoveFromInventory())
                        {
                            System.out.println("this room has already been searched.");
                        }
                        else if(!dungeonKey.getInInventory() && !(dungeonKey.getRemoveFromInventory()))
                        {
                            dungeonKey.setInInventory();
                            System.out.println("You have found the key to the dungeon (dungeonKey) that the academic dean locks students in.");
                        }
                        
                        if (getHasAllNotes()) {
                            System.out.println("You have all the notes and the dungeon key, make your way to the dungeon!");
                        } else {
                            System.out.println("You now have the dungeon key but you are not done with the notes yet! Finish finding the notes.");
                        }
                        
                        if(note5.getInInventory() || note5.getRemoveFromInventory())
                        {
                            System.out.println("This room has already been searched.");
                        }
                        else if(!(note5.getInInventory()) && !(note5.getRemoveFromInventory()))
                        {
                            note5.setInInventory();
                            if (dungeonKey.getInInventory()) 
                            {
                                if (getHasAllNotes()) 
                                {
                                    System.out.println("You also found the final note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you have all the notes and the dungeon key, make your way to the dungeon!");
                                    allNotes.setInInventory();
                                }
                                if (note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory() && !getHasAllNotes())
                                {
                                    System.out.println("You also found another note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                                }
                                if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory())) 
                                {
                                    System.out.println("You also found a note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you have the dungeon key but not the other notes, find the notes then make your way to the dungeon!");
                                }
                            }
                            
                            else if (!(dungeonKey.getInInventory())) 
                            {
                                if (getHasAllNotes()) 
                                {
                                    System.out.println("You also have found the final note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you have all the notes but still have to find the dungeon key!");
                                    allNotes.setInInventory();
                                } 
                                else if (note1.getInInventory() || note2.getInInventory() || note3.getInInventory()|| note4.getInInventory() && !getHasAllNotes()) 
                                {
                                    System.out.println("You also found another note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you still have to find the other notes and the dungeon key!");
                                } 
                                else if (!(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory())) 
                                {
                                    System.out.println("You also found a note!");
                                    System.out.println("This note says \"This is my final note, you have really proved you are good at searching. Go to the dungeon.\"");
                                    System.out.println("You realize you still have to find the other notes and the dungeon key!");
                                }
                            }
                        }
                    } 
                    
                    else 
                    {
                        System.out.println("You found nothing in this room.");
                    }
    }
    

    /**
     * uses the object that the player wants to use
     * 
     * Pre condition: the play() method must be called and the command word must be typed 
     * with proper casing and spelling. However, the items used do not have to be 
     * in proper casing, but they have to have proper spelling.
     * 
     * Post condition: a message is outputted if the item is not recognized, if it is misspelled then
     * the player just needs to type it in again spelled correclty. If the player does not have the item
     * they are trying to use, it will also output a message telling them that they do not have that item.
     * 
     */
    public void use(String item) 
    {
        if (item != closetKey1 || item != closetKey2 || item != note1 || item != note2 || item != note3 || item != note4 || item != note5 || item != allNotes || item != dungeonKey || item != gateKey) 
        {
            System.out.println("Use what?");
            return;
        } 
        else if (item.equalsIgnoreCase("closetKey1")) 
        {
            if(currentRoom == union2)
            {
                if(!(closetKey1.getInInventory()) && !(closetKey1.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                else if(closetKey1.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item");
                    }
                else if(closetKey1.getInInventory())
                    {
                        unlockedCloset1 = true;
                        System.out.println("Unlocked the student union closet");
                        closetKey1.removeFromInventory();
                        currentRoom.getLongDescription();
                    }
            }
            else if (!(currentRoom == union2))
            {
                if(!(closetKey1.getInInventory()) && !(closetKey1.getRemoveFromInventory()))
                {
                    System.out.println("You do not have this item.");
                }
                else if(closetKey1.getRemoveFromInventory())
                {
                    System.out.println("You have already used this item");
                }
                else if (closetKey1.getInInventory())
                {
                    System.out.println("You are too far away to use this item.");
                }
            }
        } 
        else if (item.equalsIgnoreCase("closetKey2")) 
        {
            if(currentRoom == subway)
            {
                if(!(closetKey2.getInInventory()) && !(closetKey2.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(closetKey2.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item");
                    }
                else if(closetKey2.getInInventory())
                    {
                        unlockedCloset2 = true;
                        System.out.println("Unlocked the subway closet");
                        closetKey2.removeFromInventory();
                        currentRoom.getLongDescription();
                    }
            }
            else if(!(currentRoom == subway))
            {
                    if(!(closetKey2.getInInventory()) && !(closetKey2.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(closetKey2.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item.");
                    }
                    else if(closetKey2.getInInventory())
                    {
                        System.out.println("You are too far away to use this item.");
                    }
            }
        }
        
        else if (item.equalsIgnoreCase("dungeonKey"))
        {
            if(currentRoom == closet2)
            {
                if(!(dungeonKey.getInInventory()) && !(dungeonKey.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                else if(dungeonKey.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item");
                    }
                else if(dungeonKey.getInInventory())
                    {
                        unlockedDungeon = true;
                        System.out.println("Unlocked the dungeon");
                        dungeonKey.removeFromInventory();
                        currentRoom.getLongDescription();
                    }
            }
            else if(!(currentRoom == closet2))
            {
                if(!(dungeonKey.getInInventory()) && !(dungeonKey.getRemoveFromInventory()))
                    {
                            System.out.println("You do not have this item.");
                    }
                    else if(dungeonKey.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item");
                    }
                    else if(dungeonKey.getInInventory())
                    {
                        System.out.println("You are too far away to use this item.");
                    }
            }
        }
        if(item.equalsIgnoreCase("note1") || item.equalsIgnoreCase("note2") || item.equalsIgnoreCase("note3") || item.equalsIgnoreCase("note4") || item.equalsIgnoreCase("note5") || item.equalsIgnoreCase("allNotes"))
        {
            if(!getHasAllNotes())    
            {
            if (currentRoom == dungeon)
            {
                if (item.equalsIgnoreCase("note1"))
                {
                    if(!(note1.getInInventory()) && !(note1.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(note1.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item.");
                    }
                    else if(note1.getInInventory())
                    {
                        System.out.println("find all the notes before you start depositing any!");
                        System.out.println("type \"use allNotes\" to deposit all notes when you have all of them.");
                    }
                }
                else if (item.equalsIgnoreCase("note2"))
                {
                    if(!(note2.getInInventory()) && !(note2.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(note2.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item.");
                    }
                    else if(note2.getInInventory())
                    {
                        System.out.println("find all the notes before you start depositing any!");
                        System.out.println("type \"use allNotes\" to deposit all notes when you have all of them.");
                    }
                }

                else if (item.equalsIgnoreCase("note3"))
                {
                    if(!(note3.getInInventory()) && !(note3.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(note3.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item.");
                    }
                    else if(note3.getInInventory())
                    {
                        System.out.println("find all the notes before you start depositing any!");
                        System.out.println("type \"use allNotes\" to deposit all notes when you have all of them.");
                    }
                }
                
                else if(item.equalsIgnoreCase("note4"))
                {
                     if (difficulty.equals("easy"))
                        {
                            System.out.println("This item does not exist");
                        }
                     else if(!(note4.getInInventory()) && !(note4.getRemoveFromInventory()))
                        {
                            System.out.println("You do not have this item.");
                        }
                        else if(note4.getRemoveFromInventory())
                        {
                            System.out.println("You have already used this item.");
                        }
                        else if(note4.getInInventory())
                        {
                            System.out.println("find all the notes before you start depositing any!");
                            System.out.println("type \"use allNotes\" to deposit all notes when you have all of them.");
                        }
                    }
                       
                
                else if(item.equalsIgnoreCase("note5"))
                    {
                        if (difficulty.equals("hard"))
                        {
                            if(!(note5.getInInventory()) && !(note5.getRemoveFromInventory()))
                            {
                                System.out.println("You do not have this item.");
                            }
                            else if(note5.getRemoveFromInventory())
                            {
                                System.out.println("You have already used this item.");
                            }
                            else if(note5.getInInventory())
                            {
                                System.out.println("find all the notes before you start depositing any!");
                                System.out.println("type \"use allNotes\" to deposit all notes when you have all of them.");
                            }
                        }
                            else if (difficulty.equals("easy") || difficulty.equals("medium"))
                            {
                                System.out.println("This item does not exist");
                            }
                        }
                    
                    }
                
            
            else if (!(currentRoom == dungeon))
            {
                if (item.equalsIgnoreCase("note1"))
                {
                    if(!(note1.getInInventory()) && !(note1.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(note1.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item.");
                    }
                    else if(note1.getInInventory())
                    {
                        System.out.println("cannot use the notes in this location and you do not have all the notes");
                        System.out.println("(and when you go to use notes, once you have them all, use the \"use allNotes\" command instead)");
                    }
                }
                else if (item.equalsIgnoreCase("note2"))
                {
                    if(!(note2.getInInventory()) && !(note2.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(note2.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item.");
                    }
                    else if(note2.getInInventory())
                    {
                        System.out.println("cannot use the notes in this location and you do not have all the notes");
                        System.out.println("(and when you go to use notes, once you have them all, use the \"use allNotes\" command instead)");
                    }
                }

                else if (item.equalsIgnoreCase("note3"))
                {
                    if(!(note3.getInInventory()) && !(note3.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(note3.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item.");
                    }
                    else if(note3.getInInventory())
                    {
                        System.out.println("cannot use the notes in this location and you do not have all the notes");
                        System.out.println("(and when you go to use notes, once you have them all, use the \"use allNotes\" command instead)");
                }
                
                if(item.equalsIgnoreCase("note4"))
                {
                    if (difficulty.equals("medium") || difficulty.equals("hard"))
                    {
                        if(!(note4.getInInventory()) && !(note4.getRemoveFromInventory()))
                        {
                            System.out.println("You do not have this item.");
                        }
                        else if(note4.getRemoveFromInventory())
                        {
                            System.out.println("You have already used this item.");
                        }
                        else if(note4.getInInventory())
                        {
                            System.out.println("cannot use the notes in this location and you do not have all the notes");
                            System.out.println("(and when you go to use notes, once you have them all, use the \"use allNotes\" command instead)");
                        }
                    }
                    else if (difficulty.equals("easy"))
                        {
                            System.out.println("This item does not exist");
                        }
                    
                    if(item.equalsIgnoreCase("note5"))
                    {
                        if (difficulty.equals("hard"))
                        {
                            if(!(note5.getInInventory()) && !(note5.getRemoveFromInventory()))
                                {
                                    System.out.println("You do not have this item.");
                                }
                                else if(note5.getRemoveFromInventory())
                                {
                                    System.out.println("You have already used this item.");
                                }
                                else if(note5.getInInventory())
                                {
                                    System.out.println("cannot use the notes in this location and you do not have all the notes");
                                    System.out.println("(and when you go to use notes, once you have them all, use the \"use allNotes\" command instead)");
                                }
                        }
                        else if (difficulty.equals("easy") || difficulty.equals("medium"))
                        {
                            System.out.println("This item does not exist");
                        }
                    }
                }
            }
        }
        }
            
        else if (getHasAllNotes())
            {
                if(currentRoom == dungeon)
                {
                    if(item.equalsIgnoreCase("allnotes") && !allNotes.getInInventory() && !(allNotes.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have all the notes!");
                    }
                    else if(allNotes.getRemoveFromInventory())
                    {
                        System.out.println("You have already used all the notes!");
                    }
                    else if(item.equalsIgnoreCase("note1") || item.equalsIgnoreCase("note2") || item.equalsIgnoreCase("note3") && !(allNotes.getRemoveFromInventory()))
                    {
                        System.out.println("Type in \"use allNotes\" instead");
                    }
                    else if(item.equalsIgnoreCase("note4") && !(allNotes.getRemoveFromInventory()))
                        {
                            if(difficulty.equals("medium") || difficulty.equals("hard"))
                            {
                                System.out.println("Type in \"use allNotes\" instead");
                            }
                              else if (difficulty.equals("easy"))
                                {
                                    System.out.println("This item does not exist");
                                }
                        }
                        
                    else if(item.equalsIgnoreCase("note5") && !(allNotes.getRemoveFromInventory()))
                        {
                            if(difficulty.equals("hard"))
                            {
                                System.out.println("Type in \"use allNotes\" instead");
                            }
                            else if (difficulty.equals("easy") || difficulty.equals("medium"))
                                {
                                    System.out.println("This item does not exist");
                                }
                        }
                    
                    
                    else if(item.equalsIgnoreCase("note1") || item.equalsIgnoreCase("note2") || item.equalsIgnoreCase("note3") && (allNotes.getRemoveFromInventory()))
                    {
                        System.out.println("You already used all the notes.");
                    }
                    else if(item.equalsIgnoreCase("note4") && allNotes.getRemoveFromInventory())
                        {
                            if(difficulty.equals("medium") || difficulty.equals("hard"))
                            {
                                System.out.println("You already used all the notes.");
                            }
                            else if (difficulty.equals("easy"))
                            {
                                System.out.println("This item does not exist");
                            }
                        }
                    else if(item.equalsIgnoreCase("note5") && allNotes.getRemoveFromInventory())
                            {
                                if(difficulty.equals("hard"))
                                {
                                    System.out.println("You already used all the notes.");
                                }
                                 else if (difficulty.equals("easy") || difficulty.equals("medium"))
                                {
                                    System.out.println("This item does not exist");
                                }
                            }
                        
                    
                    else if(item.equalsIgnoreCase("allNotes") && allNotes.getInInventory()) 
                    {
                        note1.removeFromInventory();
                        note2.removeFromInventory();
                        note3.removeFromInventory();
                        note4.removeFromInventory();
                        note5.removeFromInventory();
                        allNotes.removeFromInventory();
                        System.out.println("You have deposited all the notes! A gate key has been given to you from the mysterious slot.");
                        gateKey.setInInventory();
                        System.out.println("you are still in the dungeon.");
                    }
                
                }            
                else if(!(currentRoom == dungeon))
                {
                    if(item.equalsIgnoreCase("allNotes") && !allNotes.getInInventory() && !(allNotes.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have all the notes and you cannot use notes in this location!");
                    }
                    else if(allNotes.getRemoveFromInventory())
                    {
                        System.out.println("You have already used all the notes!");
                    }
                    else if(item.equalsIgnoreCase("note1") || item.equalsIgnoreCase("note2") || item.equalsIgnoreCase("note3") && !(allNotes.getRemoveFromInventory()))
                    {
                        System.out.println("You are too far away to use notes");
                        System.out.println("(and use \"use allNotes\" when you are in the right location)");
                        if(item.equalsIgnoreCase("note4") && !(allNotes.getRemoveFromInventory()))
                        {
                            if(difficulty.equals("medium") || difficulty.equals("hard"))
                            {
                                System.out.println("You are too far away to use notes");
                                System.out.println("(and use \"use allNotes\" when you are in the right location)");
                            }
                            else if (difficulty.equals("easy"))
                            {
                                System.out.println("This item does not exist");
                            }
                        }
                        
                        if(item.equalsIgnoreCase("note5") && !(allNotes.getRemoveFromInventory()))
                        {
                            if(difficulty.equals("hard"))
                            {
                                System.out.println("You are too far away to use notes");
                                System.out.println("(and use \"use allNotes\" when you are in the right location)");
                            }
                            else
                            {
                                System.out.println("This item does not exist");
                            }
                        }
                    }
                    }
                
                    else if(item.equalsIgnoreCase("note1") || item.equalsIgnoreCase("note2") || item.equalsIgnoreCase("note3") && (allNotes.getRemoveFromInventory()))
                    {
                        System.out.println("You already used all the notes.");
                    }
                else if(difficulty.equals("medium") || difficulty.equals("hard"))
                        {
                            if(item.equalsIgnoreCase("note4") && allNotes.getRemoveFromInventory())
                            {
                                System.out.println("You already used all the notes.");
                            }
                            else
                            {
                                System.out.println("Item does not exist");
                            }
                        }
                else if(item.equalsIgnoreCase("note5") && allNotes.getRemoveFromInventory())
                            {
                                if(difficulty.equals("hard"))
                                {
                                    System.out.println("You already used all the notes.");
                                }
                                else if (difficulty.equals("easy") || difficulty.equals("medium"))
                                {
                                    System.out.println("Item does not exist");
                                }
                   
                    }
                }
            }
        else if (item.equalsIgnoreCase("gateKey"))
            { 
                if(currentRoom == front)
                {
                    if(!gateKey.getInInventory() && !(gateKey.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(gateKey.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item");
                    }
                        
                    else if(gateKey.getInInventory()) 
                    {
                        unlockedGate = true;
                        System.out.println("You have unlocked the gate and now can walk through!");
                        gateKey.removeFromInventory();
                    }
                }
                
                else if(!(currentRoom == front))
                {
                    if(!gateKey.getInInventory() && !(gateKey.getRemoveFromInventory()))
                    {
                        System.out.println("You do not have this item.");
                    }
                    else if(gateKey.getRemoveFromInventory())
                    {
                        System.out.println("You have already used this item");
                    }
                    else if(gateKey.getInInventory()) 
                    {
                        System.out.println("You are too far away to use this item.");
                    }
                }
            }
    }


    

    /**
     * Another gag command
     * 
     * Pre condition: the play() method must be called and the command word must be
     * typed in proper case and spelling.
     * 
     * Post condition: Prints a different message if in the dungeon, otherwise you start talking to yourself.
     * The special message is just you attempting to talk to a wall... unsuccessfully.
     */
    public void speak() 
    {
        if (currentRoom.equals(dungeon)) 
        {
            System.out.println("You attempted to speak to the wall... unsuccessfully");
        } 
        else 
        {
            System.out.println("Remember, you are completely alone. There is nobody around to hear you talk, so you talk to yourself.");
            System.out.println("You keep talking to yourself, you sit down and have a full conversation with yourself before remembering you have to escape.");
        }
    }

    /**
     * Internal helper method that shows if all the notes have been collected
     * 
     * Pre condition: the play() method is called, difficulty is selected and the player has found all the notes
     * 
     * Post condition: returns if the player has all the notes
     * 
     * @return true if the player has all the notes, false if the player does not
     */
    private boolean getHasAllNotes() 
    {
        if (difficulty.equals("easy")) 
        {
            if (note1.getInInventory() && note2.getInInventory() && note3.getInInventory()) 
            {
                return true;
            }
            else 
            {
                return false;
            }
        }
        
        if (difficulty.equals("medium")) 
        {
            if (note1.getInInventory() && note2.getInInventory() && note3.getInInventory() && note4.getInInventory()) 
            {
                return true;
            } 
            else 
            {
                return false;
            }
        }
        
        if (difficulty.equals("hard")) 
        {
            if (note1.getInInventory() && note2.getInInventory() && note3.getInInventory() && note4.getInInventory() && note5.getInInventory()) 
            {
                return true;
            } 
            else 
            {
                return false;
            }
        }

        else {
            return false;
        }
    }

    /**
     * Helper that allows the teleporter to work.
     * 
     * Pre condition: the play() method has been called
     * 
     * Post condition: teleports somewhere random on the map
     * 
     */
    private void teleport()
    {
        currentRoom = rooms.get((int) (Math.random() * 15));
        pastRoom = null;
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Helper method that allows you to win.
     * 
     * Pre condition: the play() method has been called.
     * 
     * Post condition: you won the game, it is over.
     */
    private void youWin()
    {
        System.out.println("Congrats on winning!");
        System.out.println("You won on difficulty " + difficulty + " in " + moves + " moves");
        finished = true;
    }

    /**
     * Mutator method for the anyInInventory boolean. Tells if there are any items in the inventory.
     * 
     * Pre condition: the play() method has been called
     * 
     * Post condition: sets the anyInInventory field to true or false depending on if the player has any items
     */
    private void setAnyInInventory()
    {
        if(note1.getInInventory() || note2.getInInventory() || note3.getInInventory() || note4.getInInventory() || note5.getInInventory()
        || dungeonKey.getInInventory() || closetKey1.getInInventory() || closetKey2.getInInventory() || allNotes.getInInventory() || gateKey.getInInventory())
        {
            anyInInventory = true;
        }
        else
        {
            anyInInventory = false;
        }
    }
    
    /**
     * Accessor method that accesses the anyInInventory boolean
     * 
     * Pre conditoin: the play() method has been called
     * 
     * Post condition: If any item is in the inventory, it is returned as true,
     * otherwise it is returned as false.
     * @return true if there are any items in the inventory
     */
    private boolean getAnyInInventory()
    {
        return anyInInventory;
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true; // signal that we want to quit
        }
    }
}