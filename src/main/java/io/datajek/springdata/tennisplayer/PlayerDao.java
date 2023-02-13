package io.datajek.springdata.tennisplayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void createTournamentTable() {
        String sql = "CREATE TABLE TOURNAMENT (ID INTEGER, NAME VARCHAR(50), LOCATION VARCHAR(50), PRIMARY KEY (ID))";
        jdbcTemplate.execute(sql);
        System.out.println("Table created");
    }

    public List<Player> getAllPlayers() {
        String sql = "SELECT * FROM PLAYER";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Player>(Player.class));
    }

    public int insertPlayer(Player player)
    {
        String sql = "INSERT INTO PLAYER (ID, Name, Nationality,Birth_date, Titles) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update( sql, new Object[]
                { player.getId(), player.getName(), player.getNationality(),
                        new Timestamp(player.getBirthDate().getTime()),
                        player.getTitles()
                });
    }

    public int updatePlayer(Player player){
        String sql = "UPDATE PLAYER " +
                "SET Name = ?, Nationality = ?, Birth_date = ? , Titles = ? " +
                "WHERE ID = ?";

        return jdbcTemplate.update( sql, new Object[] {
                player.getName(),
                player.getNationality(),
                new Timestamp(player.getBirthDate().getTime()),
                player.getTitles(),
                player.getId() }
        );
    }

    public int deletePlayerById(int id) {
        String sql="DELETE FROM PLAYER WHERE ID = ?";
        return jdbcTemplate.update(sql, new Object[] {id});
    }

    public List<Player> getPlayerByNationality(String nationality) {
        String sql = "SELECT * FROM PLAYER WHERE NATIONALITY = ?";
        return jdbcTemplate.query(sql, new PlayerMapper(), new Object[] {nationality});
    }

    private static final class PlayerMapper implements RowMapper {

        @Override
        public Player mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Player player = new Player();
            player.setId(resultSet.getInt("id"));
            player.setName(resultSet.getString("name"));
            player.setNationality(resultSet.getString("nationality"));
            player.setBirthDate(resultSet.getDate("birth_date")); //.getTime("birth_date"));
            player.setTitles(resultSet.getInt("titles"));
            return player;
        }
    }
}