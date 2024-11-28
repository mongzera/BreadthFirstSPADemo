package com.chemicaldev.bfsspademo;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {

    /*
     * Breadth First Search Shortest Path Algorithm Implementation
     * Author: Ethan Van Q. Gamat BSCS 2 Notre Dame of Dadiangas University
     *
     * For Educational Purposes
     *
     */
    public static float DT = 0;
    public static final String TITLE = "BREADTH FIRST SEARCH SHORTEST PATH DEMO";
    public Main(){
        Canvas canvas = new Canvas();
        Canvas.WIDTH = (int) (1024 * 1f);
        Canvas.HEIGHT = (int) (720 * 1f);
        canvas.resize();

        JFrame frame = new JFrame(TITLE);
        frame.setSize(Canvas.WIDTH, Canvas.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setFocusable(true);

        frame.add(canvas);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.getContentPane().addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Canvas.WIDTH = e.getComponent().getWidth();
                Canvas.HEIGHT = e.getComponent().getHeight();
                canvas.resize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        float FPS = 60;
        float timePerFrame = 1f / FPS;
        long startTime = System.nanoTime();
        float deltaCounter = 0.0f;

        //game loop
        while(true){
            if(deltaCounter >= timePerFrame){
                DT = deltaCounter;
                canvas.repaint();
                deltaCounter -= timePerFrame;

            }
            long endTime = System.nanoTime();

            deltaCounter += (endTime - startTime) / 1000000000F;
            startTime = endTime;

            frame.setTitle(TITLE + " FPS:" + Float.toString(1.0f / DT));

        }
    }
    public static void main(String[] args) {
        new Main();
    }
}