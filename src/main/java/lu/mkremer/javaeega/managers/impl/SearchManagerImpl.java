package lu.mkremer.javaeega.managers.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import lu.mkremer.javaeega.devices.DevicePropertyValue;
import lu.mkremer.javaeega.managers.SearchManager;

@Stateless
public class SearchManagerImpl implements SearchManager{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<DevicePropertyValue> searchForProperties(String searchQuery) {
		String parts[] = searchQuery.split(" ");
		String baseQuery = "select v from DevicePropertyValue v where v.property.tags like :pq0 or v.value like :vq0";
		for(int i = 1 ; i < parts.length ; i++) {
			baseQuery += " or v.property.tags like :pq" + i + " or v.value like :vq" + i;
		}
		TypedQuery<DevicePropertyValue> query = em.createQuery(baseQuery, DevicePropertyValue.class);
		for(int i = 0 ; i < parts.length ; i++) {
			query.setParameter("pq" + i, "%" + parts[i] + "%");
			query.setParameter("vq" + i, "%" + parts[i] + "%");
		}
		return query.getResultList();
	}

}
