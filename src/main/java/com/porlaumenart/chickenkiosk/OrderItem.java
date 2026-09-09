package com.porlaumenart.chickenkiosk;

/** 주문에 포함된 메뉴와 수량을 표현합니다. */
public class OrderItem {
    private final MenuItem menuItem;
    private final int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSubtotal() {
        return menuItem.getPrice() * quantity;
    }
}
