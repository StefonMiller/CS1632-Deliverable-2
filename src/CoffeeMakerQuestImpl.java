import java.util.*;

enum Item {
	NONE,
	COFFEE,
	CREAM,
	SUGAR
}

public class CoffeeMakerQuestImpl implements CoffeeMakerQuest {
	
	ArrayList<Room> rooms;
	Player player;
	Room currentRoom;
	boolean drank;
	
	CoffeeMakerQuestImpl() {
		rooms = new ArrayList<Room>();
		currentRoom = null;
		drank = false;
	}

	/**
	 * Whether the game is over. The game ends when the player drinks the coffee.
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean isGameOver() {
		return drank;
	}

	/**
	 * Set the player to p.
	 * 
	 * @param p the player
	 */
	public void setPlayer(Player p) {
		player = p;
	}
	
	/**
	 * Add the first room in the game. If room is null or if this not the first room
	 * (there are pre-exiting rooms), the room is not added and false is returned.
	 * 
	 * @param room the room to add
	 * @return true if successful, false otherwise
	 */
	public boolean addFirstRoom(Room room) {
		//Check if room is null or arraylist is empty
		if(room == null || !rooms.isEmpty())
		{
			return false;
		}
		
		//Add room
		rooms.add(room);
		return true;
	}

	/**
	 * Attach room to the northern-most room. If either room, northDoor, or
	 * southDoor are null, the room is not added. If there are no pre-exiting rooms,
	 * the room is not added. If room is not a unique room (a pre-exiting room has
	 * the same adjective or furnishing), the room is not added. If all these tests
	 * pass, the room is added. Also, the north door of the northern-most room is
	 * labeled northDoor and the south door of the added room is labeled southDoor.
	 * Of course, the north door of the new room is still null because there is
	 * no room to the north of the new room.
	 * 
	 * @param room      the room to add
	 * @param northDoor string to label the north door of the current northern-most room
	 * @param southDoor string to label the south door of the newly added room
	 * @return true if successful, false otherwise
	 */
	public boolean addRoomAtNorth(Room room, String northDoor, String southDoor) {
		//Check if either room, northDoor or southDoor are null and if the arraylist is empty
		if(room == null || northDoor == null || southDoor == null || rooms.isEmpty())
		{
			return false;
		}
		
		//Loop through arraylist and check for room uniqueness
		for(int i = 0; i < rooms.size(); i++)
		{
			Room tempRoom = rooms.get(i);
			if(tempRoom.getAdjective() == room.getAdjective() || tempRoom.getFurnishing() == room.getFurnishing())
			{
				return false;
			}
		}
		//Set the northDoor of the northmost door to northDoor
		rooms.get(rooms.size() - 1).setNorthDoor(northDoor);
		//Add room to the list
		rooms.add(room);
		//Set the south door of the room we just added to southDoor
		rooms.get(rooms.size() - 1).setSouthDoor(southDoor);
		return true;
	}

	/**
	 * Returns the room the player is currently in. If location of player has not
	 * yet been initialized with setCurrentRoom, returns null.
	 * 
	 * @return room player is in, or null if not yet initialized
	 */ 
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	/**
	 * Set the current location of the player. If room does not exist in the game,
	 * then the location of the player does not change and false is returned.
	 * 
	 * @param room the room to set as the player location
	 * @return true if successful, false otherwise
	 */
	public boolean setCurrentRoom(Room room) {
		//Check if room is null or arraylist doesn't have the room
		if(room == null || !rooms.contains(room))
		{
			return false;
		}
		currentRoom = room;
		return true;
	}
	
	/**
	 * Get the instructions string command prompt. It returns the following prompt:
	 * " INSTRUCTIONS (N,S,L,I,D,H) > ".
	 * 
	 * @return comamnd prompt string
	 */
	public String getInstructionsString() {
		return " INSTRUCTIONS (N,S,L,I,D,H) > ";
	}
	
	/**
	 * Executes the N command
	 * @return response for the N command
	 */
	private String executeNCommand()
	{
		//If the user attempts to go north at the northmost room
		if((rooms.indexOf(currentRoom)) == (rooms.size() - 1))
		{
			return "";
		}
		//Set currentRoom to next room in arraylist
		setCurrentRoom(rooms.get(rooms.indexOf(currentRoom) + 1));
		return "";
	}
	
	/**
	 * Executes the S command
	 * @return response for the S command
	 */
	private String executeSCommand()
	{
		//If the user attempts to go south at the first room
		if(rooms.indexOf(currentRoom) == 0)
		{
			return "A door in that direction does not exist.\n";
		}
		//Set currentRoom to next room in arraylist
		setCurrentRoom(rooms.get(rooms.indexOf(currentRoom) - 1));
		return "";
	}
	
	/**
	 * Executes the L command
	 * @return response for the L command
	 */
	private String executeLCommand()
	{
		//Get item for current room
		Item item = currentRoom.getItem();
		switch(item) 
		{
			//Add corresponding item to player's inventory and return corresponding string
			case COFFEE:
				player.addItem(item);
				return "There might be something here...\nYou found some caffeinated coffee!\n";
			case CREAM:
				player.addItem(item);
				return "There might be something here...\nYou found some creamy cream!\n";
			case SUGAR:
				player.addItem(item);
				return "There might be something here...\nYou found some sweet sugar!\n";
			default:
				return "You don't see anything out of the ordinary.\n";
		}
	}
	
	/**
	 * Executes the I command
	 * @return response for the I command
	 */
	private String executeICommand()
	{
		return player.getInventoryString();
		
	}
	
	/**
	 * Executes the D command
	 * @return response for the D command
	 */
	private String executeDCommand()
	{
		//No matter what the game is now over
		drank = true;
		//Check what items the user has
		boolean coffee = player.checkCoffee();
		boolean cream = player.checkCream();
		boolean sugar = player.checkSugar();
		String result = player.getInventoryString();
		//If the user has all 3, then they win
		if(coffee && cream && sugar)
		{
			result += "\nYou drink the beverage and are ready to study!\nYou win!\n";
		}
		//Display error message customized to the user's inventory
		else if(!coffee && !cream && !sugar)
		{
			result += "\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n";
		}
		else if(cream && sugar)
		{
			result += "\nYou drink the sweetened cream, but without caffeine, you cannot study.\nYou lose!\n";
		}
		else if(coffee && cream)
		{
			result += "\nWithout sugar, the coffee is too bitter. You cannot study.\nYou lose!\n";
		}
		else if((coffee && sugar) || coffee)
		{
			result += "\nWithout cream, you get an ulcer and cannot study.\nYou lose!\n";
		}
		else if(cream)
		{
			result += "\nYou drink the cream, but without caffeine, you cannot study.\nYou lose!\n";
		}
		else if(sugar)
		{
			result += "\nYou eat the sugar, but without caffeine you cannot study.\nYou lose!\n";
		}
		
		return result;
	}
	
	/**
	 * Executes the H command
	 * @return response for the H command
	 */
	private String executeHCommand()
	{
		return "N - Go north\n" + 
				"S - Go south\n" + 
				"L - Look and collect any items in the room\n" + 
				"I - Show inventory of items collected\n" + 
				"D - Drink coffee made from items in inventory\n";
	}
	/**
	 * Processes the user command given in String cmd and returns the response
	 * string. For the list of commands, please see the Coffee Maker Quest
	 * requirements documentation (note that commands can be both upper-case and
	 * lower-case). For the response strings, observe the response strings printed
	 * by coffeemaker.jar. The "N" and "S" commands potentially change the location
	 * of the player. The "L" command potentially adds an item to the player
	 * inventory. The "D" command drinks the coffee and ends the game. Make
     * sure you use Player.getInventoryString() whenever you need to display
     * the inventory.
	 * 
	 * @param cmd the user command
	 * @return response string for the command
	 */
	public String processCommand(String cmd) {
		switch(cmd.toLowerCase())
		{
			case "n":
				return executeNCommand();
			case "s":
				return executeSCommand();
			case "l":
				return executeLCommand();
			case "i":
				return executeICommand();
			case "d":
				return executeDCommand();
			case "h":
				return executeHCommand();
			default:
				return "What?";
		}
	}
	
}
