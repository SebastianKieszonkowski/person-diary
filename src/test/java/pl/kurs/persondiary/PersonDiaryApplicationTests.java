package pl.kurs.persondiary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = PersonDiaryApplication.class)
@AutoConfigureMockMvc
class PersonDiaryApplicationTests {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void shouldAddPerson() throws Exception{
        //given
        //when
        //then
    }

}
