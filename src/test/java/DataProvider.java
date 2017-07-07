import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DataProvider {

    public static Object[][] readFile(File file) throws IOException {
        String text = FileUtils.readFileToString(file);
        String[] row = text.split(";");

        int rowNum = row.length;
        int colNum = row[0].split(",").length;

        Object[][] data = new String[rowNum][colNum];

        for (int i = 0; i < rowNum; i++) {
            String[] cols = row[i].split(",");
            for (int j = 0; j < colNum; j++) {
                data[i][j] = cols[j].trim();
                System.out.println("The value is " + data[i][j]);
            }
        }
        return data;
    }

    @org.testng.annotations.DataProvider(name = "IncorrectDataSets")
    public static Object[][] incorrectDataSets() {
        File file = new File("src/test/resources/IncorrectDataSets.txt");
        System.out.println(file.getAbsolutePath().toString());
        Object[][] returnObjArray = new Object[0][];
        try {
            returnObjArray = readFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObjArray;
    }

    @org.testng.annotations.DataProvider(name = "IncorrectTestUsers")
    public static Object[][] incorrectTestUsers() {
        File file = new File("src/test/resources/IncorrectTestUsers.txt");
        System.out.println(file.getAbsolutePath().toString());
        Object[][] returnObjArray = new Object[0][];
        try {
            returnObjArray = readFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObjArray;
    }

    @org.testng.annotations.DataProvider(name = "CorrectTestUser")
    public static Object[][] correctTestUser() {
        File file = new File("src/test/resources/CorrectTestUser.txt");
        System.out.println(file.getAbsolutePath().toString());
        Object[][] returnObjArray = new Object[0][];
        try {
            returnObjArray = readFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObjArray;
    }


}
