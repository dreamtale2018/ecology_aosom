package com.weaver.ningb.direct.entity.integration;

import java.io.Serializable;

/**
 * 任务分配表信息
 * 
 * @author liberal
 *
 */
public class TaskAssignedReview implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String zrr;
	private String mxid;
	private String cpzcpplnr;
	private String xj;
	private String gb;
	private String kslx;
	private String fsrq;
	private String dysku;
	private String cpmc;
	private String sffprw;
	
	public String getZrr() {
		return zrr;
	}
	public void setZrr(String zrr) {
		this.zrr = zrr;
	}
	public String getMxid() {
		return mxid;
	}
	public void setMxid(String mxid) {
		this.mxid = mxid;
	}
	public String getCpzcpplnr() {
		return cpzcpplnr;
	}
	public void setCpzcpplnr(String cpzcpplnr) {
		this.cpzcpplnr = cpzcpplnr;
	}
	public String getXj() {
		return xj;
	}
	public void setXj(String xj) {
		this.xj = xj;
	}
	public String getGb() {
		return gb;
	}
	public void setGb(String gb) {
		this.gb = gb;
	}
	public String getKslx() {
		return kslx;
	}
	public void setKslx(String kslx) {
		this.kslx = kslx;
	}
	public String getFsrq() {
		return fsrq;
	}
	public void setFsrq(String fsrq) {
		this.fsrq = fsrq;
	}
	public String getDysku() {
		return dysku;
	}
	public void setDysku(String dysku) {
		this.dysku = dysku;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}
	public String getSffprw() {
		return sffprw;
	}
	public void setSffprw(String sffprw) {
		this.sffprw = sffprw;
	}
}
