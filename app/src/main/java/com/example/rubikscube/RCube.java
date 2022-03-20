package com.example.rubikscube;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RCube implements Serializable {
    private final ArrayList<long[][]> facesDecimals = new ArrayList<>();
    public int[][][] facesColors = new int[6][3][3];
    private final HashMap<String, Integer> facesMap = new HashMap<>();

    private final HashMap<String, int[][]> movesMap = new HashMap<>();
    private final HashMap<Integer, Character> tempMap = new HashMap<>();

    public RCube(){

        tempMap.put(0,'w');
        tempMap.put(1,'o');
        tempMap.put(2,'r');
        tempMap.put(3,'y');
        tempMap.put(4,'b');
        tempMap.put(5,'g');


        movesMap.put("F", new int[][]{{44,43,42},{11,14,17},{45,46,47},{24,21,18}});
        movesMap.put("U", new int[][]{{0,1,2},{18,19,20},{27,28,29},{9,10,11}});
        movesMap.put("R", new int[][]{{2,5,8},{47,50,53},{33,30,27},{38,41,44}});
        movesMap.put("L", new int[][]{{0,3,6},{36,39,42},{35,32,29},{45,48,51}});
        movesMap.put("D", new int[][]{{6,7,8},{15,16,17},{33,34,35},{24,25,26}});
        movesMap.put("B", new int[][]{{36,37,38},{20,23,26},{53,52,51},{15,12,9}});

        facesMap.put("F", 0);
        facesMap.put("U", 4);
        facesMap.put("R", 2);
        facesMap.put("L", 1);
        facesMap.put("D", 5);
        facesMap.put("B", 3);


    }



    public String getStringCube(){
        StringBuilder s = new StringBuilder();
        int[] facesOrder = new int[]{4,2,0,5,1,3};
        for(int face : facesOrder){
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    switch (facesColors[face][i][j]){
                        case 0:
                            s.append("F");
                            break;
                        case 1:
                            s.append("L");
                            break;
                        case 2:
                            s.append("R");
                            break;
                        case 3:
                            s.append("B");
                            break;
                        case 4:
                            s.append("U");
                            break;
                        case 5:
                            s.append("D");
                            break;
                    }
                }
            }
        }

        return s.toString();
    }

    public void addFace(long [][] face){
        facesDecimals.add(face);
    }


    public void configureColors(){

        for(int c=0;c<facesDecimals.size();c++){
            long center = facesDecimals.get(c)[1][1];
            for(int f=0;f<facesDecimals.size();f++){
                for(int i=0;i<3;i++){
                    for(int j=0;j<3;j++){
                        if(colorsAreSimilar(facesDecimals.get(f)[i][j], center, 50)){
                            facesColors[f][i][j] = c;
                        }
                    }
                }
            }
        }
    }
    private boolean colorsAreSimilar(long x, long y, int threshold){
        int xr = (int)x >> 16;
        int xg = ((int)x - (xr << 16)) >> 8;
        int xb = ((int)x - (xr << 16) - (xg << 8));

        int yr = (int)y >> 16;
        int yg = ((int)y - (yr << 16)) >> 8;
        int yb = ((int)y - (yr << 16) - (yg << 8));

        int r = xr - yr;
        int g = xg - yg;
        int b = xb - yb;
        return (r*r + g*g + b*b) <= threshold*threshold;
    }

    public void printFacesColors(){
        char[][][] temp = new char[6][3][3];
        for(int face=0;face<6;face++){
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    temp[face][i][j] = tempMap.get(facesColors[face][i][j]);
                }
            }
        }
        Log.d("tag",Arrays.deepToString(temp));
    }



    private void rotate90Clockwise(int[][] a)
    {
        for (int j = 0; j < 2; j++)
        {
            int temp = a[0][j];
            a[0][j] = a[2 - j][0];
            a[2 - j][0] = a[2][2 - j];
            a[2][2 - j] = a[j][2];
            a[j][2] = temp;
        }
    }

    public void makeMove(String m){
        if(m.equals("")) return;

        switch (m) {
            case "F":
            case "U":
            case "R":
            case "L":
            case "B":
            case "D":
                move(m);
                break;
            case "F2":
            case "U2":
            case "R2":
            case "L2":
            case "B2":
            case "D2":
                move(m.substring(0, 1));
                move(m.substring(0, 1));
                break;
            case "F'":
            case "U'":
            case "R'":
            case "L'":
            case "B'":
            case "D'":
                move(m.substring(0, 1));
                move(m.substring(0, 1));
                move(m.substring(0, 1));
                break;
        }
    }
    public void makeMoveReverse(String m){
        if(m.equals("")) return;

        switch (m){
            case "F":
                makeMove("F'");
                break;
            case "F'":
                makeMove("F");
                break;

            case "R":
                makeMove("R'");
                break;
            case "R'":
                makeMove("R");
                break;

            case "L":
                makeMove("L'");
                break;
            case "L'":
                makeMove("L");
                break;

            case "U":
                makeMove("U'");
                break;
            case "U'":
                makeMove("U");
                break;

            case "D":
                makeMove("D'");
                break;
            case "D'":
                makeMove("D");
                break;

            case "B":
                makeMove("B'");
                break;
            case "B'":
                makeMove("B");
                break;

            case "F2":
            case "U2":
            case "R2":
            case "L2":
            case "B2":
            case "D2":
                makeMove(m);
                break;
        }

    }

    private void move(String m){
        int[][] swaps = movesMap.get(m);
        assert swaps != null;
        int a = getVal(swaps[0][0]);
        int b = getVal(swaps[0][1]);
        int c = getVal(swaps[0][2]);

        for(int i=0;i<3;i++){
            copyValFromIndex(swaps[i][0], swaps[i+1][0]);
            copyValFromIndex(swaps[i][1], swaps[i+1][1]);
            copyValFromIndex(swaps[i][2], swaps[i+1][2]);
        }
        copyValFromValue(swaps[3][0], a);
        copyValFromValue(swaps[3][1], b);
        copyValFromValue(swaps[3][2], c);

        rotate90Clockwise(facesColors[facesMap.get(m)]);

    }

    private void copyValFromValue(int a, int v) {
        int face = a/9;
        a -= (face * 9);
        int i = a/3;
        int j = a%3;

        facesColors[face][i][j] = v;
    }

    private void copyValFromIndex(int a, int b){
        int face1 = a/9;
        a -= (face1 * 9);
        int i1 = a/3;
        int j1 = a%3;

        int face2 = b/9;
        b -= (face2 * 9);
        int i2 = b/3;
        int j2 = b%3;

        facesColors[face1][i1][j1] = facesColors[face2][i2][j2];
    }
    private int getVal(int a){
        int face = a/9;
        a -= (face * 9);
        int i = a/3;
        int j = a%3;
        return facesColors[face][i][j];
    }


}
