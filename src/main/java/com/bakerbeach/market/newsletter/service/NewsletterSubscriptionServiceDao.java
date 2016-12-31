package com.bakerbeach.market.newsletter.service;

import com.bakerbeach.market.newsletter.api.model.NewsletterSubscription;

public interface NewsletterSubscriptionServiceDao {

	boolean update(NewsletterSubscription subscription);

	NewsletterSubscription findByEmail(String email);

}