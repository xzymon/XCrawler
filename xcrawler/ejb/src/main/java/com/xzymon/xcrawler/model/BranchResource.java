package com.xzymon.xcrawler.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="branch_resource")
public class BranchResource extends Resource{
	@Id
	@GeneratedValue
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
