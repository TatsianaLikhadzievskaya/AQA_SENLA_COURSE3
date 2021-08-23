package tests;

import databaseConnect.JDBCConnection;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Проверка подключения к БД sakila и отправки запросов")
public class TestDatabase extends TestSetup {

    @Test
    @Order(1)
    @DisplayName("Проверка создания таблицы в БД")
    public void testCreateTable() {
        String query = "CREATE TABLE sights ("
                + "ID int(6) NOT NULL,"
                + "TITLE VARCHAR(45) NOT NULL,"
                + "TYPE VARCHAR(45) NOT NULL,"
                + "COUNTRY VARCHAR(45) NOT NULL,"
                + "PRIMARY KEY (id))";
        JDBCConnection.createTable(query);
        query = "CREATE TABLE languages ("
                + "ID int(6) NOT NULL,"
                + "TITLE VARCHAR(45) NOT NULL,"
                + "COUNTRY VARCHAR(45) NOT NULL,"
                + "PRIMARY KEY (id))";
        JDBCConnection.createTable(query);
    }

    @Test
    @Order(2)
    @DisplayName("Отправка INSERT запроса")
    public void testInsertFirstRequest() {
        String query = "INSERT INTO sights VALUES (245721, 'Colosseum', 'Amphitheatre', 'Italy')";
        JDBCConnection.insertIntoTable(query);

        String selectQuery = "SELECT * FROM sights";
        ResultSet rs = JDBCConnection.selectFromTable(selectQuery);
        assertAll("Should return inserted data",
                () -> assertEquals("245721", rs.getString("ID")),
                () -> assertEquals("Colosseum", rs.getString("TITLE")),
                () -> assertEquals("Amphitheatre", rs.getString("TYPE")),
                () -> assertEquals("Italy", rs.getString("COUNTRY")));
    }

    @Test
    @Order(3)
    @DisplayName("Отправка INSERT запроса")
    public void testInsertSecondRequest() {
        String query = "INSERT INTO sights VALUES (245722, 'Machu Picchu', 'Ruins', 'Peru')";
        JDBCConnection.insertIntoTable(query);

        String selectQuery = "SELECT * FROM sights where ID = 245722";
        ResultSet rs = JDBCConnection.selectFromTable(selectQuery);
        assertAll("Should return inserted data",
                () -> assertEquals("245722", rs.getString("ID")),
                () -> assertEquals("Machu Picchu", rs.getString("TITLE")),
                () -> assertEquals("Ruins", rs.getString("TYPE")),
                () -> assertEquals("Peru", rs.getString("COUNTRY")));
    }

    @Test
    @Order(4)
    @DisplayName("Отправка INSERT запроса")
    public void testInsertThirdRequest() {
        String query = "INSERT INTO languages VALUES (120, 'Italian', 'Italy')," +
                "(127, 'Spanish', 'Peru')";
        JDBCConnection.insertIntoTable(query);
        String selectQuery = "SELECT * FROM languages";
        ResultSet rs = JDBCConnection.selectFromTable(selectQuery);
        assertAll("Should return inserted data",
                () -> assertEquals("120", rs.getString("ID")),
                () -> assertEquals("Italian", rs.getString("TITLE")),
                () -> assertEquals("Italy", rs.getString("COUNTRY")));
    }

    @Test
    @Order(5)
    public void testUpdateRequest() throws SQLException {
        String query = "UPDATE sights SET TITLE = 'PULA ARENA', COUNTRY = 'CROATIA' WHERE ID=245721";
        JDBCConnection.updateInTable(query);
        String selectQuery = "SELECT TITLE, COUNTRY FROM sights WHERE ID=245721";
        ResultSet rs = JDBCConnection.selectFromTable(selectQuery);
        String expectedTitle = "PULA ARENA";
        String actualTitle = rs.getString("TITLE");
        String expectedCountry = "CROATIA";
        String actualCountry = rs.getString("COUNTRY");
        assertEquals(expectedTitle,  actualTitle, "Actual town is '" +  actualTitle + "'. Expected - '" + expectedTitle + "'.");
        assertEquals(expectedCountry,  actualCountry, "Actual country is '" +  actualCountry + "'. Expected - '" + expectedCountry + "'.");
    }


    @Test
    @Order(6)
    @DisplayName("Отправка простого SELECT запроса. Выборка названий")
    public void testSimpleSelectRequest_checkTitle() throws SQLException {
        String query = "SELECT TITLE FROM sights";
        ResultSet rs = JDBCConnection.selectFromTable(query);
        String expectedTitle = "PULA ARENA";
        String actualTitle = rs.getString("TITLE");
        assertEquals(expectedTitle, actualTitle, "Actual title is '" + actualTitle + "'. Expected - '" + expectedTitle + "'.");
        rs.next();
        expectedTitle = "Machu Picchu";
        actualTitle = rs.getString("TITLE");
        assertEquals(expectedTitle, actualTitle, "Actual title is '" + actualTitle + "'. Expected - '" + expectedTitle + "'.");
    }

    @Test
    @Order(7)
    @DisplayName("Отправка SELECT запроса c условием where. Проверка типа")
    public void testSelectRequest_checkType() throws SQLException {
        String query = "SELECT * FROM sights WHERE TYPE='Ruins'";
        ResultSet rs = JDBCConnection.selectFromTable(query);
        assertAll("Should return selected data",
                () -> assertEquals("245722", rs.getString("ID")),
                () -> assertEquals("Machu Picchu", rs.getString("TITLE")),
                () -> assertEquals("Ruins", rs.getString("TYPE")),
                () -> assertEquals("Peru", rs.getString("COUNTRY")));
    }

    @Test
    @Order(8)
    @DisplayName("Отправка SELECT JOIN запроса. Поиск языка для достопримечательности")
    public void testSelectWithJoinRequest_CheckLanguageForSight() throws SQLException {
        String query = "SELECT st.title, lang.title FROM sights st INNER JOIN languages lang ON st.country=lang.country";
        ResultSet rs = JDBCConnection.selectFromTable(query);
        assertAll("Should return selected data",
                () -> assertEquals("Machu Picchu", rs.getString("st.title")),
                () -> assertEquals("Spanish", rs.getString("lang.title")));
    }

    @Test
    @Order(9)
    @DisplayName("Отправка DELETE запроса")
    public void testDeleteRequest() throws SQLException {
        String query = "DELETE FROM sights WHERE ID=245721";
        JDBCConnection.deleteFromTable(query);
        String selectQuery = "SELECT * FROM sights WHERE ID=245721";
        ResultSet rs = JDBCConnection.selectFromTable(selectQuery);
        Assertions.assertFalse(rs.next());
    }

    @Test
    @Order(10)
    @DisplayName("Проверка удаления таблицы из БД")
    public void test_dropTable() {
        JDBCConnection.dropTable("sights");
    }
}
