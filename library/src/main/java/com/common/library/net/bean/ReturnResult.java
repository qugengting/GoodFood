package com.common.library.net.bean;

import java.util.List;

/**
 * Created by xuruibin on 2017/11/6.
 * 描述：
 */

public class ReturnResult {
    private boolean success;
    private String errorCode;
    private String message;

    private List<String> list;

    private List<ReturnResultItem> itemList;

    public List<ReturnResultItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ReturnResultItem> itemList) {
        this.itemList = itemList;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
