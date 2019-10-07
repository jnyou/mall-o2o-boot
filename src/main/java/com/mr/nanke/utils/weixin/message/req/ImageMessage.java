package com.mr.nanke.utils.weixin.message.req;

/**
 * 图片消息 
 * @author 夏小颜
 *
 */
public class ImageMessage extends BaseMessage {  
    // 图片链接  
    private String PicUrl;  
  
    public String getPicUrl() {  
        return PicUrl;  
    }  
  
    public void setPicUrl(String picUrl) {  
        PicUrl = picUrl;  
    }  
}  