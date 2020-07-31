import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map.Entry;

public class dijkstraJava {

    static int N = 2000; // Set dataset size
    static double edges[][] = new double[N][N];
    static double edgesDist [][] = new double[N][N];
    static TreeMap <Integer,String> cityNames = new TreeMap <Integer,String>();
    static Set <Integer> cityKeys = cityNames.keySet();
    static graph g;
    static long startTime, endTime;
    static ArrayList <Integer> mostShortPaths = new ArrayList<Integer>();
    static TreeMap <Integer, double[]> cityLonLat = new TreeMap<Integer, double[]>();
    static Scanner scanner;
    static String file1, file2, file3; 
    static String city1, city2, city3, city4, city5, city6, city7;

    public static class graph
    {
        double[][] adj;

        graph(double[][] a) 
        {
            adj = new double[a.length][a.length];
            for (int i = 0; i < a.length; i++)
                for (int j = 0; j < a.length; j++)
                    adj[i][j] = a[i][j];
        }

        public HashSet<Integer> neighbours(int v) {
            HashSet<Integer> h = new HashSet<Integer>();
            for (int i = 0; i < adj.length; i++)
                if (adj[v][i] != 0)
                    h.add(i);
            return h;
        }

        public HashSet<Integer> vertices() {
            HashSet<Integer> h = new HashSet<Integer>();
            for (int i = 0; i < adj.length; i++)
                h.add(i);
            return h;
        }

        @SuppressWarnings("unchecked")
        ArrayList<Integer> addToEnd(int i, ArrayList<Integer> path) // Returns a new arraylist
        // returns a new path with i at the end of path
        {
            ArrayList<Integer> k;
            k = (ArrayList<Integer>) path.clone();
            k.add(i);
            return k;
        }

        @SuppressWarnings("unchecked")
        public HashSet<ArrayList<Integer>> shortestPaths1(HashSet<ArrayList<Integer>> sofar, HashSet<Integer> visited,
                int end) // Hashset of arraylists
        {
            HashSet<ArrayList<Integer>> more = new HashSet<ArrayList<Integer>>();
            HashSet<ArrayList<Integer>> result = new HashSet<ArrayList<Integer>>();
            HashSet<Integer> newVisited = (HashSet<Integer>) visited.clone();
            boolean done = false;
            boolean carryon = false;

            for (ArrayList<Integer> p : sofar) {
                for (Integer z : neighbours(p.get(p.size() - 1))) {
                    if (!visited.contains(z)) {
                        carryon = true;
                        newVisited.add(z);
                        if (z == end) {
                            done = true;
                            result.add(addToEnd(z, p));
                        } else
                            more.add(addToEnd(z, p));
                    }
                }
            }
            if (done)
                return result;
            else if (carryon)
                return shortestPaths1(more, newVisited, end); // Recursive
            else
                return new HashSet<ArrayList<Integer>>(); // Empty hashset
        }

        public HashSet<ArrayList<Integer>> shortestPaths(int first, int end) {
            HashSet<ArrayList<Integer>> sofar = new HashSet<ArrayList<Integer>>();
            HashSet<Integer> visited = new HashSet<Integer>();
            ArrayList<Integer> starting = new ArrayList<Integer>();
            starting.add(first);
            sofar.add(starting); // Arraylist in hashset
            if (first == end)
                return sofar;
            visited.add(first);
            return shortestPaths1(sofar, visited, end);
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Integer> dijkstra(int start, int end) {
            int N = adj.length;
            HashMap<Integer, Double> Q = new HashMap<Integer, Double>();
            ArrayList<Integer>[] paths = new ArrayList[N];

            for (int i = 0; i < N; i++) {
                Q.put(i, Double.POSITIVE_INFINITY);
                paths[i] = new ArrayList<Integer>();
                paths[i].add(start);
            }

            HashSet<Integer> S = new HashSet<Integer>();
            S.add(start);
            Q.put(start, 0.0);

            while (!Q.isEmpty()) {
                int v = findSmallest(Q);
                if (v == end && Q.get(v) != Double.POSITIVE_INFINITY) {
                    return paths[end];
                }
                double w = Q.get(v);
                S.add(v);
                for (int u : neighbours(v))
                    if (!S.contains(u)) {
                        double w1 = w + adj[v][u];
                        if (w1 < Q.get(u)) {
                            Q.put(u, w1);
                            paths[u] = addToEnd(u, paths[v]);
                        }
                    }
                Q.remove(v);
            }
            return new ArrayList<Integer>();
        }

        int findSmallest(HashMap<Integer, Double> t) {
            Object[] things = t.keySet().toArray();
            double val = t.get(things[0]);
            int least = (int) things[0];
            Set<Integer> k = t.keySet();
            for (Integer i : k) {
                if (t.get(i) < val) {
                    least = i;
                    val = t.get(i);
                }
            }
            return least;
        }
    }

    private static void printLine(){
        System.out.println();
    }

    private static graph setUpGraph(double [][] dataset){
        return new graph(dataset);
    }

    private static long getExecutionTime(long startTime, long endTime){
        return (endTime - startTime) / 1000000;
    }

    private static int getIDUsingName(String cityName){
        int id = 0;
        for(Entry<Integer, String> entry : cityNames.entrySet()){
            if(entry.getValue().equals(cityName))
                id = entry.getKey();
        }
        return id;
    }

    private static String getNameUsingId(int cityId){ // EDIT
        return cityNames.get(cityId);
    }

    private static void getMostShortPaths(){
        HashSet <ArrayList<Integer>> currentShortestPaths;
        int startCity = 0, endCity = 0, maxSize = 0;

        for(int startKey : cityKeys){ // loop all start ids
            for(int endKey : cityKeys){   // loop all end ids
                if(startKey != endKey){
                    currentShortestPaths = g.shortestPaths(startKey, endKey);

                    int currentSize = 0;

                    if (!currentShortestPaths.isEmpty()){
                        currentSize = currentShortestPaths.size();
                        if(currentSize > maxSize){
                            maxSize = currentSize;
                            startCity = startKey;
                            endCity = endKey;
                        }
                    }
                }
            }
        }
        mostShortPaths.add(startCity);
        mostShortPaths.add(endCity);
        mostShortPaths.add(maxSize);
    }

    @SuppressWarnings("unchecked")
    static ArrayList<Integer> firstElement (HashSet <ArrayList <Integer>> s)
    {
        return (ArrayList<Integer>)s.toArray()[0];
    }

    static ArrayList <Integer> getFurthestCitiesIds(int startCity){
        int currentSize = 0, max = 0;
        ArrayList<Integer> endList = new ArrayList<Integer>();
        ArrayList<Integer> furthestCities = new ArrayList<Integer>();

        for (int end : cityKeys){
            if (end != startCity){
                HashSet <ArrayList<Integer>> tempHashSet = g.shortestPaths(startCity, end);
                if(!tempHashSet.isEmpty()){
                    for(ArrayList a : tempHashSet){
                        currentSize = a.size();
                        if(currentSize >= max){
                            max = currentSize;
                            if(!endList.contains(end))
                                endList.add(end);
                        }
                    }
                }
            }
        }

        for(int end : endList){
            if(end != startCity){
                HashSet <ArrayList<Integer>> tempHashSet = g.shortestPaths(startCity, end);
                if (!tempHashSet.isEmpty()){
                    for(ArrayList a : tempHashSet){
                        currentSize = a.size();
                        if(currentSize == max)
                            if(!furthestCities.contains(end))
                                furthestCities.add(end);
                    }
                }
            }
        }
        return furthestCities;
    }

    static ArrayList<String> getNamesfromIdArraylist(ArrayList <Integer> furthestCities){
        ArrayList <String> furthestCitiesNames = new ArrayList<String>();
        for(int i=0; i<furthestCities.size(); i++){
            furthestCitiesNames.add(getNameUsingId(furthestCities.get(i)));
        }
        return furthestCitiesNames;
    }

    static double realDistance(double lat1, double lon1, double lat2, double lon2)
    {
        int R = 6371;
        // km (change this constant to get miles)
        double dLat = (lat2-lat1) * Math.PI / 180;
        double dLon = (lon2-lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 )* Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); double d = R * c;
        return d;
    }

    static double getLengthInWeights(int startCity, int endCity){
        ArrayList <Integer> path = g.dijkstra(startCity, endCity);
        double length = 0.0, temp = 0.0;
        if(!path.isEmpty()){
            if (path.size() == 2){
                temp = g.adj[path.get(0)][path.get(1)];
                g.adj[path.get(0)][path.get(1)] = 0.0; // bluff
                ArrayList <Integer> notDirectPath = g.dijkstra(startCity, endCity);
                for(int i=0; i<notDirectPath.size() - 1; i++)
                    length += g.adj[notDirectPath.get(i)][notDirectPath.get(i+1)]; 
                g.adj[path.get(0)][path.get(1)] = temp; // restore
            }
            else{
                for(int i=0; i<path.size() - 1; i++)
                    length += g.adj[path.get(i)][path.get(i+1)];
            }
        }
        return length;
    }

    static double getLengthInKM(int startCity, int endCity){
        double length = 0.0, temp = 0.0;
        double [] city1, city2 = null;
        edges = edgesDist;
        g = setUpGraph(edges);
        ArrayList <Integer> path = g.dijkstra(startCity, endCity);
        if(!path.isEmpty()){
            if(path.size() == 2){
                System.out.println("Q7 DEBUG BOOLEAN Path Size: " + path.size());
                temp = g.adj[path.get(0)][path.get(1)];
                g.adj[path.get(0)][path.get(1)] = 0.0; // bluff
                ArrayList <Integer> notDirectPath = g.dijkstra(startCity, endCity);
                g.adj[path.get(0)][path.get(1)] = temp; // restore

                for(int i=0; i<notDirectPath.size() -1; i++){
                    city1 = cityLonLat.get(notDirectPath.get(i));
                    city2 = cityLonLat.get(notDirectPath.get(i+1));
                    length += realDistance(city1[1], city1[0], city2[1], city2[0]);
                
                }
            }
            else{
                for(int i=0; i<path.size()-1; i++){
                    city1 = cityLonLat.get(path.get(i));
                    city2 = cityLonLat.get(path.get(i+1));
                    length += realDistance(city1[1], city1[0], city2[1], city2[0]);
                }
            }
        }
        return length;
    }

    private static void setUpEdges(){
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++)
                edges[i][j] = 0.0;
        }
    }

    private static void readCities() throws FileNotFoundException{
        scanner = new Scanner(new FileReader(file1));   //args[0]
        String strLine = scanner.nextLine();
        while (scanner.hasNext()){
            strLine = scanner.nextLine();
            String[] results = strLine.split(",");
            cityNames.put(Integer.parseInt(results[0]), results[1]);
        }
    }

    private static void readCitiesLonLat() throws FileNotFoundException{
        scanner = new Scanner(new FileReader(file2));  //args[1]
        while (scanner.hasNext()){
            String strLine = scanner.nextLine();
            String[] results = strLine.split(",");
            double[] lonLat = new double[2];
            lonLat[0] = Double.parseDouble(results[1]); // lon
            lonLat[1] = Double.parseDouble(results[2]); // lat
            cityLonLat.put(Integer.parseInt(results[0]), lonLat);
        }
    }

    private static void readCitiesWeight() throws FileNotFoundException{
        scanner = new Scanner(new FileReader(file3));   // args[2]
        while (scanner.hasNext()){
            String strLine = scanner.nextLine();
            String[] results = strLine.split(",");
            edges[Integer.parseInt(results[0])][Integer.parseInt(results[1])] = Double.parseDouble(results[2]);
            double[] sourceCity = cityLonLat.get(Integer.parseInt(results[0]));
            double[] destinationCity = cityLonLat.get(Integer.parseInt(results[1]));

            try {
                edgesDist[Integer.parseInt(results[0])][Integer.parseInt(results[1])] = realDistance(sourceCity[1], sourceCity[0], destinationCity[1], destinationCity[0]);
            } 
            
            catch (NullPointerException e) {

            }
        }
    }

    private static int numberOfshortestPaths(String city1, String city2){
        return g.shortestPaths(getIDUsingName(city1), getIDUsingName(city2)).size();
    }

    private static int lengthOfShortestPaths(){
        return firstElement(g.shortestPaths(mostShortPaths.get(0), mostShortPaths.get(1))).size();
    }

    private static void outputAns(){
        System.out.println("1. Number of shortest paths between " + city1 + " & " + city2 + " : " + numberOfshortestPaths("Athens", "Tehran"));
        System.out.println("2. Cities with the shortest paths: " + getNameUsingId(mostShortPaths.get(0)) + " and " + getNameUsingId(mostShortPaths.get(1)));
        System.out.println("3. Number of shortest paths between " + getNameUsingId(mostShortPaths.get(0)) + " and " + getNameUsingId(mostShortPaths.get(1)) + 
        " : " + mostShortPaths.get(2));

        System.out.println("4. Length of shortest paths: " + lengthOfShortestPaths());
        System.out.println("5. Furthest cities from " + city3 + " : " + getNamesfromIdArraylist(getFurthestCitiesIds(getIDUsingName(city3))));
        System.out.printf("6. Total weight of shortest path between " + city4 + " and " + city5 + ": %.2f\n", 
        getLengthInWeights(getIDUsingName("Rome"), getIDUsingName("New York")));

        System.out.printf("7. Total km of shortest path between " + city6 + " and " + city7 + ": %.2f\n", 
        getLengthInKM(getIDUsingName("Lisbon"), getIDUsingName("Manila")));

        printLine();
        endTime = System.nanoTime();
        System.out.println("Execution Time: " + getExecutionTime(startTime, endTime) + " milliseconds");
    }

    public static void main(String[] args) throws FileNotFoundException {
        startTime = System.nanoTime();
        //DecimalFormat d = new DecimalFormat(".##");
        file1 = "cities.csv";
        file2 = "cities_lon_lat.csv";
        file3 = "randomGraph.csv";
        city1 = "Athens";
        city2 = "Tehran";
        city3 = "Toronto";
        city4 = "Rome";
        city5 = "New York";
        city6 = "Lisbon";
        city7 = "Manila";

        setUpEdges();

        readCities();
        readCitiesLonLat();
        readCitiesWeight();

        g = setUpGraph(edges);

        getMostShortPaths();
        outputAns();
    }
}
