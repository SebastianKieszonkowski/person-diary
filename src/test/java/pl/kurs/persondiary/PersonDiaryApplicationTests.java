package pl.kurs.persondiary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.command.singleCommand.CreateEmployeeCommand;
import pl.kurs.persondiary.command.singleCommand.CreatePensionerCommand;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;
import pl.kurs.persondiary.controllers.PersonController;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.repositories.PersonViewRepository;
import pl.kurs.persondiary.security.jwt.UserRepository;
import pl.kurs.persondiary.services.PersonService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeEach
    public void init1() throws Exception {
        String jsonRequest = "{\"username\":\"AdamAdmin\", \"password\":\"admin\"}";

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        tokenAdmin = objectMapper.readTree(responseString).get("jwtToken").textValue();
    }

    @BeforeEach
    public void init2() throws Exception {
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("Field: salaryTo / rejected value: 'duzo'")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnUnauthorizedException() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAddStudent() throws Exception {
        //given
        CreateStudentCommand student = new CreateStudentCommand("Jan", "Kowalski", "72030491266", 1.78, 65.5,
                "jan.kowalski@wp.pl", "Politechnika Lubelska", 1, "Matematyka", 600.51);
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
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isCreated());

        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?pesel=" + student.getPesel())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(student.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(student.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pesel").value(student.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].height").value(student.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].weight").value(student.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(student.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].universityName").value(student.getUniversityName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studyField").value(student.getStudyField()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studyYear").value(student.getStudyYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].scholarship").value(student.getScholarship()));
    }

    @Test
    void shouldAddPensioner() throws Exception {
        //given
        CreatePensionerCommand pensioner = new CreatePensionerCommand("Antoni", "Bagiński", "63070824763", 1.88, 75.5,
                "antoni.baginski@wp.pl", 3600.51, 20);
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
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isCreated());

        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?pesel=" + pensioner.getPesel())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(pensioner.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(pensioner.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pesel").value(pensioner.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].height").value(pensioner.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].weight").value(pensioner.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(pensioner.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pension").value(pensioner.getPension()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].workedYears").value(pensioner.getWorkedYears()));
    }

    @Test
    void shouldAddEmployee() throws Exception {
        //given
        CreateEmployeeCommand employee = new CreateEmployeeCommand("Waldemar", "Gruszka", "64053057877", 1.72, 78.5,
                "waldemar.gruszka@wp.pl", LocalDate.of(2018, 10, 25), "Kierownik Seals", 17000.45);
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
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isCreated());

        //then
        postman.perform(MockMvcRequestBuilders.get("/persons?pesel=" + employee.getPesel())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value(type))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(employee.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(employee.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pesel").value(employee.getPesel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].height").value(employee.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].weight").value(employee.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(employee.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].version").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].hireDate").value(employee.getHireDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].position").value(employee.getPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(employee.getSalary()));
    }

    @Test
    void shouldReturnUC_STUDENT_PESELException() throws Exception {
        //given
        CreateStudentCommand student = new CreateStudentCommand("Katarzyna", "Nowak", "72082782183", 1.75, 70.0,
                "katarzyna.nowak@gmail.com", "Uniwersytet Warszawski", 2, "Psychologia", 1500.00);
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
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("UC_STUDENT_PESEL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnExceptionWhenTypeIsPensionerAndDataIsForStudent() throws Exception {
        //given
        CreateStudentCommand student = new CreateStudentCommand("Katarzyna", "Nowak", "03300214957", 1.75, 70.0,
                "katarzyna.nowak@gmail.com", "Uniwersytet Warszawski", 2, "Psychologia", 1500.00);
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
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("NULL not allowed for column \"PENSION\"")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnExceptionWhenPersonTypeNotExists() throws Exception {
        //given
        CreateStudentCommand student = new CreateStudentCommand("Katarzyna", "Nowak", "03300214957", 1.75, 70.0,
                "katarzyna.nowak@gmail.com", "Uniwersytet Warszawski", 2, "Psychologia", 1500.00);
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
        boolean isExist = personService.isPersonExists(student.getPesel(), type);
        assertEquals(isExist, false);

        postman.perform(MockMvcRequestBuilders.post("/persons")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("message: Wrong person type!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnExceptionBecauseTeacherNotExistsInPositionNameDictionary() throws Exception {
        //given
        CreateEmployeeCommand employee = new CreateEmployeeCommand("Waldemar", "Karolak", "79102593767", 1.72, 78.5,
                "waldemar.karolak@wp.pl", LocalDate.of(2018, 10, 25), "Teacher", 7000.45);
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
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessages[0]").value(Matchers.containsString("Wrong position name!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnExceptionBecauseUserIsForbidenToPostRequest() throws Exception {
        //given
        CreateEmployeeCommand employee = new CreateEmployeeCommand("Waldemar", "Karolak", "79102593767", 1.72, 78.5,
                "waldemar.karolak@wp.pl", LocalDate.of(2018, 10, 25), "Kowal", 7000.45);
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
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnExceptionBecauseUserIsUnauthorized() throws Exception {
        //given
        CreateEmployeeCommand employee = new CreateEmployeeCommand("Waldemar", "Karolak", "79102593767", 1.72, 78.5,
                "waldemar.karolak@wp.pl", LocalDate.of(2018, 10, 25), "Kowal", 7000.45);
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
