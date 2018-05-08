package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class King extends Piece{
	
	private final static int[] POSSIBLE_MOVES = {-9, -8, -7, -1, 1, 7, 8, 9};

	public King(final int position, final Colour colour) {
		super(PieceType.KING, position, colour);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int possibleMove : POSSIBLE_MOVES){
			//multiply by 1 or -1 depending on piece alliance
			final int possibleDestination = this.position + (this.colour.getDirection() * possibleMove);
			
			if(firstColumnTile(this.position, possibleDestination) ||
				eighthColumnTile(this.position, possibleDestination)){
					continue;
			}
			
			if(!BoardUtils.validTile(possibleDestination)){
				//check if candidate tile is occupied
				final Tile possibleDestinationTile = board.getTile(possibleDestination);
				
				//if tile is not occupied then add it to the set of legal moves
				if(!possibleDestinationTile.isTileOccupied()){
					legalMoves.add(new MajorMove(board, this, possibleDestination));
				}else{
					//get the piece at occupied tile, if piece is of opposing team then add it to the set of legal moves
					final Piece pieceAtDestination = possibleDestinationTile.getPiece();
					final Colour pieceColour = pieceAtDestination.getPieceColour();
					
					if(pieceColour != colour){
						legalMoves.add(new AttackMove(board, this, possibleDestination, pieceAtDestination)); }		
				}
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	//check if king's position is in the first column i.e. pos 0, 8, 16... as offsets -9 -1, 7 are invalid moves
	private static boolean firstColumnTile(final int position, final int offset){
		return BoardUtils.FIRST_COLUMN[position] && (offset == -9 || offset == -1 ||
				offset == 7);
	}
	
	private static boolean eighthColumnTile(final int position, final int offset){
		return BoardUtils.EIGHTH_COLUMN[position] && (offset == -7 || offset == 1 ||
				offset == 9);
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}

	@Override
	public King movePiece(Move move) {
		return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColour());
	}

}
