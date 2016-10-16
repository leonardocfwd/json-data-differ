package com.waesst.jsondiffer.persistence;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.waesst.jsondiffer.model.Diff;

/**
 * The DAO class to persist Diff and its byte arrays. I found it would be more useful to have my own
 * persistance layer to keep code cleaner and follow the MVC pattern.
 * 
 * @author Leonardo Nelson
 *
 */
@Repository
@Transactional
public class DiffDaoImpl implements DiffDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public Boolean save(Diff diff) {
		getSession().saveOrUpdate(diff);
		return true;
	}

	@Override
	public Diff load(String id) {
		return (Diff) getSession().get(Diff.class, id);
	}

}
