package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.chess.engine.board.Board.Builder;

public abstract class Move {
	final Board board;
	final Piece movedPiece;
	final int pieceDestination;
	
	public static final Move NULL_MOVE = new NullMove();
	
	private Move(final Board board, final Piece movedPiece, final int pieceDestination){
			this.board = board;
			this.movedPiece = movedPiece;
			this.pieceDestination = pieceDestination;
	}
	
	public int getCurrentCoordinate() {
		return this.getMovedPiece().getPosition();
	}

	public int getDestinationCoordinate() {
		return this.pieceDestination;
	}
	
	public Piece getMovedPiece() {
		return this.movedPiece;
	}
	
	//return a new board when move executes, current board is immutable
	public Board execute() {
		final Builder builder = new Builder();
		
		//go through current player's pieces and if they are not the moved piece then set their positions on transition board
		for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
			if(!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		
		
		for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
			builder.setPiece(piece);
		}
		
		//set piece onto moved position (set tile to null and change move maker to opposing player)
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColour());
		return builder.build();
	}
	
	
	//major piece moved
	public static final class MajorMove extends Move{
		public MajorMove(final Board board, final Piece movedPiece, final int pieceDestination){
			super(board, movedPiece, pieceDestination);
		}
	}
	
	//piece attacks
	public static class AttackMove extends Move{
		final Piece pieceAttacked;
		
		public AttackMove(final Board board, final Piece movedPiece,
				final int pieceDestination, final Piece pieceAttacked){
			super(board, movedPiece, pieceDestination);
			this.pieceAttacked = pieceAttacked;
		}
		
		//return a new board when move executes, current board is immutable
		@Override
		public Board execute() {
			return null;
		}
	}
	
	public static final class PawnMove extends Move{
		public PawnMove(final Board board, final Piece movedPiece,
				final int pieceDestination){
			super(board, movedPiece, pieceDestination);
		}
	}
	
	public static class PawnAttackMove extends AttackMove{
		public PawnAttackMove(final Board board, final Piece movedPiece,
				final int pieceDestination, final Piece pieceAttacked){
			super(board, movedPiece, pieceDestination, pieceAttacked);
		}
	}
	
	public static final class PawnEnPassantAttack extends PawnAttackMove{
		public PawnEnPassantAttack(final Board board, final Piece movedPiece,
				final int pieceDestination, final Piece pieceAttacked){
			super(board, movedPiece, pieceDestination, pieceAttacked);
		}
	}
	
	public static final class PawnJump extends Move{
		public PawnJump(final Board board, final Piece movedPiece,
				final int pieceDestination){
			super(board, movedPiece, pieceDestination);
		}
	}
	
	static abstract class CastleMove extends Move{
		public CastleMove(final Board board, final Piece movedPiece,
				final int pieceDestination){
			super(board, movedPiece, pieceDestination);
		}
	}
	
	public static final class KingSideCastleMove extends Move{
		public KingSideCastleMove(final Board board, final Piece movedPiece,
				final int pieceDestination){
			super(board, movedPiece, pieceDestination);
		}
	}
	
	public static final class QueenSideCastleMove extends Move{
		public QueenSideCastleMove(final Board board, final Piece movedPiece,
				final int pieceDestination){
			super(board, movedPiece, pieceDestination);
		}
	}
	
	public static final class NullMove extends Move{
		public NullMove(){
			super(null, null, -1);
		}
		
		@Override
		public Board execute() {
			throw new RuntimeException("Cannot execute a null move!");
		}
		
	}
	
	public static class MoveFactory{
		
		private MoveFactory() {
			throw new RuntimeException("Not possible to instantiate!");
		}
		
		public static Move createMove(final Board board, final int currentCoordinate,
				final int destinationCoordinate) {
			
			for(final Move move : board.getAllLegalMoves()) {
				if(move.getCurrentCoordinate() == currentCoordinate &&
						move.getDestinationCoordinate() == destinationCoordinate) {
					return move;
				}
			}
			
			return NULL_MOVE;
			
		}
		
	}
	
	
}
