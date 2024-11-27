package com.chemicaldev.bfsspademo;

import java.util.ArrayList;

public class Queue<T> {
    public ArrayList<T> objects = new ArrayList<>();

    public Queue(){

    }

    public void add(T element){
        objects.add(element);
    }

    public T deque(){
        if(size() <= 0){
            return null;
        }

        T object = objects.get(0);
        objects.remove(0);
        return object;
    }

    public void clear(){
        objects.clear();
    }

    public int size(){
        return objects.size();
    }

    public String toString(){
        String output = "";

        for(T t : objects){
            output += t.toString() + " >> ";
        }

        return output;
    }
}
