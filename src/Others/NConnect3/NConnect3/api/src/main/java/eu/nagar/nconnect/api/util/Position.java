/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.util;

public class Position {
    private int mouseX, mouseY;

    public Position(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public int getX() {
        return mouseX;
    }

    public int getY() {
        return mouseY;
    }

    public void setX(int mouseX) {
        this.mouseX = mouseX;
    }

    public void setY(int mouseY) {
        this.mouseY = mouseY;
    }

}
