package com.lord.local.gptjavaapi.dao.entity;

import java.util.ArrayList;
import java.util.List;

public class CallBackInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public CallBackInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
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

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
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

        public Criteria andCallBackIdIsNull() {
            addCriterion("call_back_id is null");
            return (Criteria) this;
        }

        public Criteria andCallBackIdIsNotNull() {
            addCriterion("call_back_id is not null");
            return (Criteria) this;
        }

        public Criteria andCallBackIdEqualTo(Integer value) {
            addCriterion("call_back_id =", value, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdNotEqualTo(Integer value) {
            addCriterion("call_back_id <>", value, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdGreaterThan(Integer value) {
            addCriterion("call_back_id >", value, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("call_back_id >=", value, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdLessThan(Integer value) {
            addCriterion("call_back_id <", value, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdLessThanOrEqualTo(Integer value) {
            addCriterion("call_back_id <=", value, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdIn(List<Integer> values) {
            addCriterion("call_back_id in", values, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdNotIn(List<Integer> values) {
            addCriterion("call_back_id not in", values, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdBetween(Integer value1, Integer value2) {
            addCriterion("call_back_id between", value1, value2, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackIdNotBetween(Integer value1, Integer value2) {
            addCriterion("call_back_id not between", value1, value2, "callBackId");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestIsNull() {
            addCriterion("call_back_request is null");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestIsNotNull() {
            addCriterion("call_back_request is not null");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestEqualTo(String value) {
            addCriterion("call_back_request =", value, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestNotEqualTo(String value) {
            addCriterion("call_back_request <>", value, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestGreaterThan(String value) {
            addCriterion("call_back_request >", value, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestGreaterThanOrEqualTo(String value) {
            addCriterion("call_back_request >=", value, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestLessThan(String value) {
            addCriterion("call_back_request <", value, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestLessThanOrEqualTo(String value) {
            addCriterion("call_back_request <=", value, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestLike(String value) {
            addCriterion("call_back_request like", value, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestNotLike(String value) {
            addCriterion("call_back_request not like", value, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestIn(List<String> values) {
            addCriterion("call_back_request in", values, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestNotIn(List<String> values) {
            addCriterion("call_back_request not in", values, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestBetween(String value1, String value2) {
            addCriterion("call_back_request between", value1, value2, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackRequestNotBetween(String value1, String value2) {
            addCriterion("call_back_request not between", value1, value2, "callBackRequest");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseIsNull() {
            addCriterion("call_back_response is null");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseIsNotNull() {
            addCriterion("call_back_response is not null");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseEqualTo(String value) {
            addCriterion("call_back_response =", value, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseNotEqualTo(String value) {
            addCriterion("call_back_response <>", value, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseGreaterThan(String value) {
            addCriterion("call_back_response >", value, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseGreaterThanOrEqualTo(String value) {
            addCriterion("call_back_response >=", value, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseLessThan(String value) {
            addCriterion("call_back_response <", value, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseLessThanOrEqualTo(String value) {
            addCriterion("call_back_response <=", value, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseLike(String value) {
            addCriterion("call_back_response like", value, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseNotLike(String value) {
            addCriterion("call_back_response not like", value, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseIn(List<String> values) {
            addCriterion("call_back_response in", values, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseNotIn(List<String> values) {
            addCriterion("call_back_response not in", values, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseBetween(String value1, String value2) {
            addCriterion("call_back_response between", value1, value2, "callBackResponse");
            return (Criteria) this;
        }

        public Criteria andCallBackResponseNotBetween(String value1, String value2) {
            addCriterion("call_back_response not between", value1, value2, "callBackResponse");
            return (Criteria) this;
        }
    }

    /**
     */
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