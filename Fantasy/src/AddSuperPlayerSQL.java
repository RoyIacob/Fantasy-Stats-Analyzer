import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.runner.Version;

public class AddSuperPlayerSQL {
	Connection con;
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	ArrayList<SuperPlayer> superPlayers;

	String insertTableSQL = "INSERT INTO player_stats" + "(name,team,isHome,opposition,"
			+ "minP,pointsP,fgpP,fgaP,fgmP,ftpP,ftmP,ftaP,tppP,tpmP,tpaP,rebP,astP,stlP,blkP,tovP,"
			+ "minA,pointsA,fgpA,fgaA,fgmA,ftpA,ftmA,ftaA,tppA,tpmA,tpaA,rebA,astA,stlA,blkA,tovA,"
			+ "minR,pointsR,fgpR,fgaR,fgmR,ftpR,ftmR,ftaR,tppR,tpmR,tpaR,rebR,astR,stlR,blkR,tovR,"
			+ "projectionWins, averageWins, date) VALUES"
			+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public AddSuperPlayerSQL(ArrayList<SuperPlayer> superPlayers) {
		this.superPlayers = superPlayers;
		try {
			// executeQuery()---for getting the data from database
			// executeUpdate()---for insert,update,delete
			// execute() any kind

			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", "gowarriors24");
			preparedStatement = con.prepareStatement(insertTableSQL);
			for (SuperPlayer p : this.superPlayers) {
				if (p != null) {
					preparedStatement.setString(1, p.name);
					preparedStatement.setString(2, p.team);
					preparedStatement.setString(3, p.home);
					preparedStatement.setString(4, p.opposition);

					preparedStatement.setDouble(5, p.minutesProjected);
					preparedStatement.setDouble(6, p.pointsProjected);
					preparedStatement.setDouble(7, p.fgpProjected);
					preparedStatement.setDouble(8, p.fgaProjected);
					preparedStatement.setDouble(9, p.fgmProjected);
					preparedStatement.setDouble(10, p.ftpProjected);
					preparedStatement.setDouble(11, p.ftmProjected);
					preparedStatement.setDouble(12, p.ftaProjected);
					preparedStatement.setDouble(13, p.tppProjected);
					preparedStatement.setDouble(14, p.tpmProjected);
					preparedStatement.setDouble(15, p.tpaProjected);
					preparedStatement.setDouble(16, p.reboundsProjected);
					preparedStatement.setDouble(17, p.assistsProjected);
					preparedStatement.setDouble(18, p.stealsProjected);
					preparedStatement.setDouble(19, p.blocksProjected);
					preparedStatement.setDouble(20, p.turnoversProjected);

					preparedStatement.setDouble(21, p.minutesAverage);
					preparedStatement.setDouble(22, p.pointsAverage);
					preparedStatement.setDouble(23, p.fgpAverage);
					preparedStatement.setDouble(24, p.fgaAverage);
					preparedStatement.setDouble(25, p.fgmAverage);
					preparedStatement.setDouble(26, p.ftpAverage);
					preparedStatement.setDouble(27, p.ftmAverage);
					preparedStatement.setDouble(28, p.ftaAverage);
					preparedStatement.setDouble(29, p.tppAverage);
					preparedStatement.setDouble(30, p.tpmAverage);
					preparedStatement.setDouble(31, p.tpaAverage);
					preparedStatement.setDouble(32, p.reboundsAverage);
					preparedStatement.setDouble(33, p.assistsAverage);
					preparedStatement.setDouble(34, p.stealsAverage);
					preparedStatement.setDouble(35, p.blocksAverage);
					preparedStatement.setDouble(36, p.turnoversAverage);

					preparedStatement.setDouble(37, p.minutesReal);
					preparedStatement.setDouble(38, p.pointsReal);
					preparedStatement.setDouble(39, p.fgpReal);
					preparedStatement.setDouble(40, p.fgaReal);
					preparedStatement.setDouble(41, p.fgmReal);
					preparedStatement.setDouble(42, p.ftpReal);
					preparedStatement.setDouble(43, p.ftmReal);
					preparedStatement.setDouble(44, p.ftaReal);
					preparedStatement.setDouble(45, p.tppReal);
					preparedStatement.setDouble(46, p.tpmReal);
					preparedStatement.setDouble(47, p.tpaReal);
					preparedStatement.setDouble(48, p.reboundsReal);
					preparedStatement.setDouble(49, p.assistsReal);
					preparedStatement.setDouble(50, p.stealsReal);
					preparedStatement.setDouble(51, p.blocksReal);
					preparedStatement.setDouble(52, p.turnoversReal);

					preparedStatement.setInt(53, p.projectionWins);
					preparedStatement.setInt(54, p.averageWins);
					preparedStatement.setString(55, p.date);

					preparedStatement.executeUpdate();
				}
			}

			// Remove duplicates.
			preparedStatement.executeQuery("ALTER IGNORE TABLE player_stats ADD UNIQUE (name,date)");
			preparedStatement.executeQuery("ALTER TABLE player_stats DROP INDEX name;");

			ResultSet rs = preparedStatement.executeQuery("SELECT projectionWins FROM player_stats");
			int projectionWins = 0;
			while (rs.next()) {
				projectionWins += rs.getInt("projectionWins");
			}

			ResultSet rs2 = preparedStatement.executeQuery("SELECT averageWins FROM player_stats");
			int averageWins = 0;
			while (rs2.next()) {
				averageWins += rs2.getInt("averageWins");
			}

			String insertTableSQL = "INSERT INTO predictions" + "(projectionWins,averageWins,date) VALUES (?,?,?)";
			preparedStatement = con.prepareStatement(insertTableSQL);
			preparedStatement.setDouble(1, projectionWins);
			preparedStatement.setDouble(2, averageWins);
			preparedStatement.setString(3, superPlayers.get(0).date);
			preparedStatement.executeUpdate();
			System.out.println("DB Updated: " + projectionWins + " - " + averageWins);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Version.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	private AddSuperPlayerSQL() {
	};

	public static void main(String[] args) {

		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", "gowarriors24");
			PreparedStatement preparedStatement = con.prepareStatement("");
			ResultSet rs = preparedStatement.executeQuery("SELECT projectionWins FROM player_stats");
			int projectionWins = 0;
			while (rs.next()) {
				projectionWins += rs.getInt("projectionWins");
			}

			ResultSet rs2 = preparedStatement.executeQuery("SELECT averageWins FROM player_stats");
			int averageWins = 0;
			while (rs2.next()) {
				averageWins += rs2.getInt("averageWins");
			}

			String insertTableSQL = "INSERT INTO predictions" + "(projectionWins,averageWins,date) VALUES (?,?,?)";
			preparedStatement = con.prepareStatement(insertTableSQL);
			preparedStatement.setDouble(1, projectionWins);
			preparedStatement.setDouble(2, averageWins);
			preparedStatement.setString(3, " meh");
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

/* Common SQL queries used: 
 * 
 * 
 * 
 * CREATE TABLE `player_stats` ( `id` int(9) NOT NULL AUTO_INCREMENT, `name`
 * varchar(25) DEFAULT NULL, `team` varchar(25) DEFAULT NULL, `isHome`
 * varchar(25) DEFAULT NULL, `opposition` varchar(25) DEFAULT NULL, `minP`
 * double(8,4) DEFAULT NULL, `pointsP` double(8,4) DEFAULT NULL, `fgpP`
 * double(8,4) DEFAULT NULL, `fgaP` double(8,4) DEFAULT NULL, `fgmP` double(8,4)
 * DEFAULT NULL, `ftpP` double(8,4) DEFAULT NULL, `ftmP` double(8,4) DEFAULT
 * NULL, `ftaP` double(8,4) DEFAULT NULL, `tppP` double(8,4) DEFAULT NULL,
 * `tpmP` double(8,4) DEFAULT NULL, `tpaP` double(8,4) DEFAULT NULL, `rebP`
 * double(8,4) DEFAULT NULL, `astP` double(8,4) DEFAULT NULL, `stlP` double(8,4)
 * DEFAULT NULL, `blkP` double(8,4) DEFAULT NULL, `tovP` double(8,4) DEFAULT
 * NULL, `minA` double(8,4) DEFAULT NULL, `pointsA` double(8,4) DEFAULT NULL,
 * `fgpA` double(8,4) DEFAULT NULL, `fgaA` double(8,4) DEFAULT NULL, `fgmA`
 * double(8,4) DEFAULT NULL, `ftpA` double(8,4) DEFAULT NULL, `ftmA` double(8,4)
 * DEFAULT NULL, `ftaA` double(8,4) DEFAULT NULL, `tppA` double(8,4) DEFAULT
 * NULL, `tpmA` double(8,4) DEFAULT NULL, `tpaA` double(8,4) DEFAULT NULL,
 * `rebA` double(8,4) DEFAULT NULL, `astA` double(8,4) DEFAULT NULL, `stlA`
 * double(8,4) DEFAULT NULL, `blkA` double(8,4) DEFAULT NULL, `tovA` double(8,4)
 * DEFAULT NULL, `minR` double(8,4) DEFAULT NULL, `pointsR` double(8,4) DEFAULT
 * NULL, `fgpR` double(8,4) DEFAULT NULL, `fgaR` double(8,4) DEFAULT NULL,
 * `fgmR` double(8,4) DEFAULT NULL, `ftpR` double(8,4) DEFAULT NULL, `ftmR`
 * double(8,4) DEFAULT NULL, `ftaR` double(8,4) DEFAULT NULL, `tppR` double(8,4)
 * DEFAULT NULL, `tpmR` double(8,4) DEFAULT NULL, `tpaR` double(8,4) DEFAULT
 * NULL, `rebR` double(8,4) DEFAULT NULL, `astR` double(8,4) DEFAULT NULL,
 * `stlR` double(8,4) DEFAULT NULL, `blkR` double(8,4) DEFAULT NULL, `tovR`
 * double(8,4) DEFAULT NULL, `projectionWins` int(5) DEFAULT NULL, `averageWins`
 * int(5) DEFAULT NULL, `date` varchar(25) DEFAULT NULL, PRIMARY KEY (`id`) )
 * ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1; DELETE FROM
 * player_stats; DELETE FROM predictions;
 * 
 * ALTER TABLE player_stats AUTO_INCREMENT = 0; ALTER TABLE predictions
 * AUTO_INCREMENT = 0;
 * 
 * UPDATE player_stats SET date="March_11_2013" WHERE id > 649;
 * 
 * UPDATE predictions SET date="March_11_2013" WHERE id > 3;
 * 
 * 
 * 
 * CREATE TABLE `player_stats` ( `id` int(9) NOT NULL AUTO_INCREMENT, `name`
 * varchar(25) DEFAULT NULL, `team` varchar(25) DEFAULT NULL, `isHome`
 * varchar(25) DEFAULT NULL, `opposition` varchar(25) DEFAULT NULL, `minP`
 * double(8,1) DEFAULT NULL, `pointsP` double(8,2) DEFAULT NULL, `fgpP`
 * double(8,2) DEFAULT NULL, `fgaP` double(8,2) DEFAULT NULL, `fgmP` double(8,2)
 * DEFAULT NULL, `ftpP` double(8,2) DEFAULT NULL, `ftmP` double(8,2) DEFAULT
 * NULL, `ftaP` double(8,2) DEFAULT NULL, `tppP` double(8,2) DEFAULT NULL,
 * `tpmP` double(8,2) DEFAULT NULL, `tpaP` double(8,2) DEFAULT NULL, `rebP`
 * double(8,2) DEFAULT NULL, `astP` double(8,2) DEFAULT NULL, `stlP` double(8,2)
 * DEFAULT NULL, `blkP` double(8,2) DEFAULT NULL, `tovP` double(8,2) DEFAULT
 * NULL, `minA` double(8,2) DEFAULT NULL, `pointsA` double(8,2) DEFAULT NULL,
 * `fgpA` double(8,2) DEFAULT NULL, `fgaA` double(8,2) DEFAULT NULL, `fgmA`
 * double(8,2) DEFAULT NULL, `ftpA` double(8,2) DEFAULT NULL, `ftmA` double(8,2)
 * DEFAULT NULL, `ftaA` double(8,2) DEFAULT NULL, `tppA` double(8,2) DEFAULT
 * NULL, `tpmA` double(8,2) DEFAULT NULL, `tpaA` double(8,2) DEFAULT NULL,
 * `rebA` double(8,2) DEFAULT NULL, `astA` double(8,2) DEFAULT NULL, `stlA`
 * double(8,2) DEFAULT NULL, `blkA` double(8,2) DEFAULT NULL, `tovA` double(8,2)
 * DEFAULT NULL, `minR` double(8,2) DEFAULT NULL, `pointsR` double(8,2) DEFAULT
 * NULL, `fgpR` double(8,2) DEFAULT NULL, `fgaR` double(8,2) DEFAULT NULL,
 * `fgmR` double(8,2) DEFAULT NULL, `ftpR` double(8,2) DEFAULT NULL, `ftmR`
 * double(8,2) DEFAULT NULL, `ftaR` double(8,2) DEFAULT NULL, `tppR` double(8,2)
 * DEFAULT NULL, `tpmR` double(8,2) DEFAULT NULL, `tpaR` double(8,2) DEFAULT
 * NULL, `rebR` double(8,2) DEFAULT NULL, `astR` double(8,2) DEFAULT NULL,
 * `stlR` double(8,2) DEFAULT NULL, `blkR` double(8,2) DEFAULT NULL, `tovR`
 * double(8,2) DEFAULT NULL, `projectionWins` int(5) DEFAULT NULL, `averageWins`
 * int(5) DEFAULT NULL, `date` varchar(25) DEFAULT NULL, PRIMARY KEY (`id`) )
 * ENGINE=MyISAM AUTO_INCREMENT=650 DEFAULT CHARSET=latin1;
 * 
 * DROP TABLE predictions;
 * 
 * CREATE TABLE `predictions` ( `id` int(9) NOT NULL AUTO_INCREMENT,
 * `projectionWins` int(7) DEFAULT NULL, `averageWins` int(7) DEFAULT NULL,
 * `date` varchar(25) DEFAULT NULL, PRIMARY KEY (`id`) ) ENGINE=MyISAM DEFAULT
 * CHARSET=latin1;
 */
