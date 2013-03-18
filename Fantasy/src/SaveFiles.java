import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SaveFiles {
	String date;
	Date d;
	String title;
	AddSuperPlayerSQL addSuperPlayerSQL;
	
	//pred, avg
	public SaveFiles(String arg0, String arg1) {
		String realStatsUrl = "http://www.basketball-reference.com/friv/dailyleaders.cgi?month=" + (new Date().getMonth() + 1) + "&day=" + (new Date().getDate() - 1) + "&year=" + (new Date().getYear() + 1900);
		d = new Date();
		date = (d.getDate()-1) + "_" + (d.getMonth()+1) + "_" + (d.getYear()+1900);
		System.out.println("Yesterday: " + date);
		System.out.println( "/home/fantasybb/scraper/records/" + date + "-results.html" );
		saveFile(getHTTPResponse(realStatsUrl),"/home/fantasybb/scraper/records/" + date + "-results.html");
		saveFile(readFile(arg0),"/home/fantasybb/scraper/records/" + date + "-predictions.txt");
		saveFile(readFile(arg1),"/home/fantasybb/scraper/records/" + date + "-averages.html");
		System.out.println("\n***************************** DONE SAVING ************************************\n");
		scraper(arg0,getHTTPResponse(realStatsUrl),arg1);
	}
	
	/*
	public SaveFiles() {//TODO test constructor
		d = new Date();
		date = (d.getDate()-1) + "_" + (d.getMonth()+1) + "_" + (d.getYear()+1900);
		System.out.println("Yesterday: " + date);
		//saveFile(getHTTPResponse(realStatsUrl),"/home/fantasybb/scraper/records/" + date + "-results.html");
		//saveFile(readFile(arg0),"/home/fantasybb/scraper/records/" + date + "-predictions.txt");
		//saveFile(readFile(arg1),"/home/fantasybb/scraper/records/" + date + "-averages.html");
		//System.out.println("\n***************************** DONE SAVING ************************************\n");	
		//TODO: WRONG>>>>>>>
		String arg0 =         "./files/11_3_2013-predictions.txt";
		String arg1 =         "./files/11_3_2013-averages.html";
		//p,r,a
		scraper(arg0,readFile("./files/11_3_2013-results.html"),arg1);
		///WRONG^^^^
	}
	*/
	
	public void scraper(String projection, String real, String average) {
		long startTime = System.currentTimeMillis();
		ArrayList<Player> playersProjected = parseNumberFireText(new File(projection));
		ArrayList<Player> playersActualStats = parseBasketballReferenceHtml(real,"stats");
		ArrayList<Player> playersAverageStats = parseBasketballReferenceAveragesHtml(readFile(average),"per_game");
		
		ArrayList<SuperPlayer> superPlayers = new ArrayList<SuperPlayer> ();
		
		date = new Date().toString();
		if(d.getHours() > 0 && d.getHours() < 5) { //yesterday...
			date = (date.substring(4,7) + "_" + (Integer.parseInt(date.substring(8,10)) - 1) + "_" + date.substring(date.length()-4));
		} else {
			date = (date.substring(4,7) + "_" + date.substring(8,10) + "_" + date.substring(date.length()-4));
		}	

		double projectionWin = 0, averageWin = 0;
		for (Player ave: playersAverageStats) {
			for (Player act: playersActualStats) {
				for (Player proj: playersProjected) { //min!=0 is about disqualifying players who are injured.
					if(ave.name.equals(act.name) && ave.name.equals(proj.name) && ave.minutes != 0)  {
						superPlayers.add(new SuperPlayer(proj,ave,act,date));
					}
				}
				projectionWin = averageWin = 0;
			}
		}
		addSuperPlayerSQL = new AddSuperPlayerSQL(superPlayers);
		System.out.println("Total time: " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
		System.out.println("\n----------------------------- DONE ------------------------------------\n");
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
			System.out.println("file2 " + f);
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
				Player projec = new Player(name, opposition, minutesProjected, pointsProjected, fgm, fga, ftm, fta, tpm, tpa, reboundsProjected, assistsProjected, stealsProjected, blocksProjected, turnoversProjected);
				//System.out.println(projec.toString());
				players.add(projec);
				if(scan.hasNext())
					scan.nextLine();
			}			
			return players;
		} catch (Exception e) {
			System.out.println("Error: Numberfire Text");
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
			String isHome = null;
			String team = null;
			
			for (Element link : stats) {
				switch(counter){
					case 1:
						name = link.text().trim();
						break;
					case 2: 
						team = link.text().trim();
						break;
					case 3:
						isHome = link.text().trim();
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
					Player p = new Player(name, opposition, minutes, points, fgm, fga, ftm, fta, tpm, tpa, rebounds, assists, steals, blocks, turnovers);
					p.setHomeOrAway(isHome);
					p.setTeam(team);
					players.add(p);
				} else counter++;
			}
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
	
	public static void main(String[] args) {
		if(args.length == 2)
			new SaveFiles(args[0], args[1]);
		else System.out.println("Argument problem in main");
		//new SaveFiles();
	}

	private void saveFile(String s, String title) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(title));
			out.write(s);
			out.close();
			System.out.println("Done writing to " + title);
		} catch (IOException e) {
			System.out.println("Exception in Save File:");
			System.out.println(e.getStackTrace());
			System.out.println(e.getMessage());
		}
	}
	
	private  String getHTTPResponse(String targetPage) {
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

	private String readFile(String filename) { // filename
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
}
