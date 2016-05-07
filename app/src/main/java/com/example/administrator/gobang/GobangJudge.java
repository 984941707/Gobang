package com.example.administrator.gobang;

import android.graphics.Point;

import java.util.List;

/**
 * User: Administrator
 * Time: 2016/5/7 11:02 02
 * Annotation:
 */
public class GobangJudge {
    public static int MAX_COUNT_IN_LINE = 5;

    public static boolean checkFiveInLine (List<Point> points) {
        for(Point p : points) {
            int x = p.x;
            int y = p.y;

            boolean win = checkHorizontal(x, y, points);
            if (win) {
                return true;
            }
            win = checkVertical (x, y, points);
            if (win) {
                return true;
            }
            win = checkLeftDiagonal (x, y, points);
            if (win) {
                return true;
            }
            win = checkRightDiagonal (x, y, points);
            if (win) {
                return true;
            }
        }
        return false;
    }

    //判断棋子是否有横向的五个棋子
    public static boolean checkHorizontal (int x, int y, List<Point> points) {

        int count = 1;
        //向左
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if(points.contains (new Point (x - i, y))) {
                count++;
            }else{
                break;
            }
        }

        //向右
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if(points.contains (new Point (x + i, y))) {
                count++;
            }else{
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    //判断棋子是否有纵向的五个棋子
    public static boolean checkVertical (int x, int y, List<Point> points) {

        int count = 1;
        //向上
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if(points.contains (new Point (x, y-i))) {
                count++;
            }else{
                break;
            }
        }

        //向下
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if(points.contains (new Point (x, y + i))) {
                count++;
            }else{
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    public static boolean checkLeftDiagonal (int x, int y, List<Point> points) {

        int count = 1;
        //向左上下
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if(points.contains (new Point (x - i, y + i))) {
                count++;
            }else{
                break;
            }
        }

        //向右上
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if(points.contains (new Point (x + i, y - i))) {
                count++;
            }else{
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    public static boolean checkRightDiagonal (int x, int y, List<Point> points) {

        int count = 1;
        //向左上
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if(points.contains (new Point (x - i, y - i))) {
                count++;
            }else{
                break;
            }
        }

        //向右下
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if(points.contains (new Point (x + i, y + i))) {
                count++;
            }else{
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }
}
