package dh.backend.dao.impl;

import dh.backend.dao.IDao;
import dh.backend.db.H2Connection;
import dh.backend.model.Domicilio;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;

public class DomicilioDAO implements IDao<Domicilio> {

    private static Logger LOGGER = Logger.getLogger(DomicilioDAO.class);
    public static String SQL_INSERT = "INSERT INTO DOMICILIOS VALUES (DEFAULT,?,?,?,?)";
    public static String SQL_SELECT_ID = "SELECT * FROM DOMICILIOS WHERE ID=?";

    @Override
    public Domicilio registrar(Domicilio domicilio) {

        Connection connection = null;

        Domicilio domicilioARegistrar = null;

        try {

            connection = H2Connection.getConnection();

            connection.setAutoCommit(false);

            // Pedimos que el P. Statement nos retorne alguna PK.
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, domicilio.getCalle());
            preparedStatement.setInt(2, domicilio.getNumero());
            preparedStatement.setString(3, domicilio.getLocalidad());
            preparedStatement.setString(4, domicilio.getProvincia());

            preparedStatement.executeUpdate();

            // Luego, recuperamos la clave mediante el Result Set (Estructura de datos que nos trae informacion desde la BD).
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                //Guardamos el Id usando el ResultSet

                Integer id = resultSet.getInt(1);
                // Creamos el nuevo domicilio con los valores para retornar

                domicilioARegistrar = new Domicilio(id, domicilio.getCalle(), domicilio.getNumero(),
                        domicilio.getLocalidad(), domicilio.getProvincia());
            }
            LOGGER.info("Domicilio Registrado: " + domicilioARegistrar);
            connection.commit();
            connection.setAutoCommit(true);

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
        return domicilioARegistrar;
    }

    @Override
    public Domicilio buscarPorId(Integer id) {

        Connection connection = null;
        Domicilio domicilioEncontrado=null;

        try{
            connection=H2Connection.getConnection();

            PreparedStatement preparedStatement= connection.prepareStatement(SQL_SELECT_ID);
            preparedStatement.setInt(1,id);
            preparedStatement.execute();

            ResultSet resultSet= preparedStatement.executeQuery();// ExecuteQuery para las consultas Select.

            while (resultSet.next()){
                Integer idEncontrado=resultSet.getInt(1);
                String calle=resultSet.getString(2);
                int numero=resultSet.getInt(3);
                String localidad=resultSet.getString(4);
                String provincia=resultSet.getString(5);
                domicilioEncontrado=new Domicilio(idEncontrado,calle,numero,localidad,provincia);
            }

            LOGGER.info("Domicilio encontrado: "+domicilioEncontrado);
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
        return domicilioEncontrado;
    }

    @Override
    public List<Domicilio> buscarTodos() {
        return null;
    }
}
