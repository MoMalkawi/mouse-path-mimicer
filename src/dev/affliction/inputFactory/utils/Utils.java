package dev.affliction.inputFactory.utils;

public class Utils {

    public static int rand(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void sleep(int val) {
        try {
            Thread.sleep(val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepUntil(Condition cond) {
        while(!cond.verify()) { Utils.sleep(10); }
    }

    public static int deviateFromMin(int num, int min, int maxDeviationPerc) {
        if(num != 0) {
            int minPerc = (int) Math.round((double) (min / num) * 100);
            double addition = ((double)Utils.rand(minPerc, minPerc + maxDeviationPerc) / 100) * num;
            if(addition >= num || Utils.rand(1,11) > 5) return (int) (num + addition);
            return (int) (num - addition);
        }
        return num;
    }

    public static int roundedGreater(double num) {
        int integer_num = (int) num;
        if((num - integer_num) != 0) return integer_num + 1;
        return integer_num;
    }

    public static int[] to1D(int[][] ar) {

        int[] newArray = new int[(ar.length*ar[1].length)];
        for(int i = 0; i < ar.length; i++) {
            int[] row = ar[i];
            System.arraycopy(ar[i], 0, newArray, i * row.length, row.length);
        }
        return newArray;
    }

    public static String assignLoc(double num) {
        if(num < 100) return "under";
        else if(num < 200) return "100";
        else if(num < 300) return "200";
        else if(num < 400) return "300";
        else if(num < 500) return "400";
        else if(num < 600) return "500";
        else if(num < 700) return "600";
        else if(num < 800) return "700";
        else if(num < 900) return "800";
        else if(num < 1000) return "900";
        else if(num < 1100) return "1000";
        else if(num < 1200) return "1100";
        else if(num < 1300) return "1200";
        else if(num < 1400) return "1300";
        else if(num < 1500) return "1400";
        else if(num < 1600) return "1500";
        else if(num < 1700) return "1600";
        else if(num < 1800) return "1700";
        else if(num < 1900) return "1800";
        else if(num < 2000) return "1900";
        else if(num < 2100) return "2000";
        else if(num < 2200) return "2100";
        else if(num < 2300) return "2200";
        else if(num < 2400) return "2300";
        else if(num < 2500) return "2400";
        else if(num < 2600) return "2500";
        else if(num < 2700) return "2600";
        else if(num < 2800) return "2700";
        else if(num < 2900) return "2800";
        else if(num < 3000) return "2900";
        else return "other";
    }
}
