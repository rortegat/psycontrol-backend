package com.riot.psycontrol.service;

import com.riot.psycontrol.PsyControlApplication;
import com.riot.psycontrol.dto.PatientDTO;
import com.riot.psycontrol.entity.Patient;
import com.riot.psycontrol.entity.User;
import com.riot.psycontrol.repo.PatientRepo;
import com.riot.psycontrol.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientServiceImplTest {

    private static Logger LOG = LoggerFactory.getLogger(PsyControlApplication.class);

    private static User user;
    private static Patient patient1;
    private static Patient patient2;
    private static PatientDTO patientDTO;
    private static ModelMapper modelMapper;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PatientRepo patientRepo;

    @InjectMocks
    private PatientServiceImpl patientServiceImpl;

    @BeforeAll
    static void setUp() {
        modelMapper = new ModelMapper();
        user = new User();
        patient1 = new Patient();
        patient2 = new Patient();
        patientDTO = new PatientDTO(1, "John", "Doe", "john@algo.com", "5512345678", "5512345678");

        user.setId(1);
        user.setUsername("test");

        patient1.setId(1);
        patient1.setFirstname("John");
        patient1.setLastname("Doe");
        patient1.setEmail("john@algo.com");
        patient1.setMobile("5512346578");
        patient1.setCreatedBy(user.getUsername());

        patient2.setId(2);
        patient2.setFirstname("Alfred");
        patient2.setLastname("Flowers");
        patient2.setEmail("alfred@algo.com");
        patient2.setMobile("5512346578");
        patient2.setCreatedBy(user.getUsername());
    }

    @Order(1)
    @Test
    void getPatients_returnsPatientsList() {
        LOG.info("GET PATIENTS");
        List<Patient> patients = new ArrayList<>();
        patients.add(patient1);
        patients.add(patient2);
        //given
        when(mapper.map(any(Patient.class), eq(PatientDTO.class)))
                .thenAnswer(invocation -> modelMapper.map(invocation.getArgument(0), invocation.getArgument(1)));
        when(patientRepo.findAllByCreatedBy(anyString())).thenReturn(patients);
        //when
        var result = patientServiceImpl.getPatients(user.getUsername());
        //then
        verify(patientRepo).findAllByCreatedBy(anyString());
        assertEquals(2, result.size());
    }

    @Order(2)
    @Test
    void getPatientById_returnsFoundPatient() {
        LOG.info("GET PATIENT BY ID");
        //given
        when(mapper.map(any(), any())).thenAnswer(invocation -> {
            Arrays.stream(invocation.getArguments()).forEach(System.out::println);
            return modelMapper.map(invocation.getArgument(0), invocation.getArgument(1));
        });
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient1));
        //when
        var result = patientServiceImpl.getPatientById(1);
        //then
        verify(patientRepo).findById(anyInt());
        assertEquals("John", result.getFirstname());
    }

    @Order(3)
    @Test
    void savePatient_returnsSavedPatient() {
        LOG.info("SAVE_PATIENT");
        //given
        when(mapper.map(any(), any())).thenAnswer(invocation ->
                modelMapper.map(invocation.getArgument(0),invocation.getArgument(1)));
        when(patientRepo.save(any(Patient.class))).thenAnswer(i -> i.getArgument(0));
        //when
        var result = patientServiceImpl.savePatient(patientDTO);
        //then
        verify(patientRepo).save(any(Patient.class));
        assertEquals("John", result.getFirstname());
    }

    @Order(4)
    @Test
    void updatePatient_returnsUpdatedPatient() {
        LOG.info("UPDATE_PATIENT");
        var update = new PatientDTO(1, "Antony", "Smith", "antony@algo.com", "5512345678", "5512345678");
        //given
        when(mapper.map(any(), any())).thenAnswer(invocation ->
                modelMapper.map(invocation.getArgument(0),invocation.getArgument(1)));
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient1));
        when(patientRepo.save(any(Patient.class))).thenAnswer(i -> i.getArgument(0));
        //when
        PatientDTO result = patientServiceImpl.updatePatient(update);
        //then
        verify(patientRepo).findById(anyInt());
        verify(patientRepo).save(any(Patient.class));
        assertEquals("Antony", result.getFirstname());
    }

    @Order(5)
    @Test
    void removePatient_verifyDelete() {
        LOG.info("REMOVE PATIENT");
        //given
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient1)).thenReturn(null);
        doNothing().when(patientRepo).delete(any(Patient.class));
        //when(patientRepo.delete(patient1));
        patientRepo.delete(patient1);
        //then
        verify(patientRepo, times(1)).delete(patient1);
    }
}