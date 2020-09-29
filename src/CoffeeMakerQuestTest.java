import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.*;
import org.mockito.*;
import static org.mockito.Mockito.*;

public class CoffeeMakerQuestTest {

	CoffeeMakerQuest cmq;
	Player player;
	Room room1;	// Small room
	Room room2;	// Funny room
	Room room3;	// Refinanced room
	Room room4;	// Dumb room
	Room room5;	// Bloodthirsty room
	Room room6;	// Rough room

	@Before
	public void setup() {
		// 0. Turn on bug injection for Player and Room.
		Config.setBuggyPlayer(true);
		Config.setBuggyRoom(true);
		
		// 1. Create the Coffee Maker Quest object and assign to cmq.
		cmq = CoffeeMakerQuest.createInstance();

		// TODO: 2. Create a mock Player and assign to player and call cmq.setPlayer(player). 
		// Player should not have any items (no coffee, no cream, no sugar)
		player = Mockito.mock(Player.class);
		cmq.setPlayer(player);

		// TODO: 3. Create mock Rooms and assign to room1, room2, ..., room6.
		// Mimic the furnishings / adjectives / items of the rooms in the original Coffee Maker Quest.
		room1 = Mockito.mock(Room.class);
		setupRoom(room1, "Small", "Quaint sofa", Item.CREAM);
		
		room2 = Mockito.mock(Room.class);
		setupRoom(room2, "Funny", "Sad record player", Item.NONE);
		
		room3 = Mockito.mock(Room.class);
		setupRoom(room3, "Refinanced", "Tight pizza", Item.COFFEE);
		
		room4 = Mockito.mock(Room.class);
		setupRoom(room4, "Dumb", "Flat energy drink", Item.NONE);
		
		room5 = Mockito.mock(Room.class);
		setupRoom(room5, "Bloodthirsty", "Beautiful bag of money", Item.NONE);
		
		room6 = Mockito.mock(Room.class);
		setupRoom(room6, "Rough", "Perfect air hockey table", Item.SUGAR);
		
		// TODO: 4. Add the rooms created above to mimic the layout of the original Coffee Maker Quest.
		cmq.addFirstRoom(room1);
		cmq.addRoomAtNorth(room2, "Magenta", "Massive");
		cmq.addRoomAtNorth(room3, "Beige", "Smart");
		cmq.addRoomAtNorth(room4, "Dead", "Slim");
		cmq.addRoomAtNorth(room5, "Vivacious", "Sandy");
		cmq.addRoomAtNorth(room6, "Purple", "Minimalist");
		
	}
	
	private void setupRoom(Room r, String adj, String furnish, Item item) {
		Mockito.when(r.getAdjective()).thenReturn(adj);
		Mockito.when(r.getFurnishing()).thenReturn(furnish);
		Mockito.when(r.getItem()).thenReturn(item);
	}

	@After
	public void tearDown() {
	}
	
	/**
	 * Test case for String getInstructionsString().
	 * Preconditions: None.
	 * Execution steps: Call cmq.getInstructionsString().
	 * Postconditions: Return value is " INSTRUCTIONS (N,S,L,I,D,H) > ".
	 */
	@Test
	public void testGetInstructionsString() {
		// Master string to test against
		String master = " INSTRUCTIONS (N,S,L,I,D,H) > ";
		
		// Execute steps
		String test = cmq.getInstructionsString();
		assertEquals("Instruction string was not correct", master, test);
	}
	
	/**
	 * Test case for boolean addFirstRoom(Room room).
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                Create a mock room and assign to myRoom.
	 * Execution steps: Call cmq.addFirstRoom(myRoom).
	 * Postconditions: Return value is false.
	 */
	@Test
	public void testAddFirstRoom() {
		// Set preconditions
		Room myRoom = Mockito.mock(Room.class);
		
		// Execute steps
		boolean test = cmq.addFirstRoom(myRoom);
		assertFalse("Adding first room to already created rooms was true", test);
	}
	
	/**
	 * Test case for boolean addRoomAtNorth(Room room, String northDoor, String southDoor).
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                Create a mock "Fake" room with "Fake bed" furnishing with no item, and assign to myRoom.
	 * Execution steps: Call cmq.addRoomAtNorth(myRoom, "North", "South").
	 * Postconditions: Return value is true.
	 *                 room6.setNorthDoor("North") is called.
	 *                 myRoom.setSouthDoor("South") is called.
	 */
	@Test
	public void testAddRoomAtNorthUnique() {
		// Set preconditions
		Room myRoom = Mockito.mock(Room.class);
		setupRoom(myRoom, "Fake", "Fake bed", Item.NONE);
		
		// Execute steps
		boolean test = cmq.addRoomAtNorth(myRoom, "North", "South");
		assertTrue("Adding valid room at north, but returned false", test);
		Mockito.verify(room6).setNorthDoor("North");
		Mockito.verify(myRoom).setSouthDoor("South");
	}
	
	/**
	 * Test case for boolean addRoomAtNorth(Room room, String northDoor, String southDoor).
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                Create a mock "Fake" room with "Flat energy drink" furnishing with no item, and assign to myRoom.
	 * Execution steps: Call cmq.addRoomAtNorth(myRoom, "North", "South").
	 * Postconditions: Return value is false.
	 *                 room6.setNorthDoor("North") is not called.
	 *                 myRoom.setSouthDoor("South") is not called.
	 */
	@Test
	public void testAddRoomAtNorthDuplicate() {
		// Set preconditions
		Room myRoom = Mockito.mock(Room.class);
		setupRoom(myRoom, "Fale", "Flat energy drink", Item.NONE);
		
		// Execute steps
		boolean test = cmq.addRoomAtNorth(myRoom, "North", "South");
		assertFalse("Adding a duplicate room is not allowed, but returned true", test);
		Mockito.verify(room6, Mockito.never()).setNorthDoor("North");
		Mockito.verify(myRoom, Mockito.never()).setSouthDoor("South");
	}
	
	/**
	 * Test case for Room getCurrentRoom().
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(Room) has not yet been called.
	 * Execution steps: Call cmq.getCurrentRoom().
	 * Postconditions: Return value is null.
	 */
	@Test
	public void testGetCurrentRoom() {
		// Check for preconditions
		//Mockito.verify(cmq, Mockito.never()).setCurrentRoom(any(Room.class));
		
		// Execute steps
		Room test = cmq.getCurrentRoom();
		assertNull("Got current room before setting current room, but value was not null", test);
	}
	
	/**
	 * Test case for void setCurrentRoom(Room room) and Room getCurrentRoom().
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(Room room) has not yet been called.
	 * Execution steps: Call cmq.setCurrentRoom(room3).
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.setCurrentRoom(room3) is true. 
	 *                 Return value of cmq.getCurrentRoom() is room3.
	 */
	@Test
	public void testSetCurrentRoom() {
		// Check for preconditions
		//Mockito.verify(cmq, Mockito.never()).setCurrentRoom(any(Room.class));
		
		// Execute first step
		boolean firstTest = cmq.setCurrentRoom(room3);
		assertTrue("Tried to set current room to room3, but returned false", firstTest);
		
		// Execute second step
		Room secondTest = cmq.getCurrentRoom();
		assertEquals("Tried to set current room to room3, but current room is different", secondTest, room3);
	}
	
	/**
	 * Test case for String processCommand("I").
	 * Preconditions: Player does not have any items.
	 * Execution steps: Call cmq.processCommand("I").
	 * Postconditions: Return value is "YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n".
	 */
	@Test
	public void testProcessCommandI() {
		// Set preconditions on mocked player
		Mockito.when(player.checkCoffee()).thenReturn(false);
		Mockito.when(player.checkCream()).thenReturn(false);
		Mockito.when(player.checkSugar()).thenReturn(false);
		Mockito.when(player.getInventoryString()).thenReturn("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n");
		// Master string to test against
		String master = "YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n";
		
		// Execute steps
		String test = cmq.processCommand("I");
		assertEquals("Attempt to check inventory on player with no items had incorrect result", master, test);
	}
	
	/**
	 * Test case for String processCommand("l").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room1) has been called.
	 * Execution steps: Call cmq.processCommand("l").
	 * Postconditions: Return value is "There might be something here...\nYou found some creamy cream!\n".
	 *                 player.addItem(Item.CREAM) is called.
	 */
	@Test
	public void testProcessCommandLCream() {
		// Set preconditions
		cmq.setCurrentRoom(room1);
		
		// Master string to test against
		String master = "There might be something here...\nYou found some creamy cream!\n";
		
		// Execute steps
		String test = cmq.processCommand("l");
		assertEquals("Attempt to check room1 for cream had incorrect result", master, test);
		Mockito.verify(player).addItem(Item.CREAM);
	}
	
	/**
	 * Test case for String processCommand("n").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room4) has been called.
	 * Execution steps: Call cmq.processCommand("n").
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.processCommand("n") is "".
	 *                 Return value of cmq.getCurrentRoom() is room5.
	 */
	@Test
	public void testProcessCommandN() {
		// Set preconditions
		cmq.setCurrentRoom(room4);
		
		// Master string to test against
		String master = "";
		
		// Execute step 1
		String test = cmq.processCommand("n");
		assertEquals("Attempt to go north from room4 had incorrect result", master, test);
		
		// Execute step 2
		Room roomTest = cmq.getCurrentRoom();
		assertEquals("Current room after going north from room4 was incorrect", room5, roomTest);
	}
	
	/**
	 * Test case for String processCommand("s").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room1) has been called.
	 * Execution steps: Call cmq.processCommand("s").
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.processCommand("s") is "A door in that direction does not exist.\n".
	 *                 Return value of cmq.getCurrentRoom() is room1.
	 */
	@Test
	public void testProcessCommandS() {
		// Set preconditions
		cmq.setCurrentRoom(room1);
		
		// Master string to test against
		String master = "A door in that direction does not exist.\n";
		
		// Execute step 1
		String test = cmq.processCommand("s");
		assertEquals("Attempt to go south from room1 had incorrect result", master, test);
		
		// Execute step 2
		Room roomTest = cmq.getCurrentRoom();
		assertEquals("Current room after failed attempt to go south was not same room", room1, roomTest);
	}
	
	/**
	 * Test case for String processCommand("D").
	 * Preconditions: Player has no items.
	 * Execution steps: Call cmq.processCommand("D").
	 *                  Call cmq.isGameOver().
	 * Postconditions: Return value of cmq.processCommand("D") is "YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n".
	 *                 Return value of cmq.isGameOver() is true.
	 */
	@Test
	public void testProcessCommandDLose() {
		// Set preconditions on mocked player
		Mockito.when(player.checkCoffee()).thenReturn(false);
		Mockito.when(player.checkCream()).thenReturn(false);
		Mockito.when(player.checkSugar()).thenReturn(false);
		Mockito.when(player.getInventoryString()).thenReturn("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n");
		// Master string to test against
		String master = "YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n";
		
		// Execute step 1
		String test = cmq.processCommand("D");
		assertEquals("Drinking with no items had incorrect result", master, test);
		
		// Execute step 2
		boolean gameTest = cmq.isGameOver();
		assertTrue("Game over is not true after drinking with no items", gameTest);
	}
	
	/**
	 * Test case for String processCommand("D").
	 * Preconditions: Player has all 3 items (coffee, cream, sugar).
	 * Execution steps: Call cmq.processCommand("D").
	 *                  Call cmq.isGameOver().
	 * Postconditions: Return value of cmq.processCommand("D") is "You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n\nYou drink the beverage and are ready to study!\nYou win!\n".
	 *                 Return value of cmq.isGameOver() is true.
	 */
	@Test
	public void testProcessCommandDWin() {
		// Set preconditions on mocked player
		Mockito.when(player.checkCoffee()).thenReturn(true);
		Mockito.when(player.checkCream()).thenReturn(true);
		Mockito.when(player.checkSugar()).thenReturn(true);
		Mockito.when(player.getInventoryString()).thenReturn("You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n");
		
		
		// Master string to test against
		String master = "You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n\nYou drink the beverage and are ready to study!\nYou win!\n";
		
		// Execute step 1
		String test = cmq.processCommand("D");
		assertEquals("Drinking with all items had incorrect result", master, test);
		
		// Execute step 2
		boolean gameTest = cmq.isGameOver();
		assertTrue("Game over is not true after drinking with all items", gameTest);
	}
	
	// TODO: Put in more unit tests of your own making to improve coverage!
	/**
	 * Test case for invalid command String "INVALID"
	 * Preconditions: None
	 * Execution steps: Call cmq.processCommand("INVALID")
	 * Postconditions: Returnvalue of cmq.processCommand("INVALID") is "What?"
	 */
	@Test
	public void testProcessCommandInvalid()
	{
		String master = "What?";
		String test = cmq.processCommand("INVALID");
		assertEquals("Invalid command but result is not 'What?'", test, master);
	}
	
	/**
	 * Test case for moving south when a door exists in that direction
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room2) has been called.
	 * Execution steps: Call cmq.processCommand("s").
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.processCommand("s") is "".
	 *                 Return value of cmq.getCurrentRoom() is room2.
	 */
	@Test
	public void testProcessCommandSValid() {
		// Set preconditions
		cmq.setCurrentRoom(room2);
		
		
		// Execute step 1
		String test = cmq.processCommand("s");
		assertEquals("Attempt to go south from room2 had incorrect result", "", test);
		
		// Execute step 2
		Room roomTest = cmq.getCurrentRoom();
		assertEquals("Current room after successful attempt to go south was not room 1", room1, roomTest);
	}
	
	/**
	 * Test case for moving north when a door in that direction doesn't exist
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room6) has been called.
	 * Execution steps: Call cmq.processCommand("N").
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.processCommand("N") is "".
	 *                 Return value of cmq.getCurrentRoom() is room6.
	 */
	@Test
	public void testProcessCommandNInvalid() {
		// Set preconditions
		cmq.setCurrentRoom(room6);
		
		
		// Execute step 1
		String test = cmq.processCommand("N");
		assertEquals("Attempt to go north from room6 had incorrect result", "", test);
		
		// Execute step 2
		Room roomTest = cmq.getCurrentRoom();
		assertEquals("Current room after failed attempt to go north was not room 6", room6, roomTest);
	}
	
	/**
	 * Test case for moving north when a door in that direction doesn't exist
	 * Preconditions: None
	 * Execution steps: Call cmq.processCommand("H").
	 * Postconditions: Return value of cmq.processCommand("H") is ""Commands:\n"
				+ "\tN: Moves north\n"
				+ "\tS: Moves south\n"
				+ "\tL: Looks for items in the current room\n"
				+ "\tI: Checks current inventory\n"
				+ "\tD: Combines all current items and attempts to drink it\n"
				+ "\tH: Displays all possible commands and thier effects";".
	 */
	@Test
	public void testProcessCommandH() 
	{
		String test = cmq.processCommand("H");
		assertEquals("Attempt to go north from room6 had incorrect result", "Commands:\n"
				+ "\tN: Moves north\n"
				+ "\tS: Moves south\n"
				+ "\tL: Looks for items in the current room\n"
				+ "\tI: Checks current inventory\n"
				+ "\tD: Combines all current items and attempts to drink it\n"
				+ "\tH: Displays all possible commands and thier effects", test);
		
	}
	
	/**
	 * Test case for moving north when a door in that direction doesn't exist
	 * Preconditions: None
	 * Execution steps: Call cmq.addRoomAtNorth(null, null, null).
	 * Postconditions: Return value of cmq.addRoomAtNorth(null, null, null) is false.
	 */
	@Test
	public void testAddRoomAtNorthNull() 
	{
		boolean result = cmq.addRoomAtNorth(null, null, null);
		assertFalse("Added null room but result is not false", result);
		
	}
	
	/**
	 * Test case for processCommands("L") when coffee is in the room
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room4) has been called.
	 * Execution steps: Call cmq.processCommand("L").
	 * Postconditions: Return value of cmq.processCommand("L") is "There might be something here...\nYou found some caffeinated coffee!\n".
	 */
	@Test
	public void testProcessCommandLCoffee() {
		// Set preconditions
		cmq.setCurrentRoom(room3);
		
		
		// Execute step 1
		String test = cmq.processCommand("L");
		assertEquals("Attempt to find coffee had incorrect results", "There might be something here...\nYou found some caffeinated coffee!\n", test);
		
	}
	
	/**
	 * Test case for processCommands("L") when sugar is in the room
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room6) has been called.
	 * Execution steps: Call cmq.processCommand("L").
	 * Postconditions: Return value of cmq.processCommand("L") is "There might be something here...\nYou found some sweet sugar!\n"
	 */
	@Test
	public void testProcessCommandLSugar() {
		// Set preconditions
		cmq.setCurrentRoom(room6);
		
		
		// Execute step 1
		String test = cmq.processCommand("L");
		assertEquals("Attempt to find sugar had incorrect results", "There might be something here...\nYou found some sweet sugar!\n", test);
		
	}
	
	/**
	 * Test case for processCommands("L") when nothing is in the room
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room2) has been called.
	 * Execution steps: Call cmq.processCommand("L").
	 * Postconditions: Return value of cmq.processCommand("L") is "You don't see anything out of the ordinary.\n"
	 */
	@Test
	public void testProcessCommandLNothing() {
		// Set preconditions
		cmq.setCurrentRoom(room2);
		
		
		// Execute step 1
		String test = cmq.processCommand("L");
		assertEquals("Attempt to find sugar had incorrect results", "You don't see anything out of the ordinary.\n", test);
		
	}
}	
