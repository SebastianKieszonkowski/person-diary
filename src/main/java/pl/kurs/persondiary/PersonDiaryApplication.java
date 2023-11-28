package pl.kurs.persondiary;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;

@SpringBootApplication
public class PersonDiaryApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(PersonDiaryApplication.class, args);
//        BufferedWriter out = new BufferedWriter(new FileWriter("src/indata/students.csv"));
//        for (int i = 0; i < 10000; i++){
//            //Long id, String firstName, String lastName, String pesel, Double height, Double weight,
//            //                   String email, String universityName, Integer studyYear, String studyField, Double scholarship
//            String linia = "name_" + i + ",lastname_" + i + "," + i + "123" + (1000-i) + "," + (1.0 + i * 0.01) + "," + (50.0 + i * 0.01) + ",name_" + i + "@gmail.com," +
//                    "UW_" + (int)(1 + i % 100) + "," + (int)(1 + i % 5) + ",kierunek_" + (int)(1 + i % 120) + "," + (500.0 + 0.02 * i);
//            out.write(linia);
//            out.newLine();
//        }
//        out.close();
    }

}
