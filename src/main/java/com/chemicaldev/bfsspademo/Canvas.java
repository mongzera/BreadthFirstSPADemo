package com.chemicaldev.bfsspademo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Canvas extends JPanel {
    class Node{
        public int x, y;
        public Node parent;
        public boolean isSolid = false;
    }

    public static int WIDTH, HEIGHT;
    private int rows = 140;
    private int cols = 140;
    private final int gridSize = 5;
    private final boolean isFast = true;

    private Node map[][] = new Node[rows][cols];
    private Queue<Node> nodeQueue = new Queue<>();
    private ArrayList<Node> path = new ArrayList<>();

    private boolean foundPath = false;

    public boolean addWallsToggle = false;

    private float elapsedTime = 0f;

    private boolean fastSearch = false;

    public void reset(){
        map = new Node[rows][cols];

        Random random = new Random();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                map[i][j] = new Node();
                map[i][j].y = i;
                map[i][j].x = j;
                if(random.nextInt(15) > 10) map[i][j].isSolid = true;
            }
        }

        nodeQueue.clear();
        path.clear();
        foundPath = false;
        addWallsToggle = false;
        elapsedTime = 0f;
        fastSearch = isFast;
        State.startNode = null;
        State.endNode = null;

    }

    public Canvas(){
        reset();

        this.setFocusable(true);
        //this.requestFocus();


        //create walls
//        for(int i = 0; i < 5; i++){
//            map[i+7][3].isSolid = true;
//        }
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

                if(e.getKeyChar() == 's') {
                    addWallsToggle = !addWallsToggle;
                    System.out.println("WALL TOGGLE: " + addWallsToggle);
                }

                if(e.getKeyChar() == 'r') {
                    System.out.println("RESET!");
                    reset();
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(addWallsToggle) return;

                int x = (int)Math.floor(e.getX() / gridSize);
                int y = (int)Math.floor(e.getY() / gridSize);

                if(State.startNode == null){
                    System.out.println("SET START >> X: " + x + ", Y: " + y);
                    State.startNode = map[y][x];
                    State.startX = x;
                    State.startY = y;
                    return;
                }

                if(State.startNode != null && State.endNode == null){
                    if(State.startNode == map[y][x]) return;
                    System.out.println("SET END >> X: " + x + ", Y: " + y);
                    State.endNode = map[y][x];
                    State.endX = x;
                    State.endY = y;
                    nodeQueue.add(State.startNode);


                    if(fastSearch && State.startNode != null && State.endNode != null){

                        while (nodeQueue.size() > 0 && !foundPath){
                            Node currentNode = nodeQueue.deque();
                            addNeighbor(currentNode);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = (int)Math.floor(e.getX() / gridSize);
                int y = (int)Math.floor(e.getY() / gridSize);

                if(addWallsToggle){
                    map[y][x].isSolid = true;

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.requestFocusInWindow();

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;

        //clear screen
        g.clearRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        drawGrid(g);

        if(foundPath && path.size() < 1){
            //System.out.println("PRINTING");
            calculatePath(State.endNode);
        }

        if(!fastSearch && State.startNode != null && State.endNode != null){

            if (nodeQueue.size() > 0 && !foundPath){
                Node currentNode = nodeQueue.deque();
                addNeighbor(currentNode);
            }
        }

        //print path
        if(path.size() > 0){
            if(!fastSearch){
                elapsedTime += Main.DT;
                for(int i = 0; i < ((int)(elapsedTime * 30) % (path.size()+1)); i++){

                    Node current = path.get(path.size() - i - 1);
                    g.setColor(Color.BLUE);
                    g.fillRect(current.x * gridSize, current.y * gridSize, gridSize, gridSize);
                    if(i == path.size()-1) fastSearch = true;
                }
            }else{
                for(int i = 0; i < path.size(); i++){
                    Node current = path.get(path.size() - i - 1);
                    g.setColor(Color.BLUE);
                    g.fillRect(current.x * gridSize, current.y * gridSize, gridSize, gridSize);
                }
            }
        }

    }

    public void calculatePath(Node current){
//        g.setColor(Color.BLUE);
//        g.fillRect(current.x * gridSize, current.y * gridSize, gridSize, gridSize);

        //return;
        if(current.parent != null && current.parent != State.startNode){
            path.add(current.parent);
            calculatePath(current.parent);
        }
    }

    public void addNeighbor(Node current){
        if(current == null) return;

        //check neighbors
        Node up = isCellValid(current.x, current.y-1);
        Node down = isCellValid(current.x, current.y+1);
        Node left = isCellValid(current.x-1, current.y);
        Node right = isCellValid(current.x+1, current.y);

        if(left != null){
            left.parent = current;
            nodeQueue.add(left);
        }

        if(right != null){
            right.parent = current;
            nodeQueue.add(right);
        }

        if(up != null){
            up.parent = current;
            nodeQueue.add(up);
        }

        if(down != null){
            down.parent = current;
            nodeQueue.add(down);
        }



        if(up == State.endNode || down == State.endNode || left == State.endNode || right == State.endNode) {
            foundPath = true;
        }
    }

    public Node isCellValid(int x, int y){
        if(x >= 0 && x < cols) if(y >= 0 && y < rows) if(!map[y][x].isSolid && map[y][x].parent == null) return map[y][x];
        return null;
    }

    private void drawGrid(Graphics2D g){
        g.setColor(Color.RED);
        for(int i = 0; i <= rows; i++){
            g.drawLine(0, i * gridSize, cols * gridSize, i * gridSize);
        }

        for(int j = 0; j <= cols; j++){
            g.drawLine(j * gridSize, 0, j * gridSize, rows * gridSize);
        }

        //print solid blocks
        for(int y = 0; y < rows; y++){
            for(int x = 0; x < cols; x++){
                if(map[y][x].isSolid){
                    g.setColor(Color.RED);
                    g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
                }
                if(map[y][x].parent != null){
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
                }


            }
        }

//        for(Node e : nodeQueue.objects){
//
//        }

        //if start node exists
        if (State.startNode != null) {
            g.setColor(Color.GREEN);
            g.fillRect(State.startX * gridSize, State.startY * gridSize, gridSize, gridSize);
        }

        //if end node exists
        if (State.endNode != null) {
            g.setColor(Color.WHITE);
            g.fillRect(State.endX * gridSize, State.endY * gridSize, gridSize, gridSize);
        }
//        for(int i = 0; i <= rows; i++){
//            for(int j = 0; j <= cols; j++){
//                if(map[i][j])
//                g.drawLine(j * gridSize, 0, j * gridSize, cols * gridSize);
//            }
//        }
    }
}
