package org.example.old.repo;

import org.example.old.domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCRepo extends DBRepository implements Repository {
  public JDBCRepo(String dbCredentialsFilename) {
    super(dbCredentialsFilename);
  }

  @Override
  public Optional<Student> findOne(Integer id) {
    if (id == null) throw new IllegalArgumentException("ID must not be null");
    String selectQuery = "select id, name from student where id = " + id;

    try (Connection connection = dbConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Student newStudent = new Student();
        newStudent.setId(resultSet.getInt("id"));
        newStudent.setName(resultSet.getString("name"));
        return Optional.of(newStudent);
      } else {
        return Optional.empty();
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public List<Student> findAll() {
    List<Student> result = new ArrayList<>();
    String selectQuery = "select id, name from student";

    try (Connection conn = dbConnection()) {
      PreparedStatement stm = conn.prepareStatement(selectQuery);
      ResultSet rs = stm.executeQuery();

      while (rs.next()) {
        Student st = new Student();

        st.setId(rs.getInt("id"));
        st.setName(rs.getString("name"));

        result.add(st);
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return result;
  }

  @Override
  public boolean save(Student entity) {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null");
    }
    String query = "insert into student values (?, ?)";
    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, entity.getId());
      statement.setString(2, entity.getName());
      statement.executeUpdate();
      return true;
    } catch (SQLException ex) {
      return false;
    }
  }

  @Override
  public boolean delete(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("ID must not be null");
    }
    String selectQuery = "select id, name from student where id = " + id;
    String deleteQuery = "delete from student where id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSetSelect = selectStatement.executeQuery();
      if (resultSetSelect.next()) {
        Student newStudent = new Student();
        newStudent.setId(resultSetSelect.getInt("id"));
        newStudent.setName(resultSetSelect.getString("name"));
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean update(Student entity) {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null");
    }
    String updateQuery = "update student set name = ? where id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
      preparedStatement.setString(1, entity.getName());
      preparedStatement.setInt(2, entity.getId());
      return preparedStatement.executeUpdate() != 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
