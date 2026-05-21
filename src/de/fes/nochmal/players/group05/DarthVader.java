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
		// squares deklarieren, um Informationen über das Spielfeld zu erhalten
		Square[][] squares = sheet.getSquares();
		// Mögliche Züge ermitteln
		PlayerChoice[] possiblePlayerChoices = PlayerUtils.getPossiblePlayerChoices(sheet, dice, log);
		
		// Habe ich schon A erfüllt?
		
		// Zuerst nehmen wir an, dass es erfüllt ist
		boolean isAFilled = true;
		
		// Überprüfen, ob Spalte A vollständig ausgefüllt ist
		for (int i = 0; i < sheet.getNumberOfRows(); i++) {
		// Wenn eines der Quadrate nicht markiert ist, ist A noch nicht erfüllt
			if (!squares[0][i].isMarked()) {
				isAFilled = false; 
				break;
			}
		}
		
		// Falls Ja: O erfüllen
		if (isAFilled) {
			int lastChoiceIndex = possiblePlayerChoices.length - 1;
		// Den ersten verfügbaren Zug ganz rechts wählen
			return possiblePlayerChoices[lastChoiceIndex];
		}
		// Falls Nein: A erfüllen
		else {
		// Den ersten verfügbaren Zug ganz links (Richtung A) wählen
			return possiblePlayerChoices[0];
		}

	}
}
