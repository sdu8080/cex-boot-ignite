package com.cex.web;

import java.sql.Timestamp;
import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cex.model.Card;
import com.cex.model.CardKey;
import com.cex.service.CacheRepository;
import com.cex.service.IgniteService;
import com.cex.util.OvalUtil;


/**
 * Web controller class to simulate business process logics.
 * @author sdu0000
 *
 */
@RestController
@RequestMapping("/card")
public class CardController{
	
	private static Logger logger = LoggerFactory.getLogger(CardController.class);
	
	@Autowired
	CacheRepository cacheRepo = null;
	
	@Autowired
	IgniteService service = null;
	
	@RequestMapping(method = RequestMethod.GET) 
	public Card getCard(
			@RequestParam(value = "cardNo", required = true) String cardNo,
			@RequestParam(value = "cardUpc", required = true) String cardUpc) {
		CardKey key = new CardKey(cardNo, cardUpc);
		return cacheRepo.getCardCache().get(key);
	}
	
	@RequestMapping(method = RequestMethod.DELETE) 
	public Boolean deleteCard(
			@RequestParam(value = "cardNo", required = true) String cardNo,
			@RequestParam(value = "cardUpc", required = true) String cardUpc) {
		CardKey key = new CardKey(cardNo, cardUpc);
		return cacheRepo.getCardCache().remove(key);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, consumes={"application/json"}) 
	public ResponseEntity<String> addCard(@RequestBody
			 Card card) {
		
		logger.debug("card={}", card.toString());
		
		Validator validator = new Validator();
		// collect the constraint violations
		List<ConstraintViolation> violations = validator.validate(card);

		if(violations !=null && violations.size()>0){
		  logger.error("Object " + card + " is invalid.");
		  return new ResponseEntity<String>(OvalUtil.getViolations(violations), HttpStatus.BAD_REQUEST);
		}
		
		CardKey key = new CardKey(card.getCardNo(), card.getCardUpc());
		card.setCreateTime(new Timestamp(System.currentTimeMillis()));
		card.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
		cacheRepo.getCardCache().put(key, card);
		return new ResponseEntity<String>( HttpStatus.CREATED);
	}
	
	
	
}

