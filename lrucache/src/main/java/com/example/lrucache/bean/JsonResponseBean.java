package com.example.lrucache.bean;

import java.util.List;

/**
 * Created by wangyuhang on 2017/1/27.
 */

public class JsonResponseBean {

    /**
     * status : 1
     * data : [{"id":1,"name":"Tony老师聊shell\u2014\u2014环境变量配置文件","picSmall":"http://img.mukewang.com/55237dcc0001128c06000338-300-170.jpg","picBig":"http://img.mukewang.com/55237dcc0001128c06000338.jpg","description":"为你带来shell中的环境变量配置文件","learner":12312},{"id":2,"name":"数学知识在CSS动画中的应用","picSmall":"http://img.mukewang.com/55249cf30001ae8a06000338-300-170.jpg","picBig":"http://img.mukewang.com/55249cf30001ae8a06000338.jpg","description":"数学知识与CSS结合实现酷炫效果","learner":45625}]
     * msg : 成功
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : Tony老师聊shell——环境变量配置文件
         * picSmall : http://img.mukewang.com/55237dcc0001128c06000338-300-170.jpg
         * picBig : http://img.mukewang.com/55237dcc0001128c06000338.jpg
         * description : 为你带来shell中的环境变量配置文件
         * learner : 12312
         */

        private int id;
        private String name;
        private String picSmall;
        private String picBig;
        private String description;
        private int learner;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicSmall() {
            return picSmall;
        }

        public void setPicSmall(String picSmall) {
            this.picSmall = picSmall;
        }

        public String getPicBig() {
            return picBig;
        }

        public void setPicBig(String picBig) {
            this.picBig = picBig;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getLearner() {
            return learner;
        }

        public void setLearner(int learner) {
            this.learner = learner;
        }
    }
}
