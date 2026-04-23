package dev.carlosmoises.projeto.enferm.controller;

import dev.carlosmoises.projeto.enferm.DTO.AppointmentResponseDTO;
import dev.carlosmoises.projeto.enferm.DTO.CreateAppointmentDTO;
import dev.carlosmoises.projeto.enferm.DTO.UpdateAppointmentDTO;
import dev.carlosmoises.projeto.enferm.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

     AppointmentController (AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping()
    public ResponseEntity<ResponseMessage> createAppointment(@RequestBody CreateAppointmentDTO createAppointmentDTO) {
         var appointmentId = appointmentService.createAppointment(createAppointmentDTO);

         var response = new ResponseMessage("Agendamento criado com sucesso.");

         return ResponseEntity.created(URI.create("/appointments/" + appointmentId)).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
         var appointments = appointmentService.getAllAppointments();

         return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable("appointmentId") Long appointmentId) {
        var appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getPatientAppointments(@PathVariable("patientId") Long patientId) {
         var patientAppointment = appointmentService.getAppointmentsByPatientId(patientId);

         return ResponseEntity.ok(patientAppointment);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody UpdateAppointmentDTO updateAppointmentDTO) {
         var updateAppointment = appointmentService.updateAppointment(appointmentId, updateAppointmentDTO);

         return ResponseEntity.ok(updateAppointment);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> deleteAppointment(@PathVariable("appointmentId") Long appointmentId) {
         appointmentService.deleteAppointment(appointmentId);
         return ResponseEntity.noContent().build();
    }
}
