/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room pasilloFP, pasilloBachiller, escaleras, aula202, aula201, aula203;
      
        // create the rooms
        escaleras = new Room("en las escaleras del segundo piso.");
        
        pasilloFP = new Room("en el pasillo de FP.\nLa puerta del aula de examenes esta cerrada.\n" +  
        "Al otro lado se pueden escuchar los alaridos de cientos de alumnos suspensos." +  
        "\n Se oye bullicio en el aula 203. Las clases ya han comenzado.");
        
        pasilloBachiller = new Room("en el pasillo de Bachiller " + 
        "Las puertas de este pasillo estan cerradas.\nHay un grupo de chicas sentadas en el suelo.");
        
        aula201 = new Room("en aula de examenes. \n Un esqueleto aun continua realizando un examen" +  
        "La cornisa parece conectar con el aula 203.");
        
        aula202 = new Room("en aula de 2º. Parece desierta.\n Una ventana al fondo esta abierta." +  
        "La cornisa parece conectar con el aula 203.");
        
        aula203 = new Room("en el aula 203, el profesor no ha llegado." + 
        "Los ordenadores estan encendidos y la gente trabajando, absorta en sus pensamientos/n" 
        + "Una ventana lateral esta abierta y parerce conectar con el aula 202");
        
        // initialise room exits
        escaleras.setExits(null, pasilloFP, null, null, pasilloBachiller);
        pasilloBachiller.setExits(null, escaleras, null, null, null);
        pasilloFP.setExits(null, aula203, aula201, aula202, escaleras);
        aula201.setExits(pasilloFP, null, null, null, null);
        aula202.setExits(null, null, aula203, null, null);
        aula203.setExits(null, null, aula202, null, pasilloFP);

        currentRoom = escaleras;  // start game outside
    }
    
    /**
     * Muestra por terminal la informacion de la sala actual.
     */
    private void printLocationInfo(){
        System.out.println();
        System.out.println("Estas " + currentRoom.getDescription());
        System.out.print("Exits: ");
        if(currentRoom.northExit != null) {
            System.out.print("north ");
        }
        if(currentRoom.eastExit != null) {
            System.out.print("east ");
        }
        if(currentRoom.southExit != null) {
            System.out.print("south ");
        }
        if(currentRoom.westExit != null) {
            System.out.print("west ");
        }
        if(currentRoom.southEastExit != null) {
            System.out.print("southEast ");
        }
        System.out.println();
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bienvenido al Felegator");
        System.out.println("Un juego basado en hechos reales");
        System.out.println("El timbre ha sonado hace severos minutos y el Felegado esta esperando para apuntarte.");
        System.out.println();
        System.out.println("Escribe 'help' si necesitas ayuda.");
        System.out.println();
        printLocationInfo();
        
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = currentRoom.northExit;
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.eastExit;
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.southExit;
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.westExit;
        }
        if(direction.equals("southWest")) {
            nextRoom = currentRoom.westExit;
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
