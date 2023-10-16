package ru.itgirl.jdbcspringexample.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itgirl.jdbcspringexample.model.Book;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @Autowired
    private DataSource dataSource;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookRepositoryImpl(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAllBooks() {
        List<Book> result = new ArrayList<>();

        String SQL_findAllBooks = "select * from books;";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_findAllBooks)) {
            while (resultSet.next()) {
                Book book = converRowToBook(resultSet);
                result.add(book);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    @Override
    public Book getBookId(Long id) {
        String sqlQuery = "select * from books where id = :id;";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", 1);
        Book book = jdbcTemplate.queryForObject(sqlQuery, parameters, (resultSet, rowNum) -> converRowToBook(resultSet));
        System.out.println(book);
        return book;
    }

    private Book converRowToBook(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Book(id, name);
    }
}
