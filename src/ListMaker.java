import java.util.ArrayList;
import java.util.Scanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lab 10: ListMaker
 * A simple line editor that lets the user create an on-screen list
 * stored in an ArrayList of Strings. Menu options add, delete, insert,
 * print, and quit. All input is handled through the SafeInput library.
 */
public class ListMaker
{
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args)
    {
        ArrayList<String> itemList = new ArrayList<>();
        String command = "";
        boolean done = false;

        do
        {
            displayListAndMenu(itemList);
            command = SafeInput.getRegExString(in,
                    "Enter your choice [A, D, I, O, P, S, Q]",
                    "[AaDdIiOoPpSsQq]");
            command = command.toUpperCase();

            switch (command)
            {
                case "A":
                    addItem(itemList);
                    break;
                case "D":
                    deleteItem(itemList);
                    break;
                case "I":
                    insertItem(itemList);
                    break;
                case "P":
                    printList(itemList);
                    break;
                case "O":
                    openList(itemList);
                    break;
                case "S":
                    saveList(itemList);
                    break;

                case "Q":
                    done = quitProgram();
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        } while (!done);

        System.out.println("Thanks for using ListMaker. Goodbye!");
    }

    /**
     * Display the current numbered list plus the menu of options
     * so the user can see what they are working with.
     * @param itemList the ArrayList of list items
     */
    private static void displayListAndMenu(ArrayList<String> itemList)
    {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Current List:");
        printNumberedItems(itemList);
        System.out.println("========================================");
        System.out.println("Menu: [A]dd  [D]elete  [I]nsert  [O]pen  [P]rint  [S]ave  [Q]uit");
    }

    /**
     * Print each list item with a human-friendly number starting at 1.
     * @param itemList the ArrayList of list items
     */
    private static void printNumberedItems(ArrayList<String> itemList)
    {
        if (itemList.size() == 0)
        {
            System.out.println("   [The list is empty]");
        }
        else
        {
            for (int i = 0; i < itemList.size(); i++)
            {
                System.out.printf("   %d. %s%n", (i + 1), itemList.get(i));
            }
        }
    }

    /**
     * Add an item to the end of the list.
     * @param itemList the ArrayList of list items
     */
    private static void addItem(ArrayList<String> itemList)
    {
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to add");
        itemList.add(item);
        System.out.println("Added \"" + item + "\" to the end of the list.");
    }

    /**
     * Delete an item the user picks by its displayed number.
     * Converts the 1-based number into a 0-based index before removing.
     * @param itemList the ArrayList of list items
     */
    private static void deleteItem(ArrayList<String> itemList)
    {
        if (itemList.size() == 0)
        {
            System.out.println("The list is empty. There is nothing to delete.");
            return;
        }

        System.out.println("\nChoose an item to delete:");
        printNumberedItems(itemList);
        int itemNumber = SafeInput.getRangedInt(in, "Enter the item number to delete", 1, itemList.size());
        String removed = itemList.remove(itemNumber - 1);
        System.out.println("Deleted \"" + removed + "\".");
    }

    /**
     * Insert an item at a location the user picks.
     * The allowed range is 1 through size()+1 so the user can also
     * insert at the end. Converts the 1-based location to a 0-based index.
     * @param itemList the ArrayList of list items
     */
    private static void insertItem(ArrayList<String> itemList)
    {
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to insert");
        int location = SafeInput.getRangedInt(in, "Enter the position to insert at", 1, itemList.size() + 1);
        itemList.add(location - 1, item);
        System.out.println("Inserted \"" + item + "\" at position " + location + ".");
    }

    /**
     * Display the numbered list on its own (the Print menu option).
     * @param itemList the ArrayList of list items
     */
    private static void printList(ArrayList<String> itemList)
    {
        System.out.println();
        System.out.println("========== Your List ==========");
        printNumberedItems(itemList);
        System.out.println("===============================");
    }

    /**
     * Ask the user if they are sure they want to quit.
     * @return true if the user confirms, false otherwise
     */
    private static boolean quitProgram()
    {
        return SafeInput.getYNConfirm(in, "Are you sure you want to quit?");
    }

    /**
     * Saves the current list to a text file.
     */
    private static void saveList(ArrayList<String> itemList)
    {
        String fileName = SafeInput.getNonZeroLenString(in,
                "Enter the file name to save");

        if (!fileName.toLowerCase().endsWith(".txt"))
        {
            fileName += ".txt";
        }

        try
        {
            Path filePath = Path.of(fileName);
            Files.write(filePath, itemList);

            System.out.println("The list was successfully saved to "
                    + filePath.toAbsolutePath());
        }
        catch (IOException e)
        {
            System.out.println("Error saving the list: " + e.getMessage());
        }
    }

    /**
     * Opens a previously saved text file and loads its
     * contents into the current list.
     */
    private static void openList(ArrayList<String> itemList)
    {
        String fileName = SafeInput.getNonZeroLenString(in,
                "Enter the file name to open");

        if (!fileName.toLowerCase().endsWith(".txt"))
        {
            fileName += ".txt";
        }

        try
        {
            Path filePath = Path.of(fileName);

            ArrayList<String> loadedItems =
                    new ArrayList<>(Files.readAllLines(filePath));

            itemList.clear();
            itemList.addAll(loadedItems);

            System.out.println("The list was successfully loaded from "
                    + filePath.toAbsolutePath());
        }
        catch (IOException e)
        {
            System.out.println("Error opening the list: "
                    + e.getMessage());
        }
    }

}