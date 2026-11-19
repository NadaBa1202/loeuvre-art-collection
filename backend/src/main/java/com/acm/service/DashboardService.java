package com.acm.service;

import com.acm.dto.DashboardResponse;
import com.acm.dto.DashboardResponse.RecentOeuvre;
import com.acm.dto.DashboardResponse.TechniqueCount;
import com.acm.repository.ArtisteRepository;
import com.acm.repository.OeuvreRepository;
import com.acm.repository.UserRepository;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
	private final ArtisteRepository artistes;
    	private final OeuvreRepository oeuvres;
	private final UserRepository users;

	public DashboardService(ArtisteRepository artistes, OeuvreRepository oeuvres, UserRepository users) {
	    this.artistes = artistes;
	    this.oeuvres = oeuvres;
	    this.users = users;
	}

	@Transactional(readOnly = true)
	public DashboardResponse get(boolean admin) {
	    List<TechniqueCount> byTechnique = oeuvres.countByTechnique().stream().map(s -> new TechniqueCount(
                        s.getTechnique() == null || s.getTechnique().isBlank() ? "Non renseignée" : s.getTechnique(),
                        s.getTotal()))
                .toList();;
	    List<RecentOeuvre> recentes = oeuvres.findTop3ByOrderByIdDesc().stream()
	            .map(o -> new RecentOeuvre(o.getId(), o.getTitre(), o.getArtiste().getNom(),
	                    o.getTechnique(), o.getAnneeCreation(), o.getImageUrl()))
	            .toList();
	    return new DashboardResponse(
	    	    artistes.count(),
	    	    oeuvres.count(),
	    	    admin ? Long.valueOf(users.count()) : null,
	    	    byTechnique,
	    	    recentes
	    	);
	}
}