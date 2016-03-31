package com.cex.web;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.cache.Cache.Entry;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cex.model.Card;
import com.cex.model.CardTransaction;
import com.cex.model.CardTransactionKey;
import com.cex.service.CacheRepository;
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
	CacheRepository cacheRepo = null;
	
	@Autowired
	IgniteService service = null;
	
	@RequestMapping(method = RequestMethod.GET) 
	public CardTransaction getTxn(
			@RequestParam(value = "id", required = true) String id) {
		CardTransactionKey key = new CardTransactionKey(id);
		return cacheRepo.getCardTransactionCache().get(key);
	}
	
	@RequestMapping(method = RequestMethod.DELETE) 
	public Boolean deleteTxn(
			@RequestParam(value = "id", required = true) String id) {
		CardTransactionKey key = new CardTransactionKey(id);
		return cacheRepo.getCardTransactionCache().remove(key);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, consumes={"application/json"}) 
	public Boolean addTxn(@RequestBody
			 CardTransaction txn) {
		
		logger.info("txn="+txn.toString());
		
		String cardN = txn.getCardNo();
		IgniteCache<BinaryObject, BinaryObject> cache = cacheRepo.getCardCache().withKeepBinary();
		
		
		

        ScanQuery<BinaryObject, BinaryObject> scan = new ScanQuery<>(
            new IgniteBiPredicate<BinaryObject, BinaryObject>() {
                @Override public boolean apply(BinaryObject key, BinaryObject card) {
                	String cn = card.<String>field("cardNo");
                    return cardN.equalsIgnoreCase(cn);
                }
            }
        );

		
		try (QueryCursor<Entry<BinaryObject, BinaryObject>> cursor = cache.query(scan)) {
			  for (Entry<BinaryObject, BinaryObject> c : cursor){
				  if(c!=null){
					  String cardId = c.getValue().field("cardId");
					  txn.setCardId(cardId);
				  }
			  }
		}
		
		CardTransactionKey key = new CardTransactionKey(txn.getTransactionId());
		txn.setTransactionTime(new Timestamp(System.currentTimeMillis()));
		cacheRepo.getCardTransactionCache().put(key, txn);
		return true;
	}
	
	@RequestMapping(path="cacheInfo", method = RequestMethod.GET)
	public  CacheSize getCacheSize(){
		CacheSize cs = new CacheSize();
		cs.setAllCacheSize(cacheRepo.getCardTransactionCache().size(CachePeekMode.ALL));
		cs.setTxnCacheSize(cacheRepo.getCardTransactionCache().size(CachePeekMode.PRIMARY));
		cs.setBackupCacheSize(cacheRepo.getCardTransactionCache().size(CachePeekMode.BACKUP));
		cs.setOnheapCacheSize(cacheRepo.getCardTransactionCache().size(CachePeekMode.ONHEAP));
		cs.setOffheapCacheSize(cacheRepo.getCardTransactionCache().size(CachePeekMode.OFFHEAP));
		cs.setSwapCacheSize(cacheRepo.getCardTransactionCache().size(CachePeekMode.SWAP));
		Collection<ClusterNode> c = service.getIgnite().cluster().nodes();
		StringBuffer sb = new StringBuffer();
		for (Iterator<ClusterNode> iterator = c.iterator(); iterator.hasNext();) {
			ClusterNode sd = (ClusterNode) iterator.next();
			if(sd.isClient()){
				sb.append(sd.id()+":isClient");
			}else{
				sb.append(sd.id()+":isServer");
			}
			sb.append("\n");
			
	    }
		cs.setNodes(sb.toString());
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
	
}

