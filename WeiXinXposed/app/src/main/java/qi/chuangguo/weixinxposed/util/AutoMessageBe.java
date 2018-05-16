package qi.chuangguo.weixinxposed.util;

/**
 * Created by chuangguo.qi on 2018/5/14.
 */

public class AutoMessageBe {


    private int reqType;
    private PerceptionBean perception;
    private UserInfoBean userInfo;

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public PerceptionBean getPerception() {
        return perception;
    }

    public void setPerception(PerceptionBean perception) {
        this.perception = perception;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class PerceptionBean {
        /**
         * inputText : {"text":"附近的酒店"}
         * inputImage : {"url":"imageUrl"}
         * selfInfo : {"location":{"city":"北京","province":"北京","street":"信息路"}}
         */

        private InputTextBean inputText;
        private InputImageBean inputImage;
        private SelfInfoBean selfInfo;

        public InputTextBean getInputText() {
            return inputText;
        }

        public void setInputText(InputTextBean inputText) {
            this.inputText = inputText;
        }

        public InputImageBean getInputImage() {
            return inputImage;
        }

        public void setInputImage(InputImageBean inputImage) {
            this.inputImage = inputImage;
        }

        public SelfInfoBean getSelfInfo() {
            return selfInfo;
        }

        public void setSelfInfo(SelfInfoBean selfInfo) {
            this.selfInfo = selfInfo;
        }

        public static class InputTextBean {
            /**
             * text : 附近的酒店
             */

            private String text;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }

        public static class InputImageBean {
            /**
             * url : imageUrl
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class SelfInfoBean {
            /**
             * location : {"city":"北京","province":"北京","street":"信息路"}
             */

            private LocationBean location;

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public static class LocationBean {
                /**
                 * city : 北京
                 * province : 北京
                 * street : 信息路
                 */

                private String city;
                private String province;
                private String street;

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getProvince() {
                    return province;
                }

                public void setProvince(String province) {
                    this.province = province;
                }

                public String getStreet() {
                    return street;
                }

                public void setStreet(String street) {
                    this.street = street;
                }
            }
        }
    }

    public static class UserInfoBean {
        /**
         * apiKey :
         * userId :
         */

        private String apiKey;
        private String userId;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
