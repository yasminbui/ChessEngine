package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.Move;

public abstract class Player {
	
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private final boolean isInCheck;
	
	Player(final Board board,
		final Collection<Move> legalMoves,
		final Collection<Move> opponentMoves){
		
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves = legalMoves;
		//get all of the opponent attacks that are attacking the player's King. if the list is not empty then the player is in check.
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPosition(), opponentMoves).isEmpty();
		
	}

	//check each of the opponent's attacking moves and whether they overlap with the King's position, if it does then add attack to collection and return.
	private static Collection<Move> calculateAttacksOnTile(int position, Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		for(final Move move : moves) {
			if(position == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		return ImmutableList.copyOf(attackMoves);
	}

	//create Kings to make board state legal, checks each piece and if it is a King, return the King as active piece
	private King establishKing() {
		for(final Piece piece : getActivePieces()) {
			if(piece.getPieceType().isKing()){
				return (King) piece;
			}
		}
		throw new RuntimeException("Why are you here?! This is not a valid board!!");
	}
	
	//check whether move belongs to player's legal set
	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}
	
	public boolean isInCheck() {
		return this.isInCheck;
	}
	
	public boolean isInCheckMate() {
		//has to be done to prevent a loop by returning this.isInCheckMate
		return this.isInCheck && !hasEscapeMoves();
	}
	
	/* go through each of the player's available moves for each piece on a theoretical board.
	 * if any of the moves can be done and the King is no longer in check then return true.
	 * if it returns false then the King must still be in check after that move.
	*/
	protected boolean hasEscapeMoves() {
		
		for(final Move move : this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			
			if(transition.getMoveStatus().isDone()) {
				return true;
			}
			
		}
		
		return false;
	}

	public boolean isInStaleMate() {
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean isCastled() {
		return false;
	}
	
	public MoveTransition makeMove(final Move move) {
		
		//return the same board as move is illegal as there are no transitions
		if(!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		final Board transitionBoard = move.execute();
		
		//calculate attack on the position of the opponent's King
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPosition(),
				transitionBoard.currentPlayer().getLegalMoves());
				
		//cannot make a move that leaves King in check
		if(!kingAttacks.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
	
		//return new transition board wrapped in a new move transition
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}

	private Piece getPlayerKing() {
		return this.playerKing;
	}

	//done polymorphically depending on white or black pieces
	public abstract Collection<Piece> getActivePieces();
	public abstract Colour getColour();
	public abstract Player getOpponent();
	
}
