package dh.backend.test;

import dh.backend.dao.impl.DomicilioDAO;
import dh.backend.dao.impl.PacienteDao;
import dh.backend.model.Domicilio;
import dh.backend.model.Paciente;
import dh.backend.service.PacienteService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class PacienteTest {

    private static Logger LOGGER = Logger.getLogger(PacienteTest.class);

    @BeforeAll
    static void crearTablas(){
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/db_clinica_1605;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
    @Test
    @DisplayName("Testeo ingreso de paciente")

    void TestPacienteGuardado(){
        Paciente paciente=new Paciente("Alzamora","Luis","12348765", LocalDate.of(2024,5,16),
                new Domicilio("Jr. Libertad",860,"Magdalena","Lima"));

        PacienteService pacienteService=new PacienteService(new PacienteDao());
        Paciente pacienteRegistrado=pacienteService.registrarPaciente(paciente);


        assertNotNull(pacienteRegistrado);
    }

    @Test
    @DisplayName("Testeo busqueda de paciente por id")

    void TestBusquedaID(){
        Integer id=1;
        PacienteService pacienteService=new PacienteService(new PacienteDao());
        Paciente pacienteEncontrado=pacienteService.buscarPorId(id);

        assertEquals(id,pacienteEncontrado.getId());
    }

}
