package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
	//class is immutable (state cannot be changed)
	
	//can only be set once at construction
	protected final int tilePosition;
	
	private static final Map<Integer,EmptyTile> EMPTY_TILES = possibleEmptyTiles();
	
	private static Map<Integer,EmptyTile> possibleEmptyTiles(){
		
		final Map<Integer, EmptyTile> emptyTileCache = new HashMap<>();
		
		//populate all empty tiles once only
		for(int i=0; i<BoardUtils.NUM_TILES; i++){
			emptyTileCache.put(i, new EmptyTile(i));
		}
		
		//turn map immutable so it cannot be changed (Guava)
		return ImmutableMap.copyOf(emptyTileCache);
	}
	
	//only way to create a tile, if piece is null it will be a cached tile, otherwise it will be an occupied tile
	public static Tile createTile(final int tilePosition, final Piece piece){
		return piece != null ? new OccupiedTile(tilePosition, piece) : EMPTY_TILES.get(tilePosition);
	}
	
	private Tile(final int tilePosition){
		this.tilePosition = tilePosition;
	}
	
	public abstract boolean isTileOccupied();
	
	//get current piece on tile
	public abstract Piece getPiece();
	
	public static final class EmptyTile extends Tile{
		private EmptyTile(final int tilePosition){
			super(tilePosition);
		}
		
		@Override
		public boolean isTileOccupied(){
			return false;
		}
		
		@Override
		public Piece getPiece(){
			return null;
		}
		
		@Override
		public String toString() {
			return "-";
		}
	}
	
	public static final class OccupiedTile extends Tile{
		
		private final Piece piece;
		
		private OccupiedTile(int tilePosition, final Piece piece){
			super(tilePosition);
			this.piece = piece;
		}
		
		@Override
		public boolean isTileOccupied(){
			return true;
		}
		
		@Override
		public Piece getPiece(){
			return this.piece;
		}
		
		@Override
		public String toString() {
			return getPiece().getPieceColour().isBlack() ? getPiece().toString().toLowerCase() :
				getPiece().toString();
		}
	}
}
