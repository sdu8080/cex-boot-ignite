package com.cex.util;

public class CacheSize {
	private int nearCacheSize;
	private int allCacheSize;
	private int txnCacheSize;
	private int backupCacheSize;
	private int onheapCacheSize;
	private int offheapCacheSize;
	private int swapCacheSize;

	private String nodes;

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public int getNearCacheSize() {
		return nearCacheSize;
	}

	public void setNearCacheSize(int nearCacheSize) {
		this.nearCacheSize = nearCacheSize;
	}

	public int getAllCacheSize() {
		return allCacheSize;
	}

	public void setAllCacheSize(int allCacheSize) {
		this.allCacheSize = allCacheSize;
	}

	public int getTxnCacheSize() {
		return txnCacheSize;
	}

	public void setTxnCacheSize(int txnCacheSize) {
		this.txnCacheSize = txnCacheSize;
	}

	public int getBackupCacheSize() {
		return backupCacheSize;
	}

	public void setBackupCacheSize(int backupCacheSize) {
		this.backupCacheSize = backupCacheSize;
	}

	public int getOnheapCacheSize() {
		return onheapCacheSize;
	}

	public void setOnheapCacheSize(int onheapCacheSize) {
		this.onheapCacheSize = onheapCacheSize;
	}

	public int getOffheapCacheSize() {
		return offheapCacheSize;
	}

	public void setOffheapCacheSize(int offheapCacheSize) {
		this.offheapCacheSize = offheapCacheSize;
	}

	public int getSwapCacheSize() {
		return swapCacheSize;
	}

	public void setSwapCacheSize(int swapCacheSize) {
		this.swapCacheSize = swapCacheSize;
	}

}
