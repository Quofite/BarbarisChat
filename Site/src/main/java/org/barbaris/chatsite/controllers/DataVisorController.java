package org.barbaris.chatsite.controllers;

public class DataVisorController {
    public static boolean isDataValid(String login, String pass) {
        return login != null && pass != null && !login.equals("") && !pass.equals("") && !login.contains(" ")
                && !pass.contains(" ") && !login.isBlank() && !pass.isBlank() && !login.isEmpty() &&
                !pass.isEmpty();
    }
}
