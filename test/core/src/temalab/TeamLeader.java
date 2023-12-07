package temalab;

import java.util.Scanner;
import java.io.*;

public class TeamLeader {
	private final OutputStream outputStream;
	private final InputStream inputStream;
	private Team team;
	private Scanner sc;

	public TeamLeader(Team team, String fileName) {
		this.team = team;
		// ProcessBuilder processBuilder = new ProcessBuilder("python3", fileName);
		// Process process = null;
		// try {
		// process = processBuilder.start();
		// } catch (IOException e) {
		// throw new RuntimeException(e);
		// }
		// outputStream = process.getOutputStream();
		// inputStream = process.getInputStream();
		outputStream = System.out;
		inputStream = System.in;
		sc = new Scanner(inputStream);
	}

	public void registerUnit() {
		System.err.print("registering");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream), true);
		out.println(team.getBudget());
		String answer = sc.nextLine();
		String[] split = answer.split(" ");
		loop:while(true) {
			switch (split[0]) {
				case "done": 
					break loop;
				case "add": {
					switch (split[1]) {
						case "tank": {
							team.addUnit(new Unit(new Position(Integer.parseInt(split[2]), Integer.parseInt(split[3])), team, Unit.Type.TANK));
						} break;
						case "scout": {
							team.addUnit(new Unit(new Position(Integer.parseInt(split[2]), Integer.parseInt(split[3])), team, Unit.Type.SCOUT));
						} break;
						case "infantry": {
							team.addUnit(new Unit(new Position(Integer.parseInt(split[2]), Integer.parseInt(split[3])), team, Unit.Type.INFANTRY));
						} break;
						default:
							break loop;
					}
				}
			}
			answer = sc.nextLine();
			split = answer.split(" ");
		}
		out.close();
		System.err.print("ENDregistering");
	}

	public void communicate() {
		System.err.print(team.getName() + "communicating");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream), true);
		// TODO: when communication will be done with python, there should be a timeout
		// value
		team.refillActionPoints();
		team.updateUnits();
		out.println(team.teamMembersToString(false).toString());
		String answer = sc.nextLine();
		String[] split = answer.split(" ");
		loop: while (true) { // TODO: a true helyett kell majd egy n seces timer, hogy ne várhasson so kideig a python
			switch (split[0]) {
				case "endTurn":
					break loop;
				case "move": {
					if (split.length == 4) {
						// TODO: a parseInt dobhat kivételt, ha nem int
						team.moveUnit(Integer.parseInt(split[1]),
								new Position(Integer.parseInt(split[2]), Integer.parseInt(split[3])));
					}
				}
					break;

				case "shoot": {
					if (split.length == 4) {
						// TODO: a parseInt dobhat kivételt, ha nem int
						team.fireUnit(Integer.parseInt(split[1]),
								new Position(Integer.parseInt(split[2]), Integer.parseInt(split[3])));
					}
				}
					break;

				default:
					break loop;
			} 
			team.updateUnits();
			out.println(team.teamMembersToString(false).toString());
			answer = sc.nextLine();
			split = answer.split(" ");
		}
		out.close();
		System.err.print("ENDcommunicating");
	}

	public void endSimu(boolean win) {
		PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream), true);
		out.println(win);
		out.close();
	}

	public Team getTeam() {
		return team;
	}
}
