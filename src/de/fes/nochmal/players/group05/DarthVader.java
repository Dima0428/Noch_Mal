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

	public static final String Id = "NichtSuperDarthVader";
	
	public DarthVader() {
		super(Id, true);
	}

	@Override
	public boolean isComputerPlayer() {
		return true;
	}

	@Override
	public void initialize(Sheet sheet, Log log) {
		// Nichts zu tun
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
		
		// 1. DYNAMISCHE GEWICHTUNG DER SPALTEN BESTIMMEN
		// Symmetrisches Basis-Array: Die Ränder (0 und 14) haben maximale Priorität 7 für die Expansion
		int[] columnWeights = {7, 6, 5, 4, 3, 2, 1, 0, 1, 2, 3, 4, 5, 6, 7};
		
		if (roundNumber <= 10) {
			boolean leftEdgeReached = false;  // Spalten 0-3
			boolean rightEdgeReached = false; // Spalten 11-14
			
			Square[] markedSquares = PlayerUtils.getMarkedSquares(sheet);
			for (Square square : markedSquares) {
				int col = square.getColumn();
				if (col <= 3) {
					leftEdgeReached = true;
				}
				if (col >= 11) {
					rightEdgeReached = true;
				}
			}
			
			if (leftEdgeReached && !rightEdgeReached) {
				// Linke Seite erreicht -> Prioritäten strikt nach rechts verschieben (14 ist das Maximum)
				columnWeights = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
			} else if (rightEdgeReached && !leftEdgeReached) {
				// Rechte Seite erreicht -> Prioritäten strikt nach links verschieben (0 ist das Maximum)
				columnWeights = new int[]{14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
			}
		}

		PlayerChoice bestChoice = null;
		boolean bestIsJoker = true;
		int bestScore = -1;

		for (PlayerChoice choice : possiblePlayerChoices) {
			if (choice.isPass()) {
				continue;
			}

			// 2. ÜBERPRÜFUNG AUF JOKER
			boolean currentIsJoker = false;
			if ((choice.getColorDie() != null && choice.getColorDie().isJoker()) || 
				(choice.getNumberDie() != null && choice.getNumberDie().isJoker())) {
				currentIsJoker = true;
			}

			// 3. ANZAHL DER FELDER BERECHNEN
			Square[] choiceSquares = choice.getSquaresToMark();
			int currentSquaresCount = (choiceSquares != null) ? choiceSquares.length : 0;

			// 4. WERTIGKEIT DER POSITION BESTIMMEN (Direktes Auslesen aus dem Gewichtungs-Array)
			int currentColumnPriority = 0;
			if (choiceSquares != null && choiceSquares.length > 0) {
				for (Square square : choiceSquares) {
					int col = square.getColumn();
					if (col >= 0 && col < columnWeights.length) {
						// Maximales Gewicht aller durch den Zug markierten Felder finden
						if (columnWeights[col] > currentColumnPriority) {
							currentColumnPriority = columnWeights[col];
						}
					}
				}
			}

			// 5. MATHEMATISCHE BEWERTUNG (SCORE) NACH SPIELPHASEN BERECHNEN
			int currentScore = 0;
			
			if (roundNumber <= 10) {
				// PHASE 1 (Bis Runde 10): Aggressive Expansion zu den Rändern.
				// Spaltenpriorität ist sehr wichtig, aber große Züge (viele Felder) werden nicht ignoriert.
				currentScore = (currentSquaresCount * 3) + currentColumnPriority;
			} else {
				// PHASE 2 (Nach Runde 10): Totale Gier.
				// Anzahl der Felder wird mit 10 multipliziert — sie dominiert absolut über der Position.
				currentScore = (currentSquaresCount * 10) + currentColumnPriority;
			}

			// Initialisierung des ersten Zuges
			if (bestChoice == null) {
				bestChoice = choice;
				bestIsJoker = currentIsJoker;
				bestScore = currentScore;
				continue;
			}

			// SPAR-REGEL: Ein regulärer Zug ist immer besser als ein Joker-Zug
			if (!currentIsJoker && bestIsJoker) {
				bestChoice = choice;
				bestIsJoker = currentIsJoker;
				bestScore = currentScore;
				continue;
			}
			if (currentIsJoker && !bestIsJoker) {
				continue;
			}

			// Vergleich der berechneten Phasen-Scores
			if (currentScore > bestScore) {
				bestChoice = choice;
				bestScore = currentScore;
			} 
			// Wenn die Züge punktgleich sind, wird die Farbe Blau als Tie-Breaker genutzt
			else if (currentScore == bestScore) {
				if (choice.getColorDie() == ColorDie.Blue && bestChoice.getColorDie() != ColorDie.Blue) {
					bestChoice = choice;
				}
			}
		}
		
		return bestChoice != null ? bestChoice : possiblePlayerChoices[0];
	}
}