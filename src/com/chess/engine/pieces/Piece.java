package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Colour;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board;

public abstract class Piece {
	protected final int position;
	protected final Colour colour;
	protected final boolean isFirstMove;
	protected final PieceType pieceType;
	private final int cachedHashCode; 
	
	Piece(final PieceType pieceType, final int position, final Colour colour){
		this.pieceType = pieceType;
		this.position = position;
		this.colour = colour;
		this.isFirstMove = false;
		this.cachedHashCode = computeHashCode();
	}
	
	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + colour.hashCode();
		result = 31 * result + position;
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		
		if(!(other instanceof Piece)) {
			return false;
		}
		
		//cast other to Piece type as if code reaches here then it must be
		final Piece otherPiece = (Piece) other;
		return position == otherPiece.getPosition() && pieceType == otherPiece.getPieceType()
				&& colour == otherPiece.getPieceColour() && isFirstMove == otherPiece.isFirstMove();
		
	}
	
	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}
	
	public boolean isFirstMove(){
		return isFirstMove;
	}
	
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	public Colour getPieceColour(){
		return this.colour;
	}
	
	public int getPosition(){
		return this.position;
	}
	
	//calculate all legal moves for a piece and return them in a collection
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	//return a new piece that represents the moved piece's new position
	public abstract Piece movePiece(Move move);
	
	public enum PieceType{
		
		PAWN("P") {
			@Override
			public boolean isKing() {
				return true;
			}
		},
		KNIGHT("N") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		BISHOP("B") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		ROOK("R") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		QUEEN("Q") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		KING("K") {
			@Override
			public boolean isKing() {
				return false;
			}
		};
		
		private String pieceName;
		
		PieceType(final String pieceName){
			this.pieceName = pieceName;
		}
		
		@Override
		public String toString() {
			return this.pieceName;
		}
		
		public abstract boolean isKing();
		
	}
	
}
