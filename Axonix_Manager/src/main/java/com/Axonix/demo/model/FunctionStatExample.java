package com.Axonix.demo.model;

import java.util.ArrayList;
import java.util.List;

public class FunctionStatExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FunctionStatExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andHelpRequestIsNull() {
            addCriterion("help_request is null");
            return (Criteria) this;
        }

        public Criteria andHelpRequestIsNotNull() {
            addCriterion("help_request is not null");
            return (Criteria) this;
        }

        public Criteria andHelpRequestEqualTo(Integer value) {
            addCriterion("help_request =", value, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestNotEqualTo(Integer value) {
            addCriterion("help_request <>", value, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestGreaterThan(Integer value) {
            addCriterion("help_request >", value, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestGreaterThanOrEqualTo(Integer value) {
            addCriterion("help_request >=", value, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestLessThan(Integer value) {
            addCriterion("help_request <", value, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestLessThanOrEqualTo(Integer value) {
            addCriterion("help_request <=", value, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestIn(List<Integer> values) {
            addCriterion("help_request in", values, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestNotIn(List<Integer> values) {
            addCriterion("help_request not in", values, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestBetween(Integer value1, Integer value2) {
            addCriterion("help_request between", value1, value2, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpRequestNotBetween(Integer value1, Integer value2) {
            addCriterion("help_request not between", value1, value2, "helpRequest");
            return (Criteria) this;
        }

        public Criteria andHelpMatchIsNull() {
            addCriterion("help_match is null");
            return (Criteria) this;
        }

        public Criteria andHelpMatchIsNotNull() {
            addCriterion("help_match is not null");
            return (Criteria) this;
        }

        public Criteria andHelpMatchEqualTo(Integer value) {
            addCriterion("help_match =", value, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchNotEqualTo(Integer value) {
            addCriterion("help_match <>", value, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchGreaterThan(Integer value) {
            addCriterion("help_match >", value, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchGreaterThanOrEqualTo(Integer value) {
            addCriterion("help_match >=", value, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchLessThan(Integer value) {
            addCriterion("help_match <", value, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchLessThanOrEqualTo(Integer value) {
            addCriterion("help_match <=", value, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchIn(List<Integer> values) {
            addCriterion("help_match in", values, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchNotIn(List<Integer> values) {
            addCriterion("help_match not in", values, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchBetween(Integer value1, Integer value2) {
            addCriterion("help_match between", value1, value2, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpMatchNotBetween(Integer value1, Integer value2) {
            addCriterion("help_match not between", value1, value2, "helpMatch");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessIsNull() {
            addCriterion("help_request_success is null");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessIsNotNull() {
            addCriterion("help_request_success is not null");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessEqualTo(Integer value) {
            addCriterion("help_request_success =", value, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessNotEqualTo(Integer value) {
            addCriterion("help_request_success <>", value, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessGreaterThan(Integer value) {
            addCriterion("help_request_success >", value, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessGreaterThanOrEqualTo(Integer value) {
            addCriterion("help_request_success >=", value, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessLessThan(Integer value) {
            addCriterion("help_request_success <", value, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessLessThanOrEqualTo(Integer value) {
            addCriterion("help_request_success <=", value, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessIn(List<Integer> values) {
            addCriterion("help_request_success in", values, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessNotIn(List<Integer> values) {
            addCriterion("help_request_success not in", values, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessBetween(Integer value1, Integer value2) {
            addCriterion("help_request_success between", value1, value2, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpRequestSuccessNotBetween(Integer value1, Integer value2) {
            addCriterion("help_request_success not between", value1, value2, "helpRequestSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessIsNull() {
            addCriterion("help_match_success is null");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessIsNotNull() {
            addCriterion("help_match_success is not null");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessEqualTo(Integer value) {
            addCriterion("help_match_success =", value, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessNotEqualTo(Integer value) {
            addCriterion("help_match_success <>", value, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessGreaterThan(Integer value) {
            addCriterion("help_match_success >", value, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessGreaterThanOrEqualTo(Integer value) {
            addCriterion("help_match_success >=", value, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessLessThan(Integer value) {
            addCriterion("help_match_success <", value, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessLessThanOrEqualTo(Integer value) {
            addCriterion("help_match_success <=", value, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessIn(List<Integer> values) {
            addCriterion("help_match_success in", values, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessNotIn(List<Integer> values) {
            addCriterion("help_match_success not in", values, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessBetween(Integer value1, Integer value2) {
            addCriterion("help_match_success between", value1, value2, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andHelpMatchSuccessNotBetween(Integer value1, Integer value2) {
            addCriterion("help_match_success not between", value1, value2, "helpMatchSuccess");
            return (Criteria) this;
        }

        public Criteria andNavigationIsNull() {
            addCriterion("navigation is null");
            return (Criteria) this;
        }

        public Criteria andNavigationIsNotNull() {
            addCriterion("navigation is not null");
            return (Criteria) this;
        }

        public Criteria andNavigationEqualTo(Integer value) {
            addCriterion("navigation =", value, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationNotEqualTo(Integer value) {
            addCriterion("navigation <>", value, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationGreaterThan(Integer value) {
            addCriterion("navigation >", value, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationGreaterThanOrEqualTo(Integer value) {
            addCriterion("navigation >=", value, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationLessThan(Integer value) {
            addCriterion("navigation <", value, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationLessThanOrEqualTo(Integer value) {
            addCriterion("navigation <=", value, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationIn(List<Integer> values) {
            addCriterion("navigation in", values, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationNotIn(List<Integer> values) {
            addCriterion("navigation not in", values, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationBetween(Integer value1, Integer value2) {
            addCriterion("navigation between", value1, value2, "navigation");
            return (Criteria) this;
        }

        public Criteria andNavigationNotBetween(Integer value1, Integer value2) {
            addCriterion("navigation not between", value1, value2, "navigation");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionIsNull() {
            addCriterion("traffic_recognition is null");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionIsNotNull() {
            addCriterion("traffic_recognition is not null");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionEqualTo(Integer value) {
            addCriterion("traffic_recognition =", value, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionNotEqualTo(Integer value) {
            addCriterion("traffic_recognition <>", value, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionGreaterThan(Integer value) {
            addCriterion("traffic_recognition >", value, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionGreaterThanOrEqualTo(Integer value) {
            addCriterion("traffic_recognition >=", value, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionLessThan(Integer value) {
            addCriterion("traffic_recognition <", value, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionLessThanOrEqualTo(Integer value) {
            addCriterion("traffic_recognition <=", value, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionIn(List<Integer> values) {
            addCriterion("traffic_recognition in", values, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionNotIn(List<Integer> values) {
            addCriterion("traffic_recognition not in", values, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionBetween(Integer value1, Integer value2) {
            addCriterion("traffic_recognition between", value1, value2, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andTrafficRecognitionNotBetween(Integer value1, Integer value2) {
            addCriterion("traffic_recognition not between", value1, value2, "trafficRecognition");
            return (Criteria) this;
        }

        public Criteria andAiAssistantIsNull() {
            addCriterion("ai_assistant is null");
            return (Criteria) this;
        }

        public Criteria andAiAssistantIsNotNull() {
            addCriterion("ai_assistant is not null");
            return (Criteria) this;
        }

        public Criteria andAiAssistantEqualTo(Integer value) {
            addCriterion("ai_assistant =", value, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantNotEqualTo(Integer value) {
            addCriterion("ai_assistant <>", value, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantGreaterThan(Integer value) {
            addCriterion("ai_assistant >", value, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantGreaterThanOrEqualTo(Integer value) {
            addCriterion("ai_assistant >=", value, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantLessThan(Integer value) {
            addCriterion("ai_assistant <", value, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantLessThanOrEqualTo(Integer value) {
            addCriterion("ai_assistant <=", value, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantIn(List<Integer> values) {
            addCriterion("ai_assistant in", values, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantNotIn(List<Integer> values) {
            addCriterion("ai_assistant not in", values, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantBetween(Integer value1, Integer value2) {
            addCriterion("ai_assistant between", value1, value2, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andAiAssistantNotBetween(Integer value1, Integer value2) {
            addCriterion("ai_assistant not between", value1, value2, "aiAssistant");
            return (Criteria) this;
        }

        public Criteria andPostPublishIsNull() {
            addCriterion("post_publish is null");
            return (Criteria) this;
        }

        public Criteria andPostPublishIsNotNull() {
            addCriterion("post_publish is not null");
            return (Criteria) this;
        }

        public Criteria andPostPublishEqualTo(Integer value) {
            addCriterion("post_publish =", value, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishNotEqualTo(Integer value) {
            addCriterion("post_publish <>", value, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishGreaterThan(Integer value) {
            addCriterion("post_publish >", value, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishGreaterThanOrEqualTo(Integer value) {
            addCriterion("post_publish >=", value, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishLessThan(Integer value) {
            addCriterion("post_publish <", value, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishLessThanOrEqualTo(Integer value) {
            addCriterion("post_publish <=", value, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishIn(List<Integer> values) {
            addCriterion("post_publish in", values, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishNotIn(List<Integer> values) {
            addCriterion("post_publish not in", values, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishBetween(Integer value1, Integer value2) {
            addCriterion("post_publish between", value1, value2, "postPublish");
            return (Criteria) this;
        }

        public Criteria andPostPublishNotBetween(Integer value1, Integer value2) {
            addCriterion("post_publish not between", value1, value2, "postPublish");
            return (Criteria) this;
        }

        public Criteria andCommentIsNull() {
            addCriterion("comment is null");
            return (Criteria) this;
        }

        public Criteria andCommentIsNotNull() {
            addCriterion("comment is not null");
            return (Criteria) this;
        }

        public Criteria andCommentEqualTo(Integer value) {
            addCriterion("comment =", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotEqualTo(Integer value) {
            addCriterion("comment <>", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThan(Integer value) {
            addCriterion("comment >", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThanOrEqualTo(Integer value) {
            addCriterion("comment >=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThan(Integer value) {
            addCriterion("comment <", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThanOrEqualTo(Integer value) {
            addCriterion("comment <=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentIn(List<Integer> values) {
            addCriterion("comment in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotIn(List<Integer> values) {
            addCriterion("comment not in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentBetween(Integer value1, Integer value2) {
            addCriterion("comment between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotBetween(Integer value1, Integer value2) {
            addCriterion("comment not between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andFeedbackIsNull() {
            addCriterion("feedback is null");
            return (Criteria) this;
        }

        public Criteria andFeedbackIsNotNull() {
            addCriterion("feedback is not null");
            return (Criteria) this;
        }

        public Criteria andFeedbackEqualTo(Integer value) {
            addCriterion("feedback =", value, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackNotEqualTo(Integer value) {
            addCriterion("feedback <>", value, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackGreaterThan(Integer value) {
            addCriterion("feedback >", value, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackGreaterThanOrEqualTo(Integer value) {
            addCriterion("feedback >=", value, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackLessThan(Integer value) {
            addCriterion("feedback <", value, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackLessThanOrEqualTo(Integer value) {
            addCriterion("feedback <=", value, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackIn(List<Integer> values) {
            addCriterion("feedback in", values, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackNotIn(List<Integer> values) {
            addCriterion("feedback not in", values, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackBetween(Integer value1, Integer value2) {
            addCriterion("feedback between", value1, value2, "feedback");
            return (Criteria) this;
        }

        public Criteria andFeedbackNotBetween(Integer value1, Integer value2) {
            addCriterion("feedback not between", value1, value2, "feedback");
            return (Criteria) this;
        }

        public Criteria andSosIsNull() {
            addCriterion("sos is null");
            return (Criteria) this;
        }

        public Criteria andSosIsNotNull() {
            addCriterion("sos is not null");
            return (Criteria) this;
        }

        public Criteria andSosEqualTo(Integer value) {
            addCriterion("sos =", value, "sos");
            return (Criteria) this;
        }

        public Criteria andSosNotEqualTo(Integer value) {
            addCriterion("sos <>", value, "sos");
            return (Criteria) this;
        }

        public Criteria andSosGreaterThan(Integer value) {
            addCriterion("sos >", value, "sos");
            return (Criteria) this;
        }

        public Criteria andSosGreaterThanOrEqualTo(Integer value) {
            addCriterion("sos >=", value, "sos");
            return (Criteria) this;
        }

        public Criteria andSosLessThan(Integer value) {
            addCriterion("sos <", value, "sos");
            return (Criteria) this;
        }

        public Criteria andSosLessThanOrEqualTo(Integer value) {
            addCriterion("sos <=", value, "sos");
            return (Criteria) this;
        }

        public Criteria andSosIn(List<Integer> values) {
            addCriterion("sos in", values, "sos");
            return (Criteria) this;
        }

        public Criteria andSosNotIn(List<Integer> values) {
            addCriterion("sos not in", values, "sos");
            return (Criteria) this;
        }

        public Criteria andSosBetween(Integer value1, Integer value2) {
            addCriterion("sos between", value1, value2, "sos");
            return (Criteria) this;
        }

        public Criteria andSosNotBetween(Integer value1, Integer value2) {
            addCriterion("sos not between", value1, value2, "sos");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}