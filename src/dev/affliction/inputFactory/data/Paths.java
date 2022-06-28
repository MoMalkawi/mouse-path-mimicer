package dev.affliction.inputFactory.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Paths {

    public static Map<int[],Double> STANDARD_MOVEMENTS = new LinkedHashMap<>();
    public static Map<int[],Double> SLOWER_ANGULAR_MOVEMENTS = new LinkedHashMap<>();
    public static Map<int[],Double> RAPID_MOVEMENTS = new LinkedHashMap<>();

    public static Map<int[],Double> STANDARD_NOISY_DRAGS = new LinkedHashMap<>();
    public static Map<int[],Double> RAPID_DRAGS = new LinkedHashMap<>();

    public static double longestDistanceFromOrigin;

    //public double interPointTotalDistanceToLongestPoint;

    //public int longestDistanceFromOriginIndex;

    public static List<int[]> pointsList = null; //x, y, timeToPoint(millis)

}
