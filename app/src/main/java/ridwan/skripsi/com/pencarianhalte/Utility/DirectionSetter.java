package ridwan.skripsi.com.pencarianhalte.Utility;

/**
 * Created by gueone on 10/17/2017.
 */

public class DirectionSetter {
    public String bearingLocation(double lat1, double lon1, double lat2, double lon2){
        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)* Math.cos(latitude2);
        double x= Math.cos(latitude1)* Math.sin(latitude2)- Math.sin(latitude1)* Math.cos(latitude2)* Math.cos(longDiff);
        double resultDegree= (Math.toDegrees(Math.atan2(y, x))+360)%360;
        String coordNames[] = {"utara","timur laut","timur", "tenggara","selatan", "barat daya", "barat","barat laut","utara"};
        double directionid = Math.round(resultDegree / 45);
        // no of array contain 360/16=22.5
        if (directionid < 0) {
            directionid = directionid + 7;
            //no. of contains in array
        }
        String compasLoc=coordNames[(int) directionid];
        return compasLoc;
    }
    public double getDistance(double lat1, double lon1, double lat2, double lon2){
        double EARTH_RADIUS = 6367.45;


        double deltalat = lat2 - lat1;
        double deltalon = lon2 - lon1;

        double a = Math.sin(deltalat / 2) * Math.sin(deltalat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltalon / 2) * Math.sin(deltalon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return (EARTH_RADIUS*c)/1000;
    }

}
