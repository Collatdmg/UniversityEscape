
/**
 * The map class outputs a map of the university with cardinal directions.
 *
 * @author Ethan Jones
 * @version 2022.11.20
 */
public class Map
{
    // instance variables - replace the example below with your own

    /**
     * Constructor for objects of class Map
     * 
     * Pre condition: none
     * 
     * Post condition: none
     */
    public Map()
    {
       
    }

   
/**
 * Prints out the map of the university (not including the dungeon)
 * 
 * Pre condition: the play() method is called in the Game Class
 * 
 * Post condition: the map will be printed and the user will be able to 
 * see it.
 */
public void map()
{
    System.out.println("                                                  ________________                                                                 ");
    System.out.println("                                                  |     Gate     |                                                                ");
    System.out.println(" _________________________________________________|______________|________________________________________________________    ");
    System.out.println("/                  |         West       |                                  |      East        |                   |Closet|      ");
    System.out.println("|     library      |       Courtyard    |              Front               |    Courtyard     |         Subway    |______|      ");
    System.out.println("|                  /        (Top) ______|__________________________________|_____ (Top)       |                          |      ");
    System.out.println("\\_________________/              /                                               \\            |__________________________|        ");
    System.out.println("/              |                 |                                               |            \\                          |     ");
    System.out.println("|     Path     |                 |                 Student Union                 |             \\          Path           |     ");
    System.out.println("|              |                 |                   (Top half)                  |              \\                        |     ");
    System.out.println("|______________|_________________|                                               |_______________\\_______________________|     ");
    System.out.println("|                    |           |                                               |                 |                     |      ");
    System.out.println("|                    |           |                                               |                 |                     |      ");
    System.out.println("|   Physics          |           |                                               |                 |                     |      ");
    System.out.println("|                    |           |                                               |                 |      Computer       |      ");
    System.out.println("|____________________|   West    |                                               |      East       |      Science        |      ");
    System.out.println("|                |    Courtyard  |                Student Union                  |    Courtyard    |                     |      ");
    System.out.println("|    Physics     |     (bottom)  |                (Bottom half)                  |     (bottom)    |                     |      ");
    System.out.println("|      Lab       |               |________                                       |                 |                     |      ");
    System.out.println("|                |               | Closet|                                       |                 |                     |      ");
    System.out.println("|________________|_______________|_______|_______________________________________|_________________|_____________________|      ");
    System.out.println("                 |               |                                               |                 |                            ");
    System.out.println("        N        |      Math     |                      South                    |     Office      |                            ");
    System.out.println("      W   E      |               |                    Courtyard                  |                 |                            ");
    System.out.println("        S        |_______________|_______________________________________________|_________________|                    "       );
   
}
}