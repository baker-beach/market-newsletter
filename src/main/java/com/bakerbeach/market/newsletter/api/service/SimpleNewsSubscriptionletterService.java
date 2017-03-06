package com.bakerbeach.market.newsletter.api.service;

import java.util.Date;

import com.bakerbeach.market.newsletter.api.model.NewsletterSubscription;
import com.bakerbeach.market.newsletter.api.service.NewsletterServiceException.AlreadySubscribedException;

public class SimpleNewsSubscriptionletterService implements NewsletterSubscriptionService {
	private NewsletterSubscriptionServiceDao newsletterDao;
	
	@Override
	public void subscribe(String prefix, String firstName, String lastName, String email, String newsletterCode, String status) throws NewsletterServiceException {
		NewsletterSubscription subscription = new NewsletterSubscription();
		subscription.setPrefix(prefix);
		subscription.setFirstName(firstName);
		subscription.setLastName(lastName);
		subscription.setEmail(email);
		subscription.setNewsletterCode(newsletterCode);
		subscription.setStatus(status);
		subscription.setLastUpdate(new Date());
		
		// TOOD: check if already exists
		// TODO: show global error message 
		boolean alreadySubscribed = newsletterDao.update(subscription);
		if (alreadySubscribed) {
			throw new AlreadySubscribedException();
		}
	}
	
	@Override
	public NewsletterSubscription getSubscription(String email) throws NewsletterServiceException {
		NewsletterSubscription subscription = newsletterDao.findByEmail(email);
		return subscription;
	}

	public void setNewsletterDao(NewsletterSubscriptionServiceDao newsletterDao) {
		this.newsletterDao = newsletterDao;
	}

}
