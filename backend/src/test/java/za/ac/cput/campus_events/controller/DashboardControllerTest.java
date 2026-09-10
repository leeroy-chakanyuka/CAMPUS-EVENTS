package za.ac.cput.campus_events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.ac.cput.campus_events.domain.*;
import za.ac.cput.campus_events.service.IDashboardService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDashboardService dashboardService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faculty  faculty;
    private Student  student;
    private Organiser organiser;
    private Event    event;
    private Admin    admin;

    @BeforeEach
    void setUp() {
        faculty = new Faculty.Builder()
                .name("Faculty of Engineering")
                .contactEmail("eng@cput.ac.za")
                .status("ACTIVE")
                .createdByAdminId(1L)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        student = new Student.Builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@cput.ac.za")
                .studentNumber("220123456")
                .verificationStatus("VERIFIED")
                .facultyId(1L)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        organiser = new Organiser.Builder()
                .firstName("John")
                .lastName("Smith")
                .email("john@cput.ac.za")
                .role("EVENT_COORDINATOR")
                .facultyId(1L)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        event = new Event.Builder()
                .title("Tech Expo 2025")
                .description("Annual tech expo")
                .eventDate(LocalDateTime.now().plusDays(10))
                .capacity(200)
                .open(true)
                .facultyId(1L)
                .organiserId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        admin = new Admin.Builder()
                .firstName("Peter")
                .lastName("Jones")
                .email("peter@cput.ac.za")
                .password("password123")
                .createdAt(LocalDateTime.now())
                .build();
    }



    @Test
    void testGetAllFaculties_ShouldReturn200() throws Exception {
        when(dashboardService.getAllFaculties()).thenReturn(List.of(faculty));

        mockMvc.perform(get("/dashboard/faculty"))
                .andExpect(status().isOk());

        verify(dashboardService, times(1)).getAllFaculties();
    }

    @Test
    void testGetAllFaculties_EmptyList_ShouldReturn200() throws Exception {
        when(dashboardService.getAllFaculties()).thenReturn(List.of());

        mockMvc.perform(get("/dashboard/faculty"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateFaculty_ValidInputs_ShouldReturn200() throws Exception {
        when(dashboardService.createFaculty(
                "Faculty of Engineering", "eng@cput.ac.za", 1L))
                .thenReturn(faculty);

        Map<String, Object> body = Map.of(
                "name",         "Faculty of Engineering",
                "contactEmail", "eng@cput.ac.za",
                "adminId",      1
        );

        mockMvc.perform(post("/dashboard/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateFaculty_InvalidEmail_ShouldReturn400() throws Exception {
        when(dashboardService.createFaculty(
                "Faculty of Engineering", "invalidemail", 1L))
                .thenThrow(new RuntimeException("Valid contact email is required"));

        Map<String, Object> body = Map.of(
                "name",         "Faculty of Engineering",
                "contactEmail", "invalidemail",
                "adminId",      1
        );

        mockMvc.perform(post("/dashboard/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Valid contact email is required"));
    }

    @Test
    void testCreateFaculty_DuplicateName_ShouldReturn400() throws Exception {
        when(dashboardService.createFaculty(
                "Faculty of Engineering", "eng@cput.ac.za", 1L))
                .thenThrow(new RuntimeException(
                        "Faculty with name 'Faculty of Engineering' already exists"));

        Map<String, Object> body = Map.of(
                "name",         "Faculty of Engineering",
                "contactEmail", "eng@cput.ac.za",
                "adminId",      1
        );

        mockMvc.perform(post("/dashboard/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateFacultyStatus_Deactivate_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .updateFacultyStatus(1L, false, 1L);

        Map<String, Object> body = Map.of(
                "active",            false,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/faculty/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Faculty status updated successfully"));
    }

    @Test
    void testUpdateFacultyStatus_Activate_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .updateFacultyStatus(1L, true, 1L);

        Map<String, Object> body = Map.of(
                "active",            true,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/faculty/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Faculty status updated successfully"));
    }

    @Test
    void testUpdateFacultyStatus_NotFound_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Faculty not found: 99"))
                .when(dashboardService).updateFacultyStatus(99L, false, 1L);

        Map<String, Object> body = Map.of(
                "active",            false,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/faculty/99/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Faculty not found: 99"));
    }


    @Test
    void testGetAllStudents_ShouldReturn200() throws Exception {
        when(dashboardService.getAllStudents()).thenReturn(List.of(student));

        mockMvc.perform(get("/dashboard/student"))
                .andExpect(status().isOk());

        verify(dashboardService, times(1)).getAllStudents();
    }

    @Test
    void testUpdateStudentStatus_Suspend_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .updateStudentStatus(1L, false, 1L);

        Map<String, Object> body = Map.of(
                "active",            false,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/student/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Student status updated successfully"));
    }

    @Test
    void testUpdateStudentStatus_Reactivate_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .updateStudentStatus(1L, true, 1L);

        Map<String, Object> body = Map.of(
                "active",            true,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/student/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Student status updated successfully"));
    }

    @Test
    void testUpdateStudentStatus_NotFound_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Student not found: 99"))
                .when(dashboardService).updateStudentStatus(99L, false, 1L);

        Map<String, Object> body = Map.of(
                "active",            false,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/student/99/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Student not found: 99"));
    }



    @Test
    void testGetAllOrganisers_ShouldReturn200() throws Exception {
        when(dashboardService.getAllOrganisers()).thenReturn(List.of(organiser));

        mockMvc.perform(get("/dashboard/organiser"))
                .andExpect(status().isOk());

        verify(dashboardService, times(1)).getAllOrganisers();
    }

    @Test
    void testUpdateOrganiserStatus_Suspend_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .updateOrganiserStatus(1L, false, 1L);

        Map<String, Object> body = Map.of(
                "active",            false,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/organiser/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Organiser status updated successfully"));
    }

    @Test
    void testUpdateOrganiserStatus_Reactivate_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .updateOrganiserStatus(1L, true, 1L);

        Map<String, Object> body = Map.of(
                "active",            true,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/organiser/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Organiser status updated successfully"));
    }

    @Test
    void testUpdateOrganiserStatus_NotFound_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Organiser not found: 99"))
                .when(dashboardService).updateOrganiserStatus(99L, false, 1L);

        Map<String, Object> body = Map.of(
                "active",            false,
                "requestingAdminId", 1
        );

        mockMvc.perform(put("/dashboard/organiser/99/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Organiser not found: 99"));
    }


    @Test
    void testGetAllEvents_ShouldReturn200() throws Exception {
        when(dashboardService.getAllEvents()).thenReturn(List.of(event));

        mockMvc.perform(get("/dashboard/event"))
                .andExpect(status().isOk());

        verify(dashboardService, times(1)).getAllEvents();
    }

    @Test
    void testForceCancelEvent_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService).forceCancelEvent(1L, 1L);

        Map<String, Object> body = Map.of("requestingAdminId", 1);

        mockMvc.perform(put("/dashboard/event/1/force-cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Event cancelled successfully"));
    }

    @Test
    void testForceCancelEvent_NotFound_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Event not found: 99"))
                .when(dashboardService).forceCancelEvent(99L, 1L);

        Map<String, Object> body = Map.of("requestingAdminId", 1);

        mockMvc.perform(put("/dashboard/event/99/force-cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Event not found: 99"));
    }



    @Test
    void testGetAllAdmins_ShouldReturn200() throws Exception {
        when(dashboardService.getAllAdmins()).thenReturn(List.of(admin));

        mockMvc.perform(get("/dashboard/admin"))
                .andExpect(status().isOk());

        verify(dashboardService, times(1)).getAllAdmins();
    }

    @Test
    void testCreateAdmin_ValidInputs_ShouldReturn200() throws Exception {
        when(dashboardService.createAdmin(
                "Peter","Jones","peter@cput.ac.za","password123",1L))
                .thenReturn(admin);

        Map<String, Object> body = Map.of(
                "firstName",         "Peter",
                "lastName",          "Jones",
                "email",             "peter@cput.ac.za",
                "temporaryPassword", "password123",
                "requestingAdminId", 1
        );

        mockMvc.perform(post("/dashboard/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateAdmin_DuplicateEmail_ShouldReturn400() throws Exception {
        when(dashboardService.createAdmin(
                "Peter","Jones","peter@cput.ac.za","password123",1L))
                .thenThrow(new RuntimeException("Email already used by another admin"));

        Map<String, Object> body = Map.of(
                "firstName",         "Peter",
                "lastName",          "Jones",
                "email",             "peter@cput.ac.za",
                "temporaryPassword", "password123",
                "requestingAdminId", 1
        );

        mockMvc.perform(post("/dashboard/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already used by another admin"));
    }

    @Test
    void testCreateAdmin_NotAdminRequester_ShouldReturn400() throws Exception {
        when(dashboardService.createAdmin(
                "Peter","Jones","peter@cput.ac.za","password123",null))
                .thenThrow(new IllegalStateException("Admin only"));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("firstName",         "Peter");
        body.put("lastName",          "Jones");
        body.put("email",             "peter@cput.ac.za");
        body.put("temporaryPassword", "password123");
        body.put("requestingAdminId", null);

        mockMvc.perform(post("/dashboard/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePassword_ValidInputs_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .changePassword(1L, "oldpass", "newpass");

        Map<String, Object> body = Map.of(
                "adminId",         1,
                "currentPassword", "oldpass",
                "newPassword",     "newpass"
        );

        mockMvc.perform(put("/dashboard/admin/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));
    }

    @Test
    void testChangePassword_WrongCurrentPassword_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Current password is incorrect"))
                .when(dashboardService).changePassword(1L, "wrongpass", "newpass");

        Map<String, Object> body = Map.of(
                "adminId",         1,
                "currentPassword", "wrongpass",
                "newPassword",     "newpass"
        );

        mockMvc.perform(put("/dashboard/admin/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Current password is incorrect"));
    }

    @Test
    void testChangePassword_AdminNotFound_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Admin not found: 99"))
                .when(dashboardService).changePassword(99L, "oldpass", "newpass");

        Map<String, Object> body = Map.of(
                "adminId",         99,
                "currentPassword", "oldpass",
                "newPassword",     "newpass"
        );

        mockMvc.perform(put("/dashboard/admin/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Admin not found: 99"));
    }


    @Test
    void testSendNotification_ValidInputs_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .sendNotification("Student", 1L, "Jane Doe", "Hello Jane!");

        Map<String, Object> body = Map.of(
                "recipientType", "Student",
                "recipientId",   1,
                "recipient",     "Jane Doe",
                "message",       "Hello Jane!"
        );

        mockMvc.perform(post("/dashboard/notification/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification sent successfully"));
    }

    @Test
    void testSendNotification_ToOrganiser_ShouldReturn200() throws Exception {
        doNothing().when(dashboardService)
                .sendNotification("Organiser", 1L, "John Smith", "Your event was reviewed.");

        Map<String, Object> body = Map.of(
                "recipientType", "Organiser",
                "recipientId",   1,
                "recipient",     "John Smith",
                "message",       "Your event was reviewed."
        );

        mockMvc.perform(post("/dashboard/notification/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification sent successfully"));
    }

    @Test
    void testSendNotification_EmptyMessage_ShouldReturn400() throws Exception {
        doThrow(new RuntimeException("Message is required"))
                .when(dashboardService)
                .sendNotification("Student", 1L, "Jane Doe", "");

        Map<String, Object> body = Map.of(
                "recipientType", "Student",
                "recipientId",   1,
                "recipient",     "Jane Doe",
                "message",       ""
        );

        mockMvc.perform(post("/dashboard/notification/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Message is required"));
    }
}
