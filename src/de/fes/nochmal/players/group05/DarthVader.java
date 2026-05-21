package de.fes.nochmal.players.group05;

import de.fes.nochmal.game.PlayerChoice;
import de.fes.nochmal.model.Dice;
import de.fes.nochmal.model.Sheet;
import de.fes.nochmal.players.AbstractComputerPlayer;
import de.fes.nochmal.players.PlayerUtils;
import de.fes.nochmal.util.log.Log;

public class DarthVader extends AbstractComputerPlayer {

	public static final String Id = "DarthVader";
	
	public DarthVader() {
		super(Id, true);
	}

	@Override
	public boolean isComputerPlayer() {
		return true;
	}

	@Override
	public void initialize(Sheet sheet, Log log) {
		//Nichts zu tun
	}

	@Override
	public PlayerChoice playTurn(Sheet sheet, int roundNumber, boolean firstPlayerInRound, Dice dice, Log log) {
		boolean pass = true;
		int color = 1;
		int number = 1; 
		int[] cord = {}; 
		
		
		//System.out.println(dice);
		
		return new PlayerChoice(pass, null, null, null);
	}
}
