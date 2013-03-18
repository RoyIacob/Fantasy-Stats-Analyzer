import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NumberFireScraper {	
	long startTime;
	ArrayList<Player> playersProjected;
	ArrayList<Player> playersActualStats;
	ArrayList<Player> playersAverageStats;
	int projectionWin = 0;
	int averageWin = 0;
	String date;
	Date d;
	String title;
	String evaluation;
	
	public NumberFireScraper(String projection, String real, String average) {
		startTime = System.currentTimeMillis();
		playersProjected = parseNumberFireText(new File(projection));
		playersActualStats = parseBasketballReferenceHtml(real,"stats");
		//slowest process
		playersAverageStats = parseBasketballReferenceAveragesHtml(readFile(average),"per_game");
		
		int count = 0;
		for (Player p: playersAverageStats) {
			for (Player q: playersActualStats) {
				for (Player r: playersProjected) { //p.min!=0 is about disqualifying players who are injured.
					if(p.name.equals(q.name) && p.name.equals(r.name) && p.minutes != 0)  {
						//System.out.println("          Actual: " + q );
						//System.out.println("      Projection: " + r);
						//System.out.println("         Average: " + p);
						Player projectionErrorPlayer = getPlayerDifferences(q,r); //creates Player stat object with the raw difference (Points-PointsPredicted)
						Player averageErrorPlayer = getPlayerDifferences(q,p);
						//System.out.println("Projection Error: " + getPlayerDifferences(q,r));
						//System.out.println("Average Error:    " + getPlayerDifferences(q,p));
						projectionWin += getProjectionWins(projectionErrorPlayer,averageErrorPlayer);
						averageWin += getAverageWins(projectionErrorPlayer,averageErrorPlayer);
						System.out.println(projectionWin + " - " + averageWin);
						count++;
					}
				}
			}
		}
		System.out.println("Total players: " + count);
		
		date = new Date().toString();
		d = new Date();
		
		if(d.getHours() > 0 && d.getHours() < 5) { //yesterday...
			date = (date.substring(4,7) + "_" + (Integer.parseInt(date.substring(8,10)) - 1) + "_" + date.substring(date.length()-4));
		} else {
			date = (date.substring(4,7) + "_" + date.substring(8,10) + "_" + date.substring(date.length()-4));
		}	
		//Save total calculation:
		System.out.println("Date: " + date);
		title =  date + "evaluation.txt";
		evaluation = "Projection win: " + projectionWin + "\n" + "   Average win: " + averageWin + "\n";
		evaluation += "Total time: " + (System.currentTimeMillis() - startTime)/1000 + "\n";
		System.out.println("Total time: " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
		saveFile(evaluation,"/home/fantasybb/scraper/records/" + title);
		System.out.println("\n***************************** DONE ************************************\n");
	}

	public NumberFireScraper() {//tester
		
		String realStatsUrl = "http://www.basketball-reference.com/friv/dailyleaders.cgi?month=" + (new Date().getMonth() + 1) + "&day=" + (new Date().getDate() - 1) + "&year=" + (new Date().getYear() + 1900); 
		System.out.println("Inspecting yesterday: " + "day=" + (new Date().getDate() - 1) + ", year=" + (new Date().getYear() + 1900));
		
		startTime = System.currentTimeMillis();
		playersProjected = parseNumberFireText(new File("./files/numberfirestats_Arg0.py.txt"));
		playersActualStats = parseBasketballReferenceHtml(getHTTPResponse(realStatsUrl),"stats");
		playersAverageStats = parseBasketballReferenceAveragesHtml(readFile("./files/averagestats_Arg1.html"),"per_game");
		
		int count = 0;
		for (Player p: playersAverageStats) {
			for (Player q: playersActualStats) {
				for (Player r: playersProjected) { //p.min!=0 is about disqualifying players who are injured.
					if(p.name.equals(q.name) && p.name.equals(r.name) && p.minutes != 0)  {
						//System.out.println("          Actual: " + q );
						//System.out.println("      Projection: " + r);
						//System.out.println("         Average: " + p);
						Player projectionErrorPlayer = getPlayerDifferences(q,r); //creates Player stat object with the raw difference (Points-PointsPredicted)
						Player averageErrorPlayer = getPlayerDifferences(q,p);
						//System.out.println("Projection Error: " + getPlayerDifferences(q,r));
						//System.out.println("Average Error:    " + getPlayerDifferences(q,p));
						projectionWin += getProjectionWins(projectionErrorPlayer,averageErrorPlayer);
						averageWin += getAverageWins(projectionErrorPlayer,averageErrorPlayer);
						System.out.println(projectionWin + " - " + averageWin);
						count++;
					}
				}
			}
		}
		System.out.println("Total players: " + count);
		date = new Date().toString();
		d = new Date();
		if(d.getHours() > 0 && d.getHours() < 5) { //yesterday...
			date = (date.substring(4,7) + "_" + (Integer.parseInt(date.substring(8,10)) - 1) + "_" + date.substring(date.length()-4));
		} else {
			date = (date.substring(4,7) + "_" + date.substring(8,10) + "_" + date.substring(date.length()-4));
		}	
		//Save total calculation:
		System.out.println("Date: " + date);
		title =  date + "evaluation.txt";
		evaluation = "Projection win: " + projectionWin + "\n" + "   Average win: " + averageWin + "\n";
		evaluation += "Total time: " + (System.currentTimeMillis() - startTime)/1000 + "\n";
		System.out.println("Total time: " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
		saveFile(evaluation,"./home/fantasybb/scraper/records/" + title);
		System.out.println("\n***************************** DONE ************************************\n");
	}
	
	//public NumberFireScraper() {
	//	saveFile(getHTTPResponse(),"numberfirehtml.html");
		//		ArrayList<Player> playersProjected = parseNumberFireHtml( getHTTPResponse()   );
	//}
	
	private int getProjectionWins(Player p, Player a) {
		int wins = 0;
		if(Math.abs(p.minutes) < Math.abs(a.minutes)) {
			wins++;
			//System.out.println(p.minutes + " < " + a.minutes);
		}
		if(Math.abs(p.points) < Math.abs(a.points)) {
			wins++;
			//System.out.println(p.points + " < " + a.points);
		}
		if(Math.abs(p.fgp) < Math.abs(a.fgp)) {
			wins++;
			//System.out.println(p.fgp + " < " + a.fgp);
		}
		if(Math.abs(p.ftp) < Math.abs(a.ftp)) {
			wins++;
			//System.out.println(p.ftp + " < " + a.ftp);
		}
		if(Math.abs(p.tpp) < Math.abs(a.tpp)) {
			wins++;
			//System.out.println(p.tpp + " < " + a.tpp);
		}
		
		if(Math.abs(p.rebounds) < Math.abs(a.rebounds)) {
			wins++;
			//System.out.println(p.rebounds + " < " + a.rebounds);
		}
		if(Math.abs(p.assists) < Math.abs(a.assists)) {
			wins++;
			//System.out.println(p.assists + " < " + a.assists);
		}
		if(Math.abs(p.steals) < Math.abs(a.steals)) {
			wins++;
			//System.out.println(p.steals + " < " + a.steals);
		}
		if(Math.abs(p.blocks) < Math.abs(a.blocks)) {
			wins++;
			//System.out.println(p.blocks + " < " + a.blocks);
		}
		if(Math.abs(p.turnovers) < Math.abs(a.turnovers)){
			wins++;
			//System.out.println(p.turnovers + " < " + a.turnovers);
		}
		return wins;
	}
	private int getAverageWins(Player p, Player a) {
		int wins = 0;
		if(Math.abs(p.minutes) > Math.abs(a.minutes)) {
			wins++;
			//System.out.println(p.minutes + " > " + a.minutes);
		}
		if(Math.abs(p.points) > Math.abs(a.points)) {
			wins++;
			//System.out.println(p.points + " > " + a.points);
		}
		
		if(Math.abs(p.fgp) > Math.abs(a.fgp)) {
			wins++;
			//System.out.println(p.fgp + " > " + a.fgp);
		}
		if(Math.abs(p.ftp) > Math.abs(a.ftp)) {
			wins++;
			//System.out.println(p.ftp + " > " + a.ftp);
		}
		if(Math.abs(p.tpp) > Math.abs(a.tpp)) {
			wins++;
			//System.out.println(p.tpp + " > " + a.tpp);
		}
		
		if(Math.abs(p.rebounds) > Math.abs(a.rebounds)) {
			wins++;
			//System.out.println(p.rebounds + " > " + a.rebounds);
		}
		if(Math.abs(p.assists) > Math.abs(a.assists)) {
			wins++;
			//System.out.println(p.assists + " > " + a.assists);
		}
		if(Math.abs(p.steals) > Math.abs(a.steals)) {
			wins++;
			//System.out.println(p.steals + " > " + a.steals);
		}
		if(Math.abs(p.blocks) > Math.abs(a.blocks)) {
			wins++;
			//System.out.println(p.blocks + " > " + a.blocks);
		}
		if(Math.abs(p.turnovers) > Math.abs(a.turnovers)){
			wins++;
			//System.out.println(p.turnovers + " > " + a.turnovers);
		}
		return wins;
	}
	
	
	
	//numfire projections - get string
	public String getNumberFireHtmlAsString() {
		URL url;
        try {
            String a="https://www.numberfire.com/nba/players/daily-fantasy-projections";
            url = new URL(a);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder html = new StringBuilder();
            
            while ((inputLine = br.readLine()) != null) {
            		html.append(inputLine + "\n");
            }
            br.close();
            System.out.println("HTML pulled.");
            return html.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}

	private  ArrayList<Player> parseNumberFireText(File f) {
		try {
			ArrayList<Player> players = new ArrayList<Player>();
			String name = null;
			String opposition = null;
			// projected:
			double minutesProjected = 0;
			double pointsProjected = 0;
			double reboundsProjected = 0;
			double assistsProjected = 0;
			double stealsProjected = 0;
			double blocksProjected = 0;
			double turnoversProjected = 0;
			double fgm, fga, tpm, tpa, fta, ftm;
			fgm = fga = tpm = tpa = fta = ftm = 0;
			Scanner scan = new Scanner(f);
			while (scan.hasNext()) { 
				name = scan.nextLine();
				opposition = scan.nextLine();
				minutesProjected = Double.parseDouble(scan.nextLine().trim());
				pointsProjected = scan.nextDouble();
				fgm = scan.nextDouble();
				fga = scan.nextDouble();
				ftm = scan.nextDouble();
				fta = scan.nextDouble();
				tpm = scan.nextDouble();
				tpa = scan.nextDouble();
				reboundsProjected = scan.nextDouble();
				assistsProjected = scan.nextDouble();
				stealsProjected = scan.nextDouble();
				blocksProjected = scan.nextDouble();
				turnoversProjected = scan.nextDouble();
				players.add(new Player(name, opposition, minutesProjected, pointsProjected, fgm, fga, ftm, fta, tpm, tpa, reboundsProjected, assistsProjected, stealsProjected, blocksProjected, turnoversProjected));
				if(scan.hasNext())
					scan.nextLine();
			}
			
			//String date = dateHtml.text().substring(28); IF YOU CANT PULL DATE BEFORE MIDNIGHT...otherwise use below method
			Date dat = new Date();
			if(dat.getHours() < 5 && dat.getHours() > 0) {
				dat.setDate(dat.getDate() - 1);
			}
			String date = (dat.getMonth() + 1) + " " + dat.getDate() + ", " + (dat.getYear() + 1900);
			//create title:
			String title = "Prediction";
			title += "_" + date + ".txt";

			StringBuilder fileContents = new StringBuilder(f.toString());
			saveFile(fileContents.toString(),"/home/fantasybb/scraper/records/" + title);

			
			return players;
		} catch (Exception e) {
			System.out.println("Error: Numberfire Text");
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	//DONT USE...no save file...
	private  ArrayList<Player> parseNumberFireHtml(String s) {
		try {
			ArrayList<Player> players = new ArrayList<Player>();
			Document d = Jsoup.parse(s);
			Element table = d.getElementById("projection-data");
			Elements stats = table.select("td");
			int counter = 0;
			String name = null;
			String opposition = null;
			// projected:
			double minutesProjected = 0;
			double pointsProjected = 0;
			String fgm_aProjected = null; // field goals made v. attempted
			String ftm_aProjected = null; // free throws made v. attempted
			String tpm_aProjected = null; // 3's made v. attempted
			double reboundsProjected = 0;
			double assistsProjected = 0;
			double stealsProjected = 0;
			double blocksProjected = 0;
			double turnoversProjected = 0;
			double fgm, fga, tpm, tpa, fta, ftm;
			fgm = fga = tpm = tpa = fta = ftm = 0;
			for (Element link : stats) {
				switch(counter){
					case 0: //Shave off (F, MIA) and leave name
						name = link.text().substring(0,link.text().indexOf("(")).trim();
						break;
					case 1:
						opposition = (link.text());
						break;
					case 2:
						minutesProjected = Double.parseDouble(link.text());
						break;
					case 3:
						pointsProjected = Double.parseDouble(link.text());
						break;
					case 4:
						fgm_aProjected = (link.text());
						fgm = Double.parseDouble(fgm_aProjected.substring(0,fgm_aProjected.indexOf("-")));
						fga = Double.parseDouble(fgm_aProjected.substring(fgm_aProjected.indexOf("-") + 1));
						break;
					case 5:
						ftm_aProjected = (link.text());
						ftm = Double.parseDouble(ftm_aProjected.substring(0,ftm_aProjected.indexOf("-")));
						fta = Double.parseDouble(ftm_aProjected.substring(ftm_aProjected.indexOf("-") + 1));
						break;
					case 6:
						tpm_aProjected = (link.text());
						tpm = Double.parseDouble(tpm_aProjected.substring(0,tpm_aProjected.indexOf("-")));
						tpa = Double.parseDouble(tpm_aProjected.substring(tpm_aProjected.indexOf("-") + 1));
						break;
					case 7:
						reboundsProjected = Double.parseDouble(link.text());
						break;
					case 8:
						assistsProjected = Double.parseDouble(link.text());
						break;
					case 9:
						stealsProjected = Double.parseDouble(link.text());
						break;
					case 10:
						blocksProjected = Double.parseDouble(link.text());
						break;
					case 11:
						turnoversProjected = Double.parseDouble(link.text());
						break;
					}
				if(counter == 14) {
					counter = 0;
					players.add(new Player(name, opposition, minutesProjected, pointsProjected, fgm, fga, ftm, fta, tpm, tpa, reboundsProjected, assistsProjected, stealsProjected, blocksProjected, turnoversProjected));
				} else counter++;
				
			}
			return players;
		} catch (Exception e) {
			System.out.println("Error: Parse NFHTML");
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Player> parseBasketballReferenceHtml(String s, String tableId) {
		try {
			ArrayList<Player> players = new ArrayList<Player>();
			Document d = Jsoup.parse(s);
			
			Element table = d.getElementById(tableId);
			Elements stats = table.select("td");
			
			int counter = 0;
			String name = null;
			String opposition = null;
			// projected:
			double minutes = 0;
			double points = 0;
			double rebounds = 0;
			double assists = 0;
			double steals = 0;
			double blocks = 0;
			double turnovers = 0;
			
			int fgm = 0, fga = 0, tpa = 0, tpm = 0, fta = 0, ftm = 0;
			for (Element link : stats) {
				switch(counter){
					case 1:
						name = link.text().trim();
						break;
					case 4:
						opposition = (link.text());
						break;
					case 6:
						String timeRaw = (link.text()); 
						//convert time format from 30:30 to 30.5 min
						minutes = Double.parseDouble(timeRaw.substring(0,timeRaw.indexOf(":"))) + Double.parseDouble(timeRaw.substring(1+timeRaw.indexOf(":")))/60;
						minutes = Math.floor((minutes*10))/10;
						break;
					case 7:
						fgm = Integer.parseInt(link.text());
						break;
					case 8:
						fga = Integer.parseInt(link.text());
						break;
					case 10:
						tpm = Integer.parseInt(link.text());
						break;
					case 11:
						tpa = Integer.parseInt(link.text());
						break;
					case 13:
						ftm = Integer.parseInt(link.text());
						break;
					case 14:
						fta = Integer.parseInt(link.text());
						break;
					case 18:
						rebounds = Double.parseDouble(link.text());
						break;
					case 19:
						assists = Double.parseDouble(link.text());
						break;
					case 20:
						steals = Double.parseDouble(link.text());
						break;
					case 21:
						blocks = Double.parseDouble(link.text());
						break;
					case 22:
						turnovers = Double.parseDouble(link.text());
						break;
					case 24:
						points = Double.parseDouble(link.text());
						break;
					}
				if(counter == 25) {
					counter = 0;
					players.add(new Player(name, opposition, minutes, points, fgm, fga, ftm, fta, tpm, tpa, rebounds, assists, steals, blocks, turnovers));
				} else counter++;
			}
			
			//String date = dateHtml.text().substring(28); IF YOU CANT PULL DATE BEFORE MIDNIGHT...otherwise use below method
			Date dat = new Date();
			if(dat.getHours() < 5 && dat.getHours() > 0) {
				dat.setDate(dat.getDate() - 1);
			}
			
			String date = (dat.getMonth() + 1) + " " + dat.getDate() + ", " + (dat.getYear() + 1900);

			//create title:
			String title = null;
			if(tableId.equals("per_game")) { //average
				title = "AverageStats";
			} else if(tableId.equals("stats")) { //results
				title = "Results";
			} else System.err.println("Error: " + "Unknown if average or results");
			title += "_" + date + ".txt";

			StringBuilder fileContents = new StringBuilder(s);
//			for(Player p: players) {
//				fileContents.append(p.name + "\n");
//				fileContents.append(p.opposition + "\n");
//				fileContents.append(p.minutes + "\n");
//				fileContents.append(p.fgm + "\n");
//				fileContents.append(p.fga + "\n");
//				fileContents.append(p.tpm + "\n");
//				fileContents.append(p.tpa + "\n");
//				fileContents.append(p.ftm + "\n");
//				fileContents.append(p.fta + "\n");
//				fileContents.append(p.rebounds + "\n");
//				fileContents.append(p.assists + "\n");
//				fileContents.append(p.steals + "\n");
//				fileContents.append(p.blocks + "\n");
//				fileContents.append(p.turnovers + "\n");
//			}
			
//			fileContents.append( "Format: name, opp, min, fgm, fga, tpm, tpa, ftm, fta, reb, assists, steals, blocks, to");
			
			saveFile(fileContents.toString(),"/home/fantasybb/scraper/records/" + title);
			return players;
		} catch (Exception e) {
			System.out.println("Error: BB Ref  (Actual Stats)...quitting");
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	
	
	public ArrayList<Player> parseBasketballReferenceAveragesHtml(String s, String tableId) {
		try {
			ArrayList<Player> players = new ArrayList<Player>();
			Document d = Jsoup.parse(s);
			Element table = d.getElementById(tableId);
			Elements stats = table.select("td");
			int counter = 0;
			String name = null;
			String opposition = null;
			// projected:
			double minutes = 0;
			double points = 0;
			double rebounds = 0;
			double assists = 0;
			double steals = 0;
			double blocks = 0;
			double turnovers = 0;
			double fgm = 0, fga = 0;
			double tpa = 0, tpm = 0;
			double fta = 0, ftm = 0;
			
			for (Element link : stats) {
				switch(counter){
					case 1:
						name = link.text().trim();
						break;
					case 7:
						minutes = Double.parseDouble(link.text());
						break;
					case 8:
						fgm = Double.parseDouble(link.text());
						break;
					case 9:
						fga = Double.parseDouble(link.text());
						break;
					case 11:
						tpm = Double.parseDouble(link.text());
						break;
					case 12:
						tpa = Double.parseDouble(link.text());
						break;
					case 14:
						ftm = Double.parseDouble(link.text());
						break;
					case 15:
						fta = Double.parseDouble(link.text());
						break;
					case 19:
						rebounds = Double.parseDouble(link.text());
						break;
					case 20:
						assists = Double.parseDouble(link.text());
						break;
					case 21:
						steals = Double.parseDouble(link.text());
						break;
					case 22:
						blocks = Double.parseDouble(link.text());
						break;
					case 23:
						turnovers = Double.parseDouble(link.text());
						break;
					case 25:
						points = Double.parseDouble(link.text());
						break;
					}
				if(counter == 25) {
					counter = 0;
					players.add(new Player(name, opposition, minutes, points, fgm, fga, ftm, fta, tpm, tpa, rebounds, assists, steals, blocks, turnovers));
				} else counter++;
			}
			
			Date dat = new Date();
			if(dat.getHours() < 5 && dat.getHours() > 0) { //yesterday...
				dat.setDate(dat.getDate() - 1);
			}
			
			String date = dat.toString();
			//creates date like: Feb_28_2013
			date = (date.substring(4,7) + "_" + date.substring(8,10) + "_" + date.substring(date.length()-4));
			//print to file:

			//create title:
			String title = null;

			if(tableId.equals("per_game")) { //average
				title = "AverageStats";
			}  else System.err.println("Error: " + "Unknown if average or results");
			title += "_" + date + ".txt";
			StringBuilder fileContents = new StringBuilder(s);

//			for(Player p: players) {
//				fileContents.append(p.name + "\n");
//				fileContents.append(p.minutes + "\n");
//				fileContents.append(p.fgm + "\n");
//				fileContents.append(p.fga + "\n");
//				fileContents.append(p.tpm + "\n");
//				fileContents.append(p.tpa + "\n");
//				fileContents.append(p.ftm + "\n");
//				fileContents.append(p.fta + "\n");
//				fileContents.append(p.rebounds + "\n");
//				fileContents.append(p.assists + "\n");
//				fileContents.append(p.steals + "\n");
//				fileContents.append(p.blocks + "\n");
//				fileContents.append(p.turnovers + "\n");
//			}
//			fileContents.append( "Format: name, min, fgm, fga, tpm, tpa, ftm, fta, reb, assists, steals, blocks, to");
			saveFile(fileContents.toString(), "/home/fantasybb/scraper/records/" + title);
			return players;
		} catch (Exception e) {
			System.out.println("Error parsing average html, quitting...");
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public Player getPlayerDifferences(Player predicted, Player actual) {
		//this object doesn't have fgm vs fga...only percentages (fgp)
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
		//System.out.println(actual.fgm/actual.fga + " - " + predicted.fgm/predicted.fga +  " = " + (actual.fgm/actual.fga - predicted.fgm/predicted.fga));
		
		//this is going to get ugly (no dividing by 0...)
		double fgpActual = (actual.fga != 0) ? actual.fgm/actual.fga : 0;
		double fgpPred = (predicted.fga != 0) ? predicted.fgm/predicted.fga : 0;
		double tppActual = (actual.tpa != 0) ? actual.tpm/actual.tpa : 0;
		double tppPred = (predicted.tpa != 0) ? predicted.tpm/predicted.tpa : 0;
		double ftpActual = (actual.fta != 0) ? actual.ftm/actual.fta : 0;
		double ftpPred = (predicted.fta != 0) ? predicted.fta/predicted.ftm : 0;
		double fgp = ((fgpActual - fgpPred));
		double tpp = (tppActual - tppPred);
		double ftp = (ftpActual - ftpPred);
		return new Player(name, opposition, minutes, points, fgp, ftp, tpp, rebounds, assists, steals, blocks, turnovers);
	}
	
	
	/*public static void main(String[] args) {	
		//pred,real,average:
		String realStatsUrl = "http://www.basketball-reference.com/friv/dailyleaders.cgi?month=";
		realStatsUrl += (new Date().getMonth() + 1);
		if(args.length == 2) //normal, else go back one day.
			realStatsUrl += "&day=" + new Date().getDate() + "&year=" + (new Date().getYear() + 1900);
		else  {
			realStatsUrl += "&day=" + (new Date().getDate() - 1) + "&year=" + (new Date().getYear() + 1900); 
			System.out.println("Inspecting yesterday: " + "day=" + (new Date().getDate() - 1) + ", year=" + (new Date().getYear() + 1900));
		}
		System.out.println(realStatsUrl);
		
		//TODO this is the normal constructor, delete the files too...will be a problem
		//new NumberFireScraper(args[0],getHTTPResponse(realStatsUrl),args[1]);
		new NumberFireScraper();
	}*/
	
	private void saveFile(String s, String title) {
		try {
			//TODO REMOVE THE DOT
			BufferedWriter out = new BufferedWriter(new FileWriter("." + title));
			out.write(s);
			out.close();
			System.out.println("Done writing to " + title);
		} catch (IOException e) {
			System.out.println("Exception in Save File:");
			System.out.println(e.getStackTrace());
			System.out.println(e.getMessage());
		}
	}

	private static String readFile(String filename) { // filename
		String text = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while (in.ready())
				text += in.readLine() + "\n";
			in.close();
		} catch (IOException e) {
			System.out.println("Exception2 ");
			System.out.println(e.getCause());
		}
		if (text != null)
			System.out.println(filename + " read successfully");
		else
			System.err.println("Error with: " + filename);
		return text;
	}
	
	private static String getHTTPResponse(String targetPage) {
		String targetURL = targetPage;
		String urlParameters;
		URL url;

		HttpURLConnection connection = null;
		try {
			url = new URL(targetURL);
			urlParameters = "stat1=" + URLEncoder.encode("S", "UTF-8") + "&stat2=" + URLEncoder.encode("S_2012", "UTF-8");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\n');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			System.out.println("connection failed...quitting");
			e.printStackTrace();
			System.exit(0);
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
