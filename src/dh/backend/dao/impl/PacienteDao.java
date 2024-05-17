package dh.backend.dao.impl;

import dh.backend.dao.IDao;
import dh.backend.db.H2Connection;
import dh.backend.model.Domicilio;
import dh.backend.model.Paciente;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao implements IDao<Paciente> {
    private static Logger LOGGER = Logger.getLogger(PacienteDao.class);
    public static String SQL_INSERT = "INSERT INTO PACIENTES VALUES(DEFAULT,?,?,?,?,?)";
    public static String SQL_SELECT_ID = "SELECT * FROM PACIENTES WHERE ID=?";
    public static String SQL_SELECT_ALL = "SELECT * FROM PACIENTES";
    @Override
    public Paciente registrar(Paciente paciente) {
        Connection connection=null;

        // Como queremos obtener el Domicilio desde la tabla Domicilio, invocamos a DAO para crear el Domicilio

        DomicilioDAO domicilioDAO=new DomicilioDAO();
        Paciente pacienteARetornar=null;

        try{

            connection= H2Connection.getConnection();
            connection.setAutoCommit(false);

            // Creamos el Domicilio

            Domicilio domicilioGuardado=domicilioDAO.registrar(paciente.getDomicilio());


            PreparedStatement preparedStatement=connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,paciente.getApellido());
            preparedStatement.setString(2,paciente.getNombre());
            preparedStatement.setString(3,paciente.getDNI());
            preparedStatement.setDate(4, Date.valueOf(paciente.getFechaIngreso())); //En la BD tenemos Date pero en la clase LocalDate.
            preparedStatement.setInt(5,domicilioGuardado.getId());
            preparedStatement.executeUpdate();

            ResultSet resultSet=preparedStatement.getGeneratedKeys();

            while (resultSet.next()){
                Integer id=resultSet.getInt(1);

                pacienteARetornar=new Paciente(id,paciente.getApellido(), paciente.getNombre(), paciente.getDNI(),
                        paciente.getFechaIngreso(),domicilioGuardado);
            }
            LOGGER.info("Paciente Registrado: "+pacienteARetornar);
            connection.commit();
            connection.setAutoCommit(true);

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } finally {
            try{
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }

        return pacienteARetornar;
    }

    @Override
    public Paciente buscarPorId(Integer id) {

        Connection connection=null;
        DomicilioDAO domicilioDAO=new DomicilioDAO();
        Paciente pacienteEncontrado=null;

        try{

            connection=H2Connection.getConnection();

            PreparedStatement preparedStatement= connection.prepareStatement(SQL_SELECT_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                // Obtenemos los datos desde ResultSet
                Integer idDevuelto=resultSet.getInt(1);
                String apellido=resultSet.getString(2);
                String nombre=resultSet.getString(3);
                String DNI=resultSet.getString(4);
                LocalDate fechaIngreso=resultSet.getDate(5).toLocalDate();
                Integer idDomicilio=resultSet.getInt(6);

                // Como Domicilio viene desde otra tabla, utilizaremos el DAO para obtener todos los datos de domicilio, no solo el ID.
                Domicilio domicilioEncontrado=domicilioDAO.buscarPorId(idDomicilio);

                pacienteEncontrado=new Paciente(idDevuelto,apellido,nombre,DNI,fechaIngreso,domicilioEncontrado);
            }

            LOGGER.info("Paciente encontrado: "+pacienteEncontrado);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } finally {
            try{
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
        return pacienteEncontrado;
    }

    @Override
    public List<Paciente> buscarTodos() {

        List<Paciente> paciente = new ArrayList<>();
        Connection connection=null;
        DomicilioDAO domicilioDAO=new DomicilioDAO();
        Paciente pacienteEncontrado=null;
        try{
            connection=H2Connection.getConnection();
            Statement statement= connection.prepareStatement(SQL_SELECT_ALL);

            ResultSet resultSet= statement.getResultSet();

            while (resultSet.next()){
                // Obtenemos los datos desde ResultSet
                Integer idDevuelto=resultSet.getInt(1);
                String apellido=resultSet.getString(2);
                String nombre=resultSet.getString(3);
                String DNI=resultSet.getString(4);
                LocalDate fechaIngreso=resultSet.getDate(5).toLocalDate();
                Integer idDomicilio=resultSet.getInt(6);

                // Como Domicilio viene desde otra tabla, utilizaremos el DAO para obtener todos los datos de domicilio, no solo el ID.
                Domicilio domicilioEncontrado=domicilioDAO.buscarPorId(idDomicilio);

                pacienteEncontrado=new Paciente(idDevuelto,apellido,nombre,DNI,fechaIngreso,domicilioEncontrado);
                paciente.add(pacienteEncontrado);
            }
            LOGGER.info(paciente);

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } finally {
            try{
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
        return paciente;
    }
}
