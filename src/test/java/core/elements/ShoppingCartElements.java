package core.elements;

import org.openqa.selenium.By;
/**
 * Locators for Shopping Cart page:
 * cart table, totals, and empty state messages.
 */
public class ShoppingCartElements {

    // ===== Cart Table =====
    public static final By CART_ROWS =
            By.cssSelector("table#shopping-cart-table tbody tr");


    public static final By ROW_QTY_INPUT =
            By.cssSelector("input.qty"); // Qty input inside a single row


    public static final By ROW_SUBTOTAL_PRICE =
            By.cssSelector("td.product-cart-total span.price"); // Line subtotal

    // ===== Totals & Actions =====
    public static final By UPDATE_SHOPPING_CART_BUTTON =
            By.cssSelector("button[title='Update Shopping Cart']");

    // Grand Total në box-in e djathtë
    public static final By GRAND_TOTAL_PRICE =
            By.cssSelector("#shopping-cart-totals-table tfoot tr.last span.price");



    public static final By FIRST_ROW_REMOVE_LINK =
            By.cssSelector("table#shopping-cart-table tbody tr:first-child td.product-cart-remove a.btn-remove");


    // ===== Empty State =====
    public static final By EMPTY_CART_TITLE =
            By.cssSelector("div.page-title h1"); // e.g., "Shopping Cart is Empty"

    // 🔹 Mesazhi “You have no items in your shopping cart.”
    public static final By EMPTY_CART_MESSAGE =
            By.cssSelector("div.cart-empty p");// "You have no items in your shopping cart."
}
