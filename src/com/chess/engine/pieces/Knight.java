package com.chess.engine.pieces;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

import static com.chess.engine.board.Move.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class Knight extends Piece{
	
	private final static int[] POSSIBLE_MOVES = {-6, -10, -15, -17, 6, 10, 15, 17};
	
	public Knight(final int position, final Colour colour) {
		super(PieceType.KNIGHT, position, colour);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board){
		List<Move> legalMoves = new ArrayList<>();
		
		//possible positions = current position + possible move offset
		for(final int possibleMove : POSSIBLE_MOVES){
			final int possibleDestination = this.position + possibleMove;
			
			if(BoardUtils.validTile(possibleDestination)){
				
				if(firstColumnTile(this.position, possibleMove) ||
						secondColumnTile(this.position, possibleMove) ||
						seventhColumnTile(this.position, possibleMove) ||
						eighthColumnTile(this.position, possibleMove)){
					continue;
				}
				
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
	
	//check if knight's position is in the first column i.e. pos 0, 8, 16... as offsets -17, -10, 6, 15 are invalid moves
	private static boolean firstColumnTile(final int position, final int offset){
		return BoardUtils.FIRST_COLUMN[position] && (offset == -17 || offset == -10 ||
				offset == 6 || offset == 15);
	}
	
	private static boolean secondColumnTile(final int position, final int offset){
		return BoardUtils.SECOND_COLUMN[position] && (offset == -10 || offset == 6);
	}
	
	private static boolean seventhColumnTile(final int position, final int offset){
		return BoardUtils.SEVENTH_COLUMN[position] && (offset == -6 || offset == 10);
	}
	
	private static boolean eighthColumnTile(final int position, final int offset){
		return BoardUtils.EIGHTH_COLUMN[position] && (offset == -15 || offset == -6 ||
				offset == 10 || offset == 17);
	}
	
	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}

	@Override
	public Knight movePiece(Move move) {
		return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColour());
	}
	
}
