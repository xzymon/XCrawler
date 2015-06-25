package com.xzymon.xcrawler.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="leaf_resource")
public class LeafResource extends Resource {
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
		sb.append("LeafResource");
		sb.append(" id: ").append(getId());
		return null;
	}
}
