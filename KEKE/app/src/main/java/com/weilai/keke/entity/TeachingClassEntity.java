package com.weilai.keke.entity;

import java.util.List;

public class TeachingClassEntity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * model : api.teachingclass
         * pk : 1
         * fields : {"week":"Monday","classStartTime":"08:46:00","classEndTime":"08:46:00","classSignTime":45,"className":"数学","teacherId":1,"studentId":[2,3]}
         */

        private String model;
        private int pk;
        private FieldsBean fields;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getPk() {
            return pk;
        }

        public void setPk(int pk) {
            this.pk = pk;
        }

        public FieldsBean getFields() {
            return fields;
        }

        public void setFields(FieldsBean fields) {
            this.fields = fields;
        }

        public static class FieldsBean {
            /**
             * week : Monday
             * classStartTime : 08:46:00
             * classEndTime : 08:46:00
             * classSignTime : 45
             * className : 数学
             * teacherId : 1
             * studentId : [2,3]
             */

            private String week;
            private String classStartTime;
            private String classEndTime;
            private int classSignTime;
            private String className;
            private String teacherId;
            private List<Integer> studentId;

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getClassStartTime() {
                return classStartTime;
            }

            public void setClassStartTime(String classStartTime) {
                this.classStartTime = classStartTime;
            }

            public String getClassEndTime() {
                return classEndTime;
            }

            public void setClassEndTime(String classEndTime) {
                this.classEndTime = classEndTime;
            }

            public int getClassSignTime() {
                return classSignTime;
            }

            public void setClassSignTime(int classSignTime) {
                this.classSignTime = classSignTime;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getTeacherId() {
                return teacherId;
            }

            public void setTeacherId(String teacherId) {
                this.teacherId = teacherId;
            }

            public List<Integer> getStudentId() {
                return studentId;
            }

            public void setStudentId(List<Integer> studentId) {
                this.studentId = studentId;
            }
        }
    }
}
