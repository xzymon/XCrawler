package com.xzymon.xcrawler.model;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="runs")
public class Run implements Serializable{
	@Transient
	private static final long serialVersionUID = 4728381033570212496L;
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="id")
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="started")
	private Date started;
	@Column(name="root_url", length=3000)
	private String rootURL;
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="serialized_policy")
	private Serializable serializedPolicy;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getStarted() {
		return started;
	}
	public void setStarted(Date started) {
		this.started = started;
	}
	public String getRootURL() {
		return rootURL;
	}
	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}
	public Serializable getSerializedPolicy() {
		return serializedPolicy;
	}
	public void setSerializedPolicy(Serializable serializedPolicy) {
		this.serializedPolicy = serializedPolicy;
	}
}
