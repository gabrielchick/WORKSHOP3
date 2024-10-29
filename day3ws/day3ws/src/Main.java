import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // To Run It:
    // javac -d bin src/*.java
    // javac -d classes src/*.java
    // java -cp classes Main
    // java -cp classes Main cartdb

    // jar cfm shoppingcart.jar Manifest.mf -C classes/ .
    // To check: jar tf shoppingcart.jar
    // java shoppingcart.jar cartdb
    // java shoppingcart.jar
    public static void main(String[] args) {
        // args arguments
        // for (String s : args) {
        // System.out.println(s);
        // }
        ShoppingCartDB cardDB;
        if (args.length > 0) {
            cardDB = new ShoppingCartDB(args[0]); // hit into parametrize constructor
        } else {
            cardDB = new ShoppingCartDB(); // hit into default constructor
        }

        cardDB.startShopping();

        // cardDB.displayCartStats();
        // startShopping();
    }

}