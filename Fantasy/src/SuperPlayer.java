public class SuperPlayer {
	// projected player constructor
	public SuperPlayer(Player projected, Player average, Player real, String date) {
		// Player differences checker
		this.name = projected.name;
		this.opposition = projected.opposition;
		this.home = real.home;
		this.team = real.team;
		this.date = date;

		this.minutesProjected = projected.minutes;
		this.pointsProjected = projected.points;
		this.fgpProjected = projected.fgp;
		this.fgmProjected = projected.fgm;
		this.fgaProjected = projected.fga;
		// TODO fix order in table. Needs to be M,A,P
		this.ftpProjected = projected.ftp;
		this.ftmProjected = projected.ftm;
		this.ftaProjected = projected.fta;

		this.tppProjected = projected.tpp;
		this.tpmProjected = projected.tpm;
		this.tpaProjected = projected.tpa;

		this.reboundsProjected = projected.rebounds;
		this.assistsProjected = projected.assists;
		this.stealsProjected = projected.steals;
		this.blocksProjected = projected.blocks;
		this.turnoversProjected = projected.turnovers;

		this.minutesAverage = average.minutes;
		this.pointsAverage = average.points;

		this.fgpAverage = average.fgp;
		this.fgmAverage = average.fgm;
		this.fgaAverage = average.fga;

		this.ftpAverage = average.ftp;
		this.ftaAverage = average.fta;
		this.ftmAverage = average.ftm;
		this.tppAverage = average.tpp;
		this.tpmAverage = average.tpm;
		this.tpaAverage = average.tpa;

		this.reboundsAverage = average.rebounds;
		this.assistsAverage = average.assists;
		this.stealsAverage = average.steals;
		this.blocksAverage = average.blocks;
		this.turnoversAverage = average.turnovers;

		this.minutesReal = real.minutes;
		this.pointsReal = real.points;

		this.fgpReal = real.fgp;
		this.fgmReal = real.fgm;
		this.fgaReal = real.fga;

		this.ftpReal = real.ftp;
		this.ftmReal = real.ftm;
		this.ftaReal = real.fta;

		this.tppReal = real.tpp;
		this.tpmReal = real.tpm;
		this.tpaReal = real.tpa;

		this.reboundsReal = real.rebounds;
		this.assistsReal = real.assists;
		this.stealsReal = real.steals;
		this.blocksReal = real.blocks;
		this.turnoversReal = real.turnovers;

		this.projectionWins = getProjectionWins(getPlayerDifferences(real, projected), getPlayerDifferences(real, average));
		this.averageWins = getAverageWins(getPlayerDifferences(real, projected), getPlayerDifferences(real, average));
	}
	
	public SuperPlayer(Player projected, Player average, Player real) { //CATEGORICAL differences TODO here!!!!
		Player projectedDifferences = getPlayerDifferences(real, projected);
		Player averageDifferences = getPlayerDifferences(real, average);
		categoryBox = new CategoryBox(projectedDifferences, averageDifferences);		
	}
	

	String name;
	String opposition;
	String home;
	String team;
	String date;

	// projected:
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

	int projectionWins, averageWins;

	CategoryBox categoryBox;
	
	// @Override
	/*
	 * public String toString() { // To string displays approximations! return
	 * name + ", " + shave(minutes) + "min, " + shave(points) + "pts, " +
	 * shave(fgp * 100) + "% fg, " + shave(ftp * 100) + "% ft, " + shave(tpp *
	 * 100) + "% tp, " + shave(rebounds) + "reb, " + shave(assists) + "ast, " +
	 * shave(steals) + "stl, " + shave(blocks) + "blk, " + shave(turnovers) +
	 * "to's."; }
	 */

	/*
	 * public String toString() {//quick check return name + ", " + (fgp*100) +
	 * "% fg, " + (ftp*100) + "% ft, " + (tpp*100) + "% tp, "; }
	 */

	public double shave(double num) { // to 2 points
		return Math.floor(num * 100) / 100;
	}

	public void setHomeOrAway(String isHome) {
		home = isHome;
	}

	public Player getPlayerDifferences(Player predicted, Player actual) {
		// this object doesn't have fgm vs fga...only percentages (fgp)
		String name = predicted.name;
		String opposition = predicted.opposition;
		// differences:
		double minutes = (actual.minutes - predicted.minutes);
		double points = (actual.points - predicted.points);
		double rebounds = (actual.rebounds - predicted.rebounds);
		double assists = (actual.assists - predicted.assists);
		double steals = (actual.steals - predicted.steals);
		double blocks = (actual.blocks - predicted.blocks);
		double turnovers = (actual.turnovers - predicted.turnovers);
		// System.out.println(actual.fgm/actual.fga + " - " +
		// predicted.fgm/predicted.fga + " = " + (actual.fgm/actual.fga -
		// predicted.fgm/predicted.fga));

		// this is going to get ugly (no dividing by 0...)
		double fgpActual = (actual.fga != 0) ? actual.fgm / actual.fga : 0;
		double fgpPred = (predicted.fga != 0) ? predicted.fgm / predicted.fga : 0;
		double tppActual = (actual.tpa != 0) ? actual.tpm / actual.tpa : 0;
		double tppPred = (predicted.tpa != 0) ? predicted.tpm / predicted.tpa : 0;
		double ftpActual = (actual.fta != 0) ? actual.ftm / actual.fta : 0;
		double ftpPred = (predicted.fta != 0) ? predicted.fta / predicted.ftm : 0;
		double fgp = ((fgpActual - fgpPred));
		double tpp = (tppActual - tppPred);
		double ftp = (ftpActual - ftpPred);
		return new Player(name, opposition, minutes, points, fgp, ftp, tpp, rebounds, assists, steals, blocks, turnovers);
	}
	
	
	private int getProjectionWins(Player p, Player a) {
		int wins = 0;
		if (Math.abs(p.minutes) < Math.abs(a.minutes)) {
			wins++;
			// System.out.println(p.minutes + " < " + a.minutes);
		}
		if (Math.abs(p.points) < Math.abs(a.points)) {
			wins++;
			// System.out.println(p.points + " < " + a.points);
		}
		if (Math.abs(p.fgp) < Math.abs(a.fgp)) {
			wins++;
			// System.out.println(p.fgp + " < " + a.fgp);
		}
		if (Math.abs(p.ftp) < Math.abs(a.ftp)) {
			wins++;
			// System.out.println(p.ftp + " < " + a.ftp);
		}
		if (Math.abs(p.tpp) < Math.abs(a.tpp)) {
			wins++;
			// System.out.println(p.tpp + " < " + a.tpp);
		}

		if (Math.abs(p.rebounds) < Math.abs(a.rebounds)) {
			wins++;
			// System.out.println(p.rebounds + " < " + a.rebounds);
		}
		if (Math.abs(p.assists) < Math.abs(a.assists)) {
			wins++;
			// System.out.println(p.assists + " < " + a.assists);
		}
		if (Math.abs(p.steals) < Math.abs(a.steals)) {
			wins++;
			// System.out.println(p.steals + " < " + a.steals);
		}
		if (Math.abs(p.blocks) < Math.abs(a.blocks)) {
			wins++;
			// System.out.println(p.blocks + " < " + a.blocks);
		}
		if (Math.abs(p.turnovers) < Math.abs(a.turnovers)) {
			wins++;
			// System.out.println(p.turnovers + " < " + a.turnovers);
		}
		return wins;
	}

	private int getAverageWins(Player p, Player a) {
		int wins = 0;
		if (Math.abs(p.minutes) > Math.abs(a.minutes)) {
			wins++;
			// System.out.println(p.minutes + " > " + a.minutes);
		}
		if (Math.abs(p.points) > Math.abs(a.points)) {
			wins++;
			// System.out.println(p.points + " > " + a.points);
		}

		if (Math.abs(p.fgp) > Math.abs(a.fgp)) {
			wins++;
			// System.out.println(p.fgp + " > " + a.fgp);
		}
		if (Math.abs(p.ftp) > Math.abs(a.ftp)) {
			wins++;
			// System.out.println(p.ftp + " > " + a.ftp);
		}
		if (Math.abs(p.tpp) > Math.abs(a.tpp)) {
			wins++;
			// System.out.println(p.tpp + " > " + a.tpp);
		}

		if (Math.abs(p.rebounds) > Math.abs(a.rebounds)) {
			wins++;
			// System.out.println(p.rebounds + " > " + a.rebounds);
		}
		if (Math.abs(p.assists) > Math.abs(a.assists)) {
			wins++;
			// System.out.println(p.assists + " > " + a.assists);
		}
		if (Math.abs(p.steals) > Math.abs(a.steals)) {
			wins++;
			// System.out.println(p.steals + " > " + a.steals);
		}
		if (Math.abs(p.blocks) > Math.abs(a.blocks)) {
			wins++;
			// System.out.println(p.blocks + " > " + a.blocks);
		}
		if (Math.abs(p.turnovers) > Math.abs(a.turnovers)) {
			wins++;
			// System.out.println(p.turnovers + " > " + a.turnovers);
		}
		return wins;
	}
	
}
