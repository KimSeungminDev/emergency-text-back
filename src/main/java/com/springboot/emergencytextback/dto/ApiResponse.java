package com.springboot.emergencytextback.dto;

import com.springboot.emergencytextback.entity.Emergency;
import java.util.List;

public class ApiResponse {
    private Header header;
    private List<Emergency> body;

    // Getters and Setters
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Emergency> getBody() {
        return body;
    }

    public void setBody(List<Emergency> body) {
        this.body = body;
    }

    // Inner class for Header
    public static class Header {
        private String resultMsg;
        private String resultCode;
        private String errorMsg;
        private int numOfRows;
        private int pageNo;
        private int totalCount;

        // Getters and Setters
        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public int getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(int numOfRows) {
            this.numOfRows = numOfRows;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }
}
