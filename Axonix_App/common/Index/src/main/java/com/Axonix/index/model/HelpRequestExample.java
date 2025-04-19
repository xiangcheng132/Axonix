package com.Axonix.index.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelpRequestExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public HelpRequestExample() {
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

        public Criteria andRequesterIdIsNull() {
            addCriterion("requester_id is null");
            return (Criteria) this;
        }

        public Criteria andRequesterIdIsNotNull() {
            addCriterion("requester_id is not null");
            return (Criteria) this;
        }

        public Criteria andRequesterIdEqualTo(Integer value) {
            addCriterion("requester_id =", value, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdNotEqualTo(Integer value) {
            addCriterion("requester_id <>", value, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdGreaterThan(Integer value) {
            addCriterion("requester_id >", value, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("requester_id >=", value, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdLessThan(Integer value) {
            addCriterion("requester_id <", value, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdLessThanOrEqualTo(Integer value) {
            addCriterion("requester_id <=", value, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdIn(List<Integer> values) {
            addCriterion("requester_id in", values, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdNotIn(List<Integer> values) {
            addCriterion("requester_id not in", values, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdBetween(Integer value1, Integer value2) {
            addCriterion("requester_id between", value1, value2, "requesterId");
            return (Criteria) this;
        }

        public Criteria andRequesterIdNotBetween(Integer value1, Integer value2) {
            addCriterion("requester_id not between", value1, value2, "requesterId");
            return (Criteria) this;
        }

        public Criteria andHelperIdIsNull() {
            addCriterion("helper_id is null");
            return (Criteria) this;
        }

        public Criteria andHelperIdIsNotNull() {
            addCriterion("helper_id is not null");
            return (Criteria) this;
        }

        public Criteria andHelperIdEqualTo(Integer value) {
            addCriterion("helper_id =", value, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdNotEqualTo(Integer value) {
            addCriterion("helper_id <>", value, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdGreaterThan(Integer value) {
            addCriterion("helper_id >", value, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("helper_id >=", value, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdLessThan(Integer value) {
            addCriterion("helper_id <", value, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdLessThanOrEqualTo(Integer value) {
            addCriterion("helper_id <=", value, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdIn(List<Integer> values) {
            addCriterion("helper_id in", values, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdNotIn(List<Integer> values) {
            addCriterion("helper_id not in", values, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdBetween(Integer value1, Integer value2) {
            addCriterion("helper_id between", value1, value2, "helperId");
            return (Criteria) this;
        }

        public Criteria andHelperIdNotBetween(Integer value1, Integer value2) {
            addCriterion("helper_id not between", value1, value2, "helperId");
            return (Criteria) this;
        }

        public Criteria andRequesterLngIsNull() {
            addCriterion("requester_lng is null");
            return (Criteria) this;
        }

        public Criteria andRequesterLngIsNotNull() {
            addCriterion("requester_lng is not null");
            return (Criteria) this;
        }

        public Criteria andRequesterLngEqualTo(BigDecimal value) {
            addCriterion("requester_lng =", value, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngNotEqualTo(BigDecimal value) {
            addCriterion("requester_lng <>", value, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngGreaterThan(BigDecimal value) {
            addCriterion("requester_lng >", value, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("requester_lng >=", value, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngLessThan(BigDecimal value) {
            addCriterion("requester_lng <", value, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngLessThanOrEqualTo(BigDecimal value) {
            addCriterion("requester_lng <=", value, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngIn(List<BigDecimal> values) {
            addCriterion("requester_lng in", values, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngNotIn(List<BigDecimal> values) {
            addCriterion("requester_lng not in", values, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("requester_lng between", value1, value2, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLngNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("requester_lng not between", value1, value2, "requesterLng");
            return (Criteria) this;
        }

        public Criteria andRequesterLatIsNull() {
            addCriterion("requester_lat is null");
            return (Criteria) this;
        }

        public Criteria andRequesterLatIsNotNull() {
            addCriterion("requester_lat is not null");
            return (Criteria) this;
        }

        public Criteria andRequesterLatEqualTo(BigDecimal value) {
            addCriterion("requester_lat =", value, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatNotEqualTo(BigDecimal value) {
            addCriterion("requester_lat <>", value, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatGreaterThan(BigDecimal value) {
            addCriterion("requester_lat >", value, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("requester_lat >=", value, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatLessThan(BigDecimal value) {
            addCriterion("requester_lat <", value, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatLessThanOrEqualTo(BigDecimal value) {
            addCriterion("requester_lat <=", value, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatIn(List<BigDecimal> values) {
            addCriterion("requester_lat in", values, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatNotIn(List<BigDecimal> values) {
            addCriterion("requester_lat not in", values, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("requester_lat between", value1, value2, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andRequesterLatNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("requester_lat not between", value1, value2, "requesterLat");
            return (Criteria) this;
        }

        public Criteria andHelperLngIsNull() {
            addCriterion("helper_lng is null");
            return (Criteria) this;
        }

        public Criteria andHelperLngIsNotNull() {
            addCriterion("helper_lng is not null");
            return (Criteria) this;
        }

        public Criteria andHelperLngEqualTo(BigDecimal value) {
            addCriterion("helper_lng =", value, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngNotEqualTo(BigDecimal value) {
            addCriterion("helper_lng <>", value, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngGreaterThan(BigDecimal value) {
            addCriterion("helper_lng >", value, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("helper_lng >=", value, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngLessThan(BigDecimal value) {
            addCriterion("helper_lng <", value, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngLessThanOrEqualTo(BigDecimal value) {
            addCriterion("helper_lng <=", value, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngIn(List<BigDecimal> values) {
            addCriterion("helper_lng in", values, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngNotIn(List<BigDecimal> values) {
            addCriterion("helper_lng not in", values, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("helper_lng between", value1, value2, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLngNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("helper_lng not between", value1, value2, "helperLng");
            return (Criteria) this;
        }

        public Criteria andHelperLatIsNull() {
            addCriterion("helper_lat is null");
            return (Criteria) this;
        }

        public Criteria andHelperLatIsNotNull() {
            addCriterion("helper_lat is not null");
            return (Criteria) this;
        }

        public Criteria andHelperLatEqualTo(BigDecimal value) {
            addCriterion("helper_lat =", value, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatNotEqualTo(BigDecimal value) {
            addCriterion("helper_lat <>", value, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatGreaterThan(BigDecimal value) {
            addCriterion("helper_lat >", value, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("helper_lat >=", value, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatLessThan(BigDecimal value) {
            addCriterion("helper_lat <", value, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatLessThanOrEqualTo(BigDecimal value) {
            addCriterion("helper_lat <=", value, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatIn(List<BigDecimal> values) {
            addCriterion("helper_lat in", values, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatNotIn(List<BigDecimal> values) {
            addCriterion("helper_lat not in", values, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("helper_lat between", value1, value2, "helperLat");
            return (Criteria) this;
        }

        public Criteria andHelperLatNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("helper_lat not between", value1, value2, "helperLat");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(Date value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(Date value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(Date value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(Date value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(Date value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<Date> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<Date> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(Date value1, Date value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(Date value1, Date value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNull() {
            addCriterion("updated_at is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNotNull() {
            addCriterion("updated_at is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtEqualTo(Date value) {
            addCriterion("updated_at =", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotEqualTo(Date value) {
            addCriterion("updated_at <>", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThan(Date value) {
            addCriterion("updated_at >", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("updated_at >=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThan(Date value) {
            addCriterion("updated_at <", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThanOrEqualTo(Date value) {
            addCriterion("updated_at <=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIn(List<Date> values) {
            addCriterion("updated_at in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotIn(List<Date> values) {
            addCriterion("updated_at not in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtBetween(Date value1, Date value2) {
            addCriterion("updated_at between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotBetween(Date value1, Date value2) {
            addCriterion("updated_at not between", value1, value2, "updatedAt");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

}