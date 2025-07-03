package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.astone.acrm.dto.analytics.ProbabilityCountDto;
import ua.com.astone.acrm.dto.analytics.StageValueDto;
import ua.com.astone.acrm.dto.analytics.StatusCountDto;
import ua.com.astone.acrm.repository.LeadRepository;
import ua.com.astone.acrm.repository.OpportunityRepository;
import ua.com.astone.acrm.service.AnalyticsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final LeadRepository leadRepository;
    private final OpportunityRepository opportunityRepository;

    @Override
    public List<StatusCountDto> countLeadsByStatus() {
        var result = leadRepository.countByStatus();
        System.out.println("leadsByStatus: " + result);
        return result;
    }

    @Override
    public List<ProbabilityCountDto> countOpportunitiesByProbability() {
        return opportunityRepository.countByProbability();
    }
    @Override
    public List<StageValueDto> sumExpectedValueByStage() {
        return opportunityRepository.sumExpectedValueByStage();
    }
}

