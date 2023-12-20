package pl.kurs.persondiary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.persondiary.controllers.PersonController;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.repositories.PersonViewRepository;
import pl.kurs.persondiary.security.jwt.UserRepository;
import pl.kurs.persondiary.services.PersonService;

import java.util.List;

@SpringBootTest(classes = PersonDiaryApplication.class, properties = "src/test/resources/application.properties")
@AutoConfigureMockMvc
//(addFilters = false)
//@Import(TestConfig.class)
class PersonDiaryApplicationTests {

    @Autowired
    private MockMvc postman;

    @Autowired
    private PersonService personService;

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

//    @Autowired
//    private SpringLiquibase liquibase;
//
//    @BeforeEach
//    void setUp() throws Exception {
//      //  liquibase.afterPropertiesSet();
//    }

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
