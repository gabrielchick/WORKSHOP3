import java.util.ArrayList;

public class ShoppingCart {
    // member variable, we dont want to expose
    private ArrayList<String> cart;

    // Default Constructor (you can make private if don't want user to construct
    // default)
    public ShoppingCart() {
        cart = new ArrayList<String>(); // empty list
    }

    /*
     * 
     * public ShoppingCart(ArrayList<String> items) {
     * // this happens to Classes.
     * // shallow copy, items and cart is same arraylist (in terms of memory)
     * // cart = items;
     * 
     * // deep copy, reconstruct and make new copy of items.
     * cart = new ArrayList<>(items);
     * }
     */

    // Parametrize constructor
    public ShoppingCart(ArrayList<String> cart) {
        // 'this.cart' belongs to this object
        // 'cart' belong to whoever constructing a shopping cart
        this.cart = new ArrayList<>(cart);
    }

    public void addCart(String item) {
        cart.add(item);
    }

    public void clearCart() {
        cart.clear();
    }

    public String removeCart(int itemNumber) {
        int indexToRemove = itemNumber - 1;
        if (indexToRemove >= 0 && indexToRemove < cart.size()) {
            String removeItem = cart.remove(indexToRemove);
            // return removeItem + " reoved from cart";
            return removeItem;
        }
        // return "Incorrect item index";
        return null;
    }

    ArrayList<String> getCart() {
        return cart;
    }

    public String getFileString() {
        String output = "";
        for (String item : cart) {
            output += item + "\n";
        }
        return output.substring(0, output.length() - 1);
    }

    // All class in java inherit from base Class
    @Override
    public String toString() {
        String output = "";
        if (cart.isEmpty()) {
            return "Your cart is empty";
        }
        for (int i = 0; i < cart.size(); ++i) {
            output += (i + 1) + ". " + cart.get(i) + "\n";
            // if (i < cart.size() - 1) {
            // output += "\n";
            // }
        }

        return output.substring(0, output.length() - 1);
    }
}
