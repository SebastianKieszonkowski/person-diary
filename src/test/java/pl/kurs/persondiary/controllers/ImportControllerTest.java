package pl.kurs.persondiary.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;
import pl.kurs.persondiary.PersonDiaryApplication;
import pl.kurs.persondiary.services.EmployeePositionService;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PersonDiaryApplication.class, properties = "src/test/resources/application.properties")
@AutoConfigureMockMvc
class ImportControllerTest {

    @Autowired
    private MockMvc postman;

//    @Autowired
//    private PersonRepository personRepository;

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

    public String getImporterToken() throws Exception {
        String jsonRequest1 = "{\"username\":\"KarolImporter\", \"password\":\"importer\"}";

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
    void importCsvFileShouldUploadFileAsAdministrator() throws Exception {
        // given
        String adminToken = getAdminToken();
        File file = ResourceUtils.getFile("classpath:data/people_data.csv");
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                file.getName(),
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(file.toPath()));
        //when
//        Long personViewSizeBeforeImport = personRepository.getTableSize();
        MvcResult result = postman.perform(MockMvcRequestBuilders.multipart("/import/upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        String taskId = objectMapper.readTree(responseString).get("status").textValue().substring(38);

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            MvcResult progressResult = postman.perform(MockMvcRequestBuilders.get("/import/status/" + taskId)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andReturn();
            String progressResponseString = progressResult.getResponse().getContentAsString();
            String actualStatus = objectMapper.readTree(progressResponseString).get("status").textValue();
            if (actualStatus.equals("Completed")) {
                break;
            }
        }
        //then
        postman.perform(MockMvcRequestBuilders.get("/import/status/" + taskId)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Completed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.processedLines").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.failureLines").value(0));

//        Long personViewSizeAfterImport = personRepository.getTableSize();
//        Long personViewExpectedSize = personViewSizeBeforeImport + 1000L;
//
//        assertEquals(personViewExpectedSize, personViewSizeAfterImport);

    }

    @Test
    void importCsvFileShouldUploadFileAsImporter() throws Exception {
        // given
        String importerToken = getImporterToken();
        File file = ResourceUtils.getFile("classpath:data/people_data3.csv");
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                file.getName(),
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(file.toPath()));
        //when
//        Long personViewSizeBeforeImport = personRepository.getTableSize();
        MvcResult result = postman.perform(MockMvcRequestBuilders.multipart("/import/upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + importerToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        String taskId = objectMapper.readTree(responseString).get("status").textValue().substring(38);

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            MvcResult progressResult = postman.perform(MockMvcRequestBuilders.get("/import/status/" + taskId)
                    .header("Authorization", "Bearer " + importerToken))
                    .andExpect(status().isOk())
                    .andReturn();
            String progressResponseString = progressResult.getResponse().getContentAsString();
            String actualStatus = objectMapper.readTree(progressResponseString).get("status").textValue();
            if (actualStatus.equals("Completed")) {
                break;
            }
        }
        //then
        postman.perform(MockMvcRequestBuilders.get("/import/status/" + taskId)
                .header("Authorization", "Bearer " + importerToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Completed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.processedLines").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.failureLines").value(0));

//        Long personViewSizeAfterImport = personRepository.getTableSize();
//        Long personViewExpectedSize = personViewSizeBeforeImport + 1000L;
//
//        assertEquals(personViewExpectedSize, personViewSizeAfterImport);

    }

    @Test
    void importCsvFileShouldInterruptFirstImportWhenSecondImportStarts() throws Exception {
        // given
        String adminToken = getAdminToken();
        File firstFile = ResourceUtils.getFile("classpath:data/people_data2.csv");
        MockMultipartFile firstMockMultipartFile = new MockMultipartFile(
                "file",
                firstFile.getName(),
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(firstFile.toPath()));

        File secondFile = ResourceUtils.getFile("classpath:data/people_data2.csv");
        MockMultipartFile secondMockMultipartFile = new MockMultipartFile(
                "file",
                secondFile.getName(),
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(secondFile.toPath()));

        //when
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        AtomicBoolean isOkReceived = new AtomicBoolean(false);
        AtomicBoolean isConflictReceived = new AtomicBoolean(false);

        Runnable firstTask = () -> {
            try {
                postman.perform(MockMvcRequestBuilders.multipart("/import/upload")
                        .file(firstMockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + adminToken))
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

        Runnable secondTask = () -> {
            try {
                postman.perform(MockMvcRequestBuilders.multipart("/import/upload")
                        .file(secondMockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + adminToken))
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

        executorService.submit(firstTask);
        executorService.submit(secondTask);

        latch.await();
        executorService.shutdown();

        // then
        assertTrue(isOkReceived.get());
        assertTrue(isConflictReceived.get());
//        Long personViewSizeBeforeImport = personRepository.getTableSize();

//        MvcResult firstImportResult = postman.perform(MockMvcRequestBuilders.multipart("/import/upload")
//                .file(firstMockMultipartFile)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .header("Authorization", "Bearer " + adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String firstResponseString = firstImportResult.getResponse().getContentAsString();
//        String firstTaskId = objectMapper.readTree(firstResponseString).get("status").textValue().substring(38);
//
//        postman.perform(MockMvcRequestBuilders.multipart("/import/upload")
//                .file(secondMockMultipartFile)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .header("Authorization", "Bearer " + adminToken))
//                .andExpect(status().isConflict())
//                .andReturn();
//
//        String firstImportStatus = null;
//        for (int i = 0; i < 10; i++) {
//            Thread.sleep(1000);
//            MvcResult firstStatusResult = postman.perform(MockMvcRequestBuilders.get("/import/status/" + firstTaskId)
//                    .header("Authorization", "Bearer " + adminToken))
//                    .andExpect(status().isOk())
//                    .andReturn();
//            String firstStatusResponse = firstStatusResult.getResponse().getContentAsString();
//            firstImportStatus = objectMapper.readTree(firstStatusResponse).get("status").textValue();
//            if (firstImportStatus.equals("Completed")) {
//                break;
//            }
//        }

        // then
//        Long personViewSizeAfterImport = personRepository.getTableSize();
//        Long personViewExpectedSize = personViewSizeBeforeImport + 1000L;
//
//        assertEquals(personViewExpectedSize, personViewSizeAfterImport);
    }

    @Test
    void importCsvFileShouldThrowExceptionBecauseUserEmployeeUserIsForbidden() throws Exception {
        // given
        String employeeToken = getEmployeeToken();
        File file = ResourceUtils.getFile("classpath:data/people_data3.csv");
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                file.getName(),
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(file.toPath()));
        //when
        postman.perform(MockMvcRequestBuilders.multipart("/import/upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }
}