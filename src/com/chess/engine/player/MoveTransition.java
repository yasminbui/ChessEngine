package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/*
- represents the transition from current board to next board when a move is made
- captures the resulting board and move status (i.e. are you in check?)

*/
public class MoveTransition {
	private final Board transitionBoard;
	private final Move move;
	private final MoveStatus moveStatus;
	
	public MoveTransition(final Board transitionBoard,
			final Move move,
			final MoveStatus moveStatus) {
		this.transitionBoard = transitionBoard;
		this.move = move;
		this.moveStatus = moveStatus;
	}
	
	public MoveStatus getMoveStatus() {
		return this.moveStatus;
	}
}
