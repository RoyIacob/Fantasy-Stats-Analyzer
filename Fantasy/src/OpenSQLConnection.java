import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.runner.Version;

public class OpenSQLConnection {
	Connection con;
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	List<SuperPlayer> superPlayers;
	String password = "*";
	int totalLines;

	public void addSuperPlayers(List<SuperPlayer> superPlayers) {
		String insertTableSQL = "INSERT INTO player_stats" + "(name,team,isHome,opposition,"
				+ "minP,pointsP,fgpP,fgaP,fgmP,ftpP,ftmP,ftaP,tppP,tpmP,tpaP,rebP,astP,stlP,blkP,tovP,"
				+ "minA,pointsA,fgpA,fgaA,fgmA,ftpA,ftmA,ftaA,tppA,tpmA,tpaA,rebA,astA,stlA,blkA,tovA,"
				+ "minR,pointsR,fgpR,fgaR,fgmR,ftpR,ftmR,ftaR,tppR,tpmR,tpaR,rebR,astR,stlR,blkR,tovR,"
				+ "projectionWins, averageWins, date) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.superPlayers = superPlayers;
		try {
			// executeQuery()---for getting the data from database
			// executeUpdate()---for insert,update,delete
			// execute() any kind

			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", password);
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
			preparedStatement.execute("ALTER IGNORE TABLE player_stats ADD UNIQUE (name,date)");
			preparedStatement.execute("ALTER TABLE player_stats DROP INDEX name");

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

			insertTableSQL = "INSERT INTO predictions" + "(projectionWins,averageWins,date) VALUES (?,?,?)";
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

	public OpenSQLConnection() {}

	public void clearTables() {
		try {
			// executeQuery()---for getting the data from database
			// executeUpdate()---for insert,update,delete
			// execute() any kind
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", password);
			preparedStatement = con.prepareStatement("DROP TABLE player_stats");
			preparedStatement.execute();
			preparedStatement = con.prepareStatement("DROP TABLE predictions");
			preparedStatement.execute();
			preparedStatement.execute(createStatsTableQuery);
			preparedStatement.execute(createPredictionsTableQuery);
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

	public String getHomeScore() {
		int averageWins = 0;
		int projectionWins = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", password);
			preparedStatement = con.prepareStatement("");
			ResultSet rs = preparedStatement.executeQuery("SELECT projectionWins FROM player_stats WHERE isHome!=\"@\"");
			while (rs.next()) {
				projectionWins += rs.getInt("projectionWins");
			}
			ResultSet rs2 = preparedStatement.executeQuery("SELECT averageWins FROM player_stats WHERE isHome!=\"@\"");
			while (rs2.next()) {
				averageWins += rs2.getInt("averageWins");
			}
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
		return projectionWins + " " + averageWins;
	}

	public String getAwayScore() {
		int averageWins = 0;
		int projectionWins = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", password);

			preparedStatement = con.prepareStatement("");

			ResultSet rs = preparedStatement.executeQuery("SELECT projectionWins FROM player_stats WHERE isHome=\"@\"");

			while (rs.next()) {
				projectionWins += rs.getInt("projectionWins");
			}

			ResultSet rs2 = preparedStatement.executeQuery("SELECT averageWins FROM player_stats WHERE isHome=\"@\"");

			while (rs2.next()) {
				averageWins += rs2.getInt("averageWins");
			}

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
		return (projectionWins) + " " + (averageWins);
	}

	public void setTotalPredAveScores(PredAveBox totalWins) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", password);
			preparedStatement = con.prepareStatement("");
			ResultSet rs = preparedStatement.executeQuery("SELECT projectionWins FROM player_stats");
			while (rs.next()) {
				totalWins.predScore += rs.getInt("projectionWins");
			}
			ResultSet rs2 = preparedStatement.executeQuery("SELECT averageWins FROM player_stats");
			while (rs2.next()) {
				totalWins.aveScore += rs2.getInt("averageWins");
			}
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

	public void bestPredictionsOnMinAveraged() {
		double predScore = 0, aveScore = 0;
		double highPred = 0;
		int highestIndex = 0;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", password);
			preparedStatement = con.prepareStatement("");
			int maxMin = 46;
			double sampleSetSize = 201;
			ResultSet rs = preparedStatement.executeQuery("SELECT id FROM player_stats");
			rs.last();
			double totalPopulation = rs.getRow();
			rs.beforeFirst();

			for (int i = 0; i < maxMin && sampleSetSize > 200; i++) {
				rs = preparedStatement.executeQuery("SELECT id FROM player_stats WHERE minA > " + i);
				rs.last();
				sampleSetSize = rs.getRow();
				rs.beforeFirst();

				predScore = aveScore = 0;
				rs = preparedStatement.executeQuery("SELECT projectionWins FROM player_stats WHERE minA > " + i);
				while (rs.next()) {
					predScore += rs.getInt("projectionWins");
				}
				ResultSet rs2 = preparedStatement.executeQuery("SELECT averageWins FROM player_stats WHERE minA > " + i);
				while (rs2.next()) {
					aveScore += rs2.getInt("averageWins");
				}

				if ((predScore / (predScore + aveScore)) > highPred) {
					highPred = (predScore / (predScore + aveScore));
					highestIndex = i;
				}
			}
			System.out.println("Highest prediction on min ave: " + highPred + ", when min = " + highestIndex + ", using "
					+ (int) ((sampleSetSize / totalPopulation) * 100) + "% of total sample\n");

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

	public void bestPredictionsOnTeam() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", password);
			preparedStatement = con.prepareStatement("");

			ResultSet rs = preparedStatement.executeQuery("SELECT team, projectionWins, averageWins FROM player_stats");

			ArrayList<TeamBox> teamBoxes = new ArrayList<TeamBox>();

			boolean found = false;
			String queryTeamName = null;
			while (rs.next()) {
				queryTeamName = rs.getString("team");
				for (TeamBox t : teamBoxes) {
					if (t.name.equals(queryTeamName)) {
						found = true;
						t.addProjectionScore(rs.getInt("projectionWins"));
						t.addAverageScore(rs.getInt("averageWins"));
						break;
					}
				}
				if(found == false) {
					teamBoxes.add(new TeamBox(queryTeamName,(double) rs.getInt("projectionWins"),(double) rs.getInt("averageWins")));
				} found = false;
			}
			TeamBox highestTeam = teamBoxes.get(0);
			TeamBox lowestTeam = teamBoxes.get(0);
			for (int i = 0; i < teamBoxes.size(); i++) {
				if(teamBoxes.get(i).accuracy > highestTeam.accuracy) {
					highestTeam = teamBoxes.get(i);
				} else if(teamBoxes.get(i).accuracy < lowestTeam.accuracy) {
					lowestTeam = teamBoxes.get(i);
				}
			}
			
			System.out.println("Highest TEAM: " + highestTeam);
			System.out.println("Lowest TEAM: " + lowestTeam);

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

	private String createStatsTableQuery = "CREATE TABLE `player_stats` (" + "  `id` int(9) NOT NULL AUTO_INCREMENT,"
			+ "  `name` varchar(25) DEFAULT NULL," + "  `team` varchar(25) DEFAULT NULL," + "  `isHome` varchar(25) DEFAULT NULL,"
			+ "  `opposition` varchar(25) DEFAULT NULL," + "  `minP` double(8,1) DEFAULT NULL," + "  `pointsP` double(8,2) DEFAULT NULL,"
			+ "  `fgpP` double(8,2) DEFAULT NULL," + "  `fgaP` double(8,2) DEFAULT NULL," + "  `fgmP` double(8,2) DEFAULT NULL,"
			+ "  `ftpP` double(8,2) DEFAULT NULL," + "  `ftmP` double(8,2) DEFAULT NULL," + "  `ftaP` double(8,2) DEFAULT NULL,"
			+ "  `tppP` double(8,2) DEFAULT NULL," + "  `tpmP` double(8,2) DEFAULT NULL," + "  `tpaP` double(8,2) DEFAULT NULL,"
			+ "  `rebP` double(8,2) DEFAULT NULL," + "  `astP` double(8,2) DEFAULT NULL," + "  `stlP` double(8,2) DEFAULT NULL,"
			+ "  `blkP` double(8,2) DEFAULT NULL," + "  `tovP` double(8,2) DEFAULT NULL," + "  `minA` double(8,2) DEFAULT NULL,"
			+ "  `pointsA` double(8,2) DEFAULT NULL," + "  `fgpA` double(8,2) DEFAULT NULL," + "  `fgaA` double(8,2) DEFAULT NULL,"
			+ "  `fgmA` double(8,2) DEFAULT NULL," + "  `ftpA` double(8,2) DEFAULT NULL," + "  `ftmA` double(8,2) DEFAULT NULL,"
			+ "  `ftaA` double(8,2) DEFAULT NULL," + "  `tppA` double(8,2) DEFAULT NULL," + "  `tpmA` double(8,2) DEFAULT NULL,"
			+ "  `tpaA` double(8,2) DEFAULT NULL," + "  `rebA` double(8,2) DEFAULT NULL," + "  `astA` double(8,2) DEFAULT NULL,"
			+ "  `stlA` double(8,2) DEFAULT NULL," + "  `blkA` double(8,2) DEFAULT NULL," + "  `tovA` double(8,2) DEFAULT NULL,"
			+ "  `minR` double(8,2) DEFAULT NULL," + "  `pointsR` double(8,2) DEFAULT NULL," + "  `fgpR` double(8,2) DEFAULT NULL,"
			+ "  `fgaR` double(8,2) DEFAULT NULL," + "  `fgmR` double(8,2) DEFAULT NULL," + "  `ftpR` double(8,2) DEFAULT NULL,"
			+ "  `ftmR` double(8,2) DEFAULT NULL," + "  `ftaR` double(8,2) DEFAULT NULL," + "  `tppR` double(8,2) DEFAULT NULL,"
			+ "  `tpmR` double(8,2) DEFAULT NULL," + "  `tpaR` double(8,2) DEFAULT NULL," + "  `rebR` double(8,2) DEFAULT NULL,"
			+ "  `astR` double(8,2) DEFAULT NULL," + "  `stlR` double(8,2) DEFAULT NULL," + "  `blkR` double(8,2) DEFAULT NULL,"
			+ "  `tovR` double(8,2) DEFAULT NULL," + "  `projectionWins` int(5) DEFAULT NULL," + "  `averageWins` int(5) DEFAULT NULL,"
			+ "  `date` varchar(25) DEFAULT NULL," + "  PRIMARY KEY (`id`)" + ") ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";

	public void bestCategoryPredicted() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb", "fantasybb", password);
			preparedStatement = con.prepareStatement("");

			String name;
			String opposition;
			double minutesProjected;
			double pointsProjected;
			double fgpProjected;
			double fgmProjected;
			double fgaProjected;
			double ftpProjected, ftmProjected, ftaProjected;
			// String tpm_aProjected; // 3's made v. attempted
			double tppProjected, tpmProjected, tpaProjected;
			double reboundsProjected;
			double assistsProjected;
			double stealsProjected;
			double blocksProjected;
			double turnoversProjected;

			double minutesAverage;
			double pointsAverage;
			double fgpAverage;
			double fgmAverage;
			double fgaAverage;
			double ftpAverage, ftmAverage, ftaAverage;
			// String tpm_aAverage; // 3's made v. attempted
			double tppAverage, tpmAverage, tpaAverage;
			double reboundsAverage;
			double assistsAverage;
			double stealsAverage;
			double blocksAverage;
			double turnoversAverage;

			double minutesReal;
			double pointsReal;
			double fgpReal;
			double fgmReal;
			double fgaReal;
			double ftpReal, ftmReal, ftaReal;
			// String tpm_aReal; // 3's made v. attempted
			double tppReal, tpmReal, tpaReal;
			double reboundsReal;
			double assistsReal;
			double stealsReal;
			double blocksReal;
			double turnoversReal;

			ArrayList<SuperPlayer> superPlayers = new ArrayList<SuperPlayer>();

			ResultSet rs = preparedStatement.executeQuery("SELECT * FROM player_stats");
			// Rebuild total Players.
			// See which category is the best.

			while (rs.next()) {
				// aveScore += rs2.getInt("averageWins");
				name = rs.getString("name");
				opposition = rs.getString("opposition");
				minutesProjected = rs.getDouble("minP");
				pointsProjected = rs.getDouble("pointsP");
				fgpProjected = rs.getDouble("fgpP");
				fgmProjected = rs.getDouble("fgaP");
				fgaProjected = rs.getDouble("fgmP");
				ftpProjected = rs.getDouble("ftpP");
				ftmProjected = rs.getDouble("ftmP");
				ftaProjected = rs.getDouble("ftaP");
				tppProjected = rs.getDouble("tppP");
				tpmProjected = rs.getDouble("tpmP");
				tpaProjected = rs.getDouble("tpaP");
				reboundsProjected = rs.getDouble("rebP");
				assistsProjected = rs.getDouble("astP");
				stealsProjected = rs.getDouble("stlP");
				blocksProjected = rs.getDouble("blkP");
				turnoversProjected = rs.getDouble("tovP");

				minutesAverage = rs.getDouble("minA");
				pointsAverage = rs.getDouble("pointsA");
				fgpAverage = rs.getDouble("fgpA");
				fgmAverage = rs.getDouble("fgaA");
				fgaAverage = rs.getDouble("fgmA");
				ftpAverage = rs.getDouble("ftpA");
				ftmAverage = rs.getDouble("ftmA");
				ftaAverage = rs.getDouble("ftaA");
				tppAverage = rs.getDouble("tppA");
				tpmAverage = rs.getDouble("tpmA");
				tpaAverage = rs.getDouble("tpaA");
				reboundsAverage = rs.getDouble("rebA");
				assistsAverage = rs.getDouble("astA");
				stealsAverage = rs.getDouble("stlA");
				blocksAverage = rs.getDouble("blkA");
				turnoversAverage = rs.getDouble("tovA");

				minutesReal = rs.getDouble("minR");
				pointsReal = rs.getDouble("pointsR");
				fgpReal = rs.getDouble("fgpR");
				fgmReal = rs.getDouble("fgaR");
				fgaReal = rs.getDouble("fgmR");
				ftpReal = rs.getDouble("ftpR");
				ftmReal = rs.getDouble("ftmR");
				ftaReal = rs.getDouble("ftaR");
				tppReal = rs.getDouble("tppR");
				tpmReal = rs.getDouble("tpmR");
				tpaReal = rs.getDouble("tpaR");
				reboundsReal = rs.getDouble("rebR");
				assistsReal = rs.getDouble("astR");
				stealsReal = rs.getDouble("stlR");
				blocksReal = rs.getDouble("blkR");
				turnoversReal = rs.getDouble("tovR");

				Player proj = new Player(name, opposition, minutesProjected, pointsProjected, fgmProjected, fgaProjected, ftmProjected,
						ftaProjected, tpmProjected, tpaProjected, reboundsProjected, assistsProjected, stealsProjected, blocksProjected,
						turnoversProjected);
				Player ave = new Player(name, opposition, minutesAverage, pointsAverage, fgmAverage, fgaAverage, ftmAverage, ftaAverage,
						tpmAverage, tpaAverage, reboundsAverage, assistsAverage, stealsAverage, blocksAverage, turnoversAverage);
				Player real = new Player(name, opposition, minutesReal, pointsReal, fgmReal, fgaReal, ftmReal, ftaReal, tpmReal, tpaReal,
						reboundsReal, assistsReal, stealsReal, blocksReal, turnoversReal);

				superPlayers.add(new SuperPlayer(proj, ave, real));
			}

			double minutesP = 0, pointsP = 0, assistsP = 0, reboundsP = 0, blocksP = 0, turnoversP = 0, stealsP = 0, fgpP = 0, ftpP = 0, tppP = 0;
			double minutesA = 0, pointsA = 0, assistsA = 0, reboundsA = 0, blocksA = 0, turnoversA = 0, stealsA = 0, fgpA = 0, ftpA = 0, tppA = 0;

			for (SuperPlayer s : superPlayers) {
				minutesP += s.categoryBox.minutesProjected;
				pointsP += s.categoryBox.pointsProjected;
				assistsP += s.categoryBox.assistsProjected;
				reboundsP += s.categoryBox.reboundsProjected;
				blocksP += s.categoryBox.blocksProjected;
				turnoversP += s.categoryBox.turnoversProjected;
				stealsP += s.categoryBox.stealsProjected;
				fgpP += s.categoryBox.fgpProjected;
				ftpP += s.categoryBox.ftpProjected;
				tppP += s.categoryBox.tppProjected;
				minutesA += s.categoryBox.minutesAverage;
				pointsA += s.categoryBox.pointsAverage;
				assistsA += s.categoryBox.assistsAverage;
				reboundsA += s.categoryBox.reboundsAverage;
				blocksA += s.categoryBox.blocksAverage;
				turnoversA += s.categoryBox.turnoversAverage;
				stealsA += s.categoryBox.stealsAverage;
				fgpA += s.categoryBox.fgpAverage;
				ftpA += s.categoryBox.ftpAverage;
				tppA += s.categoryBox.tppAverage;
			}

			System.out.println("Category Accuracy:");
			System.out.println("Min: " + shave(minutesP / (minutesP + minutesA)));
			System.out.println("Pts: " + shave(pointsP / (pointsP + pointsA)));
			System.out.println("Ast: " + shave(assistsP / (assistsP + assistsA)));
			System.out.println("Reb: " + shave(reboundsP / (reboundsP + reboundsA)));
			System.out.println("Blk: " + shave(blocksP / (blocksP + blocksA)));
			System.out.println("Tov: " + shave(turnoversP / (turnoversP + turnoversA)));
			System.out.println("Stl: " + shave(stealsP / (stealsP + stealsA)));
			System.out.println("Fgp: " + shave(fgpP / (fgpP + fgpA)));
			System.out.println("Ftp: " + shave(ftpP / (ftpP + ftpA)));
			System.out.println("Tpp: " + shave(tppP / (tppP + tppA)));

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

	public double shave(double num) { // to 2 points
		return Math.floor(num * 100) / 100;
	}

	private String createPredictionsTableQuery = "CREATE TABLE `predictions` ( `id` int(9) NOT NULL AUTO_INCREMENT,"
			+ " `projectionWins` int(7) DEFAULT NULL, `averageWins` int(7) DEFAULT NULL, `date` varchar(25) DEFAULT NULL, PRIMARY KEY (`id`) )"
			+ " ENGINE=MyISAM DEFAULT  CHARSET=latin1";

	/*
	 * public static void main(String[] args) {
	 * 
	 * Connection con; try { con =
	 * DriverManager.getConnection("jdbc:mysql://brain.isi.edu:3306/fantasybb",
	 * "fantasybb", password); PreparedStatement preparedStatement =
	 * con.prepareStatement(""); ResultSet rs =
	 * preparedStatement.executeQuery("SELECT projectionWins FROM player_stats"
	 * ); int projectionWins = 0; while (rs.next()) { projectionWins +=
	 * rs.getInt("projectionWins"); }
	 * 
	 * ResultSet rs2 =
	 * preparedStatement.executeQuery("SELECT averageWins FROM player_stats");
	 * int averageWins = 0; while (rs2.next()) { averageWins +=
	 * rs2.getInt("averageWins"); }
	 * 
	 * String insertTableSQL = "INSERT INTO predictions" +
	 * "(projectionWins,averageWins,date) VALUES (?,?,?)"; preparedStatement =
	 * con.prepareStatement(insertTableSQL); preparedStatement.setDouble(1,
	 * projectionWins); preparedStatement.setDouble(2, averageWins);
	 * preparedStatement.setString(3, " meh");
	 * preparedStatement.executeUpdate(); } catch (SQLException e) {
	 * Auto-generated catch block e.printStackTrace(); } }
	 */
}

/*
 * Common SQL queries used:
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
