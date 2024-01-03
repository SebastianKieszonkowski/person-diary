package pl.kurs.persondiary.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.persondiary.PersonDiaryApplication;
import pl.kurs.persondiary.command.personcreate.CreateEmployeeCommand;
import pl.kurs.persondiary.command.personcreate.CreatePensionerCommand;
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.command.personcreate.CreateStudentCommand;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.services.CommonService;
import pl.kurs.persondiary.services.PersonService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PersonDiaryApplication.class, properties = "src/test/resources/application.properties")
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser(username = "JanekUser", roles = {"USER"})
    void getPersonsShouldReturnPersonWithHeight183cm() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons?heightFrom=2.10&heightTo=2.12"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value("student"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Michał"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Nowakowski"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value("54082895915"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(2.11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(70))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("michal.nowakowski@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].universityName").value("Politechnika Warszawska"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studyField").value("Informatyka"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studyYear").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].scholarship").value(1800.0));
    }

    @Test
    @WithMockUser(username = "JanekUser", roles = {"USER"})
    void getPersonsShouldReturnPersonWithWeight150kg() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons?weightFrom=149&weightTo=151"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value("employee"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Piotr"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Jankowski"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value("82120351732"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(1.78))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(150))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("piotr.jankowski@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].hireDate").value("2023-11-27"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].position").value("Specjalista ds. Marketingu"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].salary").value(14200.75));
    }

    @Test
    @WithMockUser(username = "JanekUser", roles = {"USER"})
    void getPersonsShouldReturnFemaleAgeBetween25And30YearsOldAndStudentType() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons?type=student&gender=female&ageFrom=25&ageTo=30"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value("student"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Anna"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Lewandowska"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value("95070292778"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(1.73))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(60))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("anna.lewandowska@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].universityName").value("Politechnika Gdańska"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studyField").value("Architektura"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studyYear").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].scholarship").value(1400.0));
    }

    @Test
    @WithMockUser(username = "JanekUser", roles = {"USER"})
    void getPersonsShouldReturn10people() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(10));
    }

    @Test
    void getPersonsShouldReturnBadRequestForInvalidSalaryToValue() throws Exception {
        //given
        String userToken = getUserToken();
        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?salaryTo=duzo")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("For input string: \"duzo\"")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void getPersonsShouldReturnUnauthorizedException() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createPersonShouldAddStudent() throws Exception {
        //given
        String adminToken = getAdminToken();

        CreateStudentCommand student = new CreateStudentCommand("Jan", "Kowalski", "72030491266", 1.78, 65.5,
                "jan.kowalski@wp.pl", LocalDate.of(1972,3,4),"Politechnika Lubelska", 1, "Matematyka", 600.51);
        String type = "student";
        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("firstName", student.getFirstName());
        studentMap.put("lastName", student.getLastName());
        studentMap.put("pesel", student.getPesel());
        studentMap.put("height", student.getHeight());
        studentMap.put("weight", student.getWeight());
        studentMap.put("email", student.getEmail());
        studentMap.put("birthdate", CommonService.getBirthday(student.getPesel()));
        studentMap.put("universityName", student.getUniversityName());
        studentMap.put("studyYear", student.getStudyYear());
        studentMap.put("studyField", student.getStudyField());
        studentMap.put("scholarship", student.getScholarship());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(studentMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(student.getPesel(), type);
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isCreated());

        Student student1 = (Student) personService.getPersonByTypeAndPesel("72030491266", "student");
        System.out.println(student1);
        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?type=student&pesel=" + student.getPesel())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(student.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(student.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value(student.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(student.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(student.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(student.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].universityName").value(student.getUniversityName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studyField").value(student.getStudyField()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studyYear").value(student.getStudyYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].scholarship").value(student.getScholarship()));
    }

    @Test
    void createPersonShouldAddPensioner() throws Exception {
        //given
        String adminToken = getAdminToken();

        CreatePensionerCommand pensioner = new CreatePensionerCommand("Antoni", "Bagiński", "63070824763", 1.88, 75.5,
                "antoni.baginski@wp.pl",  LocalDate.of(1963,7,8), 3600.51, 20);
        String type = "pensioner";
        Map<String, Object> pensionerMap = new HashMap<>();
        pensionerMap.put("firstName", pensioner.getFirstName());
        pensionerMap.put("lastName", pensioner.getLastName());
        pensionerMap.put("pesel", pensioner.getPesel());
        pensionerMap.put("height", pensioner.getHeight());
        pensionerMap.put("weight", pensioner.getWeight());
        pensionerMap.put("email", pensioner.getEmail());
        pensionerMap.put("pension", pensioner.getPension());
        pensionerMap.put("workedYears", pensioner.getWorkedYears());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(pensionerMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(pensioner.getPesel(), type);
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isCreated());

        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?pesel=" + pensioner.getPesel())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(pensioner.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(pensioner.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value(pensioner.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(pensioner.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(pensioner.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(pensioner.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pension").value(pensioner.getPension()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].workedYears").value(pensioner.getWorkedYears()));
    }

    @Test
    void createPersonShouldAddEmployee() throws Exception {
        //given
        String adminToken = getAdminToken();

        CreateEmployeeCommand employee = new CreateEmployeeCommand("Waldemar", "Gruszka", "55012264594", 1.72, 78.5,
                "waldemar.gruszka@wp.pl", LocalDate.of(1955,1,22), LocalDate.of(2018, 10, 25), "Kierownik Seals", 17000.45);
        String type = "employee";
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put("firstName", employee.getFirstName());
        employeeMap.put("lastName", employee.getLastName());
        employeeMap.put("pesel", employee.getPesel());
        employeeMap.put("height", employee.getHeight());
        employeeMap.put("weight", employee.getWeight());
        employeeMap.put("email", employee.getEmail());
        employeeMap.put("hireDate", employee.getHireDate().toString());
        employeeMap.put("position", employee.getPosition());
        employeeMap.put("salary", employee.getSalary());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(employeeMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(employee.getPesel(), type);
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isCreated());

        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?pesel=" + employee.getPesel())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(employee.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(employee.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value(employee.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(employee.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(employee.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(employee.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].hireDate").value(employee.getHireDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].position").value(employee.getPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].salary").value(employee.getSalary()));
    }

    @Test
    @WithMockUser(username = "AdamAdmin", roles = {"ADMIN"})
    void createPersonShouldReturnUC_STUDENT_PESELException() throws Exception {
        //given
        CreateStudentCommand student = new CreateStudentCommand("Katarzyna", "Nowak", "72082782183", 1.75, 70.0,
                "katarzyna.nowak@gmail.com", LocalDate.of(1972,8,27), "Uniwersytet Warszawski", 2, "Psychologia", 1500.00);
        String type = "student";
        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("firstName", student.getFirstName());
        studentMap.put("lastName", student.getLastName());
        studentMap.put("pesel", student.getPesel());
        studentMap.put("height", student.getHeight());
        studentMap.put("weight", student.getWeight());
        studentMap.put("email", student.getEmail());
        studentMap.put("universityName", student.getUniversityName());
        studentMap.put("studyYear", student.getStudyYear());
        studentMap.put("studyField", student.getStudyField());
        studentMap.put("scholarship", student.getScholarship());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(studentMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(student.getPesel(), type);
        assertEquals(isExist, true);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("UC_STUDENT_PESEL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser(username = "AdamAdmin", roles = {"ADMIN"})
    void createPersonShouldReturnExceptionWhenTypeIsPensionerAndDataIsForStudent() throws Exception {
        //given
        CreateStudentCommand student = new CreateStudentCommand("Katarzyna", "Nowak", "03300214957", 1.75, 70.0,
                "katarzyna.nowak@gmail.com",LocalDate.of(2003,10,2),  "Uniwersytet Warszawski", 2, "Psychologia", 1500.00);
        String type = "pensioner";
        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("firstName", student.getFirstName());
        studentMap.put("lastName", student.getLastName());
        studentMap.put("pesel", student.getPesel());
        studentMap.put("height", student.getHeight());
        studentMap.put("weight", student.getWeight());
        studentMap.put("email", student.getEmail());
        studentMap.put("universityName", student.getUniversityName());
        studentMap.put("studyYear", student.getStudyYear());
        studentMap.put("studyField", student.getStudyField());
        studentMap.put("scholarship", student.getScholarship());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(studentMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(student.getPesel(), type);
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("NULL not allowed for column \"PENSION\"")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser(username = "AdamAdmin", roles = {"ADMIN"})
    void createPersonShouldReturnExceptionWhenPersonTypeNotExists() throws Exception {
        //given
        CreateStudentCommand student = new CreateStudentCommand("Katarzyna", "Nowak", "03300214957", 1.75, 70.0,
                "katarzyna.nowak@gmail.com", LocalDate.of(2003,10,2), "Uniwersytet Warszawski", 2, "Psychologia", 1500.00);
        String type = "uczen";
        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("firstName", student.getFirstName());
        studentMap.put("lastName", student.getLastName());
        studentMap.put("pesel", student.getPesel());
        studentMap.put("height", student.getHeight());
        studentMap.put("weight", student.getWeight());
        studentMap.put("email", student.getEmail());
        studentMap.put("universityName", student.getUniversityName());
        studentMap.put("studyYear", student.getStudyYear());
        studentMap.put("studyField", student.getStudyField());
        studentMap.put("scholarship", student.getScholarship());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(studentMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("message: Wrong person type!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser(username = "AdamAdmin", roles = {"ADMIN"})
    void createPersonShouldReturnExceptionBecauseTeacherNotExistsInPositionNameDictionary() throws Exception {
        //given
        CreateEmployeeCommand employee = new CreateEmployeeCommand("Waldemar", "Karolak", "79102593767", 1.72, 78.5,
                "waldemar.karolak@wp.pl", LocalDate.of(1979,10,25), LocalDate.of(2018, 10, 25), "Teacher", 7000.45);
        String type = "employee";
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put("firstName", employee.getFirstName());
        employeeMap.put("lastName", employee.getLastName());
        employeeMap.put("pesel", employee.getPesel());
        employeeMap.put("height", employee.getHeight());
        employeeMap.put("weight", employee.getWeight());
        employeeMap.put("email", employee.getEmail());
        employeeMap.put("hireDate", employee.getHireDate().toString());
        employeeMap.put("position", employee.getPosition());
        employeeMap.put("salary", employee.getSalary());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(employeeMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(employee.getPesel(), type);
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("Wrong position name!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void createPersonShouldReturnExceptionBecauseUserIsForbidden() throws Exception {
        //given
        String userToken = getUserToken();

        CreateEmployeeCommand employee = new CreateEmployeeCommand("Waldemar", "Karolak", "79102593767", 1.72, 78.5,
                "waldemar.karolak@wp.pl",LocalDate.of(1979,10,25),  LocalDate.of(2018, 10, 25), "Kowal", 7000.45);
        String type = "employee";
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put("firstName", employee.getFirstName());
        employeeMap.put("lastName", employee.getLastName());
        employeeMap.put("pesel", employee.getPesel());
        employeeMap.put("height", employee.getHeight());
        employeeMap.put("weight", employee.getWeight());
        employeeMap.put("email", employee.getEmail());
        employeeMap.put("hireDate", employee.getHireDate().toString());
        employeeMap.put("position", employee.getPosition());
        employeeMap.put("salary", employee.getSalary());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(employeeMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(employee.getPesel(), type);
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void createPersonShouldReturnExceptionBecauseUserIsUnauthorized() throws Exception {
        //given
        CreateEmployeeCommand employee = new CreateEmployeeCommand("Waldemar", "Karolak", "79102593767", 1.72, 78.5,
                "waldemar.karolak@wp.pl", LocalDate.of(1979,10,25), LocalDate.of(2018, 10, 25), "Kowal", 7000.45);
        String type = "employee";
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put("firstName", employee.getFirstName());
        employeeMap.put("lastName", employee.getLastName());
        employeeMap.put("pesel", employee.getPesel());
        employeeMap.put("height", employee.getHeight());
        employeeMap.put("weight", employee.getWeight());
        employeeMap.put("email", employee.getEmail());
        employeeMap.put("hireDate", employee.getHireDate().toString());
        employeeMap.put("position", employee.getPosition());
        employeeMap.put("salary", employee.getSalary());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(employeeMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(employee.getPesel(), type);
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void editPersonShouldEditStudent() throws Exception {
        //given
        String adminToken = getAdminToken();

        CreateStudentCommand student = new CreateStudentCommand("Marta", "Wójcik", "89110459476", 1.68, 55.0,
                "marta.wojcik@gmail.com", LocalDate.of(1989,11,4), "Akademia Górniczo-Hutnicza", 1, "Architektura", 1600.00);
        //changes
        student.setLastName("Opyrchał");
        student.setWeight(57.0);
        student.setEmail("marta.opyrchal@gmail.com");
        student.setStudyYear(2);

        String type = "student";
        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("lastName", student.getLastName());
        studentMap.put("weight", student.getWeight());
        studentMap.put("email", student.getEmail());
        studentMap.put("studyYear", student.getStudyYear());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(studentMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(student.getPesel(), type);
        assertEquals(isExist, true);

        Student student1 = (Student) personService.getPersonByTypeAndPesel("89110459476", "student");
        System.out.println(student1);

        postman.perform(MockMvcRequestBuilders.patch("/persons/" + student.getPesel())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk());

        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?pesel=" + student.getPesel())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(student.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(student.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value(student.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(student.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(student.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(student.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].universityName").value(student.getUniversityName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studyField").value(student.getStudyField()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studyYear").value(student.getStudyYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].scholarship").value(student.getScholarship()));
    }

    @Test
    void editPersonShouldEditPensioner() throws Exception {
        //given
        String adminToken = getAdminToken();
        CreatePensionerCommand pensioner = new CreatePensionerCommand("Tomasz", "Słowik", "61100992655", 1.79, 72.0,
                "tomasz.slowik@gmail.com", LocalDate.of(1961,10,9), 5500.0, 33);
        //changes
        pensioner.setEmail("t.slowik@onet.pl");
        pensioner.setPension(5700.69);

        String type = "pensioner";
        Map<String, Object> pensionerMap = new HashMap<>();
        pensionerMap.put("email", pensioner.getEmail());
        pensionerMap.put("pension", pensioner.getPension());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(pensionerMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(pensioner.getPesel(), type);
        assertEquals(isExist, true);

        postman.perform(MockMvcRequestBuilders.patch("/persons/" + pensioner.getPesel())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk());

        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?pesel=" + pensioner.getPesel())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(pensioner.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(pensioner.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value(pensioner.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(pensioner.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(pensioner.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(pensioner.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pension").value(pensioner.getPension()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].workedYears").value(pensioner.getWorkedYears()));
    }

    @Test
    @WithMockUser(username = "AdamAdmin", roles = {"ADMIN"})
    void editPersonShouldEditEmployee() throws Exception {
        //given
        CreateEmployeeCommand employee = new CreateEmployeeCommand("Katarzyna", "Nowak", "67121848249", 1.75, 70.0,
                "katarzyna.nowak@gmail.com", LocalDate.of(1967,12,18), LocalDate.of(2023, 11, 30), "Programista", 17000.45);
        //changes
        employee.setHeight(1.74);
        employee.setPosition("Kowal");
        employee.setSalary(14000.00);

        String type = "employee";
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put("height", employee.getHeight());
        employeeMap.put("position", employee.getPosition());
        employeeMap.put("salary", employee.getSalary());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(employeeMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(employee.getPesel(), type);
        assertEquals(isExist, true);

        postman.perform(MockMvcRequestBuilders.patch("/persons/" + employee.getPesel())
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk());

        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?pesel=" + employee.getPesel()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(employee.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(employee.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].pesel").value(employee.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].height").value(employee.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].weight").value(employee.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(employee.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].version").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].hireDate").value(employee.getHireDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].position").value(employee.getPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].salary").value(employee.getSalary()));
    }

    @Test
    void editPersonShouldThrowExceptionWhenConcurrentModificationOccurs() throws Exception {
        // given
        String adminToken = getAdminToken();

        String pesel = "67012949938";
        String type = "pensioner";
        CreatePensionerCommand pensioner = new CreatePensionerCommand("Natalia", "Borowska", pesel, 1.67, 55.0,
                "natalia.borowska@gmail.com", LocalDate.parse(CommonService.getBirthday(pesel)), 5200.25, 28);

        //changes
        pensioner.setPension(3700.69);
        Map<String, Object> pensionerMap = new HashMap<>();
        pensionerMap.put("pension", pensioner.getPension());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(pensionerMap);
        String pensionerJson = objectMapper.writeValueAsString(createPersonCommand);

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        AtomicBoolean isOkReceived = new AtomicBoolean(false);
        AtomicBoolean isConflictReceived = new AtomicBoolean(false);

        Runnable task = () -> {
            try {
                postman.perform(MockMvcRequestBuilders.patch("/persons/" + pesel)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pensionerJson))
                        .andExpect(result -> {
                            if (result.getResponse().getStatus() == HttpStatus.OK.value()) {
                                isOkReceived.set(true);
                            } else if (result.getResponse().getStatus() == HttpStatus.CONFLICT.value()) {
                                isConflictReceived.set(true);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        };

        executorService.submit(task);
        executorService.submit(task);

        latch.await();
        executorService.shutdown();

        // then
        assertTrue(isOkReceived.get());
        assertTrue(isConflictReceived.get());
    }

    @Test
    void editPersonShouldReturnExceptionBecauseUserIsForbidden() throws Exception {
        //given
        String employeeToken = getEmployeeToken();

        CreateEmployeeCommand employee = new CreateEmployeeCommand("Katarzyna", "Nowak", "67121848249", 1.75, 70.0,
                "katarzyna.nowak@gmail.com",LocalDate.of(1967,12,18),  LocalDate.of(2023, 11, 30), "Programista", 17000.45);
        //changes
        employee.setHeight(1.72);
        employee.setPosition("Architekt");
        employee.setSalary(15000.00);

        String type = "employee";
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put("firstName", employee.getFirstName());
        employeeMap.put("lastName", employee.getLastName());
        employeeMap.put("pesel", employee.getPesel());
        employeeMap.put("height", employee.getHeight());
        employeeMap.put("weight", employee.getWeight());
        employeeMap.put("email", employee.getEmail());
        employeeMap.put("hireDate", employee.getHireDate().toString());
        employeeMap.put("position", employee.getPosition());
        employeeMap.put("salary", employee.getSalary());
        CreatePersonCommand createPersonCommand = new CreatePersonCommand();
        createPersonCommand.setType(type);
        createPersonCommand.setParameters(employeeMap);
        String studentJson = objectMapper.writeValueAsString(createPersonCommand);
        //when
        boolean isExist = personService.isPersonExists(employee.getPesel(), type);
        assertEquals(isExist, true);

        postman.perform(MockMvcRequestBuilders.patch("/persons/" + employee.getPesel())
                .header("Authorization", "Bearer " + employeeToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isForbidden());
    }

}