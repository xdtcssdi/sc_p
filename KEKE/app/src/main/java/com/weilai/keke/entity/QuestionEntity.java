package com.weilai.keke.entity;

import java.util.List;

public class QuestionEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * StuAndQuePk : 33
         * question_pk : 2
         * questionDescribe : 我国正处于并将长期处于社会主义初级阶段。这一阶段必须坚持和完善（ ）的分配制度。
         * questionOption : ["按劳分配为主体","多种分配方式并存","按需分配为主体","按劳分配与按需分配相结合"]
         * type : 2
         * limited : 60
         * detail : 2
         */

        private int StuAndQuePk;
        private int question_pk;
        private String questionDescribe;
        private int type;
        private int limited;
        private int detail;
        private List<String> questionOption;

        public int getStuAndQuePk() {
            return StuAndQuePk;
        }

        public void setStuAndQuePk(int StuAndQuePk) {
            this.StuAndQuePk = StuAndQuePk;
        }

        public int getQuestion_pk() {
            return question_pk;
        }

        public void setQuestion_pk(int question_pk) {
            this.question_pk = question_pk;
        }

        public String getQuestionDescribe() {
            return questionDescribe;
        }

        public void setQuestionDescribe(String questionDescribe) {
            this.questionDescribe = questionDescribe;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getLimited() {
            return limited;
        }

        public void setLimited(int limited) {
            this.limited = limited;
        }

        public int getDetail() {
            return detail;
        }

        public void setDetail(int detail) {
            this.detail = detail;
        }

        public List<String> getQuestionOption() {
            return questionOption;
        }

        public void setQuestionOption(List<String> questionOption) {
            this.questionOption = questionOption;
        }
    }
}
