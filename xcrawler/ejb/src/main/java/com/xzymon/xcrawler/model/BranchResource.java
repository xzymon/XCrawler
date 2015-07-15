package com.xzymon.xcrawler.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="branch_resources")
public class BranchResource extends Resource{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("BranchResource");
		sb.append(" id: ").append(getId());
		return null;
	}
}
