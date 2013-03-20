public class CategoryBox {

	// Categorical victory
	public double minutesProjected, pointsProjected, assistsProjected, reboundsProjected, blocksProjected, turnoversProjected,
			stealsProjected, fgpProjected, ftpProjected, tppProjected;
	public double minutesAverage, pointsAverage, assistsAverage, reboundsAverage, blocksAverage, turnoversAverage, stealsAverage,
			fgpAverage, ftpAverage, tppAverage;

	public CategoryBox(Player p, Player a) {
		if (Math.abs(p.minutes) < Math.abs(a.minutes)) {
			minutesProjected++;
		//	 System.out.println(p.minutes + " < " + a.minutes);
		} else if (Math.abs(p.minutes) > Math.abs(a.minutes)) {
			minutesAverage++;
		//	 System.out.println(p.minutes + " < " + a.minutes);
		}
		if (Math.abs(p.points) < Math.abs(a.points)) {
			pointsProjected++;
			// System.out.println(p.points + " < " + a.points);
		} else if (Math.abs(p.points) > Math.abs(a.points)) {
			pointsAverage++;
			// System.out.println(p.points + " < " + a.points);
		}
		if (Math.abs(p.fgp) < Math.abs(a.fgp)) {
			fgpProjected++;
			// System.out.println(p.fgp + " < " + a.fgp);
		} else if (Math.abs(p.fgp) > Math.abs(a.fgp)) {
			fgpAverage++;
			// System.out.println(p.fgp + " < " + a.fgp);
		}
		if (Math.abs(p.ftp) < Math.abs(a.ftp)) {
			ftpProjected++;
			// System.out.println(p.ftp + " < " + a.ftp);
		} else if (Math.abs(p.ftp) > Math.abs(a.ftp)) {
			ftpAverage++;
			// System.out.println(p.ftp + " < " + a.ftp);
		}
		if (Math.abs(p.tpp) < Math.abs(a.tpp)) {
			tppProjected++;
			// System.out.println(p.tpp + " < " + a.tpp);
		} else if (Math.abs(p.tpp) > Math.abs(a.tpp)) {
			tppAverage++;
			// System.out.println(p.tpp + " < " + a.tpp);
		}
		if (Math.abs(p.rebounds) < Math.abs(a.rebounds)) {
			reboundsProjected++;
			// System.out.println(p.rebounds + " < " + a.rebounds);
		} else if (Math.abs(p.rebounds) > Math.abs(a.rebounds)) {
			reboundsAverage++;
			// System.out.println(p.rebounds + " < " + a.rebounds);
		}
		if (Math.abs(p.assists) < Math.abs(a.assists)) {
			assistsProjected++;
			// System.out.println(p.assists + " < " + a.assists);
		} else if (Math.abs(p.assists) > Math.abs(a.assists)) {
			assistsAverage++;
			// System.out.println(p.assists + " < " + a.assists);
		}
		if (Math.abs(p.steals) < Math.abs(a.steals)) {
			stealsProjected++;
			// System.out.println(p.steals + " < " + a.steals);
		} else if (Math.abs(p.steals) > Math.abs(a.steals)) {
			stealsAverage++;
			// System.out.println(p.steals + " < " + a.steals);
		}
		if (Math.abs(p.blocks) < Math.abs(a.blocks)) {
			blocksProjected++;
			// System.out.println(p.blocks + " < " + a.blocks);
		} else if (Math.abs(p.blocks) > Math.abs(a.blocks)) {
			blocksAverage++;
			// System.out.println(p.blocks + " < " + a.blocks);
		}
		if (Math.abs(p.turnovers) < Math.abs(a.turnovers)) {
			turnoversProjected++;
			// System.out.println(p.turnovers + " < " + a.turnovers);
		} else if (Math.abs(p.turnovers) > Math.abs(a.turnovers)) {
			turnoversAverage++;
			// System.out.println(p.turnovers + " < " + a.turnovers);
		}
	}
}
