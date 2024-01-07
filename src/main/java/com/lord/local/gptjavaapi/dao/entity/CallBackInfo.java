package com.lord.local.gptjavaapi.dao.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * call_back_info
 *
 * @author
 */
@Data
public class CallBackInfo implements Serializable {
    private Integer callBackId;

    private String callBackRequest;

    private String callBackResponse;

    private String callBackErrors;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CallBackInfo other = (CallBackInfo) that;
        return (this.getCallBackId() == null ? other.getCallBackId() == null : this.getCallBackId().equals(other.getCallBackId()))
                && (this.getCallBackRequest() == null ? other.getCallBackRequest() == null : this.getCallBackRequest().equals(other.getCallBackRequest()))
                && (this.getCallBackResponse() == null ? other.getCallBackResponse() == null : this.getCallBackResponse().equals(other.getCallBackResponse()))
                && (this.getCallBackErrors() == null ? other.getCallBackErrors() == null : this.getCallBackErrors().equals(other.getCallBackErrors()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCallBackId() == null) ? 0 : getCallBackId().hashCode());
        result = prime * result + ((getCallBackRequest() == null) ? 0 : getCallBackRequest().hashCode());
        result = prime * result + ((getCallBackResponse() == null) ? 0 : getCallBackResponse().hashCode());
        result = prime * result + ((getCallBackErrors() == null) ? 0 : getCallBackErrors().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", callBackId=").append(callBackId);
        sb.append(", callBackRequest=").append(callBackRequest);
        sb.append(", callBackResponse=").append(callBackResponse);
        sb.append(", callBackErrors=").append(callBackErrors);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}