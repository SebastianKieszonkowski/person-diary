package pl.kurs.generator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

//javac src/main/java/pl/kurs/generator/DataGenerator.java
//java src/main/java/pl/kurs/generator/DataGenerator.java 5000000

public class DataGenerator {
    private static final Random random = new Random();
    private static List<String> positionNameList;
    private static DecimalFormat df;

    public static void main(String[] args) throws IOException {

        System.out.println("Start generation data.");
        generateCsvFile(Integer.parseInt(args[0]));
        System.out.println("Finish generation " + args[0] + " persons.");
    }

    public static void generateCsvFile(int recordNumber) throws IOException {
        loadPositionNameDictionary();
        initDecimalFormat();
        String pathFile = "src/main/resources/personData.csv";
        try (FileWriter writer = new FileWriter(pathFile)) {

            for (int i = 0; i < recordNumber / 3; i++) {
                writer.write(generateEmployeeData(i));
                writer.write(generatePensionerData(i));
                writer.write(generateStudentData(i));
            }

            if (recordNumber % 3 == 1) {
                writer.write(generateEmployeeData(recordNumber / 3 + 1));
            }
            if (recordNumber % 3 == 2) {
                writer.write(generateEmployeeData(recordNumber / 3 + 1));
                writer.write(generatePensionerData(recordNumber / 3 + 1));
            }
        }
    }

    private static String generateEmployeeData(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("Employee,");
        sb.append(getRandomFirstName(i, "Emp")).append(",");
        sb.append(getRandomLastName(i, "Emp")).append(",");
        sb.append(getRandomPesel(LocalDate.now().minusDays(i / 1000), 1, i)).append(",");
        sb.append(getRandomDouble(1.5, 2.0)).append(",");
        sb.append(45 + random.nextInt(55)).append(",");
        sb.append(getRandomEmail(i, "Emp")).append(",");
        sb.append(getRandomDate()).append(",");
        sb.append(getRandomPosition()).append(",");
        sb.append(5000 + random.nextInt(10000)).append("\n");

        String result = sb.toString();
        return result;
    }

    private static String generatePensionerData(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pensioner,");
        sb.append(getRandomFirstName(i, "Pen")).append(",");
        sb.append(getRandomLastName(i, "Pen")).append(",");
        sb.append(getRandomPesel(LocalDate.now().minusDays(i / 1000), 2, i)).append(",");
        sb.append(getRandomDouble(1.5, 2.0)).append(",");
        sb.append(45 + random.nextInt(55)).append(",");
        sb.append(getRandomEmail(i, "Pen")).append(",");
        sb.append(4000 + random.nextInt(4000)).append(",");
        sb.append(random.nextInt(50)).append("\n");

        String result = sb.toString();
        return result;
    }

    private static String generateStudentData(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("Student,");
        sb.append(getRandomFirstName(i, "Stu")).append(",");
        sb.append(getRandomLastName(i, "Stu")).append(",");
        sb.append(getRandomPesel(LocalDate.now().minusDays(i / 1000), 3, i)).append(",");
        sb.append(getRandomDouble(1.5, 2.0)).append(",");
        sb.append(45 + random.nextInt(55)).append(",");
        sb.append(getRandomEmail(i, "Stu")).append(",");
        sb.append(getUniversityName(i)).append(",");
        sb.append(1 + random.nextInt(5)).append(",");
        sb.append(getUniversityFieldName(i)).append(",");
        sb.append(1000 + random.nextInt(4000)).append("\n");

        String result = sb.toString();
        return result;
    }

    private static String getRandomFirstName(int i, String typ) {
        return "Name" + typ + i;
    }

    private static String getRandomLastName(int i, String typ) {
        return "LastST" + typ + i;
    }

    private static String getRandomPosition() {
        return positionNameList.get(random.nextInt(positionNameList.size()));
    }

    private static String getUniversityName(int i) {
        return "Uczelnia_" + i;
    }

    private static String getUniversityFieldName(int i) {
        return "Nazwa kierunku_" + i;
    }

    private static String getRandomPesel(LocalDate dateOfBirth, int type, int i) {
        int year = dateOfBirth.getYear();
        int month = dateOfBirth.getMonthValue();
        int day = dateOfBirth.getDayOfMonth();

        int yearPart = year % 100;
        int monthPart = month;
        if (year >= 2000) {
            monthPart += 20;
        }

        String pesel = String.format("%02d%02d%02d", yearPart, monthPart, day) + type;

        int j = i % 1000;
        if (j < 10) {
            pesel += "000" + j;
        } else if (j < 100) {
            pesel += "00" + j;
        } else if (j < 1000) {
            pesel += "0" + j;
        } else if (j < 10000) {
            pesel += j;
        }

        return pesel;
    }

    private static String getRandomDouble(double min, double max) {
        double var = min + (max - min) * random.nextDouble();
        return df.format(var);
    }

    private static String getRandomEmail(int i, String typ) {
        return "Name" + typ + i + ".lastname@onet.pl";
    }

    private static String getRandomDate() {
        return LocalDate.of(1950 + random.nextInt(70), 1 + random.nextInt(12), 1 + random.nextInt(28)).toString();
    }

    private static void loadPositionNameDictionary() throws IOException {
        try (
                FileReader fr = new FileReader("src/main/resources/dictionaries/positionname.txt");
                BufferedReader br = new BufferedReader(fr, 8192)) {
            positionNameList = br.lines()
                    .map(x -> x.trim())
                    .map(x -> x.toLowerCase())
                    .collect(Collectors.toList());
        }
    }

    private static void initDecimalFormat(){
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        df = new DecimalFormat("#.##", otherSymbols);
    }

}
