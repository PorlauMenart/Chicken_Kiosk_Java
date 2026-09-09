package com.porlaumenart.chickenkiosk;

/** 치킨 키오스크의 메뉴 정보를 표현하는 모델 클래스입니다. */
public class MenuItem {
    private final String name;
    private final int price;
    private final String imageFile;
    private final String category;

    public MenuItem(String name, int price, String imageFile, String category) {
        this.name = name;
        this.price = price;
        this.imageFile = imageFile;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getCategory() {
        return category;
    }
}
