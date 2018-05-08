package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {
	
	private final static int[] POSSIBLE_MOVES = {7, 8, 9, 16};

	public Pawn(final int position, final Colour colour) {
		super(PieceType.PAWN, position, colour);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int possibleMove : POSSIBLE_MOVES){
			//multiply by 1 or -1 depending on piece alliance
			final int possibleDestination = this.position + (this.colour.getDirection() * possibleMove);
			
			if(!BoardUtils.validTile(possibleDestination)){
				continue;
			}
			
			//move case
			if(possibleMove == 8 && !board.getTile(possibleDestination).isTileOccupied()){
				legalMoves.add(new Move.MajorMove(board, this, possibleDestination));
			}
			//possible to move two spaces if it is pawns first move and they are on second row (black) or seventh row (white)
			else if(possibleMove == 16 && this.isFirstMove() && 
					(BoardUtils.SECOND_ROW[this.position] && this.getPieceColour().isBlack()) ||
					(BoardUtils.SEVENTH_ROW[this.position] && this.getPieceColour().isWhite())){	
				final int behindPossibleMove = this.position + (this.colour.getDirection() * 8);
				//check whether tiles that are one/two rows ahead are occupied
				if(!board.getTile(behindPossibleMove).isTileOccupied() && !board.getTile(possibleDestination).isTileOccupied()){
					legalMoves.add(new Move.MajorMove(board, this, possibleDestination));
				}
			}
			//attack case: handling exception if black is on first column and white is on eighth column
			else if(possibleMove == 7 &&
					!((BoardUtils.EIGHTH_COLUMN[this.position] && this.colour.isWhite() ||
					(BoardUtils.FIRST_COLUMN[this.position] && this.colour.isBlack())))){
				if(board.getTile(possibleDestination).isTileOccupied()){
					final Piece pieceOnPossibleDestination = board.getTile(possibleDestination).getPiece();
					if(pieceOnPossibleDestination.getPieceColour() != this.colour){
						legalMoves.add(new Move.MajorMove(board, this, possibleDestination));
					}
				}
			}else if(possibleMove == 9 &&
					!((BoardUtils.FIRST_COLUMN[this.position] && this.colour.isWhite() ||
					(BoardUtils.EIGHTH_COLUMN[this.position] && this.colour.isBlack())))){
				if(board.getTile(possibleDestination).isTileOccupied()){
					final Piece pieceOnPossibleDestination = board.getTile(possibleDestination).getPiece();
					if(pieceOnPossibleDestination.getPieceColour() != this.colour){
						legalMoves.add(new Move.MajorMove(board, this, possibleDestination));
						}
					}		
				}
			}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}

	@Override
	public Pawn movePiece(Move move) {
		return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColour());
	}
}
