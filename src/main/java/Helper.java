import com.uber.h3core.H3Core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Helper {

    public static String convertToClosestTimeInterval(Date date) throws ParseException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int minutes = calendar.get(Calendar.MINUTE);
        int mod = minutes % 5;
        mod = mod < 3 ? -mod : (5 - mod);
        calendar.set(Calendar.MINUTE, minutes + mod);
        calendar.set(Calendar.SECOND, 00);

        String strDateFormat = "hh:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static List<List<String>> readNeighborsList(String hexagon_id, String neighbors) {

        neighbors = neighbors.replace("{", "");
        neighbors = neighbors.replace("}", "");
        neighbors = neighbors.replace("'", "");
        neighbors = neighbors.replace(" ", "");

        List<List<String>> neighbors_list = new ArrayList<>();
        String[] list = neighbors.split(":");

//        System.out.println("List: " + list.length);

        int k = 0, t = 0;
        for (int i = 1; i <= 4; i++) {
            k += 6 * i;
            List<String> n = new ArrayList<>();
            while (t < k) {
                t++;
                if (Objects.equals(list[t - 1], hexagon_id))
                    continue;
                n.add(list[t - 1]);
            }
            if (n.size() % 6 != 0) {
                System.out.println("something wrong with neighbours");
            }
//            System.out.println(i + " Ring: " + n.size());
            neighbors_list.add(n);
        }
        return neighbors_list;
    }

    public static String getHexFromGeo(double latitude, double longitude) throws IOException {
        H3Core h3 = H3Core.newInstance();
        return h3.geoToH3Address(latitude, longitude, 9);
    }
}
