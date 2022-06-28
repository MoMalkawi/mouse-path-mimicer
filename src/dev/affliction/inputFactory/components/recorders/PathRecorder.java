package dev.affliction.inputFactory.components.recorders;

import dev.affliction.inputFactory.components.mouse.Mouse;
import dev.affliction.inputFactory.data.Paths;
import dev.affliction.inputFactory.utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PathRecorder {

    public static long previousTime = 0;

    public static void registerPath(MouseEvent e) {
        if(Paths.pointsList == null) {
            Paths.pointsList = new ArrayList<>();
            Paths.pointsList.add(new int[] {e.getX(),e.getY(), Utils.rand(2,15)});
            previousTime = System.currentTimeMillis();
            return;
        }
        Paths.pointsList.add(new int[] {e.getX(),e.getY(),
                (int) (System.currentTimeMillis() - previousTime)});
        previousTime = System.currentTimeMillis();
    }

    public static void finalizePath() {
        double largestDistance = 0;
        //int largestDistanceIndex = 0;
        int indexSize = Paths.pointsList.size() - 1;
        int[] rawStartingPoint = null;
        //double totalInterPointDistance = 0;
        for(int i = indexSize; i > 0; i--) {
            if(rawStartingPoint == null) rawStartingPoint = Paths.pointsList.get(1);
            int[] point = Paths.pointsList.get(i);
            Point current = new Point(point[0],point[1]);
            double currentToStart = current.distance(rawStartingPoint[0],rawStartingPoint[1]);
            if(currentToStart > largestDistance) {
                largestDistance = currentToStart;
                //largestDistanceIndex = i;
                //totalInterPointDistance = 0;
            }
            /*if(i != 0) {
                int[] prev = HumanPath.current.points.get(i-1);
                double distanceFromPrev = current.distance(prev[0],prev[1]);
                totalInterPointDistance += distanceFromPrev;
            }*/
        }
        Paths.longestDistanceFromOrigin = largestDistance;
        //HumanPath.current.longestDistanceFromOriginIndex = largestDistanceIndex;
        //HumanPath.current.interPointTotalDistanceToLongestPoint = totalInterPointDistance;
    }

    public static void play(int[] path, Point target, Point start, int eventType) {
        Point pathStart = new Point(path[0], path[1]);
        double wantedDistance = start.distance(target);
        int chosenIndex = getApplicable(path,pathStart,wantedDistance);
        Point chosen = new Point(path[chosenIndex],path[chosenIndex+1]);
        double pathDistanceStartEnd = pathStart.distance(chosen);
        boolean xDominant = (chosen.x - pathStart.x) > (chosen.y - pathStart.y);
        int latestDistance = (int)pathDistanceStartEnd;
        int rand = Utils.rand(0,chosenIndex+1);

        for(int i = 0; i < path.length-3; i+=3) {
            Point p = null;
            for (int k = latestDistance; k >= 0; k--) {
                double T = k / pathDistanceStartEnd;
                int cX = (int) ((1 - T) * chosen.x + T * pathStart.x);
                int cY = (int) ((1 - T) * chosen.y + T * pathStart.y);
                Point cP = new Point(cX, cY);
                if ((xDominant && cP.x == path[i]) || (!xDominant && cP.y == path[i+1])) {
                    p = cP;
                    latestDistance = k;
                    break;
                }
            }
            if (p != null) {
                double T = latestDistance / wantedDistance;
                int rX = (int) ((1 - T) * target.x + T * start.x) + (p.x - path[i]);
                int rY = (int) ((1 - T) * target.y + T * start.y) + (p.y - path[i+1]);
                if(i == chosenIndex) {
                    executeMovement(target.x,target.y,path[i+2],eventType);
                    break;
                }
                executeMovement(rX,rY,path[i+2],eventType);

                //noise

                if(rand == i) {
                    int deviation = Utils.rand(-3,4);
                    if(deviation != 0) {
                        int[] addition = new int[]{rX + deviation, rY + deviation, Utils.rand(2, 6)};
                        executeMovement(addition[0],addition[1],addition[2],eventType);
                        executeMovement(addition[0] - deviation,addition[1] - deviation,Utils.rand(2, 6),eventType);
                    }
                    rand = Utils.rand(rand+1, chosenIndex+1); //TODO: random value may be Y
                }
            }
        }
    }

    /*
    public static void play(int[][] path, Point target, Point start, int eventType) { //2D ARR METHOD
        Point pathStart = new Point(path[0][0], path[0][1]);
        double wantedDistance = start.distance(target);
        int chosenIndex = getApplicable(path,pathStart,wantedDistance);
        Point chosen = new Point(path[chosenIndex][0],path[chosenIndex][1]);
        double pathDistanceStartEnd = pathStart.distance(chosen);
        boolean xDominant = (chosen.x - pathStart.x) > (chosen.y - pathStart.y);
        int latestDistance = (int)pathDistanceStartEnd;
        int count = 0;
        int rand = Utils.rand(0,chosenIndex+1);

        for(int[] movement : path) {
            Point p = null;
            for (int k = latestDistance; k >= 0; k--) {
                double T = k / pathDistanceStartEnd;
                int cX = (int) ((1 - T) * chosen.x + T * pathStart.x);
                int cY = (int) ((1 - T) * chosen.y + T * pathStart.y);
                Point cP = new Point(cX, cY);
                if ((xDominant && cP.x == movement[0]) || (!xDominant && cP.y == movement[1])) {
                    p = cP;
                    latestDistance = k;
                    break;
                }
            }
            if (p != null) {
                double T = latestDistance / wantedDistance;
                int rX = (int) ((1 - T) * target.x + T * start.x) + (p.x - movement[0]);
                int rY = (int) ((1 - T) * target.y + T * start.y) + (p.y - movement[1]);
                if(count == chosenIndex) {
                    executeMovement(target.x,target.y,movement[2],eventType);
                    break;
                }
                executeMovement(rX,rY,movement[2],eventType);

                //noise

                if(rand == count) {
                    int deviation = Utils.rand(-3,4);
                    if(deviation != 0) {
                        int[] addition = new int[]{rX + deviation, rY + deviation, Utils.rand(2, 6)};
                        executeMovement(addition[0],addition[1],addition[2],eventType);
                        executeMovement(addition[0] - deviation,addition[1] - deviation,Utils.rand(2, 6),eventType);
                    }
                    rand = Utils.rand(rand+1, chosenIndex+1);
                }
            }
            count++;
        }
    }*/

    private static void executeMovement(int x, int y, int time, int eventType) {
        System.out.println("Moving to: X: "+x+" Y: "+y);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mouse.event(x,y,System.currentTimeMillis(),eventType);
    }

    private static int getApplicable(int[] path, Point pathStart, double wantedDistance) {
        int chosenIndex = 0;
        double currentDistance = 0;
        int fromIndexSize = path.length - 1;
        for(int i = fromIndexSize; i >= 2; i-=3) {
            double distance = pathStart.distance(path[i-2],path[i-1]);
            if(distance >= wantedDistance && (i == fromIndexSize || distance < currentDistance)) {
                currentDistance = distance;
                chosenIndex = i-2;
            }
        }
        return chosenIndex;
    }

    /*private static int getApplicable(int[][] path, Point pathStart, double wantedDistance) { //2D ARR METHOD
        int chosenIndex = 0;
        double currentDistance = 0;
        int fromIndexSize = path.length - 1;
        for(int i = fromIndexSize; i >= 0; i--) {
            double distance = pathStart.distance(path[i][0],path[i][1]);
            if(distance >= wantedDistance && (i == fromIndexSize || distance < currentDistance)) {
                currentDistance = distance;
                chosenIndex = i;
            }
        }
        return chosenIndex;
    }*/

    /*public static void playTowardsTarget(HumanPath path, int startX, int startY,
                                         int targetX, int targetY, int eventType) {
        HumanPath generated = generateApplicable(path,new Point(startX, startY), new Point(targetX,targetY));
        if(generated != null) {
            int count = 0;
            for(int[] index : generated.points) {
                System.out.println(count+": "+Arrays.toString(index));
                count++;
            }
            for (int[] point : generated.points) {
                if(point == null) continue;
                try {
                    Thread.sleep(point[2]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Mouse.event(point[0],point[1],System.currentTimeMillis(),eventType);
            }
        }
    }

    private static HumanPath generateApplicable(HumanPath path, Point start, Point target) {
        double startToTarg = start.distance(target);
        if(startToTarg <= path.longestDistanceFromOrigin) {
            int[][] result = null;

            int fromIndexSize = path.points.size() - 1;

            int[] pathStart = path.points.get(0);

            double pathDistanceStartEnd = 0;
            int latestDistance = 0;
            boolean xDominant = false;
            Point chosen = null;
            for (int i = fromIndexSize; i >= 0; i--) {
                int[] rawCurrent = path.points.get(i);
                Point current = new Point(rawCurrent[0], rawCurrent[1]);
                if (current.distance(pathStart[0], pathStart[1]) >= startToTarg) {
                    chosen = current;
                    result = new int[i + 1][];
                    result[i] = new int[]{target.x, target.y, rawCurrent[2]};
                    pathDistanceStartEnd = current.distance(pathStart[0], pathStart[1]);
                    xDominant = (chosen.x - pathStart[0]) > (chosen.y - pathStart[1]);
                    continue;
                }
                if (chosen != null) {
                    Point p = null;
                    for (int k = latestDistance; k < pathDistanceStartEnd; k++) {
                        double T = k / pathDistanceStartEnd;
                        int cX = (int) ((1 - T) * chosen.x + T * pathStart[0]);
                        int cY = (int) ((1 - T) * chosen.y + T * pathStart[1]);
                        Point cP = new Point(cX, cY);
                        if ((xDominant && cP.x == current.x) || (!xDominant && cP.y == current.y)) {
                            p = cP;
                            latestDistance = k;
                            break;
                        }
                    }
                    /*Point p = null;
                    count = 0;
                    for (int k = latestDistance; k < pathDistanceStartEnd; k++) {
                        count++;
                        double T = k / pathDistanceStartEnd;
                        int cX = (int) ((1 - T) * chosen.x + T * pathStart[0]);
                        int cY = (int) ((1 - T) * chosen.y + T * pathStart[1]);
                        Point cP = new Point(cX, cY);
                        if (p == null || cP.distance(current) < p.distance(current)) {
                            p = cP;
                            latestDistance = k;
                        }
                    }
                    System.out.println("OLD: "+latestDistance+" Count: "+count);
                    if(latestDistance != latestDistanceTest) System.out.println("PROBLEM 1337"); //end comment here todo
                    if (p != null) {
                        double T = latestDistance / startToTarg;
                        int rX = (int) ((1 - T) * target.x + T * start.x) + (p.x - current.x);
                        int rY = (int) ((1 - T) * target.y + T * start.y) + (p.y - current.y);
                        result[i] = new int[]{rX, rY, rawCurrent[2]};
                    }
                }
            }
            if (result != null) {
                HumanPath res = new HumanPath();
                res.longestDistanceFromOrigin = path.longestDistanceFromOrigin;
                res.points = Arrays.asList(result);
                addNoise(res);
                return res;
            }
        }
        return null;
    }

    private static void addNoise(HumanPath hp) {
        int size = hp.points.size();
        int rand = Utils.rand(0,size);
        while(rand < (size - 1)) {
            int deviation = Utils.rand(-3,4);
            if(deviation != 0) {
                int[] current = hp.points.get(rand);
                int[] addition = new int[]{current[0] + deviation, current[1] + deviation, Utils.rand(2, 6)};
                hp.points.add(rand + 1, addition);
                hp.points.add(rand + 2,
                        new int[]{addition[0] - deviation, addition[1] - deviation, Utils.rand(2, 6)});
            }
            rand = Utils.rand(rand, size);
        }
    }*/
}
