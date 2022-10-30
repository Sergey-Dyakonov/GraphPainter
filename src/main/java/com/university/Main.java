package com.university;

import com.university.controller.SimpleGraphController;
import com.university.view.UI;

import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        UI ui = new UI(new SimpleGraphController());
        ui.showMainFrame();
    }
}