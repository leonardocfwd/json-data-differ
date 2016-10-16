package com.waesst.jsondiffer.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Base class for storing Diff id and the byte arrays for comparison.
 * @author Leonardo Nelson
 *
 */
@Entity
@Table(name = "DIFF", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Diff {

	@Id
	@Column(name = "id", unique = true, nullable = false)
    private String id;
	
	@Column(name="left")
    private byte[] left;
	
	@Column(name="right")
    private byte[] right;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getLeft() {
		return left;
	}

	public void setLeft(byte[] left) {
		this.left = left;
	}

	public byte[] getRight() {
		return right;
	}

	public void setRight(byte[] right) {
		this.right = right;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Arrays.hashCode(left);
		result = prime * result + Arrays.hashCode(right);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Diff other = (Diff) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (!Arrays.equals(left, other.left))
			return false;
		if (!Arrays.equals(right, other.right))
			return false;
		return true;
	}
	
}
