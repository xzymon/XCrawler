package com.xzymon.xcrawler.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Resource {
	@ManyToOne
	private Run run;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created")
	private Date created;
	@Column(name="url", length=3000)
	private String url;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="downloaded")
	private Date downloaded;
	@Lob
	@Column(name="resource")
	private byte[] data;
	@Column(name="processed")
	private boolean processed;
	public void setRun(Run run) {
		this.run = run;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getDownloaded() {
		return downloaded;
	}
	public void setDownloaded(Date downloaded) {
		this.downloaded = downloaded;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public boolean isProcessed() {
		return processed;
	}
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
}
