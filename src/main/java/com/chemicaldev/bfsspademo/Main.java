package com.chemicaldev.bfsspademo;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
    public static float DT = 0;
    public Main(){
        Canvas canvas = new Canvas();
        Canvas.WIDTH = (int) (1024 * 1.5f);
        Canvas.HEIGHT = (int) (720 * 1.5f);

        JFrame frame = new JFrame("BREADTH FIRST SEARCH SHORTEST PATH DEMO");
        frame.setSize(Canvas.WIDTH, Canvas.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setFocusable(true);

        frame.add(canvas);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        long startTime = System.nanoTime();
        //game loop
        while(true){
            canvas.repaint();
            long endTime = System.nanoTime();
            DT = (endTime - startTime) / 1000000000F;
            startTime = endTime;
        }
    }
    public static void main(String[] args) {
        new Main();
    }
}