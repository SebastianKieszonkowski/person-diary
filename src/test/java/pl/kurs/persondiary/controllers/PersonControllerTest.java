package pl.kurs.persondiary.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.persondiary.PersonDiaryApplication;
import pl.kurs.persondiary.models.Employee;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PersonDiaryApplication.class)
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddPerson() throws Exception{
        //given

        Employee employee = new Employee(null, "Roman", "Test", "88071504852", 1.9,
                100.1, "roman.test@gmail.com", LocalDate.of(1992,2,5), "PM", 22_359.85);
        String json = objectMapper.writeValueAsString(employee);
        //when
        postman.perform(MockMvcRequestBuilders.post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated());

        //then
    }

}