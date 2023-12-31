package pl.kurs.persondiary.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.persondiary.PersonDiaryApplication;
import pl.kurs.persondiary.command.CreateEmployeePositionCommand;
import pl.kurs.persondiary.dto.FullEmployeePositionDto;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.services.EmployeePositionService;
import pl.kurs.persondiary.services.PersonService;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PersonDiaryApplication.class, properties = "src/test/resources/application.properties")
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeePositionService employeePositionService;

    public String getAdminToken() throws Exception {
        String jsonRequest = "{\"username\":\"AdamAdmin\", \"password\":\"admin\"}";

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseString).get("jwtToken").textValue();
    }

    public String getUserToken() throws Exception {
        String jsonRequest1 = "{\"username\":\"JanekUser\", \"password\":\"user\"}";

        MvcResult result1 = postman.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType("application/json")
                .content(jsonRequest1))
                .andExpect(status().isOk())
                .andReturn();

        String responseString1 = result1.getResponse().getContentAsString();
        return objectMapper.readTree(responseString1).get("jwtToken").textValue();
    }

    public String getEmployeeToken() throws Exception {
        String jsonRequest1 = "{\"username\":\"DarekEmployee\", \"password\":\"employee\"}";

        MvcResult result1 = postman.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType("application/json")
                .content(jsonRequest1))
                .andExpect(status().isOk())
                .andReturn();

        String responseString1 = result1.getResponse().getContentAsString();
        return objectMapper.readTree(responseString1).get("jwtToken").textValue();
    }

    @Test
    @WithMockUser(username = "DarekEmployee", roles = {"EMPLOYEE"})
    void createEmployeePositionShouldTryAddEmployeePositionToEmployeeWithPesel03291882687AndThrowExceptionWhenWorkPeriodCoversAnother() throws Exception {
        //given
        CreateEmployeePositionCommand createEmployeePositionCommand = new CreateEmployeePositionCommand("Elektryk Zakładowy", LocalDate.of(1994, 1, 1),
                LocalDate.of(2001, 1, 1), 8500.0);
        String pesel = "03291882687";
        String employeePositionJson = objectMapper.writeValueAsString(createEmployeePositionCommand);

        //when
//        boolean isExist = personService.isPersonExists(pesel, "employee");
//        assertEquals(isExist, true);

        postman.perform(MockMvcRequestBuilders.post("/employees/" + pesel + "/position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeePositionJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("The specified working period coincides with the existing ones!!!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser(username = "DarekEmployee", roles = {"EMPLOYEE"})
    void createEmployeePositionShouldTryAddEmployeePositionToEmployeeWithPesel03291882687AndThrowExceptionWhenWorkPeriodOverlapsTwoOther() throws Exception {
        //given
        CreateEmployeePositionCommand createEmployeePositionCommand = new CreateEmployeePositionCommand("Elektryk Zakładowy", LocalDate.of(1999, 1, 1),
                LocalDate.of(2011, 1, 1), 8500.0);
        String pesel = "03291882687";
        String employeePositionJson = objectMapper.writeValueAsString(createEmployeePositionCommand);

        //when
//        boolean isExist = personService.isPersonExists(pesel, "employee");
//        assertEquals(isExist, true);

        postman.perform(MockMvcRequestBuilders.post("/employees/" + pesel + "/position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeePositionJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("The specified working period coincides with the existing ones!!!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser(username = "DarekEmployee", roles = {"EMPLOYEE"})
    void createEmployeePositionShouldTryAddEmployeePositionToEmployeeWithPesel03291882687AndThrowExceptionWhenWorkPeriodsIncludedInAnother() throws Exception {
        //given
        CreateEmployeePositionCommand createEmployeePositionCommand = new CreateEmployeePositionCommand("Elektryk Zakładowy", LocalDate.of(2011, 1, 1),
                LocalDate.of(2014, 1, 1), 8500.0);
        String pesel = "03291882687";
        String employeePositionJson = objectMapper.writeValueAsString(createEmployeePositionCommand);

        //when
//        boolean isExist = personService.isPersonExists(pesel, "employee");
//        assertEquals(isExist, true);

        postman.perform(MockMvcRequestBuilders.post("/employees/" + pesel + "/position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeePositionJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("The specified working period coincides with the existing ones!!!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void createEmployeePositionShouldAddEmployeePositionToEmployeeWithPesel03291882687() throws Exception {
        // given
        String employeeToken = getEmployeeToken();
        CreateEmployeePositionCommand createEmployeePositionCommand = new CreateEmployeePositionCommand("Elektryk Zakładowy", LocalDate.of(2001, 1, 1),
                LocalDate.of(2009, 12, 31), 8500.0);
        String pesel = "03291882687";
        String employeePositionJson = objectMapper.writeValueAsString(createEmployeePositionCommand);

        //when
//        boolean isExist = personService.isPersonExists(pesel, "employee");
//        assertEquals(isExist, true);

        MvcResult newPositionMvc = postman.perform(MockMvcRequestBuilders.post("/employees/" + pesel + "/position")
                .header("Authorization", "Bearer " + employeeToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeePositionJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = newPositionMvc.getResponse().getContentAsString();
        FullEmployeePositionDto fullEmployeePositionDto = objectMapper.readValue(responseContent, FullEmployeePositionDto.class);

        Long employeePositionId = fullEmployeePositionDto.getEmployeeId();

        //then
        EmployeePosition createdEmployeePosition = employeePositionService.findById(employeePositionId);
        assertNotNull(createdEmployeePosition);
        assertEquals(createdEmployeePosition.getEmployee().getPesel(), pesel);

    }

}