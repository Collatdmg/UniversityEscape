
/**
 * The Item class creates the items that the
 * user has to find. All items are defined in the Game class in 
 * the createItems() method. There are 9 items to find, and 
 * 1 item that isn't found but is used to make the process
 * of depositing notes more simple.
 *
 * @author Ethan Jones
 * @version 2022.11.20
 */
public class Item
{
    // instance variables - replace the example below with your own
  
    private boolean inInventory, removedFromInventory;

    
    /**
     * Constructor for objects of class Item
     * 
     * Pre condition: none
     * 
     * Post condition: the item is created and is set to not in the inventory.
     */
    public Item()
    {
        // initialise instance variable      
        inInventory = false;
    }

    /**
     * Mutator method for the inInventory field
     * 
     * Pre condition: the item has been initialized. The play()
     * method must be executing.
     * 
     * Post condition: the value of inInventory is set to true.
     */
    public void setInInventory()
    {
        inInventory = true;
    }
    
    /**
     * Accessor method for the inInventory field
     * 
     * Pre condition: the item has been initialized. The play()
     * method must be executing.
     * 
     * Post condition: the value of inInventory is returned;
     * 
     * @return the value of inInventory.
     */
    public boolean getInInventory()
    {
        return inInventory;
    }

    /**
     * mutator method for the inInventory and removedFromInventory fields
     * 
     * Pre condition: the item has been initialized. The play()
     * method must be executing.
     * 
     * Post condition: sets inInventory to false and removedFromInventory to true
     */
    public void removeFromInventory()
    {
        inInventory = false;
        removedFromInventory = true;
    }

    /**
     * accessor method for the removedFromInventory field
     * 
     * Pre condition: the item has been initialized. The play()
     * method must be executing.
     * 
     * Post condition: returns the value of removedFromInventory
     * 
     * @return the value of removedFromInventory
     */
    public boolean getRemoveFromInventory()
    {
        return removedFromInventory;
    }

}
