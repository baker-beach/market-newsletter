package com.bakerbeach.market.newsletter.api.service;

import java.util.Date;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.bakerbeach.market.newsletter.api.model.NewsletterSubscription;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;

public class MorphiaNewsletterSubscriptionServiceDao implements NewsletterSubscriptionServiceDao {
	private static final String SUBSCRIPTION_COLLECTION_NAME = "newsletter_sbscriptions";
	
	@Autowired(required = false)
	@Qualifier("shopDatastore")
	private Datastore datastore;
	
	public boolean update(NewsletterSubscription subscription) {
		QueryBuilder q = new QueryBuilder();
		q.and("email").is(subscription.getEmail()).and("newsletter_code").is(subscription.getNewsletterCode());

		Date date = new Date();
		
		BasicDBObject o = new BasicDBObject();
		o.append("status", subscription.getStatus());
		o.append("last_update", subscription.getLastUpdate());
		if (subscription.getFirstName() != null && !subscription.getFirstName().isEmpty()) {
			o.append("first_name", subscription.getFirstName());
		}
		if (subscription.getLastName() != null && !subscription.getLastName().isEmpty()) {
			o.append("last_name", subscription.getLastName());
		}
		
		BasicDBObject u = new BasicDBObject("$set", o);
		u.append("$setOnInsert", new BasicDBObject("email", subscription.getEmail()).append("newsletter_code", subscription.getNewsletterCode()));
		u.append("$push", new BasicDBObject("booking", new BasicDBObject("status", subscription.getStatus()).append("date", date)));
		
		DBCollection collection = datastore.getDB().getCollection(SUBSCRIPTION_COLLECTION_NAME);
		WriteResult result = collection.update(q.get(), u, true, false);
		return result.isUpdateOfExisting();
	}

	@Override
	public NewsletterSubscription findByEmail(String email) {
		QueryBuilder q = new QueryBuilder();
		q.and("email").is(email);
		
		DBCollection collection = datastore.getDB().getCollection(SUBSCRIPTION_COLLECTION_NAME);
		DBObject dbo = collection.findOne(q.get());
		
		if (dbo != null) {
			return decodeNewsletterSubscription(dbo);			
		} else {
			return null;
		}
	}

	private NewsletterSubscription decodeNewsletterSubscription(DBObject dbo) {
		NewsletterSubscription subscription = new NewsletterSubscription();
		
		if (dbo.containsField("prefix")) {
			subscription.setPrefix((String) dbo.get("prefix"));
		}

		if (dbo.containsField("first_name")) {
			subscription.setFirstName((String) dbo.get("first_name"));
		}
		
		if (dbo.containsField("last_name")) {
			subscription.setLastName((String) dbo.get("last_name"));
		}
		
		subscription.setNewsletterCode((String) dbo.get("newsletter_code"));
		
		subscription.setEmail((String) dbo.get("email"));
		
		subscription.setLastUpdate((Date) dbo.get("last_update"));
		
		subscription.setStatus((String) dbo.get("status"));
		
		return subscription;
	}

}
