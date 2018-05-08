package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Colour;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Board {
	
	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	
	private Board(final Builder builder){
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Colour.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Colour.BLACK);
		
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer = new BlackPlayer(this, blackStandardLegalMoves, whiteStandardLegalMoves);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}
	
	//prints the board
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for(int i=0; i<BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
				builder.append("\n");
			}
		}
		
		return builder.toString();
	}
	
	public Player whitePlayer() {
		return this.whitePlayer;
	}
	
	public Player blackPlayer() {
		return this.blackPlayer;
	}
	
	public Player currentPlayer() {
		return this.currentPlayer;
	}
	
	public Collection<Piece> getBlackPieces(){
		return this.blackPieces;
	}
	
	public Collection<Piece> getWhitePieces(){
		return this.whitePieces;
	}
	
	//calculate and return legal moves for given alliance
	private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final Piece piece: pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		
		return ImmutableList.copyOf(legalMoves);
	}

	//returns a collection of active pieces
	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Colour colour) {
		
		final List<Piece> activePieces = new ArrayList<>();
		for(final Tile tile : gameBoard) {
			if(tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				if(piece.getPieceColour() == colour) {
					activePieces.add(piece);
				}
			}
		}	
		return ImmutableList.copyOf(activePieces);
	}

	private static List<Tile> createGameBoard(final Builder builder) {
		//create a board of 64 tiles
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		for(int i=0; i<BoardUtils.NUM_TILES; i++){
			//associating tiles with piece at position i, otherwise return null
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		return ImmutableList.copyOf(tiles);
	}
	
	//method to create board with initial positions
	public static Board createStandardBoard(){
		final Builder builder = new Builder();
		
		//layout for black pieces
		builder.setPiece(new Rook(0, Colour.BLACK));
		builder.setPiece(new Knight(1, Colour.BLACK));
		builder.setPiece(new Bishop(2, Colour.BLACK));
		builder.setPiece(new Queen(3, Colour.BLACK));
		builder.setPiece(new King(4, Colour.BLACK));
		builder.setPiece(new Bishop(5, Colour.BLACK));
		builder.setPiece(new Knight(6, Colour.BLACK));
		builder.setPiece(new Rook(7, Colour.BLACK));
		builder.setPiece(new Pawn(8, Colour.BLACK));
		builder.setPiece(new Pawn(9, Colour.BLACK));
		builder.setPiece(new Pawn(10, Colour.BLACK));
		builder.setPiece(new Pawn(11, Colour.BLACK));
		builder.setPiece(new Pawn(12, Colour.BLACK));
		builder.setPiece(new Pawn(13, Colour.BLACK));
		builder.setPiece(new Pawn(14, Colour.BLACK));
		builder.setPiece(new Pawn(15, Colour.BLACK));
		
		//layout for white pieces
		builder.setPiece(new Pawn(48, Colour.WHITE));
		builder.setPiece(new Pawn(49, Colour.WHITE));
		builder.setPiece(new Pawn(50, Colour.WHITE));
		builder.setPiece(new Pawn(51, Colour.WHITE));
		builder.setPiece(new Pawn(52, Colour.WHITE));
		builder.setPiece(new Pawn(53, Colour.WHITE));
		builder.setPiece(new Pawn(54, Colour.WHITE));
		builder.setPiece(new Pawn(55, Colour.WHITE));
		builder.setPiece(new Rook(56, Colour.WHITE));
		builder.setPiece(new Knight(57, Colour.WHITE));
		builder.setPiece(new Bishop(58, Colour.WHITE));
		builder.setPiece(new Queen(59, Colour.WHITE));
		builder.setPiece(new King(60, Colour.WHITE));
		builder.setPiece(new Bishop(61, Colour.WHITE));
		builder.setPiece(new Knight(62, Colour.WHITE));
		builder.setPiece(new Rook(63, Colour.WHITE));
		
		//set white to move
		builder.setMoveMaker(Colour.WHITE);
		return builder.build();
	}
	
	public Iterable<Move> getAllLegalMoves(){
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
	}

	public Tile getTile(final int position){
		return this.gameBoard.get(position);
	}
	
	public static class Builder{
		
		Map<Integer, Piece> boardConfig;
		Colour nextMoveMaker;
		public Builder(){
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece){
			this.boardConfig.put(piece.getPosition(), piece);
			return this;
		}
		
		public Builder setMoveMaker(final Colour nextMoveMaker){
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}
		
		public Board build(){
			return new Board(this);
		}
	}
}
