package de.fes.nochmal.players.group05;

import de.fes.nochmal.game.PlayerChoice;
import de.fes.nochmal.model.ColorDie;
import de.fes.nochmal.model.Dice;
import de.fes.nochmal.model.NumberDie;
import de.fes.nochmal.model.Sheet;
import de.fes.nochmal.players.AbstractComputerPlayer;
import de.fes.nochmal.players.PlayerUtils;
import de.fes.nochmal.util.log.Log;
import de.fes.nochmal.model.Square;

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
		Square[][] squares = sheet.getSquares();
		
		PlayerChoice[] possiblePlayerChoices = PlayerUtils.getPossiblePlayerChoices(sheet, dice, log);
		
		boolean isAFilled = true;
		
		System.out.println("Dise: " + dice);
		
		ColorDie[] colorDies = dice.getColorDies();
	    NumberDie[] numberDies = dice.getNumberDies();
		
		for (int i = 0; i < sheet.getNumberOfRows(); i++) {
			if (!squares[0][i].isMarked()) {
				isAFilled = false; 
				break;
			}
		}
		
		
		if (isAFilled == false) {
			return possiblePlayerChoices[0];
		}
		
		
		boolean isOFilled = true;
		int lastColumnIndex = sheet.getNumberOfColumns() - 1;
		
		for (int f = 0; f < sheet.getNumberOfRows(); f++) {
			if (!squares[lastColumnIndex][f].isMarked()) {
				isOFilled = false; 
				break;
			}
		}
		
		if (isOFilled == false) {
			int lastChoiceIndex = possiblePlayerChoices.length - 1;
			
			return possiblePlayerChoices[lastChoiceIndex];
		}
		else {
			return possiblePlayerChoices[0];
		}
		
		/*
		 * if (dice.containsNumberDie(NumberDie.Four)) {}
		 */

	}
}
