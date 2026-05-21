package de.fes.nochmal.players.group05;

import de.fes.nochmal.game.PlayerChoice;
import de.fes.nochmal.model.Dice;
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
		// обьявляем squares чтобы получать информацию о игровом поле
		Square[][] squares = sheet.getSquares();
		
		// обьяляем возможные ходы 
		PlayerChoice[] possiblePlayerChoices = PlayerUtils.getPossiblePlayerChoices(sheet, dice, log);
		
		// Habe ich schon A erfüllt?
		
		// сначало думаем что да
		boolean isAFilled = true;
		
		// проверяем заполнели ли мы А 
		for (int i = 0; i < sheet.getNumberOfRows(); i++) {
			if (!squares[0][i].isMarked()) {
				isAFilled = false; 
				break;
			}
		}
		
		if (isAFilled) {
			int lastChoiceIndex = possiblePlayerChoices.length - 1;
			
			return possiblePlayerChoices[lastChoiceIndex];
		}
		else {
			return possiblePlayerChoices[0];
		}
		
		
		return possiblePlayerChoices[0];
		
		//System.out.println(dice);

	}
}
