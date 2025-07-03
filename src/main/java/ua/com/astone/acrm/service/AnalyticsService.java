package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.analytics.ProbabilityCountDto;
import ua.com.astone.acrm.dto.analytics.StageValueDto;
import ua.com.astone.acrm.dto.analytics.StatusCountDto;

import java.util.List;

public interface AnalyticsService {
    List<StatusCountDto> countLeadsByStatus();
    List<ProbabilityCountDto> countOpportunitiesByProbability();
    List<StageValueDto> sumExpectedValueByStage();
}

