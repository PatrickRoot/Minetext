/**
 * Copyright (c) 2017 Sixlab. All rights reserved.
 * <p>
 * License information see the LICENSE file in the project's root directory.
 * <p>
 * For more information, please see
 * https://sixlab.cn/
 *
 * @time: 2017/11/3 13:25
 * @author: Patrick <root@sixlab.cn>
 */
package cn.sixlab.minesitex.plugin.wx.service;

import cn.sixlab.minesitex.bean.wx.entity.MsxWxMsg;
import cn.sixlab.minesitex.data.wx.WxMsgRepo;
import cn.sixlab.minesitex.lib.base.model.ModelJson;
import cn.sixlab.minesitex.lib.base.util.InputStreamUtil;
import cn.sixlab.minesitex.plugin.wx.business.WxBusiness;
import cn.sixlab.minesitex.plugin.wx.util.MsxWxVal;
import cn.sixlab.minesitex.plugin.wx.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class WxService {
    private static Logger logger = LoggerFactory.getLogger(WxService.class);
    
    @Autowired
    MsxWxVal wxVal;
    
    @Autowired
    WxMsgRepo msgRepo;
    
    @Autowired
    private WxBusiness wxBusiness;
    
    public boolean checkToken(String signature, String timestamp, String nonce) {
        logger.info("验证微信签名");
    
        return wxBusiness.checkToken(signature, timestamp, nonce, wxVal.getWxToken());
    }
    
    public MsxWxMsg dealMsg(InputStream is) {
        String message = InputStreamUtil.readString(is);
        
        MsxWxMsg wxMsg = WxUtil.saveMsg(message, msgRepo);
        
        //template.convertAndSend(MqTopic.WX_SAVE_MSG, me);
        return wxMsg;
    }
    
    public ModelJson<MsxWxMsg> fetchMsg(Integer id) {
        MsxWxMsg wxMsg = msgRepo.findOne(id);
    
        return new ModelJson<MsxWxMsg>().setData(wxMsg);
    }
    
    public ModelJson<List<MsxWxMsg>> fetchMsgs(Integer id) {
        MsxWxMsg wxMsg = msgRepo.findOne(id);
        List<MsxWxMsg> msgList = msgRepo.findByFromUserNameOrderById(wxMsg.getFromUserName());
        return new ModelJson<List<MsxWxMsg>>().setData(msgList);
    }
}
