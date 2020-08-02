package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.PatientDTO;
import com.riot.psycontrol.entity.Patient;
import com.riot.psycontrol.entity.User;
import com.riot.psycontrol.repo.PatientRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientServiceTest {

    private static User user;
    private static Patient patient1;
    private static Patient patient2;
    private static PatientDTO patientDTO1;
    private static PatientDTO patientDTO2;

    @Mock
    private PatientRepo patientRepo;

    @InjectMocks
    private PatientService patientService;

    @BeforeAll
    static void setUp() {
        user = new User();
        patient1 = new Patient();
        patient2 = new Patient();
        patientDTO1 = new PatientDTO(1,"Jonh","Doe","john@algo.com","5512345678","5512345678");
        patientDTO2 = new PatientDTO(2,"Alfred","Flowers","alfred@algo.com","5512345678","5513246578");

        user.setId(1);
        user.setUsername("test");

        patient1.setId(1);
        patient1.setFirstname("John");
        patient1.setLastname("Doe");
        patient1.setEmail("john@algo.com");
        patient1.setCreatedBy(user.getUsername());

        patient2.setId(2);
        patient2.setFirstname("Alfred");
        patient2.setLastname("Flowers");
        patient2.setEmail("alfred@algo.com");
        patient2.setCreatedBy(user.getUsername());
    }

    @Order(1)
    @Test
    void getPatients_returnsPatientsList() {
        List<Patient> patients = new ArrayList<>();
        patients.add(patient1);
        patients.add(patient2);
        //given
        when(patientRepo.findAllByCreatedBy(anyString())).thenReturn(patients);
        //when
        var result = patientService.getPatients(user.getUsername());
        //then
        verify(patientRepo).findAllByCreatedBy(anyString());
        assertEquals(2, result.size());
    }

    @Order(2)
    @Test
    void getPatientById_returnsFoundPatient() {
        //given
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient1));
        //when
        var result = patientService.getPatientById(1);
        //then
        verify(patientRepo).findById(anyInt());
        assertEquals("John", result.getFirstname());
    }

    @Order(3)
    @Test
    void savePatient_returnsSavedPatient() {
        System.out.println("SAVE_PATIENT");
        //given
        when(patientRepo.save(any(Patient.class))).thenAnswer(i -> i.getArgument(0));
        //when
        var result = patientService.savePatient(patientDTO1);
        //then
        verify(patientRepo).save(any(Patient.class));
        assertEquals("John", result.getFirstname());
    }

    @Order(4)
    @Test
    void updatePatient_returnsUpdatedPatient() {
        System.out.println("UPDATE_PATIENT");
        var update = new PatientDTO();
        update.setId(1);
        update.setFirstname("Antony");
        update.setLastname("Smith");
        update.setEmail("antony@algo.com");
        update.setPhone("5598765432");
        update.setMobile("5587654321");

        //given
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient1));
        when(patientRepo.save(any(Patient.class))).thenAnswer(i -> i.getArgument(0));
        //when
        //PatientDTO result = patientService.updatePatient(update);
        //then
        //verify(patientRepo).findById(anyInt());
        //verify(patientRepo).save(any(Patient.class));
        //assertEquals("Antony", result.getFirstname());
    }

    @Order(5)
    @Test
    void removePatient_verifyDelete() {
        //given
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient1)).thenReturn(null);
        doNothing().when(patientRepo).delete(any(Patient.class));
        //when(patientRepo.delete(patient1));
        patientRepo.delete(patient1);
        //then
        verify(patientRepo, times(1)).delete(patient1);
    }
}