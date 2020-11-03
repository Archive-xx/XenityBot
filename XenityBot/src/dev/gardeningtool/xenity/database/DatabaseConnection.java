package dev.gardeningtool.xenity.database;

import dev.gardeningtool.xenity.config.Config;

import java.sql.*;

/*
I'm not the best with SQL so forgive any mistakes
 */
public class DatabaseConnection {

    //Two methods, hasActiveSubcription and hasRecord, are both required

    private Connection connection;

    public DatabaseConnection(Config config) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + config.getMysqlUrl() + ":" + 3306 + "/" + config.getMysqlDatabase(),
                    config.getMysqlUsername(), config.getMysqlPassword());
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }

    public void addSubscription(long userId, int days) throws SQLException {
        if (hasRecord(userId)) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_data SET ENDTIME=? WHERE ID=?");
            preparedStatement.setLong(1, System.currentTimeMillis() + (86400 * 1000 * days));
            preparedStatement.setLong(2, userId);
            return;
        }
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_data (ID, START, ENDTIME) VALUES (?, ?, ?)");
        preparedStatement.setLong(1, userId);
        preparedStatement.setLong(2, System.currentTimeMillis());
        preparedStatement.setLong(3, System.currentTimeMillis() + ((86400 * 1000 * Math.abs(days))));
        preparedStatement.execute();
    }

    public boolean hasActiveSubscription(long userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_data WHERE ID=?;");
        preparedStatement.setLong(1, userId);
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            return false;
        }
        long subscriptionEndTime = result.getLong("ENDTIME");
        return System.currentTimeMillis() < subscriptionEndTime;
    }

    public void removeSubscription(long userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE from user_data where ID=?");
        preparedStatement.setLong(1, userId);
        preparedStatement.execute();
    }

    public boolean hasRecord(long userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_data WHERE ID=?;");
        preparedStatement.setLong(1, userId);
        ResultSet result = preparedStatement.executeQuery();
        return result.next();
    }

}
