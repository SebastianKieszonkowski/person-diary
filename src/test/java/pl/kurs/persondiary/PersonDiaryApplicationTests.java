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

import java.util.List;

@SpringBootTest(classes = PersonDiaryApplication.class, properties = "src/test/resources/application.properties")
@AutoConfigureMockMvc(addFilters = false)
class PersonDiaryApplicationTests {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonViewRepository personViewRepository;

    @Autowired
    private PersonController personController;

    @Test
    void shouldAddPerson() throws Exception {
        System.out.println("start");
        //given
        //when
        //then
    }

    @Test
    void shouldGetPersonByPesel() throws Exception {

        String response = postman.perform(MockMvcRequestBuilders.get("/persons?type=student"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<PersonView> personViewList = personViewRepository.findAll();

        System.out.println("test");
        System.out.println(response);
    }

}
