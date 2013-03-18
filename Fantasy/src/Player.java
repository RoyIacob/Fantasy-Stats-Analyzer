
public class Player {
	// projected player constructor
	public Player(String name, String opposition, double minutes, double points, double fgm, double fga, double ftm, double fta,
			double tpm, double tpa, double rebounds, double assists, double steals, double blocks, double turnovers) {
		this.name = name;
		this.opposition = opposition;
		this.minutes = minutes;
		this.points = points;
		this.fgm = fgm;
		this.fga = fga;
		if (fga != 0)
			this.fgp = shave(fgm / fga);
		else
			fgp = 0;
		this.ftm = ftm;
		this.fta = fta;
		if (fta != 0)
			this.ftp = shave(ftm / fta);
		else
			ftp = 0;
		this.tpm = tpm;
		this.tpa = tpa;
		if (tpa != 0)
			this.tpp = shave(tpm / tpa);
		else
			tpp = 0;
		this.rebounds = rebounds;
		this.assists = assists;
		this.steals = steals;
		this.blocks = blocks;
		this.turnovers = turnovers;
	}

	public Player(String name, String opposition, double minutes, double points, double fgp, double ftp, double tpp, double rebounds,
			double assists, double steals, double blocks, double turnovers) {
		// Player differences checker
		this.name = name;
		this.opposition = opposition;
		this.minutes = minutes;
		this.points = points;
		this.fgp = fgp;
		this.ftp = ftp;
		this.tpp = tpp;
		this.rebounds = rebounds;
		this.assists = assists;
		this.steals = steals;
		this.blocks = blocks;
		this.turnovers = turnovers;
	}

	String name;
	String opposition;
	// projected:
	double minutes;
	double points;
	double fgp;
	double fgm;
	double fga;
	double ftp, ftm, fta;
	// String tpm_a; // 3's made v. attempted
	double tpp, tpm, tpa;
	double rebounds;
	double assists;
	double steals;
	double blocks;
	double turnovers;
	String home;
	
	String team;
	String date;
	
	@Override
	public String toString() { // To string displays approximations!
		return name + ", " + shave(minutes) + "min, " + shave(points) + "pts, " +fgm + " fgm, " + fga + "fga, " + shave(fgp * 100) + "% fg, " + shave(ftp * 100) + "% ft, "
				+ shave(tpp * 100) + "% tp, " + shave(rebounds) + "reb, " + shave(assists) + "ast, " + shave(steals) + "stl, "
				+ shave(blocks) + "blk, " + shave(turnovers) + "to's.";
	}

	/*
	 * public String toString() {//quick check return name + ", " + (fgp*100) +
	 * "% fg, " + (ftp*100) + "% ft, " + (tpp*100) + "% tp, "; }
	 */

	public double shave(double num) { //to 2 points
		return Math.floor(num*100)/100;
	}
	
	public void setHomeOrAway(String isHome) {
		home = isHome;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

}
