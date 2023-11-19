import java.io.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

class PetRecord {
    String name;
    int health;
    int hunger;
    int happiness;
    String favoriteFood;
}

public class VirtualPetGame {
    public static void main(String[] args) throws IOException {

        printIntroMessage();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


             PetRecord[] petRecords = new PetRecord[5];

        System.out.println("Do you want to resume a saved game? (Y/N)");
        String resumeDecision = reader.readLine();
        if (resumeDecision.equals("Y")) {
             petRecords= LoadGameFromFile();
        } else {
            System.out.println("How many pets would you like to take care of?");
            int numPets = Integer.parseInt(reader.readLine());

            petRecords = createPetRecords(reader, numPets);
        }

        System.out.println("How many rounds would you like to play?");
        int numRounds = Integer.parseInt(reader.readLine());

        playGame(petRecords, numRounds);

        printAllPetDetails(petRecords);

        determineOutcome(petRecords);
    }

    public static void saveGameToFile(PetRecord[] petRecords, int round) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("pet_game_state.txt"));
        writer.write("Round: " + round + "\n");
        for (PetRecord petRecord : petRecords) {
            writer.write(petRecord.name + "," + petRecord.health + "," + petRecord.hunger + "," + petRecord.happiness + "," + petRecord.favoriteFood + "\n");
        }
        System.out.println("Game state saved successfully!");
        writer.close();
    }
      public static PetRecord[] LoadGameFromFile() throws IOException
    {
         String filename = "pet_game_state.txt";
            BufferedReader inputStream = new BufferedReader (new FileReader(filename));
            List<PetRecord> petList = new ArrayList<>();

            String pets = inputStream.readLine();
            while(pets!=null)
            {
                PetRecord pet = new PetRecord();
                String[] pet_components = pets.split(",");

                pet.name = pet_components[0];
                pet.hunger = Integer.parseInt(pet_components[1]);
                pet.health = Integer.parseInt(pet_components[2]);
                pet.happiness = Integer.parseInt(pet_components[3]);
                pet.favoriteFood = pet_components[4];

                System.out.println( pet.name + "\t" + pet.hunger + ",\t" + pet.health + ",\t" + pet.happiness + ",\t" + pet.favoriteFood );

                pets = inputStream.readLine();
            }

            inputStream.close();
            PetRecord[] petRecords = petList.toArray(new PetRecord[0]);

            return petRecords;
        }

    public static void printIntroMessage() {
        System.out.println("ฅ՞•ﻌ•՞ฅ Ｐｅｔ　Ｐｒｏｇｒａｍ‌ ฅ՞•ﻌ•՞ฅ");
        System.out.println();
        System.out.println("The purpose of the program is to look after the pets");
        System.out.println("This includes monitoring its hunger and health levels");
        System.out.println("Looking forward to meeting with your pets!");
        System.out.println();
        return;
    }

    public static PetRecord[] createPetRecords(BufferedReader reader, int numPets) throws IOException {
        PetRecord[] petRecords = new PetRecord[numPets];


        for (int i = 0; i < numPets; i++) {
            System.out.println("Enter the name of pet " + (i + 1) + ": ");
            String name = reader.readLine();
            System.out.println("Enter the favorite food of " + name + ": ");
            String favoriteFood = reader.readLine();


            PetRecord petRecord = new PetRecord();
            setPetRecord(petRecord, name, favoriteFood);

            petRecords[i] = petRecord;
        }

        return petRecords;
    }

    public static void playGame(PetRecord[] petRecords, int numRounds) throws IOException {
        Random random = new Random();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (int round = 1; round <= numRounds; round++) {
            System.out.println();
            System.out.println("ROUND " + round + ":");

            for (int i = 0; i < petRecords.length; i++) {
                PetRecord petRecord = petRecords[i];
                PetRecord health = setPetRecordHealth(petRecord, 4);

                System.out.println("Pet: " + getPetRecordName(petRecord) + ", Health: " + getPetRecordHealth(health) +
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
            System.out.println("Pet " + getPetRecordName(winningPet) + " has survived against all other pets for at least three rounds. It wins!");

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

    public static int generateHungerLevels() {
        Random random = new Random();
        return random.nextInt(5) + 1;
    }

    public static String nameHungerLevel(int hungerLevel) {
        String[] hungerDescriptions = {"full", "content", "hungry", "famished", "ravenous"};
        if (hungerLevel >= 1 && hungerLevel <= 5) {
            return hungerDescriptions[hungerLevel - 1];
        } else {
            return "Hunger level is unknown.";
        }
    }

    public static int simulateRound(int choice, int health, int hunger, int happiness, Random random,PetRecord pr) {
        setPetRecordHunger(pr, hunger);
        if (choice == 1) {
            hunger = hunger + random.nextInt(3) + 1;
            System.out.println("Your pet's hunger level is now " + hunger);
        } else if (choice == 2) {
            pr.happiness = happiness + random.nextInt(3) + 1;
            System.out.println("Your pet's happiness level is now " + getPetRecordHappiness(pr));
        } else if (choice == 3) {
            health = health + random.nextInt(2);
        } else {
            System.out.println("Invalid choice.");
            System.out.println("Your pet is now upset.");
            health = 0;
        }

        if (hunger <= 2) {
            health = health - random.nextInt(3) - 2;
        }

        System.out.println("Your pet's health level is now " + health);
        return health;
    }

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
    public static PetRecord setPetRecordHealth( PetRecord pr,int h) {
        h =4;
        pr.health = h;
        return pr;
    }
    public static PetRecord setPetRecord( PetRecord pr, String nm, String fd) {
        pr = setPetRecordName(pr,nm);
        pr = setPetRecordFood(pr,fd);
        return pr;
    }
}
