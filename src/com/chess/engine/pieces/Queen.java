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

public class Queen extends Piece {
	
	private final static int[] POSSIBLE_MOVES_VECTOR = {-9, -8, -7, -1, 1, 7, 8, 9};

	public Queen(int position, Colour colour) {
		super(PieceType.QUEEN, position, colour);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int possibleMove: POSSIBLE_MOVES_VECTOR){
			int possibleDestination = this.position;
			
			//is position valid i.e is piece off the board
			while(BoardUtils.validTile(possibleDestination)){
				
				if(firstColumnTile(this.position, possibleMove) ||
						eighthColumnTile(this.position, possibleMove)){
					break;
				}
				
				possibleDestination += possibleMove;
				
				//apply offset and check if new position is valid
				if(BoardUtils.validTile(possibleDestination)){
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
					//if tile is occupied, break from loop as bishop cannot pass through occupied squares
					break;
				}
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	//positions that do not work
	private static boolean firstColumnTile(final int position, final int offset){
		return BoardUtils.FIRST_COLUMN[position] && (offset == -9 || offset == -1 || offset == 7);
	}
	
	private static boolean eighthColumnTile(final int position, final int offset){
		return BoardUtils.EIGHTH_COLUMN[position] && (offset == -7 || offset == 1 || offset == 9);
	}
	
	@Override
	public String toString() {
		return PieceType.QUEEN.toString();
	}

	@Override
	public Queen movePiece(Move move) {
		return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColour());
	}

}
