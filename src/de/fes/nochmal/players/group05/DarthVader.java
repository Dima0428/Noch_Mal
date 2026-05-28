
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
		PlayerChoice[] possiblePlayerChoices = PlayerUtils.getPossiblePlayerChoices(sheet, dice, log);
		
		if (possiblePlayerChoices.length == 0) {
			return PlayerChoice.Pass;
		}
		if (possiblePlayerChoices.length == 1) {
			return possiblePlayerChoices[0];
		}
		
		
		// Basispriorität: von der Mitte zu den Rändern
		//int[] columnPriority = {7, 8, 6, 9, 5, 10, 4, 11, 3, 12, 2, 13, 1, 14, 0};
		int[] columnPriority = {7, 6, 8, 5, 9, 4, 10, 3, 11, 2, 12, 1, 13, 0, 14};

		PlayerChoice bestChoice = null;
		
		boolean bestIsJoker = true;
		int bestSquaresCount = -1;
		int bestColumnPriority = -1;

		for (PlayerChoice choice : possiblePlayerChoices) {
			if (choice.isPass()) {
				continue;
			}

			// ÜBERPRÜFUNG AUF JOKER
			boolean currentIsJoker = false;
			if ((choice.getColorDie() != null && choice.getColorDie().isJoker()) || 
				(choice.getNumberDie() != null && choice.getNumberDie().isJoker())) {
				currentIsJoker = true;
			}

			// ANZAHL DER FELDER BERECHNEN
			Square[] choiceSquares = choice.getSquaresToMark();
			int currentSquaresCount = (choiceSquares != null) ? choiceSquares.length : 0;

			// WERTIGKEIT DER SPALTE BESTIMMEN
			int currentColumnPriority = -1;
			if (choiceSquares != null && choiceSquares.length > 0) {
				for (Square square : choiceSquares) {
					int col = square.getColumn();
					
					for (int i = 0; i < columnPriority.length; i++) {
						if (columnPriority[i] == col) {
							if (i > currentColumnPriority) {
								currentColumnPriority = i;
							}
							break;
						}
					}
				}
			}

			if (bestChoice == null) {
				bestChoice = choice;
				bestIsJoker = currentIsJoker;
				bestSquaresCount = currentSquaresCount;
				bestColumnPriority = currentColumnPriority;
				continue;
			}

			// REGEL 1: Joker vermeiden
			if (!currentIsJoker && bestIsJoker) {
				bestChoice = choice;
				bestIsJoker = currentIsJoker;
				bestSquaresCount = currentSquaresCount;
				bestColumnPriority = currentColumnPriority;
				continue;
			}
			if (currentIsJoker && !bestIsJoker) {
				continue;
			}
		}
		
		return bestChoice != null ? bestChoice : possiblePlayerChoices[0];
		/*
		 * if (dice.containsNumberDie(NumberDie.Four)) {}
		 */

	}
}

