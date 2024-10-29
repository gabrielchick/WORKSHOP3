import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ShoppingCartDB {
    // ShoppingCart is associated with ShoppingCartDB
    // int number;
    private ShoppingCart cart;
    private String folder;
    private File directory;
    private File userFile;
    private boolean isLogin = false;

    // default constructor
    public ShoppingCartDB() {
        this.folder = "db";
        generateFolder();
    }

    // Parametrize constructor
    public ShoppingCartDB(String folder) {
        this.folder = folder;
        generateFolder();
    }

    public void displayCartStats() {
        System.out.println("Folder: " + this.folder);
    }

    private void generateFolder() {
        directory = new File(this.folder);
        // check if directory exist or not
        if (!directory.exists()) {
            // directory.mkdirs() <- mkdirs means make directories. creates the folder
            if (directory.mkdirs()) {
                System.out.println(this.folder + " Directory created successfully!");
            } else {
                System.out.println("Failed to create the directory.");
            }
        } else {
            System.out.println(this.folder + " Directory exist.");
        }
    }

    public void saveCart() {
        // Write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
            // method 1 - better method
            ArrayList<String> cartList = cart.getCart();
            for (int i = 0; i < cartList.size(); ++i) {
                writer.write(cartList.get(i));
                if (i < cartList.size() - 1) {
                    writer.newLine();
                }

            }
            // method 2 - cannot handle when the file damn big.
            // writer.write(cart.getFileString());

            System.out.println("Successfully save to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
            logout();
        }
    }

    private void printArray(String[] arr) {
        for (String s : arr) {
            System.out.println(s);
        }
    }

    private void logout() {
        System.out.println("Logging out user");
        cart.clearCart();
        userFile = null;
        isLogin = false;

    }

    private void loginFeature(String firstToken, String secondPart) {
        if (firstToken.equals("list")) {
            System.out.println(cart);
        } else if (firstToken.equals("add")) {
            // "orange, pear, lemon".split(", ") => ["orange", "pear", "lemon"]
            String tokens[] = secondPart.split(", ");

            // for range, for each loop
            for (String item : tokens) {
                cart.addCart(item);
            }

        } else if (firstToken.equals("delete")) {
            // converting string to number.
            int removeIndex = Integer.parseInt(secondPart);
            String removedItem = cart.removeCart(removeIndex);
            if (removedItem != null) {
                System.out.println(removedItem + " removed from cart");
            } else {
                System.out.println("Incorrect item index");
            }
        } else if (firstToken.equals("logout")) {
            logout();
        } else if (firstToken.equals("save")) {
            saveCart();
        } else {
            System.out.println("Unknown command try again!");
        }
    }

    private void printFilesInDirectory() {
        // System.out.println("printFilesInDirectory");
        if (directory.isDirectory()) {
            // System.out.println("printFilesInDirectory isDirectory");
            // Get the list of files and directories inside the directory
            File[] files = directory.listFiles();

            // Check if files is not null
            if (files != null && files.length >= 1) {
                int userNumber = 1;
                for (File file : files) {
                    // Print the file or directory name
                    if (file.isFile()) {
                        // file.getName() -> "fred.db"
                        // "fred.db".split(".") -> ["fred", "db"]
                        String filename = file.getName();
                        String[] fileData = filename.split("\\.");
                        System.out.println(userNumber + ". " + fileData[0]);
                        userNumber += 1;
                    }
                    // Print the folder name inside this folder.
                    // else if (file.isDirectory()) {
                    // System.out.println("Directory: " + file.getName());
                    // }
                }
            } else {
                System.out.println("No users in db!");
            }
        } else {
            System.out.println("The specified path is not a directory.");
        }
    }

    private void loginUser(String username) {
        // db/username.db
        // cartdb/username.db
        String filename = folder + "/" + username + ".db";
        userFile = new File(filename);

        // check if directory exist or not
        if (!userFile.exists()) {
            try {
                // If the file doesn't exist, create a new file
                if (userFile.createNewFile()) {
                    System.out.println("File created successfully: " + userFile.getName());
                    System.out.println("User login successfully.");
                    cart = new ShoppingCart();
                    isLogin = true;
                } else {
                    System.out.println("File could not be created.");
                    logout();
                }
            } catch (IOException e) {
                // Handle any IO exceptions
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
                logout();
            }
        } else {

            // load db
            try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                System.out.println("User login successfully.");
                cart = new ShoppingCart();
                isLogin = true;
                String line;
                while ((line = br.readLine()) != null) {
                    // "apple \n" "apple " " apple "
                    // trim remove extra space or new line
                    // file reading and loading into custom class.
                    System.out.println(line);
                    cart.addCart(line.trim());
                }
            } catch (IOException e) {
                logout();
                System.out.println("An error occurred while reading the file.");
                e.printStackTrace();
            }
        }
    }

    private boolean guestFeature(String firstToken, String secondPart) {
        // System.out.println(firstToken);
        boolean isOk = false;
        if (firstToken.equals("users")) {
            printFilesInDirectory();
            isOk = true;
        } else if (firstToken.equals("login")) {
            if (secondPart.isEmpty()) {
                System.out.println("Please enter your username when login. e.g 'login fred'");
            } else {
                loginUser(secondPart);
            }
            isOk = true;
        } else if (!isLogin) {
            System.out.println("Unknown command or login and try again !");
        }
        return isOk;
    }

    public void startShopping() {
        Scanner scanner = new Scanner(System.in);
        // list, add, delete, exit
        // all program is a loop
        // while (true) means infinite, inside the loop can break to stop.
        System.out.println("Welcome to your shopping cart");
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            // substring can extract just the first one.
            int firstSpaceIndex = command.indexOf(' ', 0);
            // "add orange, pear"
            String firstToken = command;
            String secondPart = "";
            if (firstSpaceIndex != -1) {
                // add
                firstToken = command.substring(0, firstSpaceIndex);
                // take the rest, +1 to skip the first spacebar.
                secondPart = command.substring(firstSpaceIndex + 1);
            }

            if (firstToken.equals("exit")) {
                System.out.println("Come back and shop again! Bye!");
                break;
            } else if (isLogin) {
                // during login do this.
                System.out.println("Showing login mode");
                boolean isTriggered = guestFeature(firstToken, secondPart); // can login another user, can see other
                                                                            // users.
                if (!isTriggered) {
                    loginFeature(firstToken, secondPart);
                }

            } else {
                System.out.println("Showing guest mode");
                // not login, but still can exit app. only cannot do delete, add list
                guestFeature(firstToken, secondPart);
            }
        }
    }
}
