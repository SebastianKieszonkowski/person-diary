package pl.kurs.persondiary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.persondiary.controllers.PersonController;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.repositories.PersonViewRepository;
import pl.kurs.persondiary.security.jwt.UserRepository;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PersonDiaryApplication.class, properties = "src/test/resources/application.properties")
@AutoConfigureMockMvc//(addFilters = false)
class PersonDiaryApplicationTests {

    @Autowired
    private MockMvc postman;

    @Autowired
    private PersonViewRepository personViewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private PersonController personController;

    private static String tokenAdmin;
    private static String token;

    @BeforeEach
    public void init() throws Exception {
        String jsonRequest = "{\"username\":\"AdamAdmin\", \"password\":\"admin\"}";

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        tokenAdmin = objectMapper.readTree(responseString).get("jwtToken").textValue();

        String jsonRequest1 = "{\"username\":\"JanekUser\", \"password\":\"user\"}";

        MvcResult result1 = postman.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType("application/json")
                .content(jsonRequest1))
                .andExpect(status().isOk())
                .andReturn();

        String responseString1 = result1.getResponse().getContentAsString();
        token = objectMapper.readTree(responseString1).get("jwtToken").textValue();
    }


    @Test
    void shouldReturnPersonWithHeight183cm() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons?heightFrom=1.82&heightTo=1.84")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("student"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Michał"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Nowakowski"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pesel").value("54082895915"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].height").value(1.83))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].weight").value(70))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("michal.nowakowski@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].universityName").value("Politechnika Warszawska"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studyField").value("Informatyka"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studyYear").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].scholarship").value(1800.0));
    }

    @Test
    void shouldReturnPersonWithWeight150kg() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons?weightFrom=149&weightTo=151")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("employee"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Piotr"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Jankowski"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pesel").value("82120351732"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].height").value(1.78))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].weight").value(150))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("piotr.jankowski@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].hireDate").value("2023-11-27"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].position").value("Specjalista ds. Marketingu"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(14200.75));
    }

    @Test
    void shouldReturnFemaleAgeBetween25And30YearsOldAndStudentType() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons?gender=female&ageFrom=25&ageTo=30&type=stud")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("student"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Anna"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Lewandowska"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pesel").value("95070292778"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].height").value(1.73))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].weight").value(60))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("anna.lewandowska@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].universityName").value("Politechnika Gdańska"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studyField").value("Architektura"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studyYear").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].scholarship").value(1400.0));
    }

    @Test
    void shouldReturn10people() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10));
    }

    @Test
    void shouldReturnBadRequestForInvalidSalaryToValue() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons?salaryTo=duzo")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value("Field: salaryTo / rejected value: 'duzo' / message: Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Double' for property 'salaryTo'; For input string: \"duzo\""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnUnauthorizedException() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAddPerson() throws Exception {
        System.out.println("start");
        //given
        //when
        //then
    }

    @Test
    void shouldGetPersonByPesel() throws Exception {
        //given
        //when
        //then
        String response = postman.perform(MockMvcRequestBuilders.get("/persons?weightFrom=149&weightTo=151"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<PersonView> personViewList = personViewRepository.findAll();

        System.out.println("test");
        System.out.println(response);
    }

}
