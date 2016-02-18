package com.cex.web;

import java.sql.Timestamp;

import org.apache.ignite.cache.CachePeekMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cex.ignite.model.Transaction;
import com.cex.ignite.model.TransactionKey;
import com.cex.service.IgniteService;


/**
 * Web controller class to simulate business process logics.
 * @author sdu0000
 *
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController{
	
	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	IgniteService service = null;
	
	@RequestMapping(method = RequestMethod.GET) 
	public Transaction getTxn(
			@RequestParam(value = "id", required = true) String id) {
		TransactionKey key = new TransactionKey(id);
		return service.getNearCache().get(key);
	}
	
	@RequestMapping(method = RequestMethod.DELETE) 
	public Boolean deleteTxn(
			@RequestParam(value = "id", required = true) String id) {
		TransactionKey key = new TransactionKey(id);
		return service.getNearCache().remove(key);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, consumes={"application/json"}) 
	public Boolean addTxn(@RequestBody
			 Transaction txn) {
		
		logger.info("txn="+txn.toString());
		TransactionKey key = new TransactionKey(txn.getTransactionId());
		txn.setTransactionTime(new Timestamp(System.currentTimeMillis()));
		service.getNearCache().put(key, txn);
		return true;
	}
	
	@RequestMapping(path="cacheSize", method = RequestMethod.GET)
	public  CacheSize getCacheSize(){
		CacheSize cs = new CacheSize();
		cs.setNearCacheSize(service.getNearCache().size(CachePeekMode.NEAR));
		cs.setAllCacheSize(service.getTxnCache().size(CachePeekMode.ALL));
		cs.setTxnCacheSize(service.getTxnCache().size(CachePeekMode.PRIMARY));
		cs.setBackupCacheSize(service.getTxnCache().size(CachePeekMode.BACKUP));
		cs.setOnheapCacheSize(service.getTxnCache().size(CachePeekMode.ONHEAP));
		cs.setOffheapCacheSize(service.getTxnCache().size(CachePeekMode.OFFHEAP));
		cs.setSwapCacheSize(service.getTxnCache().size(CachePeekMode.SWAP));
		return cs;
	}
	
	@SuppressWarnings("unused")
	private static class CacheSize {
		
		private int nearCacheSize;
		private int allCacheSize;
		private int txnCacheSize;
		private int backupCacheSize;
		private int onheapCacheSize;
		private int offheapCacheSize;
		private int swapCacheSize;
		
		
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
	
}

