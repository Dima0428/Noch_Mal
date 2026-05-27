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
		
		
		// Базовый приоритет: от центра к краям
		//int[] columnPriority = {7, 8, 6, 9, 5, 10, 4, 11, 3, 12, 2, 13, 1, 14, 0};
		int[] columnPriority = {7, 6, 8, 5, 9, 4, 10, 3, 11, 2, 12, 1, 13, 0, 14};
		
		// Если мы в фазе экспансии (до 10 раунда) и один из краев уже взят, меняем веса колонок
		if (roundNumber <= 10) {
			
			boolean leftEdgeReached = false;  // Достигли колонки 0 (A)?
			boolean rightEdgeReached = false; // Достигли колонки 14 (O)?
			
			Square[] markedSquares = PlayerUtils.getMarkedSquares(sheet);
			for (Square square : markedSquares) {
				int col = square.getColumn();
				if (col == 0 || col == 1 || col == 2 || col == 3) {
					leftEdgeReached = true;
				}
				if (col == 14 || col == 13 || col == 12 || col == 11) {
					rightEdgeReached = true;
					
				}
			}
			
			System.out.println(rightEdgeReached + " " + leftEdgeReached);
			
			if (leftEdgeReached && !rightEdgeReached) {
				// Лево уже захвачено, искусственно обесцениваем левую половину, чтобы бот шел вправо
				columnPriority = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
			} else if (rightEdgeReached && !leftEdgeReached) {
				// Право захвачено, обесцениваем правую половину, разворачиваем бота налево
				columnPriority = new int[]{14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
			}
		}

		PlayerChoice bestChoice = null;
		
		boolean bestIsJoker = true;
		int bestSquaresCount = -1;
		int bestColumnPriority = -1;

		for (PlayerChoice choice : possiblePlayerChoices) {
			if (choice.isPass()) {
				continue;
			}

			// ПРОВЕРКА НА ДЖОКЕРЫ
			boolean currentIsJoker = false;
			if ((choice.getColorDie() != null && choice.getColorDie().isJoker()) || 
				(choice.getNumberDie() != null && choice.getNumberDie().isJoker())) {
				currentIsJoker = true;
			}

			// СЧИТАЕМ КОЛИЧЕСТВО КВАДРАТОВ
			Square[] choiceSquares = choice.getSquaresToMark();
			int currentSquaresCount = (choiceSquares != null) ? choiceSquares.length : 0;

			// ОПРЕДЕЛЯЕМ ЦЕННОСТЬ КОЛОНКИ
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

			// ПРАВИЛО 1: Избегаем джокеров
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
			
			if (roundNumber <= 10) {
				// --- РЕЖИМ ДО 10 РАУНДА ---
				if (currentColumnPriority > bestColumnPriority) {
					bestChoice = choice;
					bestColumnPriority = currentColumnPriority;
					bestSquaresCount = currentSquaresCount;
				} 
				else if (currentColumnPriority == bestColumnPriority) {
					if (currentSquaresCount > bestSquaresCount) {
						bestChoice = choice;
						bestSquaresCount = currentSquaresCount;
					}
				}
			} else {
				// --- РЕЖИМ ПОСЛЕ 10 РАУНДА ---
				if (currentSquaresCount > bestSquaresCount) {
					bestChoice = choice;
					bestSquaresCount = currentSquaresCount;
					bestColumnPriority = currentColumnPriority;
				} 
				else if (currentSquaresCount == bestSquaresCount) {
					if (currentColumnPriority > bestColumnPriority) {
						bestChoice = choice;
						bestColumnPriority = currentColumnPriority;
					}
				}
			}
		}
		
		return bestChoice != null ? bestChoice : possiblePlayerChoices[0];
		/*
		 * if (dice.containsNumberDie(NumberDie.Four)) {}
		 */

	}
}
