import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HexagonGeneration {

    static Map<String, Hexagon> readHexagonsFromCSV(String fileName) {
        Map<String, Hexagon> hexagon_map = new HashMap<>();
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {

            br.readLine();
            String line = br.readLine();

            while (line != null) {
                line = line.replace("\"", "");
                String[] attributes = line.split(",");

                String hexagon_id = attributes[0];

                Hexagon hexagon = createHexagon(hexagon_id, attributes, hexagon_map.get(hexagon_id));
                hexagon_map.put(hexagon_id, hexagon);

                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hexagon_map;
    }

    static public Hexagon createHexagon(String hexagon_id, String[] metadata, Hexagon hexagonOld) throws ParseException {


        List<List<String>> neighbors = Helper.readNeighborsList(hexagon_id, metadata[6]);
//        System.out.println(metadata[5] + Objects.equals(metadata[5], "NULL"));
        int expectedCabs = Integer.parseInt((Objects.equals(metadata[5], "NULL") ? "0" : metadata[5]));
        String timestamp = metadata[4];
        int day_of_week = Integer.parseInt((Objects.equals(metadata[3], "NULL") ? "0" : metadata[3]));
        double center_long = Double.parseDouble(metadata[2]);
        double center_lat = Double.parseDouble(metadata[1]);


        if (hexagonOld == null) {
            Hexagon hexagon = new Hexagon(hexagon_id, day_of_week, neighbors, center_lat, center_long);
            if (hexagon.getWeightsByTime() == null) {
                Map<String, Integer> map = new HashMap<>();
                map.put(timestamp, expectedCabs);
                hexagon.setWeightsByTime(map);
            } else {
                hexagon.getWeightsByTime().put(timestamp, expectedCabs);
            }
            return hexagon;
        }
        hexagonOld.getWeightsByTime().put(timestamp, expectedCabs);

        return hexagonOld;
    }
}
