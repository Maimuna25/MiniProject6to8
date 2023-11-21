
import java.io.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class VirtualPetGame {
    public static void main(String[] args) throws IOException {

        PetProgramLevel8();
        return;
    }
    
// Plays five rounds of game then determines the pets final health state and whether it has won or not.
   public static void PetProgramLevel8 ()throws IOException {  
     printIntroMessage();

     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


     PetRecord[] petRecords;

     System.out.println("Do you want to resume a saved game? (Y/N)");
     String resumeDecision = reader.readLine();
     int startingRound = 1;

        if (resumeDecision.equals("Y")) {
             petRecords= LoadGameFromFile();
             startingRound = getStartingRound() + 1;
        } else {
            System.out.println("How many pets would you like to take care of?");
            int numPets = Integer.parseInt(reader.readLine());

            petRecords = createPetRecords(reader, numPets);
        }

        System.out.println("How many rounds would you like to play?");
        int numRounds = Integer.parseInt(reader.readLine());

        playGameFromRound(petRecords,startingRound, numRounds);

        printAllPetDetails(petRecords);

        determineOutcome(petRecords);
    }
    
//saves the game to a file so it can be accessed later on when the game is resumed    
    public static void saveGameToFile(PetRecord[] petRecords, int round) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("pet_game_state.txt"));
        writer.write("Round: " + round + "\n");
        for (PetRecord petRecord : petRecords) {
            writer.write(getPetRecordName(petRecord) + "," + getPetRecordHunger(petRecord) + "," + getPetRecordHealth(petRecord) + "," + getPetRecordHappiness(petRecord) + "," + getPetRecordFood(petRecord) + "\n");
        }
        System.out.println("Game state saved successfully!");
        writer.close();
    }
    
// Extract the round number from the saved game state
    public static int getStartingRound() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("pet_game_state.txt"));
        String roundInfo = reader.readLine(); // Read the first line containing round information
        reader.close();

        if (roundInfo != null && roundInfo.startsWith("Round: ")) {
            return Integer.parseInt(roundInfo.substring(7)); // Extract the round number
        }

        return 1; // Default starting round if there's no saved state or error in reading
    }
    
    public static PetRecord[] LoadGameFromFile() throws IOException
    {
            String filename = "pet_game_state.txt";
            BufferedReader inputStream = new BufferedReader (new FileReader(filename));
            List<PetRecord> petList = new ArrayList<>();

            String pets = inputStream.readLine();

        // Skip the line if it contains a colon
        if (pets != null && pets.contains(":")) {
            System.out.println(pets);
            pets = inputStream.readLine(); // Move to the next line
        }
            while(pets!=null)
            {
                PetRecord pet = new PetRecord();
                String[] pet_components = pets.split(",");


                if (pet_components.length == 5) { // Ensure correct number of components
                    pet.name = pet_components[0];
                    pet.hunger = Integer.parseInt(pet_components[1]);
                    pet.health = Integer.parseInt(pet_components[2]);
                    pet.happiness = Integer.parseInt(pet_components[3]);
                    pet.favoriteFood = pet_components[4];

                    System.out.println(pet.name + "\t" + pet.hunger + ",\t" + pet.health + ",\t" + pet.happiness + ",\t" + pet.favoriteFood);

                    petList.add(pet); // Add the created PetRecord to the list
                } else {
                  
                    System.out.println("Invalid data format in file: " + pets);
                }

                pets = inputStream.readLine();
            }

            inputStream.close();
            PetRecord[] petRecords = petList.toArray(new PetRecord[0]);

            return petRecords;
        }
    
// Print a description of what the purpose of the program is for pets
    public static void printIntroMessage() {
        System.out.println("ฅ՞•ﻌ•՞ฅ Ｐｅｔ　Ｐｒｏｇｒａｍ‌ ฅ՞•ﻌ•՞ฅ");
        System.out.println();
        System.out.println("The purpose of the program is to look after the pets");
        System.out.println("This includes monitoring its hunger and health levels");
        System.out.println("Looking forward to meeting with your pets!");
        System.out.println();
        return;
    }
//
    public static PetRecord[] createPetRecords(BufferedReader reader, int numPets) throws IOException {
        PetRecord[] petRecords = new PetRecord[numPets];


        for (int i = 0; i < numPets; i++) {
            System.out.println("Enter the name of pet " + (i + 1) + ": ");
            String name = reader.readLine();
            System.out.println("Enter the favorite food of " + name + ": ");
            String favoriteFood = reader.readLine();
            System.out.println("Initial health " + name + ": ");
            int health = Integer.parseInt(reader.readLine());

            PetRecord petRecord = new PetRecord();
            setPetRecord(petRecord, name, favoriteFood, health);

            petRecords[i] = petRecord;
        }

        return petRecords;
    }
//
    public static void playGameFromRound(PetRecord[] petRecords,int startingRound, int numRounds) throws IOException {
        Random random = new Random();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (int round = startingRound; round <= numRounds; round++) {
            System.out.println();
            System.out.println("ROUND " + round + ":");

            for (int i = 0; i < petRecords.length; i++) {
                PetRecord petRecord = petRecords[i];


                System.out.println("Pet: " + getPetRecordName(petRecord) + ", Health: " + getPetRecordHealth(petRecord) +
                        ", Hunger: " + nameHungerLevel(getPetRecordHunger(petRecord)) +
                        ", Favorite Food: " + getPetRecordFood(petRecord));

                int choice = getUserChoice(reader);

            simulateRound(choice, getPetRecordHealth(petRecord), getPetRecordHunger(petRecord), getPetRecordHappiness(petRecord), random, petRecord);
        }

            System.out.println("Do you want to save and quit the game? (Y/N)");
            String saveDecision = reader.readLine();
            if (saveDecision.equals("Y")) {
                saveGameToFile(petRecords, round);
                System.out.println("Game saved successfully. Quitting...");

                return;
            }
        }
    }
    
// prints out all the details of the pet after finishing the game
    public static void printAllPetDetails(PetRecord[] petRecords) {
        System.out.println("Details of all pets:");
        for (int i = 0; i < petRecords.length; i++) {
            PetRecord petRecord = petRecords[i];

            System.out.println("Pet: " + getPetRecordName(petRecord) + ", Health: " +  getPetRecordHealth(petRecord) +
                            ", Happiness: " + getPetRecordHappiness(petRecord) +
                            ", Hunger: " + nameHungerLevel(getPetRecordHunger(petRecord)) +
                            ", Favorite Food: " + getPetRecordFood(petRecord));
        }
    }

    public static void determineOutcome(PetRecord[] petRecords) {
        int numPets = petRecords.length;
        int[] roundsWonByPet = new int[numPets];
        boolean foundWinner = false;
        int winningPetIndex = -1;

        int i = 0;
        while (!foundWinner && i < numPets) {
            int j = 0;
            while (j < numPets) {
                if (i != j && petRecords[i].health > 0 && petRecords[j].health > 0) {
                    roundsWonByPet[i]++;
                }
                j++;
            }

            if (roundsWonByPet[i] >= numPets - 1) {
                foundWinner = true;
                winningPetIndex = i;
            }
            i++;
        }

     if (foundWinner) {
        PetRecord winningPet = petRecords[winningPetIndex];
        System.out.println("Pet " + getPetRecordName(winningPet) + " has survived against all other pets. It wins!");

        int j = 0;
        while (j < numPets) {
          if (winningPetIndex != j && petRecords[j].health > 0) {
              System.out.println("Pet " + getPetRecordName(petRecords[j]) + " has lost against " + getPetRecordName(winningPet));
             }
                j++;
         }
    } else {
            System.out.println("No pet has survived against all other pets for at least three rounds. Game ends in a draw.");
     }
 }
    
// Generates the pet's hunger level number and returns it.
    public static int generateHungerLevels() {
        Random random = new Random();
        return random.nextInt(5) + 1;
    }
    
// Looks up the pets hunger level names from the array list and returns it
    public static String nameHungerLevel(int hungerLevel) {
        String[] hungerDescriptions = {"full", "content", "hungry", "famished", "ravenous"};
        if (hungerLevel >= 1 && hungerLevel <= 5) {
            return hungerDescriptions[hungerLevel - 1];
        } else {
            return "Hunger level is unknown.";
        }
    }
// increases/decreases pets hunger,health and happiness levels by a random variable
  public static int simulateRound(int choice, int health, int hunger, int happiness, Random random,PetRecord pr) {
        setPetRecordHunger(pr, hunger);
        pr.health=health;
        if (choice == 1) {
            hunger = hunger + random.nextInt(3) + 1;
            System.out.println("Your pet's hunger level is now " + hunger);
        } else if (choice == 2) {
            pr.happiness = happiness + random.nextInt(3) + 1;
            System.out.println("Your pet's happiness level is now " + getPetRecordHappiness(pr));
        } else if (choice == 3) {
            health = health + random.nextInt(2) + 1;
        } else {
            System.out.println("Invalid choice.");
            System.out.println("Your pet is now upset.");
            health = 0;
        }

        if (hunger <= 2) {
            health = health - random.nextInt(3) - 1;
        }

        System.out.println("Your pet's health level is now " + health);
        return health;
    }
// allows the user to choose how to take care of the pet
    public static int getUserChoice(BufferedReader reader) throws IOException {
        int outputAnswer = 0;

        System.out.println("Choose an action for your pets:");
        System.out.println("1. Feed the pets");
        System.out.println("2. Hug the pets");
        System.out.println("3. Give medicine");

        while (outputAnswer < 1 || outputAnswer > 3) {
            System.out.println("Enter the number of your choice: ");
            outputAnswer = Integer.parseInt(reader.readLine());

            if (outputAnswer < 1 || outputAnswer > 3) {
                System.out.println("Try again");
            }
        }

        return outputAnswer;
    }
// Asks for the pet's age. Then returns the age as an integer value.
    public static String getPetRecordName(PetRecord pr) {
        return pr.name;
    }
    public static String getPetRecordFood(PetRecord pr) {
        return pr.favoriteFood;
    }
    public static int getPetRecordHunger(PetRecord pr) {
        return pr.hunger;
    }
    public static int getPetRecordHealth(PetRecord pr) {
        return pr.health;
    }
    public static int getPetRecordHappiness(PetRecord pr) {
        return pr.happiness;
    }
// Asks for the pet's age. Then returns the age as an integer value.
    public static PetRecord setPetRecordName( PetRecord pr, String nm) {
        pr.name = nm;
        return pr;
    }
    public static PetRecord setPetRecordFood( PetRecord pr, String fd) {
        pr.favoriteFood = fd;
        return pr;
    }
    public static PetRecord setPetRecordHunger( PetRecord pr, int hg) {
        hg = generateHungerLevels();
        pr.hunger = hg;
        return pr;
    }
    public static PetRecord setPetRecordHealth( PetRecord pr, int h) {
        h=4;
        pr.health = h;
        return pr;
    }
    public static PetRecord setPetRecord( PetRecord pr, String nm, String fd, int h) {
        pr = setPetRecordName(pr,nm);
        pr = setPetRecordHealth(pr,h);
        pr = setPetRecordFood(pr,fd);

        return pr;
    }
}
//this class stores different attributes of the pets
class PetRecord {
    String name;
    int health;
    int hunger;
    int happiness;
    String favoriteFood;
}
