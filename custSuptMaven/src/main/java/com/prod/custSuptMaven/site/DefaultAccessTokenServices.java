package com.prod.custSuptMaven.site;
/* class notes: chap 28, pg x.  
 * 
 */
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.prod.custSuptMaven.site.entities.SigningAccessToken;
import com.prod.custSuptMaven.site.repositories.SigningAccessTokenRepository;

public class DefaultAccessTokenServices implements SigningAccessTokenRepository {

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteInBatch(Iterable<SigningAccessToken> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SigningAccessToken> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SigningAccessToken> findAll(Sort arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SigningAccessToken> findAll(Iterable<Long> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SigningAccessToken getOne(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends SigningAccessToken> List<S> save(Iterable<S> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SigningAccessToken saveAndFlush(SigningAccessToken arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<SigningAccessToken> findAll(Pageable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(SigningAccessToken arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends SigningAccessToken> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean exists(Long arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SigningAccessToken findOne(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends SigningAccessToken> S save(S arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SigningAccessToken getByKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SigningAccessToken getByValue(String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
