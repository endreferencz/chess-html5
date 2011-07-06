package hu.mygame.shared;

import java.io.Serializable;

public class Position implements Serializable{
	private static final long serialVersionUID = 1L;
	int row, column;

	public Position() {
		this.row = -1;
		this.column = -1;
	}

	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void add(Position p) {
		this.row += p.row;
		this.column += p.column;
	}
	public void copy(Position p) {
		this.row = p.row;
		this.column = p.column;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	public int getColumn() {
		return column;
	}

	public Position getInvert(){
		return new Position(-this.row,- this.column);
	}

	public int getRow() {
		return row;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	public boolean isValid() {
		return ((row >= 0) && (row <= 7) && (column >= 0) && (column <= 7));
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
